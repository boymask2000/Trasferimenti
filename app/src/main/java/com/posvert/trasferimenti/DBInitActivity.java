package com.posvert.trasferimenti;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import database.DBComuniAdapter;
import database.DBProvinceAdapter;
import database.DBRegioniAdapter;
import database.LoaderDB;
/*
Per creare il database
1) Chiudere il server Tomcat
2) Sul server (Eclipse) lanciare il main utility.DBReceiver. Questo è un server che resta in attesa di dati dalla porta 8080
3) Lanciare l' attività DBInitActivity con loader.caricaDati(). Questa attività carica i dati dal file all'interno del database, poi invia il database al server
4) Prendere il file creato dal server e metterlo nel folder degli asset della app
 */

public class DBInitActivity extends AppCompatActivity {
    private DBRegioniAdapter dbHelper;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_db);

        LoaderDB loader = new LoaderDB(this);
        loader.caricaDati();
    //    loader.installaDB();


    }


}
