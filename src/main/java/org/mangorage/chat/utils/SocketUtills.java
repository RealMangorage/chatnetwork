package org.mangorage.chat.utils;

import org.mangorage.chat.sides.Side;

import java.net.Socket;

public class SocketUtills {
    public static String getHostWithPort(Socket socket, Side side) {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }


    public static Socket createSocket(String Host, Integer Port) {
        try {
            return new Socket(Host, Port);
        } catch (Exception e) {

        }
        return null;
    }
}
