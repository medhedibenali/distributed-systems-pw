package edu.distributedsystems.pw3;

import java.awt.event.ActionEvent;

import javax.swing.JTextArea;

public class ReadV1ActionListener extends ReadActionListener {
    public ReadV1ActionListener(JTextArea textArea) {
        super(textArea, "Read Last");
    }

    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        try {
            clientReader = new ClientReaderV1(textArea);
        } catch (Exception ex) {}
    }
}
