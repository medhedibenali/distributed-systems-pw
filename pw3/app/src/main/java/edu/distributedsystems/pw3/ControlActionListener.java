package edu.distributedsystems.pw3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class ControlActionListener extends LoggingActionListener {
    protected Replica[] replicas;
    protected int replicaIndex;
    protected String replicaId;
    protected JButton controlButton;

    public ControlActionListener(Replica[] replicas, int replicaIndex, String replicaId, JButton controlButton, JTextArea textArea) {
        super(textArea);

        this.replicas = replicas;
        this.replicaIndex = replicaIndex;
        this.replicaId = replicaId;
        this.controlButton = controlButton;
    }

    protected void startReplica() throws Exception {
        replicas[replicaIndex] = new Replica(replicaId);
        
        log("Replica " + replicaId + " started");

        controlButton.setText("Stop " + replicaId);
    }

    protected void stopReplica() throws Exception {
        replicas[replicaIndex].close();
        replicas[replicaIndex] = null;

        log("Replica " + replicaId + " stopped");

        controlButton.setText("Start " + replicaId);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (replicas[replicaIndex] == null) {
                startReplica();
                return;
            }

            stopReplica();
        } catch (Exception ex) {}
    }
}
