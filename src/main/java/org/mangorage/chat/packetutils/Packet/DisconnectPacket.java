package org.mangorage.chat.packetutils.Packet;

import org.mangorage.chat.events.DisconnectEvent;
import org.mangorage.chat.sides.Side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import static org.mangorage.chat.Main.getEventHandler;
import static org.mangorage.chat.utils.SocketUtills.getHostWithPort;

public class DisconnectPacket extends Packet {
    public final static String ID = "disconnect";
    private final String address;

    public DisconnectPacket(String address) {
        this.address = address;
    }

    public DisconnectPacket(BufferedReader reader) throws IOException {
        this(reader.readLine());
    }

    @Override
    public void postEvent() {
        getEventHandler().post(new DisconnectEvent(null, address, getClient()));
    }

    @Override
    public boolean isValidSideForEvent(Side side) {
        System.out.println(ID);
        if (side == Side.SERVER)
            return true;

        return false;
    }

    @Override
    public void send(PrintStream stream, Socket socket, Side side) {

        if (stream != null) {
            stream.println("packet");
            stream.println(ID);
            stream.println(getHostWithPort(socket, side));
            stream.flush();
        }

    }
}
