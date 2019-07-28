package com.fct.neec.oficial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.view.View.VISIBLE;

/**
 *
 */
public class NoticiasFragment extends Fragment {


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

                if (!url.startsWith("https://www.fct.unl.pt/noticias?page=") && !url.equals("https://www.fct.unl.pt/noticias")) {
                    webview.loadUrl("https://www.fct.unl.pt/noticias");
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                    return true;
                }
                return false;
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
}
