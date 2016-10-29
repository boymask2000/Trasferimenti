package com.posvert.trasferimenti;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.posvert.trasferimenti.common.Heap;
import com.posvert.trasferimenti.common.ResponseHandler;
import com.posvert.trasferimenti.common.URLHelper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import beans.Annuncio;
import beans.JSONHandler;
import liste.ListaAnnunciAdapter;

public class PaginaAnnunciActivity extends Activity {
    private ListView mylist;
    private Button cercamatch = null;
    private List<Annuncio> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_annunci);
        mylist = (ListView) findViewById(R.id.lista);
        Button creaannuncio = (Button) findViewById(R.id.creaannuncio);
        cercamatch = (Button) findViewById(R.id.cercamatch);


        cercamatch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent act = new Intent(PaginaAnnunciActivity.this, PaginaMatchActivity.class);

                startActivity(act);

            }
        });
        creaannuncio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent openPage1 = new Intent(PaginaAnnunciActivity.this, InserimentoAnnuncioActivity.class);

                startActivity(openPage1);

            }
        });
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {

                Intent act = new Intent(PaginaAnnunciActivity.this, VisualizzaAnnuncioActivity.class);
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

                    mylist.setAdapter(new ListaAnnunciAdapter(PaginaAnnunciActivity.this, lista));


                    if (lista.size() == 0) cercamatch.setEnabled(false);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private String buildUrl() {
        String url = URLHelper.build(this, "cercaAnnunciCreatiDaUtente");

        url += "username=" + Heap.getUserCorrente().getUsername();


        return url;
    }
}
