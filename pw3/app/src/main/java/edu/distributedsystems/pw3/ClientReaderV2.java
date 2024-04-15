package edu.distributedsystems.pw3;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;

public class ClientReaderV2 {
    public static final String SEND_EXCHANGE_NAME= "send_reader";
    public static final String RECEIVE_EXCHANGE_NAME= "receive_reader";

    public static final String COMMAND  = "Read All";

    private static void addString(HashMap<String,Integer> freq, String s){
        Integer freqOfString = freq.get(s);
        freq.put(s, (freqOfString == null) ? 1 : freqOfString + 1);
    }

    public static void sendCommand(Channel channel, String command) throws Exception {
        channel.exchangeDeclare(SEND_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        channel.basicPublish(SEND_EXCHANGE_NAME, "", null, command.getBytes("UTF-8"));
    }

    public static void listenForResponse(Channel channel, Connection connection) throws Exception {
        String queueName = channel.queueDeclare().getQueue();

        channel.exchangeDeclare(RECEIVE_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        channel.queueBind(queueName, RECEIVE_EXCHANGE_NAME, "");

        boolean autoAck = true;

        HashMap<String,Integer> freq= new HashMap<String,Integer>() ;

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            addString(freq, message);

            if(freq.get(message)==2)
            {
                System.out.println(" [x] Frequent line: '" + message + "'");
            }
        };

        channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> {});
    }

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        listenForResponse(channel, connection);
        sendCommand(channel, COMMAND);
    }
}