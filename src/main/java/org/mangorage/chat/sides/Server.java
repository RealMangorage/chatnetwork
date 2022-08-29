package org.mangorage.chat.sides;

import org.mangorage.chat.Main;
import org.mangorage.chat.packetutils.packets.Packet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final ArrayList<Packet> packetList = new ArrayList<>();
    private final ArrayList<AbstractClient> clients = new ArrayList<>();

    public List<AbstractClient> getClients() {
        return (List<AbstractClient>) clients.clone();
    }

    public void connect(AbstractClient client) {
        if (!clients.contains(client))
            clients.add(client);
    }

    public void disconnect(AbstractClient client) {
        if (clients.contains(client))
            clients.remove(client);
    }

    public void addPacket(Packet packet) {
        if (!packetList.contains(packet))
            packetList.add(packet);
    }

    public List<Packet> getPacketList() {
        return (List<Packet>) packetList.clone();
    }

    private Thread thread = new Thread() {
        public void run() {
            while (running) {
                Main.sleep(50);
                tick();
            }
        }
    };
    private boolean running = false;
    private ServerSocket socket;


    public Server(int port) {
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        running = !running;
        System.out.println("running Server");

        thread.start();
    }

    public void tick() {
        Socket client = null;
        try {
            client = socket.accept();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (client != null) {
            ConnectedClient connectedClient = new ConnectedClient(client);
            connect(connectedClient);
            connectedClient.init();
        }
    }

}
