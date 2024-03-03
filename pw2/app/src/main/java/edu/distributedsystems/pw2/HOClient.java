package edu.distributedsystems.pw2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HOClient {
    public static void main(String[] args) {
        final String url = System.getenv("DB_CONNECTION_STRING");
        final String user = System.getenv("DB_USER");
        final String password = System.getenv("DB_PASSWORD");

        final String selectQuery = "select * from sale";
        
        System.out.println("\nSales");
        System.out.println("Id\tDate\tRegion\tProduct\tQty\tCost\tAmt\tTax\tTotal");

        try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet result = preparedStatement.executeQuery()
        ) {
            while (result.next()) {
                System.out.print(result.getInt(1) + "\t");
                System.out.print(result.getString(2) + "\t");
                System.out.print(result.getString(3) + "\t");
                System.out.print(result.getString(4) + "\t");
                System.out.print(result.getInt(5) + "\t");
                System.out.print(result.getFloat(6) + "\t");
                System.out.print(result.getFloat(7) + "\t");
                System.out.print(result.getFloat(8) + "\t");
                System.out.println(result.getFloat(9));
            }
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}
