package com.example.duhosii;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class PopisKnjigaFragment extends Fragment {
    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    WebView webView;
    private String url="https://docs.google.com/spreadsheets/d/1JtsumSGxe9XGNiHPXdmfLNkh10VzaFXP_dbt8RK28_0/edit?fbclid=IwAR35dSsUPGIbYHL6cHzewYEyRtwwEyWf6pND-BnzBJ9aGAxtflwUt1_Uz3Q#gid=840136841";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View view=mActionBar.getCustomView();
        zaglavlje=view.findViewById(R.id.naslov);
        zaglavlje.setText("Knjižnica");

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);

        View popisKnjiga=inflater.inflate(R.layout.fragment_popis_knjiga, container, false);
        webView=popisKnjiga.findViewById(R.id.popisKnjigaWebView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.loadUrl(url);

        return popisKnjiga;
    }


}
