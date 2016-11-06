package com.posvert.trasferimenti;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.widget.ProfilePictureView;
import com.posvert.trasferimenti.common.Heap;
import com.posvert.trasferimenti.common.ResponseHandler;
import com.posvert.trasferimenti.common.URLHelper;
import com.posvert.trasferimenti.net.NetUtil;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import beans.Annuncio;
import beans.JSONHandler;
import liste.ListaAnnunciAdapter;

public class PaginaAnnunciActivity extends Activity {
    private ListView mylist;
    private Button cercamatch = null;
    private List<Annuncio> lista = new ArrayList<>();
    private ListaAnnunciAdapter listaAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_annunci);

     //   ImageView fotoView = (ImageView) findViewById(R.id.foto);
        TextView userText = (TextView) findViewById(R.id.username);
        setImage();
       userText.setText(Heap.getUserCorrente().getUsername());
        /*
        try {
            Drawable icon = new BitmapDrawable(LoginFBActivity.getFacebookProfilePicture(LoginFBActivity.getFBUserId()));

            fotoView.setImageDrawable(icon);
        }catch( Exception e){
            e.printStackTrace();
        }*/
      //  startService(new Intent(this, ChatService.class));

        mylist = (ListView) findViewById(R.id.lista);
        Button creaannuncio = (Button) findViewById(R.id.creaannuncio);

        cercamatch = (Button) findViewById(R.id.cercamatch);


        cercamatch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent act = new Intent(PaginaAnnunciActivity.this, PaginaMatchActivity.class);

                startActivity(act);

            }
        });
        creaannuncio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent openPage1 = new Intent(PaginaAnnunciActivity.this, InserimentoAnnuncioActivity.class);

                startActivityForResult(openPage1, 2);

            }
        });
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {

                Intent act = new Intent(PaginaAnnunciActivity.this, VisualizzaAnnuncioActivity.class);
                Annuncio an = lista.get(pos);
                String pkg = getPackageName();
                act.putExtra(pkg + "ID", "" + an.getId());
                startActivityForResult(act, 2);
            }
        });
        loadData();


    }
    private void setImage(){
        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.foto);
        profilePictureView.setProfileId(LoginFBActivity.getFBUserId());

       /* ImageView fotoView = (ImageView) findViewById(R.id.foto);
        try {
            Drawable icon = new BitmapDrawable(LoginFBActivity.getFacebookProfilePicture(LoginFBActivity.getFBUserId()));

            fotoView.setImageDrawable(icon);
        }catch( Exception e){
            e.printStackTrace();
        }*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch(id)
        {
            case R.id.profilo:
                Intent act = new Intent(PaginaAnnunciActivity.this, GestioneProfiloActivity.class);

                startActivity(act);

                break;
            case R.id.invia_commento:
                Intent act1 = new Intent(PaginaAnnunciActivity.this, InviaCommentoActivity.class);

                startActivity(act1);
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = new MenuInflater(this);
        inf.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void loadData() {
        URLHelper.invokeURL(this, buildUrl(), new ResponseHandler() {
            @Override
            public void parseResponse(String response) {
                lista.clear();
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        Annuncio u = JSONHandler.parseAnnuncioJSON(array.getJSONObject(i));
                        lista.add(u);
                    }
                    listaAdapter = new ListaAnnunciAdapter(PaginaAnnunciActivity.this, lista);
                    mylist.setAdapter(listaAdapter);


                    if (lista.size() == 0) cercamatch.setEnabled(false);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {
            loadData();
            listaAdapter.notifyDataSetChanged();
        }
    }

    private String buildUrl() {
        String url = URLHelper.build(this, "cercaAnnunciCreatiDaUtente");


        try {
            url += "username=" + URLEncoder.encode(Heap.getUserCorrente().getUsername(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return url;
    }
}
