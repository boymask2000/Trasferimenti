package com.posvert.mobility.common;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.posvert.mobility.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by giovanni on 29/10/16.
 */

public class URLHelper {
    private static String server = null;
    private static String serverWS = null;



    public static String buildWithPref(Context context,  String prefix,String restKey, boolean isGet) {
        if (server == null) server = Config.getServerAddress(context);

        String url = "http://" + server +  prefix+"/" + restKey;

        return url;
    }

    public static String build(Context context, String restKey) {
        if (server == null) server = Config.getServerAddress(context);

        return "http://" + server  + restKey + "?";
    }

    public static String build(Context context, String restKey, String prefix) {
        if (server == null) server = Config.getServerAddress(context);

        return "http://" + server + prefix+"/" + restKey + "?";
    }

    public static String buildPOST(Context context, String restKey) {
        if (server == null) server = Config.getServerAddress(context);

        return "http://" + server  + restKey;
    }

    public static String buildPOST(Context context, String restKey, String prefix) {
        if (server == null) server = Config.getServerAddress(context);

        return "http://" + server + prefix+"/" + restKey;
    }

    public static String buildWSCall(Context context, String utenteAnnuncio) {
        if (serverWS == null) serverWS = BuildConfig.HOSTWS;



        String url = null;
        try {
            url = "ws://" + serverWS ;
            URLBuilder builder = new URLBuilder(url);
            builder.addParameter("name", Heap.getUserCorrente().getUsername());
            builder.addParameter("dest", utenteAnnuncio);
            url = builder.getUrl();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("WS", url);
        return url;
    }

    public static void invokeURL(Context ctx, String url, final ResponseHandler handler) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        Log.e("URL", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Log.e("RESPONSE", response);

                        handler.parseResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //        mTextView.setText("That didn't work!");
                Log.e("ERRORE", error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.start();
    }


    public static void invokeURLPOST(Context ctx, String url, final ResponseHandlerPOST handler) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        Log.e("URL", url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Log.e("RESPONSE", response);

                        handler.parseResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //        mTextView.setText("That didn't work!");
                Log.e("ERRORE", error.toString());
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String msg = handler.getJSONMessage();
                if (msg == null) return null;
                return msg.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.start();
    }
}
