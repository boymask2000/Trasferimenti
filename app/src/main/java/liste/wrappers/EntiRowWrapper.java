package liste.wrappers;

import android.view.View;
import android.widget.TextView;

import com.posvert.mobility.R;

import beans.Annuncio;
import beans.Ente;

/**
 * Created by giovanni on 21/10/16.
 */

public class EntiRowWrapper {

    private TextView provinciaTextView;

    private TextView NomeTextView;

    private TextView regioneTextView;

    private TextView comuneTextView;

    public EntiRowWrapper(View convertView)
    {
        provinciaTextView = (TextView)
                convertView.findViewById(R.id.provincia);
        NomeTextView = (TextView)
                convertView.findViewById(R.id.nome);
        regioneTextView = (TextView)
                convertView.findViewById(R.id.regione);
        comuneTextView = (TextView)
                convertView.findViewById(R.id.comune);
    }

    public void populate(Ente annuncio)
    {
        NomeTextView.setText(""+annuncio.getNome());
        provinciaTextView.setText(""+(annuncio.getProvincia()==null?"":annuncio.getProvincia()));
        regioneTextView.setText(""+annuncio.getRegione());
        comuneTextView.setText(""+annuncio.getComune());

    }

}