package org.mangorage.chat.packetutils.packets;

import org.mangorage.chat.eventutills.events.ConnectEvent;
import org.mangorage.chat.sides.AbstractClient;
import org.mangorage.chat.sides.Side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import static org.mangorage.chat.Main.getEventHandler;
import static org.mangorage.chat.utils.SocketUtills.getHostWithPort;

public class ConnectionPacket extends Packet {
    public static String ID = "connection";
    private final String username;
    private final String address;
    public ConnectionPacket(String Username, String address) {
        this.username = Username;
        this.address = address;
    }

    public ConnectionPacket(AbstractClient client, BufferedReader reader) throws IOException {
        this(reader.readLine(), reader.readLine());
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {return address; }


    @Override
    public void postEvent() {
        getEventHandler().post(new ConnectEvent(username, address, getClient()));
    }

    @Override
    public boolean isValidSideForEvent(Side side) {
        if (side == Side.CLIENT)
            return true;

        return false;
    }

    @Override
    public void send(PrintStream stream, Socket socket, Side side) {
        if (stream != null) {
            stream.println("packet");
            stream.println(ID);
            stream.println(username);
            stream.println(getHostWithPort(socket, side));
        }

    }
}
