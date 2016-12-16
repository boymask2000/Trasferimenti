package com.posvert.mobility.chat;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.posvert.mobility.R;

import java.util.List;



/**
 * Created by giovanni on 11/12/16.
 */

public class MessagesListAdapterRec extends RecyclerView.Adapter<MessagesListAdapterRec.MyViewHolder> {

    private List<Messaggio> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView testo, mittente;

        public MyViewHolder(View view) {
            super(view);
            mittente = (TextView) view.findViewById(R.id.lblMsgFrom);
            testo = (TextView) view.findViewById(R.id.txtMsg);

        }
    }


    public MessagesListAdapterRec(List<Messaggio> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_message_left_rec, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Messaggio movie = moviesList.get(position);
        holder.testo.setText(movie.getTesto());

        holder.mittente.setText(movie.getMittente());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}