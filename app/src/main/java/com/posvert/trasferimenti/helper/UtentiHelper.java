package com.posvert.trasferimenti.helper;

import android.content.Context;
import android.net.ConnectivityManager;

import com.posvert.trasferimenti.common.Heap;
import com.posvert.trasferimenti.common.ResponseHandler;
import com.posvert.trasferimenti.common.URLHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import beans.JSONHandler;
import beans.Utente;

/**
 * Created by giovanni on 11/11/16.
 */

public class UtentiHelper {
   public static void getUtenteByUsername(Context ctx, String username, final IExecutor job){

       URLHelper.invokeURL(ctx, buildUrl_getUtenteByUsername(ctx, username), new ResponseHandler() {
           @Override
           public void parseResponse(String response) {


               try {
                   JSONObject obj = new JSONObject(response);
                   Utente  u = JSONHandler.parseUtenteJSON(obj);

                   job.exec(u);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       });


   }

    private static String buildUrl_getUtenteByUsername(Context ctx, String username) {
        String url = URLHelper.build(ctx,"getUtenteByUsername" );
        try {
            url += "username="+ URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static boolean isUserOnline(Context ctx)
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
