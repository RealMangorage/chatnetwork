package org.mangorage.chat.packetutils.Packet;

import org.mangorage.chat.events.ChatEvent;
import org.mangorage.chat.sides.Side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import static org.mangorage.chat.Main.getEventHandler;

public class ChatPacket extends Packet {
    public static final String ID = "chat";
    public final String message;
    public final String user;

    public ChatPacket(String message, String User) {
        this.message = message;
        this.user = User;
    }

    public ChatPacket(BufferedReader reader) throws IOException {
        this(reader.readLine(), reader.readLine());
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean sendBackToClients() {
        return true;
    }

    @Override
    public void postEvent() {
        getEventHandler().post(new ChatEvent(message, user));
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
            stream.println(message);
            stream.println(user);
            stream.flush();
        }
    }
}
