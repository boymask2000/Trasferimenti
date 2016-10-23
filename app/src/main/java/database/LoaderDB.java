package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.posvert.trasferimenti.R;
import com.posvert.trasferimenti.common.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class LoaderDB {
    private static final String DBPATH = "/data/data/com.posvert.trasferimenti/databases/";
    private static final String DBNAME = DBOpenHelper.DATABASE_NAME;

    private final Context context;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    public LoaderDB(Context context) {
        this.context = context;
    }

    public boolean checkDB() {
        SQLiteDatabase db = null;
//        File f = new File(DBPATH + DBNAME);
//        boolean ok = f.exists();
        try {
            db = SQLiteDatabase.openDatabase(DBPATH + DBNAME, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            return false;

        }
        if (db != null) db.close();
        return db != null ? true : false;
    }

    public void caricaDati() {
        carica();

        inviaDB();

    }

    public void installaDB() {

        if (checkDB()) return;
        DBOpenHelper  dbHelper = new DBOpenHelper(context);
        dbHelper.init();

        try {
            InputStream is = context.getAssets().open("db.db");

            OutputStream os = new FileOutputStream(DBPATH + DBNAME);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);

            }
            os.flush();
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        test();
    }

    private void test() {
        DBRegioniAdapter dbHelper;
        dbHelper = new DBRegioniAdapter(context);
        dbHelper.open();
        Cursor cursor = dbHelper.fetchAllRegioni();


        while (cursor.moveToNext()) {

            String contactID = cursor.getString(cursor.getColumnIndex(DBRegioniAdapter.KEY_NAME));
            Log.d("DB", "contact id = " + contactID);
        }
        cursor.close();

        dbHelper.close();
    }

    private void carica() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("Elenco-comuni-01_07_2016.csv"), "UTF-8"));

            // do reading, usually loop until end of file reading
            String line;

            DBRegioniAdapter regioniHelper = new DBRegioniAdapter(context);
            DBProvinceAdapter provinceHelper = new DBProvinceAdapter(context);
            DBComuniAdapter comuniHelper = new DBComuniAdapter(context);
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                counter++;

                if (counter == 1) continue;
                String[] campi = line.split(";");
                String comune = campi[5];
                String regione = campi[9];
                String provincia = campi[10];
                String metro = campi[11];
                String val=provincia.equals("-")?metro:provincia;

                Log.i("DDDDDDDD", counter+"["+regione +"]  ["+val+"] ["+comune+"]");
                inserisciRegione(regione, regioniHelper);
                inserisciProvincia(val, regione, provinceHelper);
                inserisciComune(comune, val, comuniHelper);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            try {
                Socket socket = new Socket(Config.getServerAddress(context), 8080);

                OutputStream sOS = socket.getOutputStream();

                FileInputStream is = new FileInputStream(DBPATH + DBNAME);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) > 0) {
                    sOS.write(buffer, 0, len);

                }
                sOS.flush();
                sOS.close();
                is.close();
            } catch (Exception e) {
            }
            return 0L;
        }

        protected void onProgressUpdate(Integer... progress) {
            //     setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //       showDialog("Downloaded " + result + " bytes");
        }
    }


    private void inviaDB() {
        new DownloadFilesTask().execute();

    }

    public void cleanAll() {

    }

    private void inserisciRegione(String regione, DBRegioniAdapter helper) {

        helper.open();
        Cursor c = helper.fetchContactsByFilter(regione);
        //    startManagingCursor(c);
        if (!c.moveToNext())
            helper.createRegione(regione);

        helper.close();
    }

    private void inserisciProvincia(String provincia, String regione, DBProvinceAdapter helper) {

        helper.open();
        Cursor c = helper.fetchContactsByFilter(provincia);
        //    startManagingCursor(c);
        if (!c.moveToNext())
            helper.createProvincia(provincia, regione);

        helper.close();
    }

    private void inserisciComune(String comune, String provincia, DBComuniAdapter helper) {
        helper.open();
      //  Cursor c = helper.fetchContactsByFilter(comune);
        //    startManagingCursor(c);

            helper.createComune(comune, provincia);

        helper.close();
    }
}
