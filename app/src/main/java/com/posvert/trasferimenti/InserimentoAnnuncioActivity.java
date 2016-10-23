package com.posvert.trasferimenti;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.posvert.trasferimenti.common.Config;
import com.posvert.trasferimenti.common.Heap;
import com.posvert.trasferimenti.common.SpinnerInitializer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import beans.Utente;

public class InserimentoAnnuncioActivity extends Activity {
    private Spinner spinnerProvince;
    private Spinner spinnerRegioni;
    private Spinner spinnerComuni;

    private SpinnerInitializer spinnerInitializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_annuncio);

        spinnerRegioni = (Spinner) findViewById(R.id.spinnerRegioni);
        spinnerProvince = (Spinner) findViewById(R.id.spinnerProvince);
        spinnerComuni = (Spinner) findViewById(R.id.spinnerComuni);

        spinnerInitializer = new SpinnerInitializer(spinnerRegioni, spinnerProvince, spinnerComuni, this);

        Button bottone1 = (Button) findViewById(R.id.inserisci);

        bottone1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

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
                                finish();
                                // Display the first 500 characters of the response string.
                                //         mTextView.setText("Response is: "+ response.substring(0,500));
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
        });

    }

    //http://localhost:8080/Trasferimenti/trasferimenti/createAnnuncio?regione=Campania&username=giovanni&tipo=A
    private String buildUrl() {
        String server = Config.getServerAddress(this);
Utente user=Heap.getUserCorrente();
        String url = "http://" + server + ":8080/Trasferimenti/trasferimenti/createAnnuncio?";
        try {
            url += "username=" + Heap.getUserCorrente().getUsername();

            url += "&regione=" + spinnerInitializer.getRegione();
            url += "&provincia=" + spinnerInitializer.getProvincia();
            url += "&comune=" + URLEncoder.encode(spinnerInitializer.getComune(), "UTF-8");
            url += "&ente=" + URLEncoder.encode(((EditText) findViewById(R.id.ente)).getText().toString(), "UTF-8");

            url += "&note=" + URLEncoder.encode(((EditText) findViewById(R.id.note)).getText().toString(), "UTF-8");

            url += "&livello=" + ((EditText) findViewById(R.id.livello)).getText().toString();
            url += "&tipo=A";

            url += "&regione_s=" + user.getRegione();
            url += "&provincia_s=" + user.getProvincia();
            url += "&comune_s=" + URLEncoder.encode(user.getComune(), "UTF-8");
            url += "&ente_s=" + URLEncoder.encode(user.getEnte(), "UTF-8");
            url += "&livello_s=" + user.getLivello();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
