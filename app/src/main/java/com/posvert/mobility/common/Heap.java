package com.posvert.mobility.common;

import beans.Utente;

/**
 * Created by giovanni on 21/10/16.
 */

public class Heap {
    private static boolean loginFB = false;
    private static Utente userCorrente;

    public static Utente getUserCorrente() {
        return userCorrente;
    }

    public static void setUserCorrente(Utente userCorrente) {
        Heap.userCorrente = userCorrente;
    }

    public static void setLoginFB(boolean loginFB) {
        Heap.loginFB = loginFB;
    }

    public static boolean isLoginFB() {
        return loginFB;
    }
}
