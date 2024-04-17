package edu.distributedsystems.pw3;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class Process {
    protected final Connection connection;
    protected final Channel channel;

    protected Process() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    protected void declareExchange(String exchangeName) throws Exception {
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);
    }

    protected void basicPublish(String exchangeName, String message) throws Exception {
        channel.basicPublish(exchangeName, "", null, message.getBytes("UTF-8"));
    }

    public void close() throws Exception {
        channel.close();
        connection.close();
    }
}
