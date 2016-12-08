package liste.wrappers;

import android.view.View;
import android.widget.TextView;

import com.posvert.mobility.R;
import com.posvert.mobility.chat.ChatRequest;

/**
 * Created by giovanni on 21/10/16.
 */

public class ChatUserRowWrapper {

    private TextView nomeView;
    private TextView dateView;


    public ChatUserRowWrapper(View convertView) {
        nomeView = (TextView)
                convertView.findViewById(R.id.nome);
        dateView = (TextView)
                convertView.findViewById(R.id.data);
    }
    public void populate(String user)
    {
        nomeView.setText(user);

    }

}