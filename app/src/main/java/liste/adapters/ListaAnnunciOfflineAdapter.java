package liste.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import beans.MessaggioOffline;
import beans.Utente;

import com.facebook.login.widget.ProfilePictureView;
import com.posvert.trasferimenti.R;
import com.posvert.trasferimenti.helper.IExecutor;
import com.posvert.trasferimenti.helper.UtentiHelper;

/**
 * Created by giovanni on 10/11/16.
 */

public class ListaAnnunciOfflineAdapter extends ArrayAdapter<MessaggioOffline> {
    private List<MessaggioOffline> modelItems = null;
    private Context context;

    public ListaAnnunciOfflineAdapter(Context context, List<MessaggioOffline> resource) {
        super(context, R.layout.row_annuncioffline, resource);

        this.context = context;
        this.modelItems = resource;
    }

    static Map<String, Utente> map = new HashMap<>();
    static Set<String> prenota = new HashSet<>();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();


        convertView = inflater.inflate(R.layout.row_annuncioffline, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        TextView data = (TextView) convertView.findViewById(R.id.data);
        //   final  CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
        final String mittente = modelItems.get(position).getMittente();
        name.setText(mittente);
        data.setText(""+modelItems.get(position).getData());


        Utente u = map.get(mittente);
        if (u == null) {
            if (prenota.contains(mittente)) return convertView;
            prenota.add(mittente);
      /*      try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            final View finalConvertView = convertView;
            UtentiHelper.getUtenteByUsername(context, mittente, new IExecutor() {
                @Override
                public void exec(Object obj) {
                    Utente u = (Utente) obj;
                    map.put(mittente, u);
                    if (u.getFbUserid() != null) {
                        ProfilePictureView profilePictureView = (ProfilePictureView) finalConvertView.findViewById(R.id.foto);
                        profilePictureView.setProfileId(u.getFbUserid());
                    }
                }
            });
        } else {
            if (u.getFbUserid() != null) {
                ProfilePictureView profilePictureView = (ProfilePictureView) convertView.findViewById(R.id.foto);
                profilePictureView.setProfileId(u.getFbUserid());
            }
        }


 /*           cb.setChecked(false);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = cb.isChecked();
                Log.e("YY", ""+"ddd");
            }
        });*/

        return convertView;
    }
}
