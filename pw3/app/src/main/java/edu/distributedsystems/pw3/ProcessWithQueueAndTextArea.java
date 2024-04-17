package edu.distributedsystems.pw3;

import javax.swing.JTextArea;

public abstract class ProcessWithQueueAndTextArea extends ProcessWithQueue {
    protected JTextArea textArea;

    protected ProcessWithQueueAndTextArea() throws Exception {
        super();
        textArea = null;
    }

    protected ProcessWithQueueAndTextArea(JTextArea textArea) throws Exception {
        super();
        this.textArea = textArea;
    }

    protected void println(String text) {
        if (textArea == null) {
            System.out.println(text);
            return;
        }

        textArea.append(text + '\n');
    }

}
