package com.fct.neec.oficial;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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

import com.google.android.material.tabs.TabLayout;

import static android.view.View.VISIBLE;

public class MapaFragment extends Fragment {


    /**
     * Create a new instance of the fragment
     */

    private WebView webview;

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
            webview.setVisibility(View.INVISIBLE);
            webview.loadUrl("https://www.google.com/maps/d/viewer?mid=1puDPKCs1qt4eyU1fK2EfzPCHyQzkfm6n&ll=38.661303032631146%2C-9.205898544352294&z=16");


            webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            webview.setWebViewClient(new WebViewClient() {

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    Intent myIntent = new Intent(getContext(), AlgoErradoAconteceu.class);
                    startActivity(myIntent);
                }
            }
            );


            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    Log.d("TAB", "clicou em: " + position);
                    if (position == 0) {
                        webview.setVisibility(View.INVISIBLE);
                        webview.loadUrl("https://www.google.com/maps/d/viewer?mid=1puDPKCs1qt4eyU1fK2EfzPCHyQzkfm6n&ll=38.661303032631146%2C-9.205898544352294&z=16");
                        webview.setWebViewClient(new WebViewClient() {


                            @Override
                            public void onPageFinished(WebView view, String url) {

                                webview.loadUrl("javascript:(function() { " +
                                        "document.getElementById('gbr').style.display='none';})()");
                                webview.setVisibility(VISIBLE);


                            }
                        });
                    }
                    if (position == 1) {
                        webview.setVisibility(View.INVISIBLE);
                        webview.loadUrl("https://www.google.com/maps/d/u/0/viewer?mid=1TdpAcDgdncinIqJLrr504ZMAJe6zQ2il&ll=38.661303032631146%2C-9.205898544352294&z=16");
                        webview.setWebViewClient(new WebViewClient() {

                            @Override
                            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                //Your code to do
                                Intent myIntent = new Intent(getContext(), AlgoErradoAconteceu.class);
                                startActivity(myIntent);
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {

                                webview.loadUrl("javascript:(function() { " +
                                        "document.getElementById('gbr').style.display='none';})()");
                                webview.setVisibility(VISIBLE);


                            }
                        });
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }

        webview.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {

                webview.loadUrl("javascript:(function() { " +
                        "document.getElementById('gbr').style.display='none';})()");
                webview.setVisibility(VISIBLE);


            }
        });
        return view;
    }

}



