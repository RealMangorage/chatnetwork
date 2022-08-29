package org.mangorage.chat.packetutils;

import org.mangorage.chat.packetutils.packets.*;
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
    public static Packet handle(AbstractClient client, BufferedReader reader, Side side) {
        try {
            String packetID = reader.readLine();

            System.out.println("RECIEVED: " + packetID);


            if (packetID != null) {
                Constructor constructor = registred.get(packetID).getConstructor(AbstractClient.class, reader.getClass());
                Packet packet = (Packet) constructor.newInstance(client, reader);

                packet.setClient(client);

                if (packet.isValidSideForEvent(side))
                    packet.postEvent();

                if (packet instanceof DisconnectPacket)
                    packet.postEvent();

                if (packet.sendBackToClients() && getServer() != null)
                   sendPacket(packet, Side.CLIENT, true, packet.ignoreOriginal() ? null : client);

                return packet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void sendPacket(Packet packet, Side sendTo, boolean storePacket, AbstractClient clienttoIgnore) {
        if (sendTo == Side.SERVER) {
            packet.send(getClient().getOut(), getClient().getSocket(), sendTo);
        } else if (sendTo == Side.CLIENT) {
            if (storePacket)
                getServer().addPacket(packet);

            getServer().getClients().forEach(client -> {
                if (clienttoIgnore == null) {
                    packet.send(client.getOut(), client.getSocket(), sendTo);
                } else if (clienttoIgnore != client) {
                    packet.send(client.getOut(), client.getSocket(), sendTo);
                }

                packet.send(client.getOut(), client.getSocket(), sendTo);
            });
        }
    }
}
