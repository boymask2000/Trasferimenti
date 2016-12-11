package com.posvert.mobility;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.posvert.mobility.chat.ChatRequest;
import com.posvert.mobility.common.Heap;
import com.posvert.mobility.common.ResponseHandler;
import com.posvert.mobility.common.URLBuilder;
import com.posvert.mobility.common.URLHelper;
import com.posvert.mobility.geo.GeoUtil;
import com.posvert.mobility.geo.MyLocationListener;
import com.posvert.mobility.helper.IExecutor;
import com.posvert.mobility.helper.MessaggioOfflineHelper;
import com.posvert.mobility.helper.UtentiHelper;
import com.posvert.mobility.helper.Util;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import beans.Annuncio;
import beans.JSONHandler;
import beans.Utente;
import liste.adapters.ListaAnnunciAdapter;
import liste.adapters.ListaChatAdapter;

public class PaginaAnnunciActivity extends AppCompatActivity implements PopupFragment.OnFragmentInteractionListener {
    private ListView mylist;
    private Button cercamatch = null;
    private List<Annuncio> lista = new ArrayList<>();
    private GeoUtil gUtil = null;

    private List<ChatRequest> listaChat = new ArrayList<>();
    private ListaAnnunciAdapter listaAnnunciAdapter = null;
    private ListaChatAdapter listaChatAdapter = null;
    private Button gestioneMessaggi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_annunci);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        //   ImageView fotoView = (ImageView) findViewById(R.id.foto);
        TextView userText = (TextView) findViewById(R.id.username);
        if (Heap.isLoginFB())
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
        mylist.setBackgroundColor(Color.CYAN);
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


        gestioneMessaggi = (Button) findViewById(R.id.vedimessaggi);
        gestioneMessaggi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent act = new Intent(PaginaAnnunciActivity.this, GestioneMessaggiOfflineActivity.class);

                startActivity(act);

            }
        });

        Button map = (Button) findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent act = new Intent(PaginaAnnunciActivity.this, MapsActivity.class);

                startActivity(act);

            }
        });
        Button chat = (Button) findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent act = new Intent(PaginaAnnunciActivity.this, GlobalChatActivity.class);

                startActivity(act);

            }
        });


        loadData();

        checkProfilo();

        initGeo();

    }
    /*----Method to Check GPS is enable or disable ----- */

    private void initGeo() {


        gUtil = new GeoUtil(this);
        gUtil.check(this);
        //     gUtil.init(this);


    }

    private void setImage() {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
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
                    if (lista.size() == 0) buildFragment();
                    listaAnnunciAdapter = new ListaAnnunciAdapter(PaginaAnnunciActivity.this, lista);
                    mylist.setAdapter(listaAnnunciAdapter);


                    //     if (lista.size() == 0) cercamatch.setEnabled(false);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
  /*      listaChatAdapter = new ListaChatAdapter(PaginaAnnunciActivity.this, listaChat);
        ChatRequest req = new ChatRequest("pippo");
        listaChat.add(req);
        listaChatAdapter.notifyDataSetChanged();*/
    }

    private void checkProfilo() {
        UtentiHelper.getUtenteByUsername(this, Heap.getUserCorrente().getUsername(), new IExecutor() {
            @Override
            public void exec(Object obj) {
                Utente u = (Utente) obj;
                boolean err = Util.isEmpty(u.getComune()) ||
                        Util.isEmpty(u.getProvincia()) ||
                        Util.isEmpty(u.getRegione());
                if( err){
                    MessaggioOfflineHelper.sendMessage(getBaseContext(), "SYSTEM", Heap.getUserCorrente().getUsername(), "Il tuo profilo utente è incompleto. ");
                }
            }
        });
    }

    private void buildFragment() {
/*        FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final Fragment fragment = new NuovoEnteFragment();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.commit();*/

        final Fragment fragment = PopupFragment.newInstance("Non hai nessun annuncio !!!! \nCreane uno col pulsante \"INSERISCI ANNUNCIO\"");
        DialogFragment newFragment = (DialogFragment) fragment;

        newFragment.show(getFragmentManager(), "dialog");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {
            loadData();
            listaAnnunciAdapter.notifyDataSetChanged();
        }
    }

    private String buildUrl() {

        String url = URLHelper.build(this, "cercaAnnunciCreatiDaUtente");

        URLBuilder builder = new URLBuilder(url);
        builder.addParameter("username", Heap.getUserCorrente().getUsername());
        url = builder.getUrl();


        return url;
    }

    @Override
    protected void onDestroy() {
        Heap.setLoginFB(false);
        Log.e("WW", "onDestroy");
        gUtil.unsetLocation();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("WW", "onResume");
        UtentiHelper.hasUnreadMessages(this, Heap.getUserCorrente().getUsername(), new ResponseHandler() {
            @Override
            public void parseResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.length() > 0)
                        gestioneMessaggi.setText("MESSAGGi (" + array.length() + ")");
                    else gestioneMessaggi.setText("MESSAGGi");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        Log.e("WW", "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        LoginManager.getInstance().logOut();

        Log.e("WW", "onStop");
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Vuoi uscire ?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PaginaAnnunciActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
