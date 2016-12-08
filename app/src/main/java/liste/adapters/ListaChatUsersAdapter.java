package liste.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.posvert.mobility.R;
import java.util.List;
import liste.wrappers.ChatUserRowWrapper;

/**
 * Created by giovanni on 21/10/16.
 */

public class ListaChatUsersAdapter extends BaseAdapter {
    private final Activity act;
    private final List<String> lista;

    public ListaChatUsersAdapter(Activity act, List<String> ll ){
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
        ChatUserRowWrapper wrapper;
        if (convertView == null)
        {
            convertView = act.getLayoutInflater().inflate(
                    R.layout.row_listautentichat, null);
            wrapper = new ChatUserRowWrapper(convertView);
            convertView.setTag(wrapper);
        }
        else
        {
            wrapper = (ChatUserRowWrapper) convertView.getTag();
        }
        String annuncio = (String) getItem(position);
        wrapper.populate(annuncio);

        return convertView;
    }
}
