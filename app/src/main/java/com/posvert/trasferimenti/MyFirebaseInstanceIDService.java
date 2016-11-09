package com.posvert.trasferimenti;


import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.posvert.trasferimenti.common.Heap;
import com.posvert.trasferimenti.common.ResponseHandler;
import com.posvert.trasferimenti.common.ResponseHandlerPOST;
import com.posvert.trasferimenti.common.URLHelper;

import java.io.StringWriter;

import beans.Commento;
import beans.UtentePushKey;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(this, refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    public static void sendRegistrationToServer(Context ctx, final String token) {
        if(Heap.getUserCorrente()==null)return;
        URLHelper.invokeURLPOST(ctx, buildUrl(ctx), new ResponseHandlerPOST() {
            @Override
            public void parseResponse(String response) {
                Log.e("TOKEN", response);
            }

            @Override
            public String getJSONMessage() {
                StringWriter sw = new StringWriter();
                UtentePushKey u = new UtentePushKey();
                u.setUsername(Heap.getUserCorrente().getUsername());
                u.setKey(token);
                Log.e("len",""+token.length());
                Gson gson = new Gson();
                gson.toJson(u, sw);
                String val = sw.toString();
                return val;
            }
        });
    }

    private static String buildUrl(Context ctx) {
        String url = URLHelper.buildPOST(ctx, "createUserKey", "push");
Log.e("URL", url);
        return url;
    }
}
