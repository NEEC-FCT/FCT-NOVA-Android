package com.fct.neec.oficial.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class SobreNEEC extends Fragment {

    /**
     * Create a new instance of the fragment
     */
    public static SobreNEEC newInstance(int index) {
        SobreNEEC fragment = new SobreNEEC();
        return fragment;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sobreneec, container, false);

        if (!isInternetAvailable()) {
            Intent intent = new Intent(getContext(), SemNet.class);
            startActivity(intent);
        } else {


            final TabLayout tabLayout = view.findViewById(R.id.tabLayout);
            final WebView webview = view.findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.loadUrl("https://fctapp.neec-fct.com/SobreNEEC/");
            webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            webview.setWebViewClient(new WebViewClient() {

                                         @Override
                                         public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                             Intent myIntent = new Intent(getContext(), AlgoErradoAconteceu.class);
                                             startActivity(myIntent);
                                         }
                                     }
            );
            webview.setHorizontalScrollBarEnabled(false);
            webview.setOnTouchListener(new View.OnTouchListener() {
                float m_downX;

                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getPointerCount() > 1) {
                        //Multi touch detected
                        return true;
                    }

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            // save the x
                            m_downX = event.getX();
                            break;
                        }
                        case MotionEvent.ACTION_MOVE:
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP: {
                            // set x so that it doesn't move
                            event.setLocation(m_downX, event.getY());
                            break;
                        }

                    }
                    return false;
                }
            });
            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                        view.getContext().startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    Log.d("TAB", "clicou em: " + position);
                    if (position == 0) {
                        webview.loadUrl("https://fctapp.neec-fct.com/SobreNEEC/");
                        webview.setHorizontalScrollBarEnabled(false);
                        webview.setOnTouchListener(new View.OnTouchListener() {
                            float m_downX;

                            public boolean onTouch(View v, MotionEvent event) {

                                if (event.getPointerCount() > 1) {
                                    //Multi touch detected
                                    return true;
                                }

                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_DOWN: {
                                        // save the x
                                        m_downX = event.getX();
                                        break;
                                    }
                                    case MotionEvent.ACTION_MOVE:
                                    case MotionEvent.ACTION_CANCEL:
                                    case MotionEvent.ACTION_UP: {
                                        // set x so that it doesn't move
                                        event.setLocation(m_downX, event.getY());
                                        break;
                                    }

                                }
                                return false;
                            }
                        });
                        webview.setWebViewClient(new WebViewClient() {
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                                    view.getContext().startActivity(
                                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });

                    }
                    if (position == 1) {
                        webview.loadUrl("https://fctapp.neec-fct.com/equipa/about.html");
                        webview.setHorizontalScrollBarEnabled(true);
                        webview.setOnTouchListener(null);
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
        return view;
    }

}