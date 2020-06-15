package com.example.duhosii;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MolitvaOpsirno extends Fragment {

    private TextView zaglavlje;
    private BottomNavigationView bottomNavigationView;
    private Molitva molitva;
    private View molitvaView;
    private TextView naslov,tekst;
    private ImageView slika;


    public MolitvaOpsirno(Molitva molitva) {
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
        String url=molitva.getSlika();
        Picasso.with(getContext()).load(url).into(slika);
        naslov.setText(molitva.getNaslov());
        tekst.setText(molitva.getTekst());
        return molitvaView;
    }


}