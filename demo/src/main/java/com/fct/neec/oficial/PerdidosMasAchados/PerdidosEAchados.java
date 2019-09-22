package com.fct.neec.oficial.PerdidosMasAchados;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fct.neec.oficial.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PerdidosEAchados extends AppCompatActivity {

    Dialog myDialog;
    public static final int PICK_IMAGE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perdidosmasachados);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {


            String realPath = ImageFilePath.getPath(PerdidosEAchados.this, data.getData());

            Log.i("PERDIDOS", "onActivityResult: file path : " + realPath);
            //setup params
            Map<String, String> params = new HashMap<String, String>(2);
            params.put("nome", "Veloso");
            params.put("local", "ed 7");
            params.put("descricao", "descricao");
            params.put("categoria", "Material eletrónico");

            String result = PedidoFormData.multipartRequest("http://fcthub.neec-fct.com/PHP/uploadPerdidos.php", params, realPath, "image", "multipart/form-data;");
            //next parse result string
            Log.i("PERDIDOS", "Pedido deu em  : " + result);

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
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Listeners
        Button pickFoto =(Button) myDialog.findViewById(R.id.pickFoto);
        pickFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        myDialog.show();
    }
}
