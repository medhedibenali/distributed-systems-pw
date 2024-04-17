package edu.distributedsystems.pw3;

public class ClientWriter extends Process {
    protected final String exchangeName = Config.WRITER_EXCHANGE_NAME;

    public ClientWriter() throws Exception {
        super();
        declareExchange(exchangeName);
    }

    public void sendMessage(String message) throws Exception {
        basicPublish(exchangeName, message);
    }

    public static void main(String[] args) throws Exception {
        String message = getMessage(args);

        ClientWriter clientWriter = new ClientWriter();
        clientWriter.sendMessage(message);
   
        clientWriter.close();
    }

    private static String getMessage(String[] args) throws Exception {
        if (args.length < 1) {
            throw new Exception("No message provided.");
        }

        return String.join(" ", args);
    }
}
