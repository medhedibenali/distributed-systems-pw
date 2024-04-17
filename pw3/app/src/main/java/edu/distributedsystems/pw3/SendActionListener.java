package edu.distributedsystems.pw3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class SendActionListener implements ActionListener {
    private ClientWriter clientWriter;
    private JTextField textField;

    public SendActionListener(ClientWriter clientWriter, JTextField textField) {
        this.clientWriter = clientWriter;
        this.textField = textField;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            String message = textField.getText();
            clientWriter.sendMessage(message);
        } catch (Exception ex) {}
    }
}
