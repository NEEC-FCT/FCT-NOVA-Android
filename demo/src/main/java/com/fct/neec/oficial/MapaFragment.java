package com.fct.neec.oficial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.VISIBLE;

public class MapaFragment extends Fragment {

    private FrameLayout fragmentContainer;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

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


    /**
     * Refresh
     */
    public void refresh() {

    }

    /**
     * Called when a fragment will be displayed
     */
    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            fragmentContainer.startAnimation(fadeIn);
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    public void willBeHidden() {
        if (fragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            fragmentContainer.startAnimation(fadeOut);
        }
    }
}
