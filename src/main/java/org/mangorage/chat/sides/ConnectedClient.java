package org.mangorage.chat.sides;

import org.mangorage.chat.eventutills.events.ConnectEvent;
import org.mangorage.chat.eventutills.events.DisconnectEvent;
import org.mangorage.chat.packetutils.packets.ChatPacket;
import org.mangorage.chat.packetutils.PacketRegistry;

import java.io.IOException;
import java.net.Socket;

import static org.mangorage.chat.Main.*;
import static org.mangorage.chat.Main.getEventHandler;
import static org.mangorage.chat.packetutils.PacketRegistry.sendPacket;

public class ConnectedClient extends AbstractClient {


    private boolean disconnected = false;
    private final Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                System.out.println( "Received a connection" );

                subscribeEvents();

                String line = getIn().readLine();
                getServer().getPacketList().forEach(packet -> packet.send(getSelf().getOut(), getSocket(), Side.CLIENT));

                while (getSocket().isConnected() && !disconnected) { // Recieving

                    if (line != null) {
                        if (line.equals("packet")) {
                            PacketRegistry.handle(getSelf(), getIn(), Side.CLIENT); // From ConnectedClient!
                        }
                    } else {
                        disconnected = true;
                    }

                    line = getIn().readLine();
                    int i = 0;
                }

                // Close our connection
                getIn().close();
                getOut().close();
                getSocket().close();


                if (getUsername() != null)
                    sendPacket(new ChatPacket(getUsername() + " has left!", "Server"), Side.CLIENT, true, null);


                unSubscribeEvents();
                getServer().disconnect(getSelf());

                System.out.println( "Connection closed" );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    };

    public ConnectedClient(Socket socket) {
        super(socket);
    }

    @Override
    public void subscribeEvents() {
        getEventHandler().subscribe(ConnectEvent.class, this::onConnection);
        getEventHandler().subscribe(DisconnectEvent.class, this::onDisconnect);
    }

    @Override
    public void unSubscribeEvents() {
        getEventHandler().unSubscribe(ConnectEvent.class, this::onConnection);
        getEventHandler().unSubscribe(DisconnectEvent.class, this::onDisconnect);
    }

    @Override
    public void init() {
        thread.start();
    }

    public void onConnection(ConnectEvent event) {
        if (event.getClient() == this && getUsername() == null) {
            setUsername(event.getUsername());
            sendPacket(new ChatPacket(getUsername() + " has Joined!", "Server"), Side.CLIENT, true, null);
        }

    }

    public void onDisconnect(DisconnectEvent event) {
        if (event.getClient() == this) {
            this.disconnected = true;
            getServer().disconnect(this);
            sendPacket(new ChatPacket(getUsername() + " has Left!", "Server"), Side.CLIENT, true, null);
        }
    }


}
