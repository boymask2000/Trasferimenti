package liste.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.posvert.mobility.R;

import java.util.List;

import beans.Annuncio;
import beans.Ente;
import liste.wrappers.AnnunciRowWrapper;
import liste.wrappers.EntiRowWrapper;

/**
 * Created by giovanni on 21/10/16.
 */

public class ListaEntiAdapter extends BaseAdapter {
    private final Activity act;
    private final List<Ente> lista;

    public ListaEntiAdapter(Activity act, List<Ente> ll ){
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
        EntiRowWrapper wrapper;
        if (convertView == null)
        {
            convertView = act.getLayoutInflater().inflate(
                    R.layout.row_ente, null);
            wrapper = new EntiRowWrapper(convertView);
            convertView.setTag(wrapper);
        }
        else
        {
            wrapper = (EntiRowWrapper) convertView.getTag();
        }
        Ente annuncio = (Ente) getItem(position);
        wrapper.populate(annuncio);

        return convertView;
    }
}
