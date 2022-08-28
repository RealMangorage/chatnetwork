package org.mangorage.chat.events;

import org.mangorage.chat.sides.AbstractClient;

public class ConnectEvent extends Event {
    private final String username;
    private final String address;
    private final AbstractClient client;

    public ConnectEvent(String username, String address, AbstractClient client) {
        this.username = username;
        this.address = address;
        this.client = client;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public AbstractClient getClient() {
        return client;
    }

}
