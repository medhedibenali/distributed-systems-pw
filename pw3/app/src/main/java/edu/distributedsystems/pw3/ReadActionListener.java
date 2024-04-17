package edu.distributedsystems.pw3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

public abstract class ReadActionListener extends LoggingActionListener {
    protected ClientReader clientReader;
    protected String message;

    public ReadActionListener(JTextArea textArea, String message) {
        super(textArea);

        clientReader = null;
        this.message = message;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            clientReader.close();
        } catch (Exception ex) {}

        log(message);
    }
}
