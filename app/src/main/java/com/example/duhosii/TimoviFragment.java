package com.example.duhosii;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class TimoviFragment extends Fragment {

    TextView zaglavlje,stoZelimoTeskt,cuposTekst,liturgijskiTekst,medijskiTekst,bandTeskt,volonteriTekst;
    BottomNavigationView bottomNavigationView;
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
        zaglavlje.setText("DUHOS timovi");

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);

        View timoviFragmentView=inflater.inflate(R.layout.fragment_timovi, container, false);
        stoZelimoTeskt=timoviFragmentView.findViewById(R.id.stoZelimoTeskt);
        cuposTekst=timoviFragmentView.findViewById(R.id.cuposTekst);
        liturgijskiTekst=timoviFragmentView.findViewById(R.id.liturgijskiTekst);
        medijskiTekst=timoviFragmentView.findViewById(R.id.medijskiTekst);
        bandTeskt=timoviFragmentView.findViewById(R.id.bandTeskt);
        volonteriTekst=timoviFragmentView.findViewById(R.id.volonteriTekst);


        return timoviFragmentView;
    }

}
