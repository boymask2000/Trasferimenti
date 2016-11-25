package database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

/**
 * Created by giovanni on 22/10/16.
 */

public class DBRegioniAdapter extends CommonAdapter{


    @SuppressWarnings("unused")
    private static final String LOG_TAG = DBRegioniAdapter.class.getSimpleName();

    private Context context;
    private SQLiteDatabase database;
    private DBOpenHelper dbHelper;

    // Database fields
    private static final String DATABASE_TABLE = DBOpenHelper.TABELLA_REGIONI;

    public static final String KEY_CONTACTID = "_id";
    public static final String KEY_NAME = "name";


    public DBRegioniAdapter(Context context) {
        this.context = context;
    }

    public DBRegioniAdapter open() throws SQLException {
        dbHelper = new DBOpenHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
        database.close();
    }

    private ContentValues createContentValues(String name) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, clean(name));


        return values;
    }

    //create a contact
    public long createRegione(String name) {
        ContentValues initialValues = createContentValues(name);
        return database.insertOrThrow(DATABASE_TABLE, null, initialValues);
    }

//    //update a contact
//    public boolean updateContact(long contactID, String name, String surname, String sex, String birth_date) {
//        ContentValues updateValues = createContentValues(name, surname, sex, birth_date);
//        return database.update(DATABASE_TABLE, updateValues, KEY_ CONTACTID + "=" + contactID, null) > 0;
//    }
//
//    //delete a contact
//    public boolean deleteContact(long contactID) {
//        return database.delete(DATABASE_TABLE, KEY_ CONTACTID + "=" + contactID, null) > 0;
//    }

    //fetch all contacts
    public Cursor fetchAllRegioni() {
        return database.query(DATABASE_TABLE, new String[]{KEY_CONTACTID, KEY_NAME}, null, null, null, null, null);
    }


    //fetch contacts filter by a string
    // da usare per verificare se la regione è presente
   public Cursor fetchContactsByFilter(String filter) {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[]{
                        KEY_CONTACTID, KEY_NAME},
                KEY_NAME + " = '" + clean(filter) + "'", null, null, null, null, null);

        return mCursor;
    }
}