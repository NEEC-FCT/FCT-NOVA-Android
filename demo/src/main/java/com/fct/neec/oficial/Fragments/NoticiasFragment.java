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
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fct.neec.oficial.AlgoErradoAconteceu;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.SemNet;

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


    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);

        if (!isInternetAvailable()) {
            Intent intent = new Intent(getContext(), SemNet.class);
            startActivity(intent);
        } else {


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
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    Intent myIntent = new Intent(getContext(), AlgoErradoAconteceu.class);
                    startActivity(myIntent);
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


            final SwipeRefreshLayout sw_refresh = (SwipeRefreshLayout) view.findViewById(R.id.sw_refresh);

            sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                //following line is important to stop animation for refreshing
                    webview.setVisibility(View.INVISIBLE);
                    webview.reload();
                    sw_refresh.setRefreshing(false);
                }
            });

            webview.loadUrl("https://www.fct.unl.pt/noticias");
        }
        return view;

    }


}
