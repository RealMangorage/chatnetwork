package org.mangorage.chat.eventutills.events;

import org.mangorage.chat.eventutills.Event;

public class ChatEvent extends Event {
    private final String message;
    private final String user;

    public ChatEvent(String message, String username) {
        this.message = message;
        this.user = username;
    }

    public String getRawMessage() {
        return message;
    }

    public String getMessage() {
        return "[" + user + "] > " + message;
    }

}
