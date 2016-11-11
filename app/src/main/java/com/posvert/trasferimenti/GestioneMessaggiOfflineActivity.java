package com.posvert.trasferimenti;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.posvert.trasferimenti.common.Heap;
import com.posvert.trasferimenti.common.ResponseHandler;
import com.posvert.trasferimenti.common.URLBuilder;
import com.posvert.trasferimenti.common.URLHelper;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import beans.JSONHandler;
import beans.MessaggioOffline;
import liste.adapters.ListaAnnunciOfflineAdapter;


public class GestioneMessaggiOfflineActivity extends AppCompatActivity {
    private List<MessaggioOffline> lista = new ArrayList<>();
    private ListView lv;
    private TextView areatext;
    private MessaggioOffline messaggioSel = null;
    private ListaAnnunciOfflineAdapter adapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_messaggi_offline);

        areatext = (TextView) findViewById(R.id.testo);

        lv = (ListView) findViewById(R.id.listView1);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                view.setSelected(true);

                messaggioSel = (MessaggioOffline) parent.getItemAtPosition(position);
                areatext.setText(messaggioSel.getTesto());

            }
        });


        loadData();

        setBottoni();
    }

    private void setBottoni() {
        Button esci = (Button) findViewById(R.id.esci);
        Button cancella = (Button) findViewById(R.id.cancella);
        Button rispondi = (Button) findViewById(R.id.rispondi);

        esci.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        cancella.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
              if( messaggioSel==null)return;

                URLBuilder b = new URLBuilder(GestioneMessaggiOfflineActivity.this, "deleteMessaggioById", "messages");
                b.addParameter("id", ""+messaggioSel.getId());
                String url = b.getUrl();

                URLHelper.invokeURL(GestioneMessaggiOfflineActivity.this, url, new ResponseHandler() {
                    @Override
                    public void parseResponse(String response) {
                        Snackbar.make(findViewById(R.id.esci), "Messaggio cancellato",
                                Snackbar.LENGTH_LONG)
                                .show();
loadData();
                        adapter.notifyDataSetChanged();
                        areatext.setText("");

                    }
                });

            }
        });
        rispondi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if( messaggioSel==null)return;

                Intent act = new Intent(GestioneMessaggiOfflineActivity.this, InviaMessaggioOffineActivity.class);
                String pkg = getPackageName();
                act.putExtra(pkg + "USERNAME", messaggioSel.getMittente());
                startActivity(act);
            }
        });
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
                     adapter = new ListaAnnunciOfflineAdapter(GestioneMessaggiOfflineActivity.this, lista);
                    lv.setAdapter(adapter);


                    //    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
/*        listaChatAdapter = new ListaChatAdapter(PaginaAnnunciActivity.this, listaChat);
        ChatRequest req = new ChatRequest("pippo");
        listaChat.add(req);*/

    }

    private String buildUrlGetMessaggi() {
        URLBuilder builder = new URLBuilder(this, "getMessagesForUser", "messages");
        builder.addParameter("username", Heap.getUserCorrente().getUsername());
        return builder.getUrl();
      /*  String url = URLHelper.build(this, "getMessagesForUser", "messages");
        try {
            url += "username="+ URLEncoder.encode(Heap.getUserCorrente().getUsername(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;*/
    }
}
