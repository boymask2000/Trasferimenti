package liste.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.posvert.mobility.R;

import java.util.List;

import beans.Ente;
import beans.Qualifica;
import liste.wrappers.EntiRowWrapper;
import liste.wrappers.QualificheRowWrapper;

/**
 * Created by giovanni on 21/10/16.
 */

public class ListaQualificheAdapter extends BaseAdapter {
    private final Activity act;
    private final List<Qualifica> lista;

    public ListaQualificheAdapter(Activity act, List<Qualifica> ll ){
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
        QualificheRowWrapper wrapper;
        if (convertView == null)
        {
            convertView = act.getLayoutInflater().inflate(
                    R.layout.row_qualifica, null);
            wrapper = new QualificheRowWrapper(convertView);
            convertView.setTag(wrapper);
        }
        else
        {
            wrapper = (QualificheRowWrapper) convertView.getTag();
        }
        Qualifica a = (Qualifica) getItem(position);
        wrapper.populate(a);

        return convertView;
    }
}
