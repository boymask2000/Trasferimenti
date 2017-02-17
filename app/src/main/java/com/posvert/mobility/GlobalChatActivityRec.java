package com.posvert.mobility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.codebutler.android_websockets.WebSocketClient;
import com.google.gson.Gson;
import com.posvert.mobility.chat.MessagesListAdapterRec;
import com.posvert.mobility.chat.Messaggio;
import com.posvert.mobility.chat.UserListAdapterRec;
import com.posvert.mobility.common.Heap;
import com.posvert.mobility.common.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import beans.JSONHandler;
import beans.Utente;
import liste.adapters.ListaChatUsersAdapter;

public class GlobalChatActivityRec extends AppCompatActivity {
    private WebSocketClient client = null;

    private ListView listViewMessages;
    private ListView listViewUsers;
    private List<Messaggio> listMessages = new ArrayList<>();
    private List<Utente> utenti = new ArrayList<>();
  //  private ListaChatUsersAdapter listaUsersAdapter;
    //   private MessagesListAdapter adapter;
    private EditText inputMsg;
    private MessagesListAdapterRec mAdapter;
    private UserListAdapterRec mListaUsersAdapter;

    private RecyclerView listViewMessages_rec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_chat_rec);

 /*       listViewMessages = (ListView) findViewById(R.id.list_view_messages);
        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);*/


        listViewMessages_rec = (RecyclerView) findViewById(R.id.list_view_messages_rec);

        setListaMessaggi(listMessages);





        RecyclerView listViewUsers_rec = (RecyclerView) findViewById(R.id.users);
        mListaUsersAdapter = new UserListAdapterRec(utenti, this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listViewUsers_rec.setLayoutManager(layoutManager);
        listViewUsers_rec.setItemAnimator(new DefaultItemAnimator());
        listViewUsers_rec.setAdapter(mListaUsersAdapter);

     /*   listViewUsers = (ListView) findViewById(R.id.users);
        listaUsersAdapter = new ListaChatUsersAdapter(this, utenti);
        listViewUsers.setAdapter(listaUsersAdapter);*/

        Button btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);


        gretingd();

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Sending message to web socket server
                Messaggio u = new Messaggio(Heap.getUserCorrente().getUsername(), "");
                u.setRoom("PUBLIC");
                Gson gson = new Gson();
                u.setTesto(inputMsg.getText().toString());
                u.setOperation(Messaggio.SEND_MSG, inputMsg.getText().toString());
                u.setSelf(true);
                //         appendMessage(u);
                String msg = gson.toJson(u);
                client.send(msg);

                // Clearing the input filed once message was sent
                inputMsg.setText("");
            }
        });


/*    List<NameValuePair> extraHeaders = Arrays.asList(
            new intBasicNameValuePair("Cookie", "session=abcd")
    );*/

        client = new WebSocketClient(URI.create(URLHelper.buildWSCall(this, Heap.getUserCorrente().getUsername())), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {
                Log.e("WS", "Connected!");
                Gson gson = new Gson();
                Messaggio u = new Messaggio(Heap.getUserCorrente().getUsername(), "");
                //   currOP = Messaggio.ASK_FOR_CHAT;
                u.setOperation(Messaggio.JOIN);
                u.setRoom("PUBLIC");
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

                    switch (azione) {
                        case Messaggio.SEND_MSG:
                            appendMessage(msg);
                            break;
                        case Messaggio.SEND_USER_LIST:
                            fillUserList(msg.getTesto());
                            break;
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

    private void setListaMessaggi(List<Messaggio> ll) {
        mAdapter = new MessagesListAdapterRec(ll);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listViewMessages_rec.setLayoutManager(mLayoutManager);
        listViewMessages_rec.setItemAnimator(new DefaultItemAnimator());
        listViewMessages_rec.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void fillUserList(String json) {
        try {
            JSONArray array = new JSONArray(json);
            utenti.clear();
            for (int i = 0; i < array.length(); i++) {

                String u = array.get(i).toString();
                Log.e("UUU", u);
                Utente uu = new Utente();
                uu.setUsername(u);
         //      for(int j=0;j<20; j++)
                utenti.add(uu);

            }
            mListaUsersAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void gretingd() {
        Messaggio m = new Messaggio();
        m.setMittente("TrasferimentiPA");

        appendMessage(m);
    }

    private void appendMessage(final Messaggio m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);

                mAdapter.notifyDataSetChanged();

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
    protected void onResume() {
        super.onResume();
        //   join();
    }

    @Override
    public void onDestroy() {
        leave();
        super.onDestroy();

        //      client.disconnect();
    }

    private void leave() {
        Gson gson = new Gson();
        Messaggio u = new Messaggio(Heap.getUserCorrente().getUsername(), "");
        //   currOP = Messaggio.ASK_FOR_CHAT;
        u.setOperation(Messaggio.LEAVE);
        u.setRoom("PUBLIC");
        String msg = gson.toJson(u);
// Later…

        client.send(msg);
    }

    private void join() {
        if (client == null) return;
        Gson gson = new Gson();
        Messaggio u = new Messaggio(Heap.getUserCorrente().getUsername(), "");
        //   currOP = Messaggio.ASK_FOR_CHAT;
        u.setOperation(Messaggio.JOIN);
        u.setRoom("PUBLIC");
        String msg = gson.toJson(u);
// Later…

        client.send(msg);
    }

    public static Bitmap getFacebookProfilePicture(String userID) throws Exception {
        URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        return bitmap;
    }
}