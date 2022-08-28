package org.mangorage.chat.sides;

import org.mangorage.chat.events.ConnectEvent;
import org.mangorage.chat.events.DisconnectEvent;
import org.mangorage.chat.packetutils.Packet.ChatPacket;
import org.mangorage.chat.packetutils.PacketRegistry;

import java.io.IOException;
import java.net.Socket;

import static org.mangorage.chat.Main.*;
import static org.mangorage.chat.Main.getEventHandler;

public class ConnectedClient extends AbstractClient {


    private final Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                System.out.println( "Received a connection" );

                subscribeEvents();

                String line = getIn().readLine();
                getServer().getPacketList().forEach(packet -> packet.send(getSelf().getOut(), getSocket(), Side.CLIENT));

                while (getSocket().isConnected()) { // Recieving
                    if (line != null) {
                        if (line.equals("packet")) {
                            PacketRegistry.handle(getSelf(), getIn(), Side.CLIENT); // From ConnectedClient!
                        }
                    }

                    if (getUsername() == null)
                        line = null;

                    line = getIn().ready() ? getIn().readLine() : null;
                }

                // Close our connection
                getIn().close();
                getOut().close();
                getSocket().close();


                if (getUsername() != null)
                    getHandler().sendPacket(new ChatPacket(getUsername() + " has left!", "Server"), Side.CLIENT, true);


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
            getHandler().sendPacket(new ChatPacket(getUsername() + " has Joined!", "Server"), Side.CLIENT, true);
        }

    }

    public void onDisconnect(DisconnectEvent event) {
        if (event.getClient() == this) {
            getServer().disconnect(this);
            getHandler().sendPacket(new ChatPacket(getUsername() + " has Left!", "Server"), Side.CLIENT, true);
        }
    }


}
