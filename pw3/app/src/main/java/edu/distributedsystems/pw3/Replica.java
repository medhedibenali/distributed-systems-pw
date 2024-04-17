package edu.distributedsystems.pw3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.rabbitmq.client.Delivery;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Replica extends ProcessWithQueue {
    protected final String writerExchangeName = Config.WRITER_EXCHANGE_NAME;
    protected final String readerSendExchangeName = Config.READER_SEND_EXCHANGE_NAME;
    protected final String readerReceiveExchangeName = Config.READER_RECEIVE_EXCHANGE_NAME;
    
    protected final String readLastCommand = Config.READ_LAST_COMMAND;
    protected final String readAllCommand = Config.READ_ALL_COMMAND;

    protected final String id;

    protected final File f;
    protected final PrintWriter out;

    public Replica(String id) throws Exception {
        super();
        this.id = id;

        f = getFile();
        out = getWriter();

        declareExchangeAndBind(writerExchangeName);
        declareExchangeAndBind(readerSendExchangeName);
        declareExchange(readerReceiveExchangeName);
    }

    protected File getFile() {
        String filePath = "rep_" + id + "/fichier.txt";

        File f = new File(filePath);
        f.getParentFile().mkdirs();

        return f;
    }

    protected PrintWriter getWriter() throws Exception {
        boolean append = true;

        FileWriter fw = new FileWriter(f, append);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);

        return out;
    }
    
    protected List<String> readFile() throws Exception {
        try (FileReader fr = new FileReader(f);
            BufferedReader in = new BufferedReader(fr)) {
            String line = "";
            List<String> lines = new LinkedList<String>();

            while((line = in.readLine()) != null) {
                lines.add(line);
            }

            return lines;
        }
    }

    protected boolean handleRead(String message) throws Exception {
        boolean isLast = message.equalsIgnoreCase(readLastCommand);
        boolean isAll = message.equalsIgnoreCase(readAllCommand);

        if (!isLast && !isAll) {
            return false;
        }

        List<String> lines = readFile();

        if (lines.size() == 0) {
            return true;
        }

        if (isLast) {
            String lastLine = lines.getLast();

            lines = new LinkedList<String>();
            lines.add(lastLine);
        }

        for(String line : lines) {
            channel.basicPublish(readerReceiveExchangeName, "", null, line.getBytes("UTF-8"));
        }

        return true;
    }


    protected void handleWrite(String message) {
        out.println(message);
        out.flush();
    }

    protected void handle(String consumerTag, Delivery delivery) throws IOException {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [" + id +"] Received '" + message + "'");
        
        try {
            if (handleRead(message)) {
                return;
            }

            handleWrite(message);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new Exception("No replica id provided.");
        }

        String replicaId = args[0];

        new Replica(replicaId);
    }
}
