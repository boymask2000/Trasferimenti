package com.posvert.trasferimenti.common;

import android.app.Activity;
import android.content.Context;

import com.posvert.trasferimenti.RegistrazioneActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by giovanni on 19/10/16.
 */

public class Config {
    private static Properties props=null;
    private static Context context;
    private static boolean inited=false;


    public static String getServerAddress(Activity act) {
        if( !inited) init(act);
        return props.getProperty("server_address");
    }

    public static void init(Activity act) {
        context = act.getBaseContext();
        props = new Properties();
        try {
            InputStream inputStream = context.getAssets().open("config.properties");

            props.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        inited=true;
    }


}
