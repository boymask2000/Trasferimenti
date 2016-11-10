package com.posvert.trasferimenti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.posvert.trasferimenti.common.Heap;
import com.posvert.trasferimenti.common.ResponseHandler;
import com.posvert.trasferimenti.common.URLHelper;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import beans.JSONHandler;
import beans.MessaggioOffline;
import liste.AnnunciOfflineAdapter;


public class GestioneMessaggiOfflineActivity extends AppCompatActivity {
    private List<MessaggioOffline> lista =new ArrayList<>();
    private ListView lv;
    private TextView areatext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_messaggi_offline);

areatext= (TextView) findViewById(R.id.testo);

         lv = (ListView) findViewById(R.id.listView1);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                Log.e("CL", ""+position);
// When clicked, show a toast with the TextView text
                MessaggioOffline c = (MessaggioOffline) parent.getItemAtPosition(position);
                areatext.setText(c.getTesto());

            }
        });

        loadData();
    }
    private void loadData() {
        URLHelper.invokeURL(this, buildUrlGetMessaggi(), new ResponseHandler() {
            @Override
            public void parseResponse(String response) {
                lista.clear();

                Log.e("MMM", response);
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        MessaggioOffline u = JSONHandler.parseMessaggioOffline(array.getJSONObject(i));
                        lista.add(u);
                    }
                    AnnunciOfflineAdapter adapter = new AnnunciOfflineAdapter(GestioneMessaggiOfflineActivity.this, lista);
                    lv.setAdapter(adapter);




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
/*        listaChatAdapter = new ListaChatAdapter(PaginaAnnunciActivity.this, listaChat);
        ChatRequest req = new ChatRequest("pippo");
        listaChat.add(req);
        listaChatAdapter.notifyDataSetChanged();*/
    }

    private String buildUrlGetMessaggi() {
        String url = URLHelper.build(this, "getMessagesForUser", "messages");
        try {
            url += "username="+ URLEncoder.encode(Heap.getUserCorrente().getUsername(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
