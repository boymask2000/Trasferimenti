package com.posvert.trasferimenti;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        setContentView(R.layout.activity_main);


        Button  bottone1 = (Button) findViewById(R.id.bottone1);

        Button bottone3 = (Button) findViewById(R.id.bottone3);


        bottone1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent openPage1 = new Intent(MainActivity.this, RegistrazioneActivity.class);


                startActivity(openPage1);

            }
        });

        bottone3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent openPage1 = new Intent(MainActivity.this, PaginaAnnunciActivity.class);


                startActivity(openPage1);

            }
        });



       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
