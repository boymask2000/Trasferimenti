package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by giovanni on 22/10/16.
 */

public class DBComuniAdapter extends CommonAdapter{


    @SuppressWarnings("unused")
    private static final String LOG_TAG = DBComuniAdapter.class.getSimpleName();

    private Context context;
    private SQLiteDatabase database;
    private DBOpenHelper dbHelper;

    // Database fields
    private static final String DATABASE_TABLE = DBOpenHelper.TABELLA_COMUNI;

    public static final String KEY_CONTACTID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PROVINCIA= "provincia";


    public DBComuniAdapter(Context context) {
        this.context = context;
    }

    public DBComuniAdapter open() throws SQLException {
        dbHelper = new DBOpenHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    private ContentValues createContentValues(String name, String provincia) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, clean(name));
        values.put(KEY_PROVINCIA, clean(provincia));
        ;

        return values;
    }

    //create a contact
    public long createComune(String name, String provincia) {
        ContentValues initialValues = createContentValues(name, provincia);
        return database.insertOrThrow(DATABASE_TABLE, null, initialValues);
    }

    //update a contact
  /*  public boolean updateContact(long contactID, String name, String surname, String sex, String birth_date) {
        ContentValues updateValues = createContentValues(name, surname, sex, birth_date);
        return database.update(DATABASE_TABLE, updateValues, KEY_ CONTACTID + "=" + contactID, null) > 0;
    }

    //delete a contact
    public boolean deleteContact(long contactID) {
        return database.delete(DATABASE_TABLE, KEY_ CONTACTID + "=" + contactID, null) > 0;
    }
*/
    //fetch all contacts
    public Cursor fetchAllComuni() {
        return database.query(DATABASE_TABLE, new String[]{KEY_CONTACTID, KEY_NAME, KEY_PROVINCIA}, null, null, null, null, null);
    }


    // recupero tuttle i comuni della provincia
    public Cursor getAllComuni(String provincia) {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[]{
                        KEY_CONTACTID, KEY_NAME, KEY_PROVINCIA},
                //     KEY_REGIONE + " like '%" + filter + "%'", null, null, null, null, null);
                KEY_PROVINCIA + " = '" + clean(provincia) + "'", null, null, null, null, null);

        return mCursor;
    }

    //fetch contacts filter by a string
    public Cursor fetchContactsByFilter(String filter) {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[]{
                        KEY_CONTACTID, KEY_NAME, KEY_PROVINCIA},
              //  KEY_PROVINCIA + " like '%" + filter + "%'", null, null, null, null, null);
                KEY_NAME + " = '" + clean(filter) + "'", null, null, null, null, null);

        return mCursor;
    }
}