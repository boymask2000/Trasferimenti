package com.posvert.mobility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.app.Activity.RESULT_OK;

/**
 * Created by giovanni on 26/02/17.
 */
public class QualificaXMLHandler {
    private final Activity act;
    public static int ID=77;

    public QualificaXMLHandler(final Activity act) {
        this.act=act;
        Button cerca = (Button) act.findViewById(R.id.cercaQualifica);

        cerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                Intent intent = new Intent(act, GestioneQualificheActivity.class);

                act.startActivityForResult(intent, ID);
            }
        });

    }
  /*  public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            String nome = data.getStringExtra("nome");
            String s_codiceQualifica = data.getStringExtra("codice");
        EditText t = (EditText) act.findViewById(R.id.ente);
            t.setText(nome);

        }

    }*/
}
