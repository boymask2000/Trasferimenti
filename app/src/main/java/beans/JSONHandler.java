package beans;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by giovanni on 21/10/16.
 */

public class JSONHandler {
    public static Utente parseUtenteJSON(JSONObject obj) throws JSONException {
        Utente u = new Utente();
        u.setEmail(obj.getString("email"));
        u.setRegione(obj.getString("regione"));
        u.setProvincia(obj.getString("provincia"));
        u.setComune(obj.getString("comune"));
        u.setEnte(obj.getString("ente"));
        u.setLivello(obj.getString("livello"));
        u.setUsername(obj.getString("username"));
        u.setPassword(obj.getString("password"));
        u.setUserid(obj.getInt("userid"));

        return u;

    }

    public static Annuncio parseAnnuncioJSON(JSONObject obj) throws JSONException {
        Annuncio u = new Annuncio();
        u.setEmail(obj.getString("email"));
        u.setRegione(clean(obj.getString("regione")));
        u.setProvincia(clean(obj.getString("provincia")));
        u.setComune(clean(obj.getString("comune")));
        u.setEnte(clean(obj.getString("ente")));
        u.setLivello(clean(obj.getString("livello")));
        u.setUsername(clean(obj.getString("username")));
        u.setNote(clean(obj.getString("note")));
        u.setId(obj.getInt("id"));


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = null;
        try {
            parsed = format.parse(obj.getString("data"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
        u.setData(sqlDate);
        return u;

    }

    private static String clean(String s) {
        if (s == null || s.equals("null")){

            return null;
        }

        return s;
    }
}
