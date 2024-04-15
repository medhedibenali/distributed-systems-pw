package edu.distributedsystems.pw3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Replica {
    public static final String WRITER_EXCHANGE_NAME = "writer";
    public static final String SEND_EXCHANGE_NAME = "send_reader";
    public static final String RECEIVE_EXCHANGE_NAME = "receive_reader";
    
    public static final String LAST_COMMAND = "Read Last";
    public static final String ALL_COMMAND = "Read All";
    
    public static List<String> readFile(File f) throws Exception {
        FileReader fr = new FileReader(f);
        BufferedReader in = new BufferedReader(fr);

        String line = "";
        List<String> lines = new LinkedList<String>();

        while((line = in.readLine()) != null) {
            lines.add(line);
        }

        in.close();
        fr.close();

        return lines;
    }

    public static boolean handleRead(Channel channel, String message, File f) throws Exception {
        boolean isLast = message.equalsIgnoreCase(LAST_COMMAND);
        boolean isAll = message.equalsIgnoreCase(ALL_COMMAND);

        if (!isLast && !isAll) {
            return false;
        }

        List<String> lines = readFile(f);

        if (lines.size() == 0) {
            return true;
        }

        if (isLast) {
            String lastLine = lines.getLast();

            lines = new LinkedList<String>();
            lines.add(lastLine);
        }

        for(String line : lines) {
            channel.basicPublish(RECEIVE_EXCHANGE_NAME, "", null, line.getBytes("UTF-8"));
        }

        return true;
    }


    public static void handleWrite(String message, PrintWriter out) {
        out.println(message);
        out.flush();
    }

    public static void processMessage(Channel channel, String message, File f, PrintWriter out) {
        try {
            if (handleRead(channel, message, f)) {
                return;
            }

            handleWrite(message, out);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new Exception("No replica id provided.");
        }

        String replicaId = args[0];

        boolean append = true;

        String filePath = "rep_" + replicaId + "/fichier.txt";

        File f = new File(filePath);
        f.getParentFile().mkdirs();

        FileWriter fw = new FileWriter(f, append);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String queueName = channel.queueDeclare().getQueue();
        
        channel.exchangeDeclare(WRITER_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        channel.queueBind(queueName, WRITER_EXCHANGE_NAME, "");

        channel.exchangeDeclare(SEND_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        channel.queueBind(queueName, SEND_EXCHANGE_NAME, "");

        channel.exchangeDeclare(RECEIVE_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        boolean autoAck = true;

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            
            processMessage(channel, message, f, out);
        };

        channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> {});
    }
}
