package com.fct.neec.oficial;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sobreneec, container, false);
        final TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        final WebView webview = view.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://fctapp.neec-fct.com/");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("TAB", "clicou em: " + position);
                if (position == 0) {
                    webview.loadUrl("https://fctapp.neec-fct.com/");
                }
                if (position == 1) {
                    webview.loadUrl("https://fctapp.neec-fct.com/equipa/about.html");
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