package org.mangorage.chat.packetutils.Packet;

import org.mangorage.chat.sides.AbstractClient;
import org.mangorage.chat.sides.Side;

import java.io.PrintStream;
import java.net.Socket;

public abstract class Packet {

    private AbstractClient client;

    public void send(PrintStream stream, Socket socket, Side side) {

    }

    public void postEvent() {

    }

    public void setClient(AbstractClient client) {
        this.client = client;
    }

    public AbstractClient getClient() {
        return client;
    }

    public boolean sendBackToClients() {
        return false;
    }

    public boolean isValidSideForEvent(Side side) {
        return false;
    }
}
