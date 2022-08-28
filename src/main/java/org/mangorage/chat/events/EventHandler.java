package org.mangorage.chat.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class EventHandler {
    private final HashMap<Class<? extends Event>, List<Consumer>> listenerList = new HashMap<>();

    public <T extends Event> void subscribe(Class<? extends Event> classz, Consumer<T> listener) {
        if (!listenerList.containsKey(classz))
            listenerList.put(classz, new ArrayList<>());
        if (!listenerList.get(classz).contains(listener))
            listenerList.get(classz).add(listener);
    }

    public <T extends Event> void unSubscribe(Class<? extends Event> classz, Consumer<T> listener) {
        if (listenerList.containsKey(classz))
            if (listenerList.get(classz).contains(listener))
                listenerList.get(classz).remove(listener);
    }

    public void post(Event event) {
        if (listenerList.containsKey(event.getClass()))
            listenerList.get(event.getClass()).forEach(listener -> {
                listener.accept(event);
            });
    }

}
