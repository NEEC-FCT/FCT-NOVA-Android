package com.fct.neec.oficial;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SemNet extends AppCompatActivity {

    // Init
    private Handler handler = new Handler();
    private Runnable runnable;

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.semnet);


        runnable = new Runnable() {
            @Override
            public void run() {
                if (isInternetAvailable()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else {
                    handler.postDelayed(this, 600);
                }

            }
        };
        //iniciar
        handler.postDelayed(runnable, 500);

    }

    @Override
    public void onBackPressed() {
        if (isInternetAvailable()){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
