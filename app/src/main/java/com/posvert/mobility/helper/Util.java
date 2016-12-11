package com.posvert.mobility.helper;

/**
 * Created by giovanni on 09/12/16.
 */

public class Util {
    public static boolean isEmpty(String s) {
        if (s == null) return true;
        if (s.trim().length() == 0) return true;
        return false;
    }
}
