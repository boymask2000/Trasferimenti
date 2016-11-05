package com.posvert.trasferimenti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.posvert.trasferimenti.common.Heap;
import com.posvert.trasferimenti.common.ResponseHandler;
import com.posvert.trasferimenti.common.URLHelper;

import org.json.JSONObject;

import beans.Annuncio;
import beans.JSONHandler;
import beans.Utente;

public class VisualizzaMatchActivity extends AppCompatActivity {

    private String id;
    private Annuncio annuncio;
    private Utente utente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_match);

        setBottoni();

        Bundle bundle = getIntent().getExtras();
        String pkg = getPackageName();
        id = bundle.getString(pkg + "ID");




        URLHelper.invokeURL(this, buildUrl(id), new ResponseHandler() {
            @Override
            public void parseResponse(String response) {
                try {
                    JSONObject a = new JSONObject(response);
                    annuncio = JSONHandler.parseAnnuncioJSON(a);

                    setVal(R.id.regione, annuncio.getRegione());
                    setVal(R.id.provincia, annuncio.getProvincia());
                    setVal(R.id.comune, annuncio.getComune());
                    setVal(R.id.ente, annuncio.getEnte());
                    setVal(R.id.livello, annuncio.getLivello());
                    setVal(R.id.note, annuncio.getNote());
                    setVal(R.id.data, annuncio.getData());

                    fillSecondary(annuncio.getUsername());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void fillSecondary(String username) {
        URLHelper.invokeURL(this, buildUrlUser(username), new ResponseHandler() {
            @Override
            public void parseResponse(String response) {
                try {
                    JSONObject a = new JSONObject(response);
                    utente = JSONHandler.parseUtenteJSON(a);

                    setVal(R.id.sregione, utente.getRegione());
                    setVal(R.id.sprovincia, utente.getProvincia());
                    setVal(R.id.scomune, utente.getComune());
                    setVal(R.id.sente, utente.getEnte());
                    setVal(R.id.slivello, utente.getLivello());


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private String buildUrlUser(String username) {
        String url = URLHelper.build(this, "getUtenteByUsername");
        url += "username=" + username;
        return url;
    }

    private void setVal(int id, Object val) {
        if (val == null) return;
        EditText text = (EditText) findViewById(id);
        text.setText(val.toString(), TextView.BufferType.EDITABLE);

    }

    private String buildUrl(String id) {
        String url = URLHelper.build(this, "getAnnuncioById");
        url += "id=" + id;
        return url;
    }

    private String buildUrlContatto(String from, String to, int id) {
        String url = URLHelper.build(this, "creaContatto");
        url += "idannuncio=" + id;
        url += "&mittente=" + from;
        url += "&destinatario=" + to;
        return url;
    }

    private void setBottoni() {
        Button esci = (Button) findViewById(R.id.esci_vismatch);

        esci.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
//---------------------
        Button contatta = (Button) findViewById(R.id.contatta);

        contatta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                URLHelper.invokeURL(VisualizzaMatchActivity.this, buildUrlContatto(Heap.getUserCorrente().getUsername(),utente.getUsername(), annuncio.getId()), new ResponseHandler() {
                    @Override
                    public void parseResponse(String response) {
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
//---------------------
        Button chat = (Button) findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent act = new Intent(VisualizzaMatchActivity.this, ChatActivity.class);

                startActivity(act);
            }
        });
//---------------------
    }
}
