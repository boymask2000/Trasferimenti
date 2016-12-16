package com.posvert.mobility.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.posvert.mobility.R;
import com.posvert.mobility.helper.IExecutor;
import com.posvert.mobility.helper.UtentiHelper;

import java.util.List;

import beans.Utente;

/**
 * Created by giovanni on 13/12/16.
 */

public class UserListAdapterRec extends RecyclerView.Adapter<UserListAdapterRec.MyViewHolder> {

    private final Context ctx;
    private List<Utente> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final ProfilePictureView foto;
        public TextView nome;
        public TextView nome2;

        public MyViewHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.nome);
            nome2 = (TextView) view.findViewById(R.id.nome2);
            foto = (ProfilePictureView) view.findViewById(R.id.foto);
            //     testo = (TextView) view.findViewById(R.id.txtMsg);


        }
    }


    public UserListAdapterRec(List<Utente> moviesList, Context ctx) {
        this.ctx = ctx;
        this.moviesList = moviesList;
    }

    @Override
    public UserListAdapterRec.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user_chat, parent, false);

        return new UserListAdapterRec.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserListAdapterRec.MyViewHolder holder, int position) {
        Utente movie = moviesList.get(position);

        setImage(holder.foto,  movie.getUsername());
        //    holder.mittente.setText(movie.getMittente());
        int index = movie.getUsername().indexOf(' ');
        if( index==-1){
            holder.nome.setText(movie.getUsername());
        }else
        {
            String n1 = movie.getUsername().substring(0,index).trim();
            String n2=movie.getUsername().substring(index+1);
            holder.nome.setText(n1);
            holder.nome2.setText(n2);
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    private void setImage(final View view, String name) {
        UtentiHelper.getUtenteByUsername(ctx, name, new IExecutor() {
            @Override
            public void exec(Object obj) {
                Utente u = (Utente) obj;
                ProfilePictureView profilePictureView = (ProfilePictureView) view;
                profilePictureView.setProfileId(u.getFbUserid());view.invalidate();
            }
        });


       /* ImageView fotoView = (ImageView) findViewById(R.id.foto);
        try {
            Drawable icon = new BitmapDrawable(LoginFBActivity.getFacebookProfilePicture(LoginFBActivity.getFBUserId()));

            fotoView.setImageDrawable(icon);
        }catch( Exception e){
            e.printStackTrace();
        }*/
    }
}