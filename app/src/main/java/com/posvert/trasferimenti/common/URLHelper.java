package com.posvert.trasferimenti.common;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by giovanni on 29/10/16.
 */

public class URLHelper {
    private static String server = null;
    private static String pref = null;


    public static String build(Context context, String restKey) {
        if (server == null) server = Config.getServerAddress(context);

        if (pref == null) pref = Config.getVal(context, "url_prefix");
        return "http://" + server + pref + restKey + "?";
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
}
