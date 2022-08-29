package org.mangorage.chat.eventutills.events;

import org.mangorage.chat.eventutills.events.ConnectEvent;
import org.mangorage.chat.sides.AbstractClient;

public class DisconnectEvent extends ConnectEvent {
    public DisconnectEvent(String username, String address, AbstractClient client) {
        super(username, address, client);
    }
}
