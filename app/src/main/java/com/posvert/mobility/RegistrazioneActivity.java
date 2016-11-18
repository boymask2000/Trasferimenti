package com.posvert.mobility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.posvert.mobility.common.Config;
import com.posvert.mobility.common.SnackMsg;
import com.posvert.mobility.common.SpinnerInitializer;
import com.posvert.mobility.common.URLHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class RegistrazioneActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = null;
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private Spinner spinnerProvince;
    private Spinner spinnerRegioni;
    private Spinner spinnerComuni;

    private SpinnerInitializer spinnerInitializer;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_registrazione);

        mVisible = true;
        spinnerRegioni = (Spinner) findViewById(R.id.spinnerRegioni);
        spinnerProvince = (Spinner) findViewById(R.id.spinnerProvince);
        spinnerComuni = (Spinner) findViewById(R.id.spinnerComuni);

        spinnerInitializer = new SpinnerInitializer(spinnerRegioni, spinnerProvince, spinnerComuni, this);
        //  initSpinnerRegioni(spinnerRegioni);

        Button cercaEnte = (Button) findViewById(R.id.cercaEnte);


        cercaEnte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent act = new Intent(RegistrazioneActivity.this, GestioneEntiActivity.class);

                startActivityForResult(act,1);

            }
        });


        Button bottone1 = (Button) findViewById(R.id.registra);

        bottone1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!campiObbligatoriOK()) return;

                RequestQueue queue = Volley.newRequestQueue(getBaseContext());
                String url = buildUrl();
                Log.e("QQQQQQQQQQ", url);
// Request a string response from the provided URL.5.95.234.131
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response.equalsIgnoreCase("duplicated"))
                                    SnackMsg.showErrMsg(findViewById(R.id.registra), "Utente già registrato");

                                if (response.equalsIgnoreCase("ok")) {
                                    SnackMsg.showInfoMsg(findViewById(R.id.registra), "Registrazione eseguita");

                                }
                                //   Toast.makeText(context, "Utente già registrato", Toast.LENGTH_LONG);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Log.e("EEEEEEE", error.toString());
                    }
                });
// Add the request to the RequestQueue.
                queue.add(stringRequest);
                queue.start();

            }
        });

        String server = Config.getServerAddress(this);
        System.out.println(server);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (resultCode == RESULT_OK) {
                String nome = data.getStringExtra("nome");
                EditText t =(EditText)findViewById(R.id.ente);
                t.setText(nome);

            }

    }
    private boolean campiObbligatoriOK() {
        if (isEmpty(getVal(R.id.username))) {
            SnackMsg.showErrMsg(findViewById(R.id.registra), "Username è obbligatorio");

            return false;
        }
        if (isEmpty(getVal(R.id.password))) {
            SnackMsg.showErrMsg(findViewById(R.id.registra), "Password è obbligatorio");

            return false;
        }
        if (isEmpty(getVal(R.id.email))) {
            SnackMsg.showErrMsg(findViewById(R.id.registra), "Email è obbligatorio");

            return false;
        }
  /*      if (isEmpty(getVal(R.id.telefono))) {
            SnackMsg.showErrMsg(findViewById(R.id.registra), "Telefono è obbligatorio");

            return false;
        }*/
        return true;
    }

    private String getVal(int id) {
        return ((EditText) findViewById(id)).getText().toString();
    }

    private boolean isEmpty(String s) {
        if (s == null) return true;
        if (s.trim().length() == 0) return true;
        return false;
    }


//http://localhost:8080/Trasferimenti/trasferimenti/createUser?name=giovanni&password=zaq12wsx&regione=Campania&comune=Napoli&ente=Comune&livello=3

    private String buildUrl() {

        String url = URLHelper.build(this, "createUser");

        try {
            url += "name=" + URLEncoder.encode(((EditText) findViewById(R.id.username)).getText().toString(),"UTF-8");
            url += "&password=" + ((EditText) findViewById(R.id.password)).getText().toString();
            url += "&regione=" + spinnerInitializer.getRegione();
            url += "&provincia=" + spinnerInitializer.getProvincia();
            url += "&comune=" + URLEncoder.encode(spinnerInitializer.getComune(), "UTF-8");
            url += "&ente=" + URLEncoder.encode(((EditText) findViewById(R.id.ente)).getText().toString(), "UTF-8");
            url += "&telefono=" + ((EditText) findViewById(R.id.telefono)).getText().toString();
            url += "&email=" + ((EditText) findViewById(R.id.email)).getText().toString();
            url += "&livello=" + ((EditText) findViewById(R.id.livello)).getText().toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = null;
        if (actionBar != null) {
            actionBar.hide();
        }
//        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
