package com.example.duhosii;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class MolitvaOpsirno extends Fragment {

    private TextView zaglavlje;
    private BottomNavigationView bottomNavigationView;
    private Molitva molitva;
    private View molitvaView;
    private TextView naslov,tekst;
    private ImageView slika;
    private String izvor;
    private ImageButton shareButton;
    private ScrollView scrollView;


    public MolitvaOpsirno(Molitva molitva, String izvor) {
        this.izvor=izvor;
        this.molitva = molitva;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.show();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View viewActionBar = mActionBar.getCustomView();
        zaglavlje = viewActionBar.findViewById(R.id.naslov);
        zaglavlje.setText(getContext().getResources().getString(R.string.odSrcaKsrcuNaslov));

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);

        molitvaView = inflater.inflate(R.layout.fragment_molitva_opsirno, container, false);
        naslov=molitvaView.findViewById(R.id.naslovOpsirno);
        tekst=molitvaView.findViewById(R.id.tekstOpsirno);
        slika=molitvaView.findViewById(R.id.slikaOpsirno);
        shareButton=molitvaView.findViewById(R.id.shareButton);
        scrollView=molitvaView.findViewById(R.id.molitva_opsirno_scollView);

        naslov.setText(molitva.getNaslov());
        tekst.setText(molitva.getTekst());


        if(izvor.equals(getContext().getResources().getString(R.string.molitvaIpozonostiString))) {
            slika.setImageDrawable(getResources().getDrawable(R.drawable.standardne_molitve));
        }
        if(izvor.equals(getContext().getResources().getString(R.string.nadahnucaString))) {
            slika.setImageDrawable(getResources().getDrawable(R.drawable.molitve));
        }
        if(izvor.equals(getContext().getResources().getString(R.string.marijaString))) {
            slika.setImageDrawable(getResources().getDrawable(R.drawable.marijanske_molitve));
        }
        if(izvor.equals(getContext().getResources().getString(R.string.svjedocanstvaString))) {
            slika.setImageDrawable(getResources().getDrawable(R.drawable.devetnice));
        }

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        return molitvaView;
    }

    public void share() {
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, naslov.getText().toString()+"\n\n"+tekst.getText().toString());
        share.putExtra(Intent.EXTRA_SUBJECT, naslov.getText().toString());
        share.setType("text/plain");
        getContext().startActivity(share.createChooser(share, "Share using"));
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).SetNavItemChecked(2);
    }
}