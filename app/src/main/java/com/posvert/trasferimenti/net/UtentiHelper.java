package com.posvert.trasferimenti.net;

import android.app.Activity;

import com.posvert.trasferimenti.LoginFBActivity;
import com.posvert.trasferimenti.common.ResponseHandler;
import com.posvert.trasferimenti.common.URLHelper;

import org.json.JSONObject;

import beans.JSONHandler;
import beans.Utente;

/**
 * Created by giovanni on 06/11/16.
 */

public class UtentiHelper {
    public static Utente getUtenteByUsername(Activity act, String username) {
        final Utente[] u = new Utente[1];
        URLHelper.invokeURL(act, buildCheckUrl(act, username), new ResponseHandler() {
            @Override
            public void parseResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                     u[0] = JSONHandler.parseUtenteJSON(obj);
                }catch( Exception e ){

                }
            }
        });
        return u[0];
    }

    private static String buildCheckUrl(Activity act, String username) {
        String url = URLHelper.build(act, "getUtenteByUsername");
        url += "username=" + username;
        return url;
    }
}
