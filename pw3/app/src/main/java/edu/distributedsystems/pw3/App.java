package edu.distributedsystems.pw3;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class App extends JFrame {
    private ClientWriter clientWriter;
    private Replica[] replicas;

    public App() throws Exception {
        clientWriter = new ClientWriter();
        replicas = new Replica[3];

        for (int i = 1; i < 4; i++) {
            replicas[i - 1] = new Replica(String.valueOf(i));
        }

        setupGUI();
    }

    private void setupGUI() {
        setTitle("App");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setLayout(
            new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)
        );

        JTextArea displayTextArea = new JTextArea();
        displayTextArea.setEditable(false);

        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 20, 25);

        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(flowLayout);

        JTextField sendTextField = new JTextField("Please write a message", 40);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendActionListener(clientWriter, sendTextField));

        sendPanel.add(sendTextField);
        sendPanel.add(sendButton);

        add(sendPanel);
        
        JPanel readPanel = new JPanel();
        readPanel.setLayout(flowLayout);
        
        JButton readLastButton = new JButton("Read Last"); 
        readLastButton.addActionListener(new ReadV1ActionListener(displayTextArea));
        
        JButton readAllButton = new JButton("Read All"); 
        readAllButton.addActionListener(new ReadV2ActionListener(displayTextArea));

        readPanel.add(readLastButton);
        readPanel.add(readAllButton);

        add(readPanel);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(flowLayout);
        
        for (int i = 1; i < 4; i++) {
            JButton controlButton = new JButton("Stop " + i);
            controlButton.addActionListener(new ControlActionListener(replicas, i - 1, String.valueOf(i), controlButton, displayTextArea));

            controlPanel.add(controlButton);
        }

        add(controlPanel);

        JScrollPane displayScrollPane = new JScrollPane(displayTextArea);
        displayScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        Dimension displayDimension = new Dimension(600, 500);

        displayScrollPane.setMaximumSize(displayDimension);
        displayScrollPane.setPreferredSize(displayDimension);

        add(displayScrollPane);

        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new App();
    }
}
