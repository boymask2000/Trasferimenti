package com.posvert.trasferimenti;

import android.app.AlertDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.posvert.trasferimenti.common.Heap;
import com.posvert.trasferimenti.common.ResponseHandlerPOST;
import com.posvert.trasferimenti.common.URLHelper;

import java.io.StringWriter;

import beans.Commento;
import beans.Utente;

public class InviaCommentoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invia_commento);
        setBottoni();
    }

    private void setBottoni() {
        Button esci = (Button) findViewById(R.id.esci);

        esci.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }

        });

        Button invia = (Button) findViewById(R.id.invia);

        invia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                URLHelper.invokeURLPOST(InviaCommentoActivity.this, buildUrlPOST(), new ResponseHandlerPOST() {
                    @Override
                    public void parseResponse(String response) {
                        AlertDialog.Builder builder = new
                                AlertDialog.Builder(InviaCommentoActivity.this);

                        builder.setTitle("Avviso");
                        builder.setMessage("Attenzione! Questo è un avviso!");
                        builder.setCancelable(true);
                        AlertDialog alert = builder.create();
                        Log.e("QQQ", response);
                        Snackbar.make(findViewById(R.id.esci), "ok",
                                Snackbar.LENGTH_LONG)
                                .show();

                        finish();
                    }

                    @Override
                    public String getJSONMessage() {
                        StringWriter sw = new StringWriter();
                        Commento u = getValori();
                        Gson gson = new Gson();
                        gson.toJson(u, sw);
                        String val = sw.toString();
                        return val;
                    }
                });
            }

        });
    }

    private Commento getValori() {
        Commento u = new Commento();

        u.setUsername(Heap.getUserCorrente().getUsername());

        u.setMessaggio(((EditText) findViewById(R.id.commento)).getText().toString());

        return u;
    }

    private String buildUrlPOST() {
        return URLHelper.buildPOST(this, "inserisciCommento");

    }
}
