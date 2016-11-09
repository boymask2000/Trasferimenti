package liste;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.posvert.trasferimenti.R;
import com.posvert.trasferimenti.chat.ChatRequest;

import java.util.List;

import beans.Annuncio;

/**
 * Created by giovanni on 21/10/16.
 */

public class ListaChatAdapter extends BaseAdapter {
    private final Activity act;
    private final List<ChatRequest> lista;

    public ListaChatAdapter(Activity act, List<ChatRequest> ll ){
        this.act=act;
        this.lista=ll;
    }
    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatRowWrapper wrapper;
        if (convertView == null)
        {
            convertView = act.getLayoutInflater().inflate(
                    R.layout.listachatrow, null);
            wrapper = new ChatRowWrapper(convertView);
            convertView.setTag(wrapper);
        }
        else
        {
            wrapper = (ChatRowWrapper) convertView.getTag();
        }
        ChatRequest annuncio = (ChatRequest) getItem(position);
        wrapper.populate(annuncio);

        return convertView;
    }
}
