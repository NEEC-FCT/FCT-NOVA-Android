package com.fct.neec.oficial.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.fct.neec.oficial.AlgoErradoAconteceu;
import com.fct.neec.oficial.BuildConfig;
import com.fct.neec.oficial.MainActivity;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.RegrasSegurança.RegrasDoenca;
import com.fct.neec.oficial.RegrasSegurança.RegrasEvacuacao;
import com.fct.neec.oficial.RegrasSegurança.RegrasIncendio;
import com.fct.neec.oficial.RegrasSegurança.RegrasSismo;
import com.fct.neec.oficial.SemNet;
import com.github.clans.fab.FloatingActionButton;


public class InfoFragment extends Fragment {


    public static String CONTENT_AUTHORITY;
    private WebView webview;

    /**
     * Create a new instance of the fragment
     */
    public static InfoFragment newInstance(int index) {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info, container, false);

        // Set provider authority
        CONTENT_AUTHORITY = getString(R.string.provider_authority);

        if (!isInternetAvailable()) {
            Intent intent = new Intent(getContext(), SemNet.class);
            startActivity(intent);
        } else {


            webview = view.findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.loadUrl("https://fctapp.neec-fct.com/Informacao/");
            webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

            webview.setWebViewClient(new WebViewClient() {

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    //Your code to do
                    Intent myIntent = new Intent(getContext(), AlgoErradoAconteceu.class);
                    startActivity(myIntent);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    webview.setVisibility(View.GONE);

                    if (url.startsWith("https://www.fct.unl.pt/estudante/informacao-academica/prazos")) {
                        ((MainActivity) getActivity()).changeFragment(3 , false);
                        return false;
                    } else {
                        ((MainActivity) getActivity()).changeFragment(1 , false);
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                    }
                    return false;
                }
            });

            //FAB listeners
            //Telemovel
            FloatingActionButton phone = view.findViewById(R.id.ligar);
            phone.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom)
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
            });
            //sismo
            FloatingActionButton sismo = view.findViewById(R.id.sismo);
            sismo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent myIntent = new Intent(getContext(), RegrasSismo.class);
                    startActivity(myIntent);
                }
            });
            //Fogo
            FloatingActionButton fogo = view.findViewById(R.id.fogo);
            fogo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent myIntent = new Intent(getContext(), RegrasIncendio.class);
                    startActivity(myIntent);
                }
            });
            //Doença
            FloatingActionButton doenca = view.findViewById(R.id.doenca);
            doenca.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent myIntent = new Intent(getContext(), RegrasDoenca.class);
                    startActivity(myIntent);
                }
            });
            //Fogo
            FloatingActionButton alarme = view.findViewById(R.id.alarme);
            alarme.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent myIntent = new Intent(getContext(), RegrasEvacuacao.class);
                    startActivity(myIntent);
                }
            });


        }

        return view;
    }


    @SuppressLint("NewApi")
    private void enableStrictMode() {
        if (!BuildConfig.DEBUG)
            return;

        // Enable StrictMode
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll().penaltyLog().build());

        // Policy applied to all threads in the virtual machine's process
        final StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder();
        vmPolicyBuilder.detectAll();
        vmPolicyBuilder.penaltyLog();


        StrictMode.setVmPolicy(vmPolicyBuilder.build());
    }


}