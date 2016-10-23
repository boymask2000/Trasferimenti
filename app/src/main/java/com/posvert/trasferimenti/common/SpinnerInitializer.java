package com.posvert.trasferimenti.common;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import database.DBComuniAdapter;
import database.DBProvinceAdapter;
import database.DBRegioniAdapter;

/**
 * Created by giovanni on 23/10/16.
 */

public class SpinnerInitializer {
    private final Spinner spinnerRegioni;
    private final Spinner spinnerProvince;
    private final Spinner spinnerComuni;
    private final Context context;

    private String regione;


    private String provincia;
    private String comune;

    public SpinnerInitializer(Spinner spinnerRegioni, Spinner spinnerProvince,

                              Spinner spinnerComuni, Context context) {
        this.spinnerRegioni = spinnerRegioni;
        this.spinnerProvince = spinnerProvince;
        this.spinnerComuni = spinnerComuni;
        this.context = context;

        setListeners();

        fillSpinnerRegioni(spinnerRegioni);

    }

    private void setListeners() {
        spinnerRegioni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                regione = parent.getItemAtPosition(pos).toString();
                fillSpinnerProvince(spinnerProvince, regione);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                provincia = parent.getItemAtPosition(pos).toString();
                fillSpinnerComuni(spinnerComuni, provincia);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        spinnerComuni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                comune = parent.getItemAtPosition(pos).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void fillSpinnerRegioni(Spinner spinner) {

        DBRegioniAdapter dbHelper;
        dbHelper = new DBRegioniAdapter(context);
        dbHelper.open();
        Cursor cursor = dbHelper.fetchAllRegioni();

        List<String> lista = new ArrayList<>();
        while (cursor.moveToNext()) {

            String val = cursor.getString(cursor.getColumnIndex(DBRegioniAdapter.KEY_NAME));
            lista.add(val);
        }
        cursor.close();

        dbHelper.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, lista);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void fillSpinnerComuni(Spinner spinner, String provincia) {

        DBComuniAdapter dbHelper;
        dbHelper = new DBComuniAdapter(context);
        dbHelper.open();
        Cursor cursor = dbHelper.getAllComuni(provincia);

        List<String> lista = new ArrayList<>();
        while (cursor.moveToNext()) {

            String val = cursor.getString(cursor.getColumnIndex(DBRegioniAdapter.KEY_NAME));
            lista.add(val);
        }
        cursor.close();

        dbHelper.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, lista);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void fillSpinnerProvince(Spinner spinner, String regione) {

        DBProvinceAdapter dbHelper;
        dbHelper = new DBProvinceAdapter(context);
        dbHelper.open();
        Cursor cursor = dbHelper.getAllProvince(regione);

        List<String> lista = new ArrayList<>();
        while (cursor.moveToNext()) {

            String val = cursor.getString(cursor.getColumnIndex(DBRegioniAdapter.KEY_NAME));
            lista.add(val);
        }
        cursor.close();

        dbHelper.close();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, lista);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public String getRegione() {
        return regione;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getComune() {
        return comune;
    }
}
