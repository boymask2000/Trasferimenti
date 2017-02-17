package com.posvert.mobility.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giovanni on 23/12/16.
 */

public class RoomSet {
    private Map<String, Room> map = new HashMap<>();

    public RoomSet() {
        createRoom("PUBLIC");
    }

    public Room createRoom(String name) {
        Room r = new Room(name);
        map.put(name, r);
        return r;
    }

    public void destroyRoom(String name) {
        map.remove(name);
    }

    public void receiveMessage(Messaggio m) {
        String roo = m.getRoom();
        Room r = map.get(roo);
        if (r == null) r = createRoom(roo);
        r.receiveMessage(m);
    }
}
