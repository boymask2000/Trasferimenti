package com.posvert.mobility;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.posvert.mobility.common.URLHelper;

import org.json.JSONObject;

import beans.Annuncio;
import beans.JSONHandler;

public class VisualizzaAnnuncioActivity extends AppCompatActivity {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_annuncio);

        setBottoni();

        Bundle bundle = getIntent().getExtras();

        //Stringa univoca per identificare i dati
        String pkg = getPackageName();

        //Recupero la stringa passata
        id = bundle.getString(pkg + "ID");

        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        String url = buildUrl(id);
        Log.e("URL", url);
// Request a string response from the provided URL.5.95.234.131
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Log.e("QQQQQQQQQQ", response);

                        parseResponse(response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //        mTextView.setText("That didn't work!");

                Log.e("EEEEEEE", error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.start();
    }

    private void setBottoni() {
        Button esci = (Button) findViewById(R.id.esci);
        esci.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                finish();

            }
        });

        Button elimina = (Button) findViewById(R.id.elimina);
        elimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                RequestQueue queue = Volley.newRequestQueue(getBaseContext());
                String url = buildUrlElimina(id);
                Log.e("URL", url);
// Request a string response from the provided URL.5.95.234.131
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                Log.e("QQQQQQQQQQ", response);

                                finish();
                                // parseResponse(response);


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //        mTextView.setText("That didn't work!");

                        Log.e("EEEEEEE", error.toString());
                    }
                });
// Add the request to the RequestQueue.
                queue.add(stringRequest);
                queue.start();

            }
        });
    }

    private String buildUrlElimina(String id) {
        String url = URLHelper.build(this, "deleteAnnuncioById");

        url += "id=" + id;


        return url;
    }

    private void parseResponse(String response) {

        try {
            JSONObject a = new JSONObject(response);
            Annuncio u = JSONHandler.parseAnnuncioJSON(a);

            setVal(R.id.regione, u.getRegione());
            setVal(R.id.provincia, u.getProvincia());
            setVal(R.id.comune, u.getComune());
            setVal(R.id.ente, u.getEnte());
            setVal(R.id.livello, u.getLivello());
            setVal(R.id.note, u.getNote());
            setVal(R.id.data, u.getData());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setVal(int id, Object val) {
        EditText text = (EditText) findViewById(id);
        if (val == null) return;
        text.setText(val.toString(), TextView.BufferType.EDITABLE);
    }

    private String buildUrl(String id) {
        String url = URLHelper.build(this, "getAnnuncioById");

        url += "id=" + id;


        return url;
    }
}

