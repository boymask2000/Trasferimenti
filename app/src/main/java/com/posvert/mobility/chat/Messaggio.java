package com.posvert.mobility.chat;

/**
 * Created by giovanni on 05/11/16.
 */


public class Messaggio {
    public static final int ASK_FOR_CHAT = 1;
    public static final int SEND_MSG = 2;
    public static final int JOIN = 3;
    public static final int LEAVE = 4;
    public static final int SEND_USER_LIST = 5;

    private String mittente;
    private String destinatario;
    private String testo="";
    private String room="";
    private int azione;
    private boolean self;

    public Messaggio(){

    }

    public Messaggio(String mittente, String destinatario) {
        this.mittente = mittente;
        this.destinatario = destinatario;
    }

    public void setOperation(int azione, String... parms) {
        this.azione = azione;

        switch (azione) {
            case ASK_FOR_CHAT:
                break;
            case SEND_MSG:
                testo=parms[0];
                break;
        }

    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getMittente() {
        return mittente;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public int getAzione() {
        return azione;
    }

    public void setAzione(int azione) {
        this.azione = azione;
    }

    public static int getAskForChat() {
        return ASK_FOR_CHAT;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

}
