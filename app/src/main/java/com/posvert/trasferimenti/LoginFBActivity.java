package com.posvert.trasferimenti;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.posvert.trasferimenti.common.Heap;
import com.posvert.trasferimenti.common.ResponseHandler;
import com.posvert.trasferimenti.common.URLHelper;
import com.posvert.trasferimenti.helper.UtentiHelper;


import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;

import beans.JSONHandler;
import beans.Utente;
import database.LoaderDB;

public class LoginFBActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private static AccessToken tok;
    private ProfileTracker profileTracker;
    private String fbname;

    private TextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Heap.setLoginFB(false);


        //    FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_login_fb);

        showVersion();


        if (!UtentiHelper.isUserOnline(this)) {
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

// 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Non sei in rete")
                    .setTitle("Attenzione!");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.e("WWW", "RIPROVA");
                }
            });
            builder.setNegativeButton("Esci", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });

// 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();

        }


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        callbackManager = CallbackManager.Factory.create();


/*        startService(new Intent(this, MyFirebaseInstanceIDService.class));
        startService(new Intent(this, MyFirebaseMessagingService.class));*/

        //    View view = inflater.inflate(R.layout.splash, container, false);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        //  loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Collection<String> c = Arrays.asList("public_profile");
                // App code Arrays.asList("public_profile")
                //     LoginManager.getInstance().logInWithReadPermissions(LoginFBActivity.this,c);
                Heap.setLoginFB(true);

                tok = AccessToken.getCurrentAccessToken();
                Log.e("FACEB", tok.getUserId());
                getToken();


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                if (currentProfile == null) return;
                String firstName = currentProfile.getFirstName();
                String lastName = currentProfile.getLastName();
                fbname = currentProfile.getName();


                // App code

                URLHelper.invokeURL(LoginFBActivity.this, buildCheckUrl(), new ResponseHandler() {
                    @Override
                    public void parseResponse(String response) {
                        Log.e("WWW", response);
                        if (response.length() == 0)
                            initialRegistration();

                        else {
                            try {
                                JSONObject obj = new JSONObject(response);

                                Utente u = JSONHandler.parseUtenteJSON(obj);

                                Heap.setUserCorrente(u);
                                getToken();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            startAll();
                        }
                    }
                });

            }
        };

        LoaderDB loader = new LoaderDB(this);
        //   loader.caricaDati();
        loader.installaDB();


        // Set up the login form.
        mEmailView = (TextView) findViewById(R.id.username);
        //    populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
      /*  mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });*/

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eseguiControllo();
            }
        });

/*        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);*/

        Button bottone1 = (Button) findViewById(R.id.bottone1);
        bottone1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent openPage1 = new Intent(LoginFBActivity.this, RegistrazioneActivity.class);


                startActivity(openPage1);

            }
        });
        getNumUtenti();


    }

    private void showVersion(){
        try {
            PackageInfo packageinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String ss =packageinfo.versionName.toString();

            TextView ver = (TextView) findViewById(R.id.versione);
            ver.setText("Ver "+ss);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getToken() {

    //    tok = AccessToken.getCurrentAccessToken();
        String vv = FirebaseInstanceId.getInstance().getToken();
        Log.e("FACEB1", vv);
        MyFirebaseInstanceIDService.sendRegistrationToServer(LoginFBActivity.this, vv);

    }

    private String buildUrlCount() {
        String url = URLHelper.build(this, "getCountUtenti");
        url = url + "dummy=0";
        return url;
    }

    private int getNumUtenti() {
        URLHelper.invokeURL(LoginFBActivity.this, buildUrlCount(), new ResponseHandler() {
            @Override
            public void parseResponse(String response) {
                TextView ur = (TextView) findViewById(R.id.utenti_registrati);
                ur.setText(response, TextView.BufferType.EDITABLE);
            }
        });

        return 0;
    }

    private void initialRegistration() {
        URLHelper.invokeURL(this, buildregistrUrl(), new ResponseHandler() {
            @Override
            public void parseResponse(String response) {
                if (response.equalsIgnoreCase("ok")) {
                    Utente u = new Utente();
                    u.setUsername(fbname);

                    Heap.setUserCorrente(u);

                    Intent act = new Intent(LoginFBActivity.this, GestioneProfiloActivity.class);

                    startActivityForResult(act, 44);
                }

            }
        });
    }

    private String buildregistrUrl() {

        String url = URLHelper.build(this, "createUser");


        url += "name=" + URLEncoder.encode(fbname);
        url += "&password=";
        url += "&regione=";
        url += "&provincia=";
        url += "&comune=";
        url += "&ente=";
        url += "&telefono=";
        url += "&email=";
        url += "&livello=";
        url += "&fbuserid=" + getFBUserId();
        return url;
    }

    private String buildCheckUrl() {
        String url = URLHelper.build(this, "getUtenteByFBUserid");
        url += "fbuserid=" + getFBUserId();
        return url;
    }

    public static String getFBUserId() {
        String userId = tok.getUserId();
        return userId;
    }

    public static InputStream getFacebookProfilePicture(String userID) throws Exception {
        String imageURL;

        Bitmap bitmap = null;
        imageURL = "http://graph.facebook.com/" + userID + "/picture?type=small";
        InputStream in = (InputStream) new URL(imageURL).getContent();
        bitmap = BitmapFactory.decodeStream(in);

        return in;
    }

    public void startAll() {
        Intent openPage1 = new Intent(LoginFBActivity.this, PaginaAnnunciActivity.class);
        startActivity(openPage1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 44:
                startAll();
                break;
            default:
                callbackManager.onActivityResult(requestCode, resultCode, data);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setUserOffline();
        if (profileTracker != null)
            profileTracker.stopTracking();
    }

    private void eseguiControllo() {

        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        String url = buildUrl();
        Log.e("URL", url);
// Request a string response from the provided URL.5.95.234.131
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Log.e("QQQQQQQQQQ", response);
                        if (response != null && response.contains("userid")) {

                            try {
                                JSONObject obj = new JSONObject(response);

                                Utente u = JSONHandler.parseUtenteJSON(obj);

                                Heap.setUserCorrente(u);
                                getToken();

                                //      FirebaseInstanceId.getInstance().deleteInstanceId();


                                setUserOnline();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            Intent openPage1 = new Intent(LoginFBActivity.this, PaginaAnnunciActivity.class);


                            startActivity(openPage1);
                        }
                        if (response == null || !response.equalsIgnoreCase("1")) {


                            Snackbar.make(findViewById(R.id.bottone1), "Username o passowrd errata",
                                    Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Snackbar.make(findViewById(R.id.bottone1), message,
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.start();
    }

    private String buildUrl() {
        String url = URLHelper.build(this, "checkLoginUtente");

        url += "username=" + mEmailView.getText().toString();
        url += "&password=" + mPasswordView.getText().toString();

        return url;
    }

    private void setUserOnline() {
        if (Heap.getUserCorrente() == null) return;
        String url = URLHelper.build(this, "setUserOnline");
        url += "username=" + Heap.getUserCorrente().getUsername();
        URLHelper.invokeURL(this, url, new ResponseHandler() {
            @Override
            public void parseResponse(String response) {

            }
        });
    }

    private void setUserOffline() {
        if (Heap.getUserCorrente() == null) return;
        String url = URLHelper.build(this, "setUserOffline");
        url += "username=" + Heap.getUserCorrente().getUsername();
        URLHelper.invokeURL(this, url, new ResponseHandler() {
            @Override
            public void parseResponse(String response) {

            }
        });
    }


}
