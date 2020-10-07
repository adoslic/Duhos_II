package com.duhos.duhosii;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
public class CasoslovFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    private View casoslovView;
    private static final String TAG ="TAG";
    private WebView casoslovWebView;
    private String url="https://bozanskicasoslov.wordpress.com";

    public CasoslovFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.show();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View viewActionBar=mActionBar.getCustomView();
        zaglavlje=viewActionBar.findViewById(R.id.naslov);
        zaglavlje.setText(getContext().getResources().getString(R.string.casoslovNaslov));

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);

        casoslovView = inflater.inflate(R.layout.fragment_casoslov,container,false);
        casoslovWebView=casoslovView.findViewById(R.id.casoslovWebView);

        if(URLUtil.isValidUrl(url)) {
            casoslovWebView.getSettings().setJavaScriptEnabled(true);
            casoslovWebView.getSettings().setLoadWithOverviewMode(true);
            casoslovWebView.getSettings().setUseWideViewPort(true);
            casoslovWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
            casoslovWebView.loadUrl(url);
        }
        else
            Toast.makeText(getContext(),getContext().getResources().getString(R.string.neispravanLinkString),Toast.LENGTH_SHORT).show();

        return casoslovView;
    }
}
