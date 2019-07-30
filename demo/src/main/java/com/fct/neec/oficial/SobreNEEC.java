package com.fct.neec.oficial;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        if (!isInternetAvailable()) {
            Intent intent = new Intent(getContext(), SemNet.class);
            startActivity(intent);
        }

        View view = inflater.inflate(R.layout.sobreneec, container, false);
        final TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        final WebView webview = view.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://fctapp.neec-fct.com/SobreNEEC/");
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
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
                }
                if (position == 1) {
                    webview.loadUrl("https://fctapp.neec-fct.com/equipa/about.html");
                    webview.setHorizontalScrollBarEnabled(true);
                    webview.setOnTouchListener( null );
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return view;
    }

}