package liste;

import android.view.View;
import android.widget.TextView;

import com.posvert.trasferimenti.R;

import beans.Annuncio;

/**
 * Created by giovanni on 21/10/16.
 */

public class RowWrapper {

    private TextView provinciaTextView;

    private TextView codiceTextView;

    private TextView regioneTextView;

    public RowWrapper(View convertView)
    {
        provinciaTextView = (TextView)
                convertView.findViewById(R.id.provincia);
        codiceTextView = (TextView)
                convertView.findViewById(R.id.data);
        regioneTextView = (TextView)
                convertView.findViewById(R.id.regione);
    }

    public void populate(Annuncio annuncio)
    {
        codiceTextView.setText(""+annuncio.getData());
        provinciaTextView.setText(""+annuncio.getProvincia());
        regioneTextView.setText(""+annuncio.getRegione());

    }
}