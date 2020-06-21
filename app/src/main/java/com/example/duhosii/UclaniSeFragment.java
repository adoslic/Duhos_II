package com.example.duhosii;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class UclaniSeFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    WebView webView;
    private String url="https://docs.google.com/forms/d/e/1FAIpQLSfe8xETG3lyWJkftnyf4N7gcPJ0-UNoB2TjALKaGGv6NY0GAg/viewform?fbclid=IwAR2ppsjJ9aMN3MmjjQCqDIm69SDqKUJuTBL0VFgrgUV3IKBb3sWfTeCEceo";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View view=mActionBar.getCustomView();
        zaglavlje=view.findViewById(R.id.naslov);
        zaglavlje.setText("DUHOS timovi");

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);

        View uclaniSeView=inflater.inflate(R.layout.fragment_uclani_se, container, false);
        webView=uclaniSeView.findViewById(R.id.uclaniSeWebView);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.loadUrl(url);

        return uclaniSeView;
    }

}
