package liste.wrappers;

import android.view.View;
import android.widget.TextView;

import com.posvert.mobility.R;

import beans.Ente;
import beans.Qualifica;

/**
 * Created by giovanni on 21/10/16.
 */

public class QualificheRowWrapper {

    private TextView descrizioneTextView;



    public QualificheRowWrapper(View convertView)
    {
        descrizioneTextView = (TextView)
                convertView.findViewById(R.id.descrizione);

    }

    public void populate(Qualifica annuncio)
    {
        descrizioneTextView.setText(""+annuncio.getDescrizione());

    }

}