package edu.distributedsystems.pw3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

public abstract class LoggingActionListener implements ActionListener {
    protected JTextArea textArea;

    public LoggingActionListener(JTextArea textArea) {
        this.textArea = textArea;
    }

    protected void log(String message) {
        textArea.append("*** " + message + " ***\n");
    }
}
