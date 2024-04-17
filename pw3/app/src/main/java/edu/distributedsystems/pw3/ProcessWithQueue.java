package edu.distributedsystems.pw3;

import java.io.IOException;

import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public abstract class ProcessWithQueue extends Process {
    protected final String queueName;
    protected final DeliverCallback deliverCallback;

    protected ProcessWithQueue() throws Exception {
        super();
        queueName = channel.queueDeclare().getQueue();
        
        deliverCallback = (consumerTag, delivery) -> handle(consumerTag, delivery);

        listenForResponse();
    }

    protected void declareExchangeAndBind(String exchangeName) throws Exception {
        declareExchange(exchangeName);
        channel.queueBind(queueName, exchangeName, "");
    }

    protected abstract void handle(String consumerTag, Delivery delivery) throws IOException;

    protected void listenForResponse() throws Exception {
        boolean autoAck = true;
        channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> {});
    }
}
