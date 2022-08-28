package org.mangorage.chat.gui;

import org.jetbrains.annotations.NotNull;
import org.mangorage.chat.configs.Config;
import org.mangorage.chat.events.ChatEvent;
import org.mangorage.chat.packetutils.Packet.ChatPacket;
import org.mangorage.chat.sides.Side;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mangorage.chat.Main.*;

public class ClientScreen extends JPanel implements KeyListener, ActionListener {
    private Timer timer;
    private ArrayList<String> chat = new ArrayList<>();
    private boolean connected = false;
    private String message = "";
    private final String regex = "abcdefghijklmnopqrstuvwxyz1234567890 !@#$%^&*()-=_+[]{}|\\;',.<>/?`~" + '"';
    private final Config config = new Config("settings/client.properties");


    public ClientScreen() {

        setPreferredSize(new Dimension(1080, 720));
        setBackground(new Color(232, 232, 232));
        setFocusable(true);


        /**
         * IP PORT USERNAME
         * CONNECT -> Sends : connectionpacket:username
         */

        JTextField HOST = new JTextField(10);
        JTextField PORT = new JTextField(10);
        JTextField USERNAME = new JTextField(10);
        JButton CONNECT = new JButton("Connect to Server");
        CONNECT.setFocusable(false);

        Properties properties = config.getProperties();

        if (properties.containsKey("HOST"))
            HOST.setText(properties.getProperty("HOST"));


        if (properties.containsKey("PORT"))
            PORT.setText(properties.getProperty("PORT"));

        if (properties.containsKey("USERNAME"))
            USERNAME.setText(properties.getProperty("USERNAME"));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        add(new JLabel("Server IP"), gbc);
        gbc.gridx++;
        add(HOST, gbc);
        gbc.gridx--;
        gbc.gridy++;

        add(new JLabel("Server Port"), gbc);
        gbc.gridx++;
        add(PORT, gbc);
        gbc.gridx--;
        gbc.gridy++;

        add(new JLabel("Username"), gbc);
        gbc.gridx++;
        add(USERNAME, gbc);
        gbc.gridx--;
        gbc.gridy++;

        add(CONNECT, gbc);

        CONNECT.addActionListener(listener -> {
            if (Integer.valueOf(PORT.getText()) != null) {

                removeAll();
                StartClient(HOST.getText(), Integer.valueOf(PORT.getText()), USERNAME.getText());
                addKeyListener(this);
                connected = true;


                properties.setProperty("HOST", HOST.getText());
                properties.setProperty("PORT", PORT.getText());
                properties.setProperty("USERNAME", USERNAME.getText());
                config.saveProperties();
            }
        });



        timer = new Timer(25, this);
        timer.start();

        getEventHandler().subscribe(ChatEvent.class, this::onChatEvent);
    }

    public void onChatEvent(@NotNull ChatEvent event) {
        chat.add(event.getMessage());
    }

    public ArrayList<String> getChat() {
        return (ArrayList<String>) chat.clone();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (connected) {
            AtomicInteger i = new AtomicInteger(1);

            getChat().forEach(message -> {
                g.drawString(message, 10, 15 * (i.getAndIncrement()));
            });

            g.drawString("Send (%username%) :".replace("%username%", getClient().getUsername()) + message, 10, 700);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (connected) {
            if (regex.contains(String.valueOf(e.getKeyChar()).toLowerCase())) {
                message = message + e.getKeyChar();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (connected) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && message.length() > 0) {
                message = message.substring(0, message.length() - 1);
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                getHandler().sendPacket(new ChatPacket(message, getClient().getUsername()), Side.SERVER, false);
                message = "";
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
