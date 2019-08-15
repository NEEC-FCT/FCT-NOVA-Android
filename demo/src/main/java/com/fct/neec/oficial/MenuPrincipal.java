package com.fct.neec.oficial;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fct.neec.oficial.RegrasSegurança.RegrasDoenca;
import com.fct.neec.oficial.RegrasSegurança.RegrasEvacuacao;
import com.fct.neec.oficial.RegrasSegurança.RegrasIncendio;
import com.fct.neec.oficial.RegrasSegurança.RegrasSismo;

public class MenuPrincipal extends AppCompatActivity {


    final String[] items = new String[]{"Contactar Segurança", "Em caso de doença súbita/acidente",
            "Em caso de alarme de evacuação", "Em caso de incêndio", "Em caso de sismo"};
    private boolean star = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        final NDSpinner spinner = findViewById(R.id.popupspinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (star) {
                    Object item = parent.getItemAtPosition(position);
                    Log.d("Spinner", item.toString());

                    //Contactar Segurança
                    if (item.toString().equals(items[0])) {
                        new AlertDialog.Builder(MenuPrincipal.this, R.style.AlertDialogCustom)
                                .setTitle("Contactar Segurança")
                                .setMessage("Não te esqueças:\n" +
                                        "o Localização\n" +
                                        "o Número de vítimas e idade\n" +
                                        "o Sintomas ou informações importantes\n" +
                                        "o Outros perigos (gases perigosos, incêndio)")


                                .setNeutralButton("Cancelar", null)

                                .setPositiveButton("Grave", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String phone = "112";
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                        startActivity(intent);
                                    }
                                })

                                .setNegativeButton("Não é grave", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String phone = "+351916025546";
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                        startActivity(intent);
                                    }
                                })


                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }

                    //Caso de doença
                    else if (item.toString().equals(items[1])) {
                        Intent myIntent = new Intent(MenuPrincipal.this, RegrasDoenca.class);
                        startActivity(myIntent);
                    }

                    //Caso de Evacuação
                    else if (item.toString().equals(items[2])) {
                        Intent myIntent = new Intent(MenuPrincipal.this, RegrasEvacuacao.class);
                        startActivity(myIntent);
                    }

                    //Caso de incêndio
                    else if (item.toString().equals(items[3])) {
                        Intent myIntent = new Intent(MenuPrincipal.this, RegrasIncendio.class);
                        startActivity(myIntent);
                    }

                    //Caso de sismo
                    else if (item.toString().equals(items[4])) {
                        Intent myIntent = new Intent(MenuPrincipal.this, RegrasSismo.class);
                        startActivity(myIntent);
                    }
                }

                star = true;

            }

            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Time", spinner.getSelectedItem().toString());
            }
        });

        //info
        ImageView info = findViewById(R.id.infobutton);
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(1);
            }
        });

        //calendar
        ImageView calendar = findViewById(R.id.calbutton);
        calendar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(3);
            }
        });
        //Mapa
        ImageView map = findViewById(R.id.mapbutton);
        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(4);
            }
        });

        //Mapa
        ImageView noticias = findViewById(R.id.noticiasbutton);
        noticias.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(2);
            }
        });

        //Happy meal
        ImageView hm = findViewById(R.id.happymealbutton);
        hm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startNewActivity(getApplicationContext(), "miguelcalado.restauracaofinal");
            }
        });

        //Team button
        ImageView team = findViewById(R.id.teambutton);
        team.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMainActivity(0);

            }
        });

        //Botão SOS
        final Button sos = findViewById(R.id.sos);
        sos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                spinner.performClick();


            }
        });
    }

    private void startMainActivity(int positon) {
        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt("Separador", positon);
        edit.commit();
        //start activity
        Intent myIntent = new Intent(MenuPrincipal.this, MainActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
        //fecha a app
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
