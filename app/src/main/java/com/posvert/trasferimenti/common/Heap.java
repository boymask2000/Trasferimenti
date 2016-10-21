package com.posvert.trasferimenti.common;

import beans.Utente;

/**
 * Created by giovanni on 21/10/16.
 */

public class Heap {
    private static Utente userCorrente;
    
    public static Utente getUserCorrente() {
        return userCorrente;
    }

    public static void setUserCorrente(Utente userCorrente) {
        Heap.userCorrente = userCorrente;
    }


}
