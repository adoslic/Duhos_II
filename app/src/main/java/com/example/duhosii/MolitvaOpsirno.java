package com.example.duhosii;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class MolitvaOpsirno extends Fragment {

    private TextView zaglavlje;
    private BottomNavigationView bottomNavigationView;
    private Molitva molitva;
    private View molitvaView;
    private TextView naslov,tekst;
    private ImageView slika;
    private String izvor;


    public MolitvaOpsirno(Molitva molitva, String izvor) {
        this.izvor=izvor;
        this.molitva = molitva;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View viewActionBar = mActionBar.getCustomView();
        zaglavlje = viewActionBar.findViewById(R.id.naslov);
        zaglavlje.setText("Molitva");

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);

        molitvaView = inflater.inflate(R.layout.fragment_molitva_opsirno, container, false);
        naslov=molitvaView.findViewById(R.id.naslovOpsirno);
        tekst=molitvaView.findViewById(R.id.tekstOpsirno);
        slika=molitvaView.findViewById(R.id.slikaOpsirno);
        naslov.setText(molitva.getNaslov());
        tekst.setText(molitva.getTekst());

        if(izvor.equals("OpćeMolitve")) {
            slika.setImageDrawable(getResources().getDrawable(R.drawable.molitve));
        }
        if(izvor.equals("Nadahnuća")) {
            slika.setImageDrawable(getResources().getDrawable(R.drawable.standardne_molitve));
        }
        if(izvor.equals("Marija")) {
            slika.setImageDrawable(getResources().getDrawable(R.drawable.marijanske_molitve));
        }
        if(izvor.equals("Pobožnosti")) {
            slika.setImageDrawable(getResources().getDrawable(R.drawable.devetnice));
        }
        return molitvaView;
    }


}