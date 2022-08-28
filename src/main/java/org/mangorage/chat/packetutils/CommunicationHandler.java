package org.mangorage.chat.packetutils;

import org.mangorage.chat.packetutils.Packet.Packet;
import org.mangorage.chat.sides.Side;

import static org.mangorage.chat.Main.getClient;
import static org.mangorage.chat.Main.getServer;

public class CommunicationHandler {

    public void sendPacket(Packet packet, Side sendTo, boolean storePacket) {
        if (sendTo == Side.SERVER) {
            packet.send(getClient().getOut(), getClient().getSocket(), sendTo);
        } else if (sendTo == Side.CLIENT) {
            if (storePacket)
                getServer().addPacket(packet);

            getServer().getClients().forEach(client -> packet.send(client.getOut(), client.getSocket(), sendTo));
        }
    }
}
