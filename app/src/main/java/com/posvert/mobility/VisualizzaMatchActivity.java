package com.posvert.mobility;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.posvert.mobility.common.Config;
import com.posvert.mobility.common.Heap;
import com.posvert.mobility.common.ResponseHandler;
import com.posvert.mobility.common.URLBuilder;
import com.posvert.mobility.common.URLHelper;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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


    private void fillSecondary(final String username) {
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
                    setVal(R.id.autoreannuncio, username);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private String buildUrlUser(String username) {
        String url = URLHelper.build(this, "getUtenteByUsername");
        try {
            url += "username=" + URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
        Button chat = (Button) findViewById(R.id.chat);
        chat.setVisibility(View.GONE);
        if (Config.isChatEnabled(this)) {
            chat.setVisibility(View.VISIBLE);
            chat.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //   startActivity(new Intent(VisualizzaMatchActivity.this, ChatTabActivity.class));
                    Intent act = new Intent(VisualizzaMatchActivity.this, ChatActivity.class);

                    String pkg = getPackageName();
                    act.putExtra(pkg + "USERNAME", utente.getUsername());
                    startActivity(act);
                }
            });
        }
//---------------------
        Button messaggio = (Button) findViewById(R.id.messaggio);
        messaggio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent act = new Intent(VisualizzaMatchActivity.this, InviaMessaggioOffineActivity.class);
                String pkg = getPackageName();
                act.putExtra(pkg + "USERNAME", utente.getUsername());
                startActivity(act);
            }
        });

        Button nascondi = (Button) findViewById(R.id.nascondi);

        nascondi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VisualizzaMatchActivity.this);

// 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Se procedi non vedrai più questo annuncio. Sei sicuro ?")
                        .setTitle("Attenzione!");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        URLHelper.invokeURL(VisualizzaMatchActivity.this, buildUrlNascondi(), new ResponseHandler() {
                            @Override
                            public void parseResponse(String response) {

                            }
                        });
                    }


                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private String buildUrlNascondi() {
        URLBuilder builder = new URLBuilder(VisualizzaMatchActivity.this, "inserisci", "nonvedo");
        builder.addParameter("idannuncio", "" + id);
        builder.addParameter("username", Heap.getUserCorrente().getUsername());
        return builder.getUrl();
    }
}
