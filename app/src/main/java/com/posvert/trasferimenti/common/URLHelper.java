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

    public static String build( Context context, String restKey){
        String server = Config.getServerAddress(context);

        String pref = Config.getVal(context, "url_prefix");
        return "http://"+server+pref+restKey+"?";
    }

    public static void invokeURL(Context ctx, String url,final ResponseHandler handler){
        RequestQueue queue = Volley.newRequestQueue(ctx);
      //  String url = buildUrl();
        Log.e("URL", url);
// Request a string response from the provided URL.5.95.234.131
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Log.e("QQQQQQQQQQ", response);

                        handler.parseResponse(response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //        mTextView.setText("That didn't work!");
                System.out.println(error);
                Log.e("EEEEEEE", error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.start();
    }
}
