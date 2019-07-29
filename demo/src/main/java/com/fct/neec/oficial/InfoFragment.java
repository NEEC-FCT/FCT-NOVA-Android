package com.fct.neec.oficial;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {


    private WebView webview;

    /**
     * Create a new instance of the fragment
     */
    public static InfoFragment newInstance(int index) {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info, container, false);
        webview = view.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://fctapp.neec-fct.com/Informacao/");
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.setVisibility(View.GONE);

                if (url.startsWith("https://www.fct.unl.pt/estudante/informacao-academica/prazos") ) {
                    ((DemoActivity)getActivity()).changeFragment(3);
                    return false;
                }
                return false;
            }
        });
        return view;
    }


}