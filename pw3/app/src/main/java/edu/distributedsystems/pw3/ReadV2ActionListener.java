package edu.distributedsystems.pw3;

import java.awt.event.ActionEvent;

import javax.swing.JTextArea;

public class ReadV2ActionListener extends ReadActionListener {
    public ReadV2ActionListener(JTextArea textArea) {
        super(textArea, "Read All");
    }

    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        try {
            clientReader = new ClientReaderV2(textArea);
        } catch (Exception ex) {}
    }
}
