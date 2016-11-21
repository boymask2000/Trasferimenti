package com.posvert.mobility;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.posvert.mobility.common.Heap;
import com.posvert.mobility.common.ResponseHandlerPOST;
import com.posvert.mobility.common.SnackMsg;
import com.posvert.mobility.common.URLHelper;

import java.io.StringWriter;

import beans.MessaggioOffline;

public class InviaMessaggioOffineActivity extends AppCompatActivity {

    private String utenteAnnuncio;
    private EditText testoMessaggio;
    private TextView counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invia_messaggio_offine);
        counter = (TextView) findViewById(R.id.counter);

        Bundle bundle = getIntent().getExtras();
        String pkg = getPackageName();
        utenteAnnuncio = bundle.getString(pkg + "USERNAME");

        TextView target = (TextView) findViewById(R.id.destinatario);
        target.setText(utenteAnnuncio);

        Button esci = (Button) findViewById(R.id.esci);

        esci.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }

        });

        Button invia = (Button) findViewById(R.id.invia);

        invia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                URLHelper.invokeURLPOST(InviaMessaggioOffineActivity.this, buildUrlInviaMsg(), new ResponseHandlerPOST() {
                    @Override
                    public void parseResponse(String response) {
                        SnackMsg.showInfoMsg(findViewById(R.id.esci), "Messaggio inviato");

                    }

                    @Override
                    public String getJSONMessage() {
                        StringWriter sw = new StringWriter();
                        MessaggioOffline u = getValori();
                        Gson gson = new Gson();
                        gson.toJson(u, sw);
                        String val = sw.toString();
                        return val;
                    }
                });
            }
        });

        testoMessaggio = (EditText) findViewById(R.id.testo);
        testoMessaggio.addTextChangedListener(new TextWatcher() {
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
    }

    private MessaggioOffline getValori() {
        MessaggioOffline u = new MessaggioOffline();

        u.setMittente(Heap.getUserCorrente().getUsername());
        u.setDestinatario(utenteAnnuncio);

        u.setTesto(((EditText) findViewById(R.id.testo)).getText().toString());

        return u;
    }

    private String buildUrlInviaMsg() {
        return URLHelper.buildPOST(this, "inserisciMessaggio", "messages");

    }
}
