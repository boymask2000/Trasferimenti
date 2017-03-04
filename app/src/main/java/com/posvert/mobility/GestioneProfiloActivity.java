package com.posvert.mobility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.posvert.mobility.common.Heap;
import com.posvert.mobility.common.ResponseHandlerPOST;
import com.posvert.mobility.common.SnackMsg;
import com.posvert.mobility.common.SpinnerInitializer;
import com.posvert.mobility.common.URLHelper;

import java.io.StringWriter;

import beans.Utente;

public class GestioneProfiloActivity extends AppCompatActivity {

    private Spinner spinner_Regioni;
    private Spinner spinner_Province;
    private Spinner spinner_Comuni;
    private SpinnerInitializer spinnerInitializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_profilo);

        setBottoni();
        if (Heap.isLoginFB()) {
            EditText pass = (EditText) findViewById(R.id.password);
            pass.setVisibility(View.GONE);
        }

        spinner_Regioni = (Spinner) findViewById(R.id.spinnerRegioni);
        spinner_Province = (Spinner) findViewById(R.id.spinnerProvince);
        spinner_Comuni = (Spinner) findViewById(R.id.spinnerComuni);


        spinner_Regioni.setEnabled(false);
        spinner_Province.setEnabled(false);
        spinner_Comuni.setEnabled(false);

        spinnerInitializer = new SpinnerInitializer(spinner_Regioni, spinner_Province, spinner_Comuni, this, false, false);

        setValori();
    }

    private void setBottoni() {
        Button sblocca = (Button) findViewById(R.id.sblocca);
        sblocca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                spinnerInitializer.setListeners();
                spinner_Regioni.setEnabled(true);
                spinner_Province.setEnabled(true);
                spinner_Comuni.setEnabled(true);
            }

        });

        Button esci = (Button) findViewById(R.id.esci);

        esci.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }

        });

        Button salva = (Button) findViewById(R.id.salva);

        salva.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!campiObbligatoriOK()) return;

                URLHelper.invokeURLPOST(GestioneProfiloActivity.this, buildUrlPOST(), new ResponseHandlerPOST() {
                    @Override
                    public void parseResponse(String response) {
                        Log.e("QQQ", response);
                        SnackMsg.showInfoMsg(findViewById(R.id.esci), "Modifica eseguita");
                     /*   Snackbar.make(findViewById(R.id.esci), "Modifica eseguita",
                                Snackbar.LENGTH_LONG)
                                .show();*/

                    }

                    @Override
                    public String getJSONMessage() {
                        StringWriter sw = new StringWriter();
                        Utente u = getValori();
                        Gson gson = new Gson();
                        gson.toJson(u, sw);
                        String val = sw.toString();
                        return val;
                    }
                });
            }

        });
        Button cercaEnte = (Button) findViewById(R.id.cercaEnte);


        cercaEnte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent act = new Intent(GestioneProfiloActivity.this, GestioneEntiActivity.class);

                startActivityForResult(act, 1);

            }
        });
        QualificaXMLHandler qualificaXML = new QualificaXMLHandler(this);
    }

    private String codiceQualifica = "";
    private int iCodiceQualifica = 0;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode != QualificaXMLHandler.ID) {
                String nome = data.getStringExtra("nome");
                EditText t = (EditText) findViewById(R.id.ente);
                t.setText(nome);
            }

            if (requestCode == QualificaXMLHandler.ID && data != null) {
                String nome = data.getStringExtra("nome");
                Utente u = Heap.getUserCorrente();

                codiceQualifica = data.getStringExtra("codice");
                if (codiceQualifica != null) {
                    try {
                        iCodiceQualifica = Integer.parseInt(codiceQualifica);
                        u.setCodQualifica(iCodiceQualifica);
                    } catch (NumberFormatException e) {

                    }
                }

                EditText t = (EditText) findViewById(R.id.qualifica);
                t.setText(nome);
            }
        }
    }

    private Utente getValori() {
        Utente u = Heap.getUserCorrente();

        if (spinner_Province.getSelectedItem() != null)
            u.setProvincia(spinner_Province.getSelectedItem().toString());
        if (spinner_Comuni.getSelectedItem() != null)
            u.setComune(spinner_Comuni.getSelectedItem().toString());

        u.setPassword(((EditText) findViewById(R.id.password)).getText().toString());
        u.setRegione(spinner_Regioni.getSelectedItem().toString());


        u.setEnte(((EditText) findViewById(R.id.ente)).getText().toString());
        u.setTelefono(((EditText) findViewById(R.id.telefono)).getText().toString());
        u.setEmail(((EditText) findViewById(R.id.email)).getText().toString());
        u.setLivello(((EditText) findViewById(R.id.livello)).getText().toString());
        u.setCodQualifica(iCodiceQualifica);
        return u;
    }

    private String buildUrlPOST() {
        return URLHelper.buildPOST(this, "aggiornaUtenteByUsername");

    }

    private void setValori() {
        ((TextView) findViewById(R.id.username)).setText(Heap.getUserCorrente().getUsername());
        ((EditText) findViewById(R.id.password)).setText(Heap.getUserCorrente().getPassword());
        ((EditText) findViewById(R.id.email)).setText(Heap.getUserCorrente().getEmail());
        ((EditText) findViewById(R.id.telefono)).setText(Heap.getUserCorrente().getTelefono());
        ((EditText) findViewById(R.id.ente)).setText(Heap.getUserCorrente().getEnte());
        ((EditText) findViewById(R.id.livello)).setText(Heap.getUserCorrente().getLivello());

        ((EditText) findViewById(R.id.qualifica)).setText(Heap.getUserCorrente().getDescQualifica());

        spinner_Regioni.setSelection(getIndex(spinner_Regioni, Heap.getUserCorrente().getRegione()));

        spinnerInitializer.fillSpinnerProvince(spinner_Province, Heap.getUserCorrente().getRegione());
        spinner_Province.setSelection(getIndex(spinner_Province, Heap.getUserCorrente().getProvincia()));

        spinnerInitializer.fillSpinnerComuni(spinner_Comuni, Heap.getUserCorrente().getProvincia());
        spinner_Comuni.setSelection(getIndex(spinner_Comuni, Heap.getUserCorrente().getComune()));


        //      spinnerInitializer.setListeners();
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                Log.e("AAA1", "" + index);
                break;
            }
        }

        return index;
    }

    private boolean campiObbligatoriOK() {
        if (!Heap.isLoginFB()) {
            if (isEmpty(getVal(R.id.password))) {
                SnackMsg.showErrMsg(findViewById(R.id.esci), "Password è obbligatorio");

                return false;
            }
        }
        if (isEmpty(getVal(R.id.email))) {
            SnackMsg.showErrMsg(findViewById(R.id.esci), "Email è obbligatorio");

            return false;
        }
/*        if (isEmpty(getVal(R.id.telefono))) {
            SnackMsg.showErrMsg(findViewById(R.id.esci), "Telefono è obbligatorio");

            return false;
        }*/
        return true;
    }

    private boolean isEmpty(String s) {
        if (s == null) return true;
        if (s.trim().length() == 0) return true;
        return false;
    }

    private String getVal(int id) {
        return ((EditText) findViewById(id)).getText().toString();
    }

    private String getValText(int id) {
        return ((TextView) findViewById(id)).getText().toString();
    }
}
