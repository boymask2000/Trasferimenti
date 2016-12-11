package com.posvert.mobility.helper;

import android.content.Context;
import android.widget.EditText;

import com.google.gson.Gson;
import com.posvert.mobility.InviaMessaggioOffineActivity;
import com.posvert.mobility.R;
import com.posvert.mobility.common.Heap;
import com.posvert.mobility.common.ResponseHandlerPOST;
import com.posvert.mobility.common.SnackMsg;
import com.posvert.mobility.common.URLHelper;

import java.io.StringWriter;

import beans.MessaggioOffline;

/**
 * Created by giovanni on 09/12/16.
 */

public class MessaggioOfflineHelper {
    public static void sendMessage(Context context, String mittente, String destinatario, String testo){


        final MessaggioOffline u = new MessaggioOffline();

        u.setMittente(mittente);
        u.setDestinatario(destinatario);

        u.setTesto(testo);

        URLHelper.invokeURLPOST(context, buildUrlInviaMsg(context), new ResponseHandlerPOST() {
            @Override
            public void parseResponse(String response) {


            }

            @Override
            public String getJSONMessage() {
                StringWriter sw = new StringWriter();

                Gson gson = new Gson();
                gson.toJson(u, sw);
                String val = sw.toString();
                return val;
            }
        });
    }
    private static String buildUrlInviaMsg(Context context) {
        return URLHelper.buildPOST(context, "inserisciMessaggio", "messages");

    }
}
