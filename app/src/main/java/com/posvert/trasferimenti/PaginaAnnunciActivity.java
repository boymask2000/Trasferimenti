package com.posvert.trasferimenti;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.posvert.trasferimenti.common.Config;
import com.posvert.trasferimenti.common.Heap;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import beans.Annuncio;
import beans.Utente;
import beans.JSONHandler;
import liste.ListaAnnunciAdapter;

public class PaginaAnnunciActivity extends Activity {
    private ListView mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_annunci);
        mylist = (ListView) findViewById(R.id.lista);
        Button  bottone2 = (Button) findViewById(R.id.bottone2);

        bottone2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent openPage1 = new Intent(PaginaAnnunciActivity.this, InserimentoAnnuncioActivity.class);


                startActivity(openPage1);

            }
        });
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                Log.d("", "Selezionato " + pos + " " + id);
            }
        });


        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        String url = buildUrl();
        Log.e("URL", url);
// Request a string response from the provided URL.5.95.234.131
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Log.e("QQQQQQQQQQ", response);

                        parseResonse(response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //        mTextView.setText("That didn't work!");
                System.out.println(error);
                Log.e("EEEEEEE", error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.start();
    }

    private void parseResonse(String response) {
        List<Annuncio> lista = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);

            for (int i = 0; i < array.length(); i++) {

                Annuncio u = JSONHandler.parseAnnuncioJSON(array.getJSONObject(i));
                lista.add(u);
            }


        //    final ArrayAdapter<Annuncio> adapter = new ArrayAdapter<Annuncio>(this, android.R.layout.simple_list_item_1, lista);
         //   setListAdapter(new ListaAnnunciAdapter(this, lista ));
            // inietto i dati
            mylist.setAdapter(new ListaAnnunciAdapter(this, lista ));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildUrl() {
        String server = Config.getServerAddress(this);

        String url = "http://" + server + ":8080/Trasferimenti/trasferimenti/cercaAnnunciCreatiDaUtente?";
        url += "username=" + Heap.getUserCorrente().getUsername();


        return url;
    }
}
