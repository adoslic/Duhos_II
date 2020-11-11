package com.duhos.duhosii.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.duhos.duhosii.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class ChaplainsFragment extends Fragment {

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
        zaglavlje.setText(getContext().getResources().getString(R.string.kapelaniNaslov));

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        bottomNavigationView.setBackground(getContext().getResources().getDrawable(R.color.white));


        View kapelaniFragmentView=inflater.inflate(R.layout.fragment_kapelani, container, false);
        kapelan1Tekst=kapelaniFragmentView.findViewById(R.id.kapelan1Tekst);
        kapelan2Tekst=kapelaniFragmentView.findViewById(R.id.kapelan2Tekst);

        return kapelaniFragmentView;
    }

}
