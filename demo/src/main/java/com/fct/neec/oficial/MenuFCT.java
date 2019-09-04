package com.fct.neec.oficial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuFCT extends AppCompatActivity {




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menufct);

        //calendario
        final ImageView calendario = (ImageView) findViewById(R.id.calendario);
        calendario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(3);
            }
        });
        //Mapa
        final ImageView mapa = (ImageView) findViewById(R.id.mapa);
        mapa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(4);
            }
        });
        //Noticias
        final ImageView noticias = (ImageView) findViewById(R.id.noticias);
        noticias.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(2);
            }
        });
        //ECO
        final ImageView eco = (ImageView) findViewById(R.id.eco);
        eco.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
            }
        });
        //Restaurantes
        final ImageView restaurante = (ImageView) findViewById(R.id.restaurante);
        restaurante.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startNewActivity(getApplicationContext(), "miguelcalado.restauracaofinal");
            }
        });
        //Informacao
        final ImageView informacao = (ImageView) findViewById(R.id.informacao);
        informacao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(1);
            }
        });
        //Sobre
        final ImageView sobre = (ImageView) findViewById(R.id.sobre);
        sobre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fctapp.neec-fct.com/equipa/about.html"));
                startActivity(browserIntent);
            }
        });



        }

    private void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void startMainActivity(int positon) {
        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt("Separador", positon);
        edit.commit();
        //start activity
        Intent myIntent = new Intent(MenuFCT.this, MainActivity.class);
        startActivity(myIntent);
    }



}
