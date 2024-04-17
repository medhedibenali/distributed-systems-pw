package edu.distributedsystems.pw3;

import java.io.IOException;
import java.util.HashMap;

import com.rabbitmq.client.Delivery;

import javax.swing.JTextArea;

public class ClientReaderV2 extends ClientReader {
    protected HashMap<String,Integer> freq = new HashMap<String,Integer>();

    protected ClientReaderV2() throws Exception {
        super(Config.READ_ALL_COMMAND);
    }

    protected ClientReaderV2(JTextArea textArea) throws Exception {
        super(textArea, Config.READ_ALL_COMMAND);
    }

    private void addString(HashMap<String,Integer> freq, String s){
        Integer freqOfString = freq.get(s);
        freq.put(s, (freqOfString == null) ? 1 : freqOfString + 1);
    }

    protected void handle(String consumerTag, Delivery delivery) throws IOException {
        String message = new String(delivery.getBody(), "UTF-8");
        addString(freq, message);

        if (freq.get(message) == 2) {
            println(" [x] Frequent line: '" + message + "'");
        }
   }

    public static void main(String[] args) throws Exception {
        new ClientReaderV2();
    }
}
