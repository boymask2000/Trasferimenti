package com.posvert.trasferimenti.net;

import android.util.Log;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by giovanni on 03/11/16.
 */

public class NetUtil {

    public static InetAddress getIp() throws SocketException {
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();

        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            if (!ni.isLoopback() && ni.isUp()) {
                for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                    Log.e("IPPPP_PP", ia.getAddress().toString());
                    if (ia.getAddress().getAddress().length == 4)
                        return ia.getAddress();
                }
            }
        }
        return null;
    }

}
