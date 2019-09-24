package com.fct.neec.oficial;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MenuFCT extends AppCompatActivity {




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menufct);

        //NEECLogo
        final TextView power = (TextView) findViewById(R.id.power);
        power.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://neec-fct.com/"));
                startActivity(browserIntent);
            }
        });

        //NEECLogo
        final ImageView neec = (ImageView) findViewById(R.id.neec);
        neec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://neec-fct.com/"));
                startActivity(browserIntent);
            }
        });

        //Facebook
        final ImageView facebook = (ImageView) findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/fct.nova/"));
                startActivity(browserIntent);
            }
        });

        //instagram
        final ImageView instagram = (ImageView) findViewById(R.id.instagram);
        instagram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/fctnova/"));
                startActivity(browserIntent);
            }
        });

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
        //Horario
        final ImageView horario = (ImageView) findViewById(R.id.horario);
        horario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(7);
            }
        });
        //Clip
        final ImageView clip = (ImageView) findViewById(R.id.clip);
        clip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(9);
            }
        });
        //avaliacao
        final ImageView avaliacao = (ImageView) findViewById(R.id.avaliacao);
        avaliacao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(10);
            }
        });
        //ECO
        final ImageView eco = (ImageView) findViewById(R.id.eco);
        eco.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("ECO", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("ECO", 1);
                editor.commit();
                startMainActivity(4);
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

        //Propinas
        final ImageView propinas = (ImageView) findViewById(R.id.propinas);
        propinas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //start activity
                Intent myIntent = new Intent(MenuFCT.this, ShowMB.class);
                startActivity(myIntent);

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


    @Override
    public void onBackPressed() {
        //Volta noticias
        startMainActivity(2);
    }

    private void startMainActivity(final int positon) {
        //mudar de fragmento
        //call back after permission granted
        if( positon == 9 || positon == 7 || positon == 10 ){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if( positon == 9){
                    Log.d("CLIP", "Selecionei 10");
                    if (ClipSettings.getYearSelected( MenuFCT.this ) != null) {
                        Log.d("CLIP", "Vai para o horario");

                        if (ClipSettings.getYearSelected( MenuFCT.this ) != null) {
                            Log.d("CLIP", "Vai para o horario");

                            goToMain(8);
                        }
                        // If the user has already login, start the StudentNumbersActivity instead
                        else if (ClipSettings.isUserLoggedIn( MenuFCT.this )) {
                            Log.d("CLIP", "ConnectClipActivity - user has already login");

                            goToMain(6);
                        } else {
                            goToMain(5);
                        }
                    }
                    // If the user has already login, start the StudentNumbersActivity instead
                    else if (ClipSettings.isUserLoggedIn(MenuFCT.this)) {
                        Log.d("CLIP", "ConnectClipActivity - user has already login");

                        goToMain(6);
                    } else {
                        Log.d("CLIP", "Vai para o login");
                        goToMain(5);
                    }
                }
                else if( positon == 7){
                    Log.d("CLIP", "Selecionei 10");
                    if (ClipSettings.getYearSelected( MenuFCT.this ) != null) {
                        Log.d("CLIP", "Vai para o horario");

                        if (ClipSettings.getYearSelected( MenuFCT.this ) != null) {
                            Log.d("CLIP", "Vai para o horario");

                            goToMain(7);
                        }
                        // If the user has already login, start the StudentNumbersActivity instead
                        else if (ClipSettings.isUserLoggedIn( MenuFCT.this )) {
                            Log.d("CLIP", "ConnectClipActivity - user has already login");

                            goToMain(6);
                        } else {
                            goToMain(5);
                        }
                    }
                    // If the user has already login, start the StudentNumbersActivity instead
                    else if (ClipSettings.isUserLoggedIn(MenuFCT.this)) {
                        Log.d("CLIP", "ConnectClipActivity - user has already login");

                        goToMain(6);
                    } else {
                        Log.d("CLIP", "Vai para o login");
                        goToMain(5);
                    }
                }
                else if( positon == 10){
                    Log.d("CLIP", "Selecionei 10");
                    if (ClipSettings.getYearSelected( MenuFCT.this ) != null) {
                        Log.d("CLIP", "Vai para o horario");

                        if (ClipSettings.getYearSelected( MenuFCT.this ) != null) {
                            Log.d("CLIP", "Vai para o horario");

                            goToMain(10);
                        }
                        // If the user has already login, start the StudentNumbersActivity instead
                        else if (ClipSettings.isUserLoggedIn( MenuFCT.this )) {
                            Log.d("CLIP", "ConnectClipActivity - user has already login");

                            goToMain(6);
                        } else {
                            goToMain(5);
                        }
                    }
                    // If the user has already login, start the StudentNumbersActivity instead
                    else if (ClipSettings.isUserLoggedIn(MenuFCT.this)) {
                        Log.d("CLIP", "ConnectClipActivity - user has already login");

                        goToMain(6);
                    } else {
                        Log.d("CLIP", "Vai para o login");
                        goToMain(5);
                    }
                }
                else {
                    goToMain(positon);
                }

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }

        };

        //check all needed permissions together
        TedPermission.with(MenuFCT.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("Se recusar não poderá usar o CLIP\n" +
                        "\n" +
                        "Por favor vá a [Definições] > [Permissões]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
        }
        else {
            goToMain(positon);
        }


    }

    private void goToMain(int positon) {
        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt("Separador", positon);
        edit.commit();
        //start activity
        Intent myIntent = new Intent(MenuFCT.this, MainActivity.class);
        startActivity(myIntent);
    }


}
