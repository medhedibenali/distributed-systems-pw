package edu.distributedsystems.pw3;

import javax.swing.JTextArea;

public abstract class ClientReader extends ProcessWithQueueAndTextArea {
    protected final String sendExchangeName = Config.READER_SEND_EXCHANGE_NAME;
    protected final String receiveExchangeName = Config.READER_RECEIVE_EXCHANGE_NAME;

    protected final String command;

    protected ClientReader(String command) throws Exception {
        super();
        this.command = command;

        setup();
    }

    protected ClientReader(JTextArea textArea, String command) throws Exception {
        super(textArea);
        this.command = command;

        setup();
    }

    private void setup() throws Exception {
        declareExchange(sendExchangeName);
        declareExchangeAndBind(receiveExchangeName);
    
        sendCommand();
    } 

    protected void sendCommand() throws Exception {
        basicPublish(sendExchangeName, command);
    }
}
