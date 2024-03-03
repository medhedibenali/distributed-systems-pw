package edu.distributedsystems.pw1;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogDirect {
    private final static String EXCHANGE_NAME = "direct_logs";

    private static String getSeverity(String[] args) {
        return args[0];
    }

    private static String getMessage(String[] args) {
        return Arrays.stream(args)
            .skip(1)
            .collect(Collectors.joining(" "));
    }

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        
        try (
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
        ) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            String severity = getSeverity(args);
            String message = getMessage(args);

            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));

            System.out.println(" [X] Sent '" + severity + "':'" + message + "'");
        }
    }
}
