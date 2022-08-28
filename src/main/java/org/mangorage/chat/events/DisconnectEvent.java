package org.mangorage.chat.events;

import org.mangorage.chat.sides.AbstractClient;

public class DisconnectEvent extends ConnectEvent {
    public DisconnectEvent(String username, String address, AbstractClient client) {
        super(username, address, client);
    }
}
