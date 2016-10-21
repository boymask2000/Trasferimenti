package com.posvert.trasferimenti;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.posvert.trasferimenti.common.Config;
import com.posvert.trasferimenti.common.Heap;

public class InserimentoAnnuncioActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_annuncio);

        Button bottone1 = (Button) findViewById(R.id.inserisci);

        bottone1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                RequestQueue queue = Volley.newRequestQueue(getBaseContext());
                String url = buildUrl();
                Log.e("URL",url);
// Request a string response from the provided URL.5.95.234.131
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                Log.e("QQQQQQQQQQ",response);
                                finish();
                                // Display the first 500 characters of the response string.
                                //         mTextView.setText("Response is: "+ response.substring(0,500));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //        mTextView.setText("That didn't work!");
                        System.out.println(error);
                        Log.e("EEEEEEE",error.toString());
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

        String url = "http://" + server + ":8080/Trasferimenti/trasferimenti/createAnnuncio?";
        url += "username=" + Heap.getUserCorrente().getUsername();

        url += "&regione=" + ((EditText) findViewById(R.id.regione)).getText().toString();
        url += "&provincia=" + ((EditText) findViewById(R.id.provincia)).getText().toString();
        url += "&comune=" + ((EditText) findViewById(R.id.comune)).getText().toString();
        url += "&ente=" + ((EditText) findViewById(R.id.ente)).getText().toString();
        url += "&livello=" + ((EditText) findViewById(R.id.livello)).getText().toString();
        url += "&tipo=A";
        return url;
    }
}
