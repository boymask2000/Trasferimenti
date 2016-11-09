package liste;

import android.view.View;
import android.widget.TextView;

import com.posvert.trasferimenti.R;
import com.posvert.trasferimenti.chat.ChatRequest;

import beans.Annuncio;

/**
 * Created by giovanni on 21/10/16.
 */

public class ChatRowWrapper {

    private TextView nomeView;


    public ChatRowWrapper(View convertView) {
        nomeView = (TextView)
                convertView.findViewById(R.id.nome);
    }
    public void populate(ChatRequest req)
    {
        nomeView.setText(req.getUsername());
    }

}