package beans;

import android.util.Log;

import com.posvert.trasferimenti.chat.Messaggio;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by giovanni on 21/10/16.
 */

public class JSONHandler {
    public static Messaggio jsonToMessaggioChat(JSONObject obj) throws JSONException {
        Messaggio u = new Messaggio();
        u.setMittente(obj.getString("mittente"));
        u.setDestinatario(obj.getString("destinatario"));
        u.setTesto(obj.getString("testo"));
        u.setAzione(obj.getInt("azione"));
        return u;
    }

    public static String messaggioChatToJson(Messaggio m) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("mittente", m.getMittente());
        json.put("destinatario", m.getDestinatario());
        json.put("testo", m.getTesto());
        json.put("azione", m.getAzione());

        return json.toString();
    }
    public static Messaggio parseMessaggioJSON(JSONObject obj) throws JSONException {
        Messaggio u = new Messaggio();
        u.setMittente(obj.getString("mittente"));
        u.setDestinatario(obj.getString("destinatario"));
        u.setTesto(obj.getString("testo"));
        u.setAzione(obj.getInt("azione"));
        return u;
    }
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
        u.setFbUserid(obj.getString("fbUserid"));

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
    public static MessaggioOffline parseMessaggioOffline(JSONObject obj) throws JSONException {
        MessaggioOffline u = new MessaggioOffline();
        u.setTesto(obj.getString("testo"));
        u.setMittente(clean(obj.getString("mittente")));
        u.setDestinatario(clean(obj.getString("destinatario")));

        u.setId(obj.getInt("id"));


       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = null;
        try {
            Long ld = Long.parseLong(obj.getString("data"));
            java.sql.Date sqlDate = new java.sql.Date(ld);
            u.setData(sqlDate);
        //    parsed = format.parse(obj.getString("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
      //  java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
       // u.setData(sqlDate);
        return u;

    }

    private static String clean(String s) {
        if (s == null || s.equals("null")){

            return null;
        }

        return s;
    }
}
