package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by giovanni on 22/10/16.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    public static final String TABELLA_REGIONI = "regioni";
    public static final String TABELLA_PROVINCE = "province";
    public static final String TABELLA_COMUNI = "comuni";

    public static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Lo statement SQL di creazione del database
    private static final String DATABASE_CREATE_REGIONI = "create table " + TABELLA_REGIONI + //
            " (_id integer primary key autoincrement, name text not null )";
    private static final String DATABASE_CREATE_PROVINCE = "create table " + TABELLA_PROVINCE + //
            " (_id integer primary key autoincrement, name text not null, regione text not null)";
    private static final String DATABASE_CREATE_COMUNI = "create table " + TABELLA_COMUNI + //
            " (_id integer primary key autoincrement, name text not null, provincia text not null)";
    private final Context context;

    // Costruttore
    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Questo metodo viene chiamato durante la creazione del database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_REGIONI);
        database.execSQL(DATABASE_CREATE_PROVINCE);
        database.execSQL(DATABASE_CREATE_COMUNI);
    }


    // Questo metodo viene chiamato durante l'upgrade del database, ad esempio quando viene incrementato il numero di versione
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS " + TABELLA_REGIONI);
        database.execSQL("DROP TABLE IF EXISTS " + TABELLA_PROVINCE);
        database.execSQL("DROP TABLE IF EXISTS " + TABELLA_COMUNI);
        onCreate(database);

    }


    public  void init(){
        getReadableDatabase();
    }

}