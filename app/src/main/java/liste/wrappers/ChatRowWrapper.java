package liste.wrappers;

import android.view.View;
import android.widget.TextView;

import com.posvert.trasferimenti.R;
import com.posvert.trasferimenti.chat.ChatRequest;

/**
 * Created by giovanni on 21/10/16.
 */

public class ChatRowWrapper {

    private TextView nomeView;
    private TextView dateView;


    public ChatRowWrapper(View convertView) {
        nomeView = (TextView)
                convertView.findViewById(R.id.nome);
        dateView = (TextView)
                convertView.findViewById(R.id.data);
    }
    public void populate(ChatRequest req)
    {
        nomeView.setText(req.getUsername());
        dateView.setText(req.getUsername());
    }

}