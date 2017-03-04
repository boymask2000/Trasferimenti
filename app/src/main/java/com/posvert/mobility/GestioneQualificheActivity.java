package com.posvert.mobility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.posvert.mobility.common.ResponseHandler;
import com.posvert.mobility.common.URLBuilder;
import com.posvert.mobility.common.URLHelper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import beans.JSONHandler;
import beans.Qualifica;
import liste.adapters.ListaQualificheAdapter;

public class GestioneQualificheActivity extends AppCompatActivity {
    private List<Qualifica> lista = new ArrayList<>();
    private ListView mylist;
    private Qualifica qSel = null;
    private ListaQualificheAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_qualifiche);

        mylist = (ListView) findViewById(R.id.lista);//mylist.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                view.setSelected(true);

                qSel = (Qualifica) parent.getItemAtPosition(position);
                Log.e("QQQ", qSel.getDescrizione());

                Intent data = new Intent();
                data.putExtra("nome", "" + qSel.getDescrizione());
                data.putExtra("codice", "" + qSel.getCodice());

                setResult(RESULT_OK, data);
                finish();


            }
        });
        URLHelper.invokeURL(GestioneQualificheActivity.this, buildUrlCerca(), new ResponseHandler() {
            @Override
            public void parseResponse(String response) {
                Log.e("E", response);
                lista.clear();
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        Qualifica u = JSONHandler.parseQualificaJSON(array.getJSONObject(i));
                        lista.add(u);
                    }
                    ListaQualificheAdapter adapter = new ListaQualificheAdapter(GestioneQualificheActivity.this, lista);
                    mylist.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                    //     if (lista.size() == 0) cercamatch.setEnabled(false);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String buildUrlCerca() {
        String url = URLHelper.buildWithPref(this, "qualifiche", "getAll", true);

        URLBuilder builder = new URLBuilder(url);


        return builder.getUrl();

    }
}
