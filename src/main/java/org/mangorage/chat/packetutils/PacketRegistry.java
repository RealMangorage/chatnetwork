package org.mangorage.chat.packetutils;

import org.mangorage.chat.packetutils.Packet.ChatPacket;
import org.mangorage.chat.packetutils.Packet.ConnectionPacket;
import org.mangorage.chat.packetutils.Packet.DisconnectPacket;
import org.mangorage.chat.packetutils.Packet.Packet;
import org.mangorage.chat.sides.AbstractClient;
import org.mangorage.chat.sides.Side;

import java.io.BufferedReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;

import static org.mangorage.chat.Main.*;

public class PacketRegistry {
    private static final HashMap<String, Class<? extends Packet>> registred = new HashMap<>();

    static {
        register(ChatPacket.class);
        register(ConnectionPacket.class);
        register(DisconnectPacket.class);
    }


    public static void register(Class<? extends Packet> packetClass) {

        String ID = "";

        try {
            Field field = packetClass.getField("ID");
            ID = (String) field.get("");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        if (!registred.containsKey(ID))
            registred.put(ID, packetClass);
    }


    public static Packet handle(BufferedReader reader, Side side) {
        return handle(null, reader, side);
    }


    public static Packet handle(AbstractClient client, BufferedReader reader, Side side) {
        try {
            String packetID = reader.readLine();

            if (packetID != null) {
                Constructor constructor = registred.get(packetID).getConstructor(reader.getClass());
                Packet packet = (Packet) constructor.newInstance(reader);
                packet.setClient(client);

                if (packet.isValidSideForEvent(side))
                    packet.postEvent();

                if (packet instanceof DisconnectPacket)
                    packet.postEvent();

                if (packet.sendBackToClients() && getServer() != null)
                    getHandler().sendPacket(packet, Side.CLIENT, true);

                return packet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
