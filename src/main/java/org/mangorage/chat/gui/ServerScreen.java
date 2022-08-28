package org.mangorage.chat.gui;

import org.mangorage.chat.Main;
import org.mangorage.chat.configs.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import static org.mangorage.chat.Main.*;


public class ServerScreen extends JPanel implements KeyListener, ActionListener {

    private Timer timer;

    private JTextPane textArea = new JTextPane();

    private Config config = new Config("settings/server.properties");

    public ServerScreen() {
        setPreferredSize(new Dimension(1080, 720));
        setBackground(new Color(232, 232, 232));


        JTextField PORT = new JTextField(10);
        JButton STARTSERVER = new JButton("Run Server");


        Properties properties = config.getProperties();

        if (properties.containsKey("PORT"))
            PORT.setText(properties.getProperty("PORT"));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        add(new JLabel("Port"), gbc);
        add(PORT);
        gbc.gridx++;
        add(STARTSERVER);

        Thread update =  new Thread() {
            public void run() {
                while (isAlive()) {
                    Main.sleep(100);

                    AtomicReference<String> string = new AtomicReference<>("Server Port: " + getPort() + "\n" + "Clients:" + "\n");

                    getServer().getClients().forEach(client -> {
                        string.set(string.get() + client.getUsername() + " : " + client.getSocket().getInetAddress().getHostAddress() + "\n");
                    });


                    textArea.setText(string.get());
                }
            }
        };

        STARTSERVER.addActionListener(action -> {
            if (Integer.valueOf(PORT.getText()) != null) {
                StartServer(Integer.valueOf(PORT.getText()));

                removeAll();
                textArea.setSize(100, 100);
                textArea.setEditable(false);
                textArea.setText("Server Port: " + getPort() + "\n" + "Clients:");
                add(textArea);
                update.start();

                properties.setProperty("PORT", String.valueOf(getPort()));
                config.saveProperties();
            }
        });



        timer = new Timer(25, this);
        timer.start();
    }









    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
