package edu.distributedsystems.pw2;

import java.net.ConnectException;
import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class BORabbitMQ {
    public static void main(String[] args) throws Exception {
        final String rabbitMQLocalHost = System.getenv("RABBITMQ_LOCAL_HOST");
        final int rabbitMQLocalPort = Integer.parseInt(System.getenv("RABBITMQ_LOCAL_PORT"));
        final String rabbitMQLocalQueue = System.getenv("RABBITMQ_LOCAL_QUEUE");

        final boolean autoAck = false;

        final String rabbitMQRemoteHost = System.getenv("RABBITMQ_REMOTE_HOST");
        final int rabbitMQRemotePort = Integer.parseInt(System.getenv("RABBITMQ_REMOTE_PORT"));
        final String rabbitMQRemoteQueue = System.getenv("RABBITMQ_REMOTE_QUEUE");

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(rabbitMQLocalHost);
        factory.setPort(rabbitMQLocalPort);

        Connection localConnection = factory.newConnection();
        Channel localChannel = localConnection.createChannel();

        localChannel.queueDeclare(rabbitMQLocalQueue, true, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        factory.setHost(rabbitMQRemoteHost);
        factory.setPort(rabbitMQRemotePort);
   
        Connection tempRemoteConnection;

        while (true) {
            try {
                tempRemoteConnection = factory.newConnection();
                break;
            } catch (ConnectException e) {
                Thread.sleep(5000);
            }
        }

        Connection remoteConnection = tempRemoteConnection;

        DefaultConsumer consumer = new DefaultConsumer (localChannel) {
            @Override
            public void handleDelivery (
                    String consumerTag,
                    Envelope envelope,
                    BasicProperties properties,
                    byte[] body
            ) throws IOException {
                long deliveryTag = envelope.getDeliveryTag();
                
                try {
                    Channel remoteChannel = remoteConnection.createChannel();
                    remoteChannel.queueDeclare(rabbitMQRemoteQueue, true, false, false, null);
            
                    remoteChannel.basicPublish("", rabbitMQRemoteQueue, null, body);

                    localChannel.basicAck(deliveryTag, false);

                    System.out.println("Sale sent successfully!");
                } catch (Exception e) {
                    localChannel.basicNack(deliveryTag, false, true);
                }
            }
        };

        localChannel.basicConsume(rabbitMQLocalQueue, autoAck, "bo-consumer-tag", consumer);
    }
}
