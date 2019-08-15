package com.fct.neec.oficial.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.fct.neec.oficial.AlgoErradoAconteceu;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.SemNet;
import com.google.android.material.tabs.TabLayout;

import static android.view.View.VISIBLE;

public class MapaFragment extends Fragment {


    /**
     * Create a new instance of the fragment
     */

    private WebView webview;
    private int position = 0;


    public static MapaFragment newInstance(int index) {
        MapaFragment fragment = new MapaFragment();
        return fragment;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mapa, container, false);

        if (!isInternetAvailable()) {
            Intent intent = new Intent(getContext(), SemNet.class);
            startActivity(intent);
        } else {


            final TabLayout tabLayout = view.findViewById(R.id.tabLayout);
            webview = view.findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            webview.setVisibility(View.GONE);
            webview.loadUrl("https://www.google.com/maps/d/viewer?mid=1puDPKCs1qt4eyU1fK2EfzPCHyQzkfm6n&ll=38.661303032631146%2C-9.205898544352294&z=16");

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    position = tab.getPosition();
                    Log.d("TAB", "clicou em: " + position);
                    if (position == 0) {
                        webview.loadUrl("https://www.google.com/maps/d/viewer?mid=1puDPKCs1qt4eyU1fK2EfzPCHyQzkfm6n&ll=38.661303032631146%2C-9.205898544352294&z=16");
                        webview.setVisibility(View.GONE);

                    }
                    if (position == 1) {
                        webview.loadUrl("https://www.google.com/maps/d/u/0/viewer?mid=1TdpAcDgdncinIqJLrr504ZMAJe6zQ2il&ll=38.661303032631146%2C-9.205898544352294&z=16");
                        webview.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            webview.setWebViewClient(new WebViewClient() {


                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    Log.d("URL" , url);
                    if (url.equals("https://www.google.com/maps/d/u/0/viewer?mid=1TdpAcDgdncinIqJLrr504ZMAJe6zQ2il&ll=38.661303032631146%2C-9.205898544352294&z=16") || url.equals("https://www.google.com/maps/d/viewer?mid=1puDPKCs1qt4eyU1fK2EfzPCHyQzkfm6n&ll=38.661303032631146%2C-9.205898544352294&z=16")) {
                        webview.loadUrl(url);
                    }
                    if (url.startsWith("https://accounts.google")){
                        Log.d("URL" , "Apanhei google");
                        if (position == 0) {
                            webview.loadUrl("https://www.google.com/maps/d/viewer?mid=1puDPKCs1qt4eyU1fK2EfzPCHyQzkfm6n&ll=38.661303032631146%2C-9.205898544352294&z=16");
                            webview.setVisibility(View.GONE);

                        }
                        if (position == 1) {
                            webview.loadUrl("https://www.google.com/maps/d/u/0/viewer?mid=1TdpAcDgdncinIqJLrr504ZMAJe6zQ2il&ll=38.661303032631146%2C-9.205898544352294&z=16");
                            webview.setVisibility(View.GONE);
                        }
                    }
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {

                    webview.loadUrl("javascript:(function() { " +
                            "document.getElementById('gbr').style.display='none';})()");
                    webview.setVisibility(VISIBLE);


                }
            });
        }

        return view;
    }

}



