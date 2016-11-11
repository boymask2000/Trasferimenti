package liste.wrappers;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.posvert.trasferimenti.R;

import beans.Annuncio;

/**
 * Created by giovanni on 21/10/16.
 */

public class AnnunciRowWrapper {

    private TextView provinciaTextView;

    private TextView codiceTextView;

    private TextView regioneTextView;

    private TextView comuneTextView;

    public AnnunciRowWrapper(View convertView)
    {
        provinciaTextView = (TextView)
                convertView.findViewById(R.id.provincia);
        codiceTextView = (TextView)
                convertView.findViewById(R.id.data);
        regioneTextView = (TextView)
                convertView.findViewById(R.id.regione);
        comuneTextView = (TextView)
                convertView.findViewById(R.id.comune);
    }

    public void populate(Annuncio annuncio)
    {
        codiceTextView.setText(""+annuncio.getData());
        provinciaTextView.setText(""+(annuncio.getProvincia()==null?"":annuncio.getProvincia()));
        regioneTextView.setText(""+annuncio.getRegione());
        comuneTextView.setText(""+annuncio.getComune());

    }

}