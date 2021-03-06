package com.posvert.mobility;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.posvert.mobility.common.Heap;
import com.posvert.mobility.common.ResponseHandler;
import com.posvert.mobility.common.URLHelper;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import beans.Annuncio;
import beans.JSONHandler;
import liste.adapters.ListaMatchAdapter;

public class PaginaMatchActivity extends AppCompatActivity {
    private ListView mylist;

    private List<Annuncio> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_match);

        AdView mAdView = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mylist = (ListView) findViewById(R.id.lista);
        Button esci = (Button) findViewById(R.id.esci);

        esci.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                finish();

            }
        });

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {

                Intent act = new Intent(PaginaMatchActivity.this, VisualizzaMatchActivity.class);
                Annuncio an = lista.get(pos);
                String pkg = getPackageName();
                act.putExtra(pkg + "ID", "" + an.getId());
                startActivity(act);
            }
        });

        URLHelper.invokeURL(this, buildUrl(), new ResponseHandler() {
            @Override
            public void parseResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        Annuncio u = JSONHandler.parseAnnuncioJSON(array.getJSONObject(i));
                        lista.add(u);
                    }

                    mylist.setAdapter(new ListaMatchAdapter(PaginaMatchActivity.this, lista));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private String buildUrl() {
        String url = URLHelper.build(this, "cercaMatchAnnunciPerUtente");

        try {
            url += "username=" + URLEncoder.encode(Heap.getUserCorrente().getUsername(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return url;
    }
}
