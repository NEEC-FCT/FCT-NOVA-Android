package com.fct.neec.oficial.PerdidosMasAchados;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fct.neec.oficial.R;
import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PerdidosEAchados extends AppCompatActivity {

    Dialog myDialog;
    static ProgressDialog dialog;
    public static final int PICK_IMAGE = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perdidosmasachados);

        myDialog = new Dialog(this);
        //Get Object
        //NEECLogo
        final ImageView add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShowPopup( findViewById(android.R.id.content) );
            }
        });
        WebView web = findViewById(R.id.webview);
        web.loadUrl("https://fctapp.neec-fct.com/PerdidosAchados/");
    }


    public static void uploadDone(){
        Log.d("Uploader" , "Upload done");
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {


            String realPath = ImageFilePath.getPath(PerdidosEAchados.this, data.getData());

            Log.i("PERDIDOS", "onActivityResult: file path : " + realPath);
            //setup params
             dialog = ProgressDialog.show(PerdidosEAchados.this, "",
                    "Enviando,por favor aguarde ... ", true);
            new UploadBackground().execute(realPath,"nome","local","descricao","categoria");


        } else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

    }

    public void ShowPopup(View v) {

        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.popupperdidos);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        //Spinner
        //get the spinner from the xml.
        Spinner dropdown = myDialog.findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{ "Roupa" , "Cartões" , "Material eletrónico" , "Óculos" , "Malas" , "Chaves" , "Livros, sebentas e cadernos" , "Outros" };
        String[] valuesPOST = new String[]{ "roupa" , "cartoes" , "eletro" , "oculos" , "malas" , "chaves" , "livros" , "outros" };

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Listeners
        Button pickFoto =(Button) myDialog.findViewById(R.id.pickFoto);
        pickFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mudar de fragmento
                //call back after permission granted
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }

                };

                //check all needed permissions together
                TedPermission.with(PerdidosEAchados.this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("Se recusar não poderá usar o CLIP\n" +
                                "\n" +
                                "Por favor vá a [Definições] > [Permissões]")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();

            }
        });

        myDialog.show();
    }





}
