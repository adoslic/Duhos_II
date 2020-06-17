package com.example.duhosii;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;


public class MolitveneGrupeFregment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    private View molitvaFragmentView;
    private static final String TAG ="TAG";
    private RelativeLayout molitva,marijanske,devetnice,standard;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View viewActionBar=mActionBar.getCustomView();
        zaglavlje=viewActionBar.findViewById(R.id.naslov);
        zaglavlje.setText("Molitva");

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);

        molitvaFragmentView = inflater.inflate(R.layout.fragment_grupe_molitvi,container,false);
        molitva=molitvaFragmentView.findViewById(R.id.grupaMolitve);
        marijanske=molitvaFragmentView.findViewById(R.id.grupaMarijanskeMolitve);
        devetnice=molitvaFragmentView.findViewById(R.id.grupaDevetnice);
        standard=molitvaFragmentView.findViewById(R.id.grupaStandard);


        molitva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,new MolitvaFragment()).commit();
            }
        });
        return molitvaFragmentView;
    }

    private void switchFragment() {
    }

}
