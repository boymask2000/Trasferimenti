package com.posvert.mobility;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.posvert.mobility.common.ResponseHandler;
import com.posvert.mobility.common.URLBuilder;
import com.posvert.mobility.common.URLHelper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import beans.Annuncio;
import beans.Ente;
import beans.JSONHandler;
import beans.MessaggioOffline;
import liste.adapters.ListaAnnunciAdapter;
import liste.adapters.ListaEntiAdapter;

public class GestioneEntiActivity extends AppCompatActivity {
    private List<Ente> lista = new ArrayList<>();
    private ListView mylist;
    private Ente enteSel = null;
    private ListaEntiAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_enti);

        mylist = (ListView) findViewById(R.id.lista);//mylist.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                view.setSelected(true);

                enteSel = (Ente) parent.getItemAtPosition(position);
                Log.e("QQQ", enteSel.getNome());

                Intent data = new Intent();
                data.putExtra("nome",""+enteSel.getNome());

                setResult(RESULT_OK,data);
                finish();


            }
        });
        adapter = new ListaEntiAdapter(GestioneEntiActivity.this, lista);
        mylist.setAdapter(adapter);

        Button inserisci = (Button) findViewById(R.id.inserisci);

        inserisci.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             buildFragment();



                                         }
                                     });

           //     Button seleziona = (Button) findViewById(R.id.seleziona);

        final EditText query = (EditText) findViewById(R.id.query);
        Button cerca = (Button) findViewById(R.id.cerca);

        cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URLHelper.invokeURL(GestioneEntiActivity.this, buuldUrlCerca(query), new ResponseHandler() {
                    @Override
                    public void parseResponse(String response) {
                        Log.e("E", response);
                        lista.clear();
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                Ente u = JSONHandler.parseEnteJSON(array.getJSONObject(i));
                                lista.add(u);
                            }
                    /*        ListaEntiAdapter adapter = new ListaEntiAdapter(GestioneEntiActivity.this, lista);
                            mylist.setAdapter(adapter);*/

                            adapter.notifyDataSetChanged();
                            //     if (lista.size() == 0) cercamatch.setEnabled(false);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void buildFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final Fragment fragment = new NuovoEnteFragment();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.commit();


    }

    private String buuldUrlCerca(EditText query) {
        String url = URLHelper.buildWithPref(this, "enti", "cercaEnteByName", true);

        URLBuilder builder = new URLBuilder(url);
        builder.addParameter("ente", query.getText().toString());

        return builder.getUrl();

    }
}
