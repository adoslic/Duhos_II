package com.example.duhosii;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import org.w3c.dom.Text;

import java.io.Externalizable;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class KapelaniFragment extends Fragment {

    TextView zaglavlje,kapelan1Tekst,kapelan2Tekst;
    BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.show();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View view = mActionBar.getCustomView();
        zaglavlje = view.findViewById(R.id.naslov);
        zaglavlje.setText("Kapelani");

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);


        View kapelaniFragmentView=inflater.inflate(R.layout.fragment_kapelani, container, false);
        kapelan1Tekst=kapelaniFragmentView.findViewById(R.id.kapelan1Tekst);
        kapelan2Tekst=kapelaniFragmentView.findViewById(R.id.kapelan2Tekst);

        return kapelaniFragmentView;
    }

}
