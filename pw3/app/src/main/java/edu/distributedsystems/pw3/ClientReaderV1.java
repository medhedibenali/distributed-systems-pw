package edu.distributedsystems.pw3;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import com.rabbitmq.client.Delivery;

import javax.swing.JTextArea;

public class ClientReaderV1 extends ClientReader {
    protected ReentrantLock mutex = new ReentrantLock();

    protected ClientReaderV1() throws Exception {
        super(Config.READ_LAST_COMMAND);
    }

    protected ClientReaderV1(JTextArea textArea) throws Exception {
        super(textArea, Config.READ_LAST_COMMAND);
    }

    protected void handle(String consumerTag, Delivery delivery) throws IOException {
        if (!mutex.tryLock()) {
            return;
        }

        String message = new String(delivery.getBody(), "UTF-8");
        println(" [x] Last line: '" + message + "'");

        try {
            close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
        new ClientReaderV1();
    }
}
