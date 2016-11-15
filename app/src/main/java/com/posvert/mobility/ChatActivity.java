package com.posvert.mobility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.posvert.mobility.chat.MessagesListAdapter;
import com.posvert.mobility.chat.Messaggio;
import com.posvert.mobility.common.Heap;
import com.posvert.mobility.common.URLHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import beans.JSONHandler;


public class ChatActivity extends AppCompatActivity {
    private WebSocketClient client = null;

    private ListView listViewMessages;
    private List<Messaggio> listMessages;
    private MessagesListAdapter adapter;
    private EditText inputMsg;
    private String utenteAnnuncio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle bundle = getIntent().getExtras();
        String pkg = getPackageName();
        utenteAnnuncio = bundle.getString(pkg + "USERNAME");

        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
        listMessages = new ArrayList<>();

        Button btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);

        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);

        gretingd();

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Sending message to web socket server
                Messaggio u = new Messaggio(Heap.getUserCorrente().getUsername(), utenteAnnuncio);
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

        client = new WebSocketClient(URI.create(URLHelper.buildWSCall(this,utenteAnnuncio)), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {
                Log.e("WS", "Connected!");
                Gson gson = new Gson();
                Messaggio u = new Messaggio(Heap.getUserCorrente().getUsername(), utenteAnnuncio);
             //   currOP = Messaggio.ASK_FOR_CHAT;
                u.setOperation(Messaggio.ASK_FOR_CHAT);

                String msg = gson.toJson(u);
// Later…

                client.send(msg);
            }

            @Override
            public void onMessage(String jsonMessage) {
                Log.e("WS", String.format("Got string message! %s", jsonMessage));


                try {
                    JSONObject jo = new JSONObject(jsonMessage);
                    Messaggio msg = JSONHandler.jsonToMessaggioChat(jo);


                    int azione = msg.getAzione();
                //    String testo = msg.getTesto();

                    if (azione == Messaggio.SEND_MSG) {


                        appendMessage(msg);
                    }

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
    private void gretingd(){
        Messaggio m = new Messaggio();
        m.setMittente("TrasferimentiPA");
        m.setTesto("Stai chattando con "+utenteAnnuncio);
        appendMessage(m);
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
    public void onStop() {
        super.onStop();
    //    client.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
  //      client.disconnect();
    }

    public static Bitmap getFacebookProfilePicture(String userID) throws Exception {
        URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        return bitmap;
    }
}