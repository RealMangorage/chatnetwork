package org.mangorage.chat.sides;

import org.mangorage.chat.Main;
import org.mangorage.chat.packetutils.Packet.ConnectionPacket;
import org.mangorage.chat.packetutils.Packet.DisconnectPacket;
import org.mangorage.chat.packetutils.PacketRegistry;
import org.mangorage.chat.utils.SocketUtills;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Supplier;

import static org.mangorage.chat.Main.getHandler;
import static org.mangorage.chat.utils.SocketUtills.getHostWithPort;

public class LocalClient extends AbstractClient {

    private final Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                String line = getSelf().getIn().readLine();

                while (line != null) { // Recieving from Server
                    if (line != null) {
                        if (line.equals("packet")) {
                            PacketRegistry.handle(getSelf().getIn(), Side.CLIENT);
                        }
                    }
                    line = getIn().readLine();
                }


                getOut().close();
                getIn().close();
                getSocket().close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    };

    public LocalClient(String Host, int Port, String Username) {
        super(Host, Port, Username);
    }


    @Override
    public void disconnect() {
        getHandler().sendPacket(new DisconnectPacket(SocketUtills.getHostWithPort(getSocket(), Side.CLIENT)), Side.SERVER, false);
    }

    @Override
    public void init() {
        thread.start();
        new Thread() {
            public void run() {
                Main.sleep(100);
                getHandler().sendPacket(new ConnectionPacket(getUsername(), "address"), Side.SERVER, false);
            }
        }.start();
    }
}
