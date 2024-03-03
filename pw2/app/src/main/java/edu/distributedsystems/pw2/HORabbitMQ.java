package edu.distributedsystems.pw2;

import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class HORabbitMQ {
    public static void main(String[] args) throws Exception {
        final String url = System.getenv("DB_CONNECTION_STRING");
        final String user = System.getenv("DB_USER");
        final String password = System.getenv("DB_PASSWORD");

        final String insertQuery = "INSERT INTO sale (date, region, product, qty, cost, amt, tax, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        final String rabbitMQHost = System.getenv("RABBITMQ_LOCAL_HOST");
        final int rabbitMQPort = Integer.parseInt(System.getenv("RABBITMQ_LOCAL_PORT"));
        final String rabbitMQQueue = System.getenv("RABBITMQ_LOCAL_QUEUE");

        final boolean autoAck = true;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQHost);
        factory.setPort(rabbitMQPort);

        Connection rabbitMQConnection = factory.newConnection();
        Channel channel = rabbitMQConnection.createChannel();

        channel.queueDeclare(rabbitMQQueue, true, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try(java.sql.Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);)
            {
                Sale sale = (Sale) SerializationUtils.deserialize(delivery.getBody());
                
                preparedStatement.setString(1, sale.getDate());
                preparedStatement.setString(2, sale.getRegion());
                preparedStatement.setString(3, sale.getProduct());
                preparedStatement.setInt(4, sale.getQty());
                preparedStatement.setFloat(5, sale.getCost());
                preparedStatement.setFloat(6, sale.getAmt());
                preparedStatement.setFloat(7, sale.getTax());
                preparedStatement.setFloat(8, sale.getTotal());

                preparedStatement.executeUpdate();
                
                System.out.println("Sale inserted successfully!");
            }
            catch(Exception e){
                System.err.println(e.getMessage());
            }
        };

        channel.basicConsume(rabbitMQQueue, autoAck, deliverCallback, consumerTag -> { });
    }
}
