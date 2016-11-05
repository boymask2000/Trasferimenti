package com.posvert.trasferimenti;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.codebutler.android_websockets.WebSocketClient;
import com.google.gson.Gson;
import com.posvert.trasferimenti.chat.MessagesListAdapter;
import com.posvert.trasferimenti.chat.Messaggio;
import com.posvert.trasferimenti.common.Heap;
import com.posvert.trasferimenti.common.URLHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import beans.JSONHandler;
import beans.Utente;


public class ChatActivity extends AppCompatActivity {
    private WebSocketClient client = null;
    private int currOP = 0;
    private ListView listViewMessages;
    private List<Messaggio> listMessages;
    private MessagesListAdapter adapter;
    private EditText inputMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
        listMessages = new ArrayList<>();

        Button btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);

        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Sending message to web socket server
                Messaggio u = new Messaggio(Heap.getUserCorrente().getUsername(), "joe");
                Gson gson = new Gson();
                u.setTesto(inputMsg.getText().toString());
                u.setOperation(Messaggio.SEND_MSG, inputMsg.getText().toString());
                u.setSelf(true);
                appendMessage(u);
                String msg = gson.toJson(u);
                client.send(msg);

                // Clearing the input filed once message was sent
                inputMsg.setText("");
            }
        });


/*    List<NameValuePair> extraHeaders = Arrays.asList(
            new intBasicNameValuePair("Cookie", "session=abcd")
    );*/

        client = new WebSocketClient(URI.create(URLHelper.buildWSCall(this)), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {
                Log.e("WS", "Connected!");
                Gson gson = new Gson();
                Messaggio u = new Messaggio("joe", "joe");
                currOP = Messaggio.ASK_FOR_CHAT;
                u.setOperation(Messaggio.ASK_FOR_CHAT);

                String msg = gson.toJson(u);
// Later…

                client.send(msg);
            }

            @Override
            public void onMessage(String message) {
                Log.e("WS", String.format("Got string message! %s", message));

                try {
                    JSONObject obj = new JSONObject(message);
                    String msg = obj.getString("message");

                    Messaggio m = new Messaggio();
                    m.setTesto(msg);
                    appendMessage(m);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onMessage(byte[] data) {
                //      Log.d("y", String.format("Got binary message! %s", toHexString(data)));
            }

            @Override
            public void onDisconnect(int code, String reason) {
                Log.e("WS", String.format("Disconnected! Code: %d Reason: %s", code, reason));
            }

            @Override
            public void onError(Exception error) {
                Log.e("WS", "Error!", error);
            }
        }, null);


        client.connect();


        // client.send(new byte[] { 0xDE, 0xAD, 0xBE, 0xEF });
    }

    private void appendMessage(final Messaggio m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);

                adapter.notifyDataSetChanged();

                // Playing device's notification
                playBeep();
            }
        });
    }

    public void playBeep() {

        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        client.disconnect();
    }

}