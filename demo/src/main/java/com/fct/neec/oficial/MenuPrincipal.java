package com.fct.neec.oficial;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPrincipal extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        //info
        ImageView info =  findViewById(R.id.infobutton);
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(1);
            }
        });

        //calendar
        ImageView calendar =  findViewById(R.id.calbutton);
        calendar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(3);
            }
        });
        //Mapa
        ImageView map =  findViewById(R.id.mapbutton);
        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(4);
            }
        });

        //Mapa
        ImageView noticias =  findViewById(R.id.noticiasbutton);
        noticias.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(2);
            }
        });

        //Happy meal
        ImageView hm =  findViewById(R.id.happymealbutton);
        hm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startNewActivity(getApplicationContext(), "miguelcalado.restauracaofinal");
            }
        });

        //Team button
        ImageView team =  findViewById(R.id.teambutton);
        team.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(0);

            }
        });
    }

    private void startMainActivity(int positon){
        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
        SharedPreferences.Editor edit= preferences.edit();
        edit.putInt("Separador", positon);
        edit.commit();
        //start activity
        Intent myIntent = new Intent(MenuPrincipal.this, DemoActivity.class);
        startActivity(myIntent);
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
}
