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
    private static Properties props = null;
    private static boolean inited = false;


    public static String getServerAddress(Context context) {
        if (!inited) init(context);
        return props.getProperty("server_address");
    }
    public static String getVal(Context context, String key) {
        if (!inited) init(context);
        return props.getProperty(key);
    }

    public static void init(Context context) {
        //   context = act.getBaseContext();
        props = new Properties();
        try {
            InputStream inputStream = context.getAssets().open("config.properties");

            props.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        inited = true;
    }


}
