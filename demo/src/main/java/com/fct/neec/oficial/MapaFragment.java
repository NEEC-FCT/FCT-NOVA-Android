package com.fct.neec.oficial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.view.View.VISIBLE;

public class MapaFragment extends Fragment {


    /**
     * Create a new instance of the fragment
     */
    public static MapaFragment newInstance(int index) {
        MapaFragment fragment = new MapaFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        final WebView webview = view.findViewById(R.id.webview);
        webview.setVisibility(View.GONE);

        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {

                webview.loadUrl("javascript:(function() { " +
                        "document.getElementById('gbr').style.display='none';})()");
                webview.setVisibility(VISIBLE);


            }
        });

        webview.loadUrl("https://www.google.com/maps/d/viewer?mid=1puDPKCs1qt4eyU1fK2EfzPCHyQzkfm6n&ll=38.661303032631146%2C-9.205898544352294&z=16");

        return view;

    }


}
