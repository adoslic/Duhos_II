package com.example.duhosii;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import androidx.recyclerview.widget.RecyclerView;

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
        shareButton=molitvaView.findViewById(R.id.shareButton);
        scrollView=molitvaView.findViewById(R.id.molitva_opsirno_scollView);

        naslov.setText(molitva.getNaslov());
        tekst.setText(molitva.getTekst());

        if(izvor.equals("Molitva")) {
            slika.setImageDrawable(getResources().getDrawable(R.drawable.molitve));
        }
        if(izvor.equals("Standard")) {
            slika.setImageDrawable(getResources().getDrawable(R.drawable.standardne_molitve));
        }
        if(izvor.equals("Marijanske")) {
            slika.setImageDrawable(getResources().getDrawable(R.drawable.marijanske_molitve));
        }
        if(izvor.equals("Devetnice")) {
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
        share.putExtra(Intent.EXTRA_TEXT, tekst.getText().toString());
        share.putExtra(Intent.EXTRA_SUBJECT, naslov.getText().toString());
        share.setType("text/plain");
        getContext().startActivity(share.createChooser(share, "Share using"));
    }


}