package com.posvert.trasferimenti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.posvert.trasferimenti.common.ResponseHandler;
import com.posvert.trasferimenti.common.URLHelper;

import org.json.JSONObject;

import beans.Annuncio;
import beans.JSONHandler;
import beans.Utente;

public class VisualizzaMatchActivity extends AppCompatActivity {

    private String id;

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
                    Annuncio u = JSONHandler.parseAnnuncioJSON(a);

                    setVal(R.id.regione, u.getRegione());
                    setVal(R.id.provincia, u.getProvincia());
                    setVal(R.id.comune, u.getComune());
                    setVal(R.id.ente, u.getEnte());
                    setVal(R.id.livello, u.getLivello());
                    setVal(R.id.note, u.getNote());
                    setVal(R.id.data, u.getData());

                    fillSecondary(u.getUsername());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
       
    }


    private void fillSecondary(String username){
        URLHelper.invokeURL(this, buildUrlUser(username), new ResponseHandler() {
            @Override
            public void parseResponse(String response) {
                try {
                    JSONObject a = new JSONObject(response);
                    Utente u = JSONHandler.parseUtenteJSON(a);

                    setVal(R.id.sregione, u.getRegione());
                    setVal(R.id.sprovincia, u.getProvincia());
                    setVal(R.id.scomune, u.getComune());
                    setVal(R.id.sente, u.getEnte());
                    setVal(R.id.slivello, u.getLivello());


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private String buildUrlUser(String username) {
        String url =    URLHelper.build(this, "getUtenteByUsername");
        url += "username=" + username;
        return url;
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

    private void setBottoni() {
        Button esci = (Button) findViewById(R.id.esci_vismatch);

        esci.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }
}
