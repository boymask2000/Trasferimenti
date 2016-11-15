package liste.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.posvert.mobility.R;

import java.util.List;

import beans.Annuncio;
import liste.wrappers.AnnunciRowWrapper;

/**
 * Created by giovanni on 21/10/16.
 */

public class ListaAnnunciAdapter extends BaseAdapter {
    private final Activity act;
    private final List<Annuncio> lista;

    public ListaAnnunciAdapter(Activity act, List<Annuncio> ll ){
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
        AnnunciRowWrapper wrapper;
        if (convertView == null)
        {
            convertView = act.getLayoutInflater().inflate(
                    R.layout.row_annunci, null);
            wrapper = new AnnunciRowWrapper(convertView);
            convertView.setTag(wrapper);
        }
        else
        {
            wrapper = (AnnunciRowWrapper) convertView.getTag();
        }
        Annuncio annuncio = (Annuncio) getItem(position);
        wrapper.populate(annuncio);

        return convertView;
    }
}
