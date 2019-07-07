package com.fct.neec.oficial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.VISIBLE;

/**
 *
 */
public class NoticiasFragment extends Fragment {

    private FrameLayout fragmentContainer;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Create a new instance of the fragment
     */
    public static NoticiasFragment newInstance(int index) {
        NoticiasFragment fragment = new NoticiasFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_noticias, container, false);
        final WebView webview = view.findViewById(R.id.webview);
        webview.setVisibility(View.GONE);

        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.setVisibility(View.GONE);

                if(!url.startsWith("https://www.fct.unl.pt/noticias?page=") && !url.equals("https://www.fct.unl.pt/noticias")) {
                    webview.loadUrl("https://www.fct.unl.pt/noticias");
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                    return true;
                }
                return  false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                webview.loadUrl("javascript:(function() { " +
                        "document.getElementById('sliding-popup').style.display='none';})()");
                webview.loadUrl("javascript:(function() { " +
                        "document.getElementById('header3').style.display='none';})()");
                webview.loadUrl("javascript:(function() { " +
                        "document.getElementById('content_top').style.display='none';})()");
                webview.loadUrl("javascript:(function() { " +
                        "document.getElementById('header2').style.display='none';})()");
                webview.loadUrl("javascript:(function() { " +
                        "document.getElementById('header1').style.display='none';})()");
                webview.loadUrl("javascript:(function() { " +
                        "document.getElementById('breadcrumb').style.display='none';})()");
                webview.loadUrl("javascript:(function() { " +
                        "document.getElementById('footer').style.display='none';})()");
                webview.setVisibility(VISIBLE);


            }
        });

        webview.loadUrl("https://www.fct.unl.pt/noticias");

        return view;

    }


    /**
     * Refresh
     */
    public void refresh() {
        if (getArguments().getInt("index", 0) > 0 && recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
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
