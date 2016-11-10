package liste;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import beans.MessaggioOffline;
import com.posvert.trasferimenti.R;

/**
 * Created by giovanni on 10/11/16.
 */

public class AnnunciOfflineAdapter extends ArrayAdapter<MessaggioOffline> {
    private  List<MessaggioOffline> modelItems = null;
    private Context context;

    public AnnunciOfflineAdapter(Context context, List<MessaggioOffline> resource) {
        super(context, R.layout.annunciofflinerow, resource);

        this.context = context;
        this.modelItems = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.annunciofflinerow, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
   //   final  CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
        name.setText(modelItems.get(position).getMittente());

 /*           cb.setChecked(false);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = cb.isChecked();
                Log.e("YY", ""+"ddd");
            }
        });*/

        return convertView;
    }
}
