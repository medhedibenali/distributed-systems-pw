package edu.distributedsystems.pw2;

import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class BOClient {
    public static void main(String[] args) {
        final String url = System.getenv("DB_CONNECTION_STRING");
        final String user = System.getenv("DB_USER");
        final String password = System.getenv("DB_PASSWORD");
        
        final String insertQuery = "INSERT INTO sale (date, region, product, qty, cost, amt, tax, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        final String rabbitMQHost = System.getenv("RABBITMQ_LOCAL_HOST");
        final int rabbitMQPort = Integer.parseInt( System.getenv("RABBITMQ_LOCAL_PORT"));
        final String rabbitMQQueue = System.getenv("RABBITMQ_LOCAL_QUEUE");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQHost);
        factory.setPort(rabbitMQPort);

        try (
            java.sql.Connection sqlConnection = DriverManager.getConnection(url,user,password);
            PreparedStatement preparedStatement = sqlConnection.prepareStatement(insertQuery);
            Connection rabbitMQConnection = factory.newConnection();
            Channel channel = rabbitMQConnection.createChannel();
        ) {
            Sale sale = new Sale(args);
            
            //persisting the sale in the local database
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

            channel.queueDeclare(rabbitMQQueue, true, false, false, null);
            
            byte[] saleBytes = SerializationUtils.serialize(sale);

            channel.basicPublish("", rabbitMQQueue, null, saleBytes);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}
