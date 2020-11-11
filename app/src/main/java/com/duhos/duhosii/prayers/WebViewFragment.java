package com.duhos.duhosii.prayers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.duhos.duhosii.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class WebViewFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    WebView webView;
    private String url;
    private String zaglavljeText;

    public WebViewFragment(String url,String zaglavljeText) {
        this.zaglavljeText=zaglavljeText;
        this.url = url;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.show();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View view=mActionBar.getCustomView();
        zaglavlje=view.findViewById(R.id.naslov);
        zaglavlje.setText(zaglavljeText);

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        bottomNavigationView.setBackground(getContext().getResources().getDrawable(R.color.white));

        View webViewView=inflater.inflate(R.layout.fragment_webview, container, false);
        webView=webViewView.findViewById(R.id.webView);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                // all links  with in ur site will be open inside the webview
                //links that start ur domain example(http://www.example.com/)
                if (url != null && url.startsWith("htt")){
                    return false;
                }
                // all links that points outside the site will be open in a normal android browser
                else
                {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }
            }
        });
        return webViewView;
    }

}
