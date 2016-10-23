package com.posvert.trasferimenti;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import database.LoaderDB;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);








        Button bottone3 = (Button) findViewById(R.id.bottone3);




        bottone3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent openPage1 = new Intent(MainActivity.this, PaginaAnnunciActivity.class);


                startActivity(openPage1);

            }
        });


    }

}
