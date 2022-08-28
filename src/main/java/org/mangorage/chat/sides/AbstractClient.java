package org.mangorage.chat.sides;

import org.mangorage.chat.utils.SocketUtills;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public abstract class AbstractClient {

    private final Socket socket;
    private final PrintStream out;
    private final BufferedReader in;

    private String Username;


    public AbstractClient(Socket socket) {
        this.socket = socket;

        BufferedReader reader = null;
        PrintStream stream = null;


        try {
            stream = new PrintStream(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {

        }

        out = stream;
        in = reader;

    }

    public AbstractClient(String Host, int Port, String Username) {
        this(SocketUtills.createSocket(Host, Port));
        this.Username = Username;
    }

    public AbstractClient getSelf() {
        return this;
    }

    public void subscribeEvents() {

    }

    public void unSubscribeEvents() {

    }

    public PrintStream getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUsername() {
        return Username;
    }

    public void disconnect() {

    }

    public void init() {}


    public void setUsername(String username) {
        if (this.Username == null)
            this.Username = username;
    }





}
