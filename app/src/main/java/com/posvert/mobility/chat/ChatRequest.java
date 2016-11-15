package com.posvert.mobility.chat;

/**
 * Created by gposabella on 11/9/16.
 */

public class ChatRequest {


    private final String username;

    public ChatRequest(String username) {
        this.username=username;
    }

    public String getUsername() {
        return username;
    }
}
