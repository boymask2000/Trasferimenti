package com.posvert.mobility;

import android.app.AlertDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.BaseKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.posvert.mobility.common.Heap;
import com.posvert.mobility.common.ResponseHandlerPOST;
import com.posvert.mobility.common.URLHelper;

import java.io.StringWriter;

import beans.Commento;

public class InviaCommentoActivity extends AppCompatActivity {
    private TextView counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invia_commento);

         counter = (TextView) findViewById(R.id.counter);

        setBottoni();
    }

    private void setBottoni() {

        EditText text = (EditText) findViewById(R.id.commento);
       text.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               int len = charSequence.toString().length();

               counter.setText(""+(200-len)+" caratteri disponibili");
           }

           @Override
           public void afterTextChanged(Editable editable) {
               int len = editable.toString().length();
             if( len>200)  editable.delete(200, len);
           }
       });


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
        String testo = ((EditText) findViewById(R.id.commento)).getText().toString();
        if( testo.length()>200)testo= testo.substring(0,200);

        u.setMessaggio(testo);

        return u;
    }

    private String buildUrlPOST() {
        return URLHelper.buildPOST(this, "inserisciCommento");

    }
}
