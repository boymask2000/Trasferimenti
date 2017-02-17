package com.posvert.mobility.chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 23/12/16.
 */

public class Room {
    private final String name;
    private List<Messaggio> listaMessaggi = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    public void receiveMessage(Messaggio m) {
        listaMessaggi.add(m);
    }
}
