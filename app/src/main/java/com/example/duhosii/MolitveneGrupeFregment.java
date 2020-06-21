package com.example.duhosii;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.ArrayList;


public class MolitveneGrupeFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    private View molitvaFragmentView;
    private static final String TAG ="TAG";
    private LinearLayout molitva,marijanske,devetnice,standard;
    private RelativeLayout molitvaTextLayout,marijanskeTextLayout,devetniceTextLayout,standardTextLayout;
    private RelativeLayout molitvaImageLayout,marijanskeImageLayout,devetniceImageLayout,standardImageLayout;

    ArrayList<ArrayList<Molitva>> itemList = new ArrayList<>();

    public MolitveneGrupeFragment(ArrayList<ArrayList<Molitva>> itemList) {
        this.itemList=itemList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar_without_back);
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

        molitvaTextLayout=molitvaFragmentView.findViewById(R.id.molitva_relativeLayout);
        marijanskeTextLayout=molitvaFragmentView.findViewById(R.id.marijanske_relativeLayout);
        devetniceTextLayout=molitvaFragmentView.findViewById(R.id.devetnice_relativeLayout);
        standardTextLayout=molitvaFragmentView.findViewById(R.id.standardne_relativeLayout);

        molitvaImageLayout=molitvaFragmentView.findViewById(R.id.slikaBiblijeLayout);
        marijanskeImageLayout=molitvaFragmentView.findViewById(R.id.slikaMarijeLayout);
        devetniceImageLayout=molitvaFragmentView.findViewById(R.id.slikaKruniceLayout);
        standardImageLayout=molitvaFragmentView.findViewById(R.id.slikaStandardLayout);

        molitvaImageLayout.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_transition_animation));
        molitvaTextLayout.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.scale_transition_animation));
        marijanskeImageLayout.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_transition_animation));
        marijanskeTextLayout.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.scale_transition_animation));
        devetniceImageLayout.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_transition_animation));
        devetniceTextLayout.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.scale_transition_animation));
        standardImageLayout.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_transition_animation));
        standardTextLayout.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.scale_transition_animation));



        molitva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,new MolitvaFragment(itemList.get(0))).addToBackStack("molitvaFragment").commit();
            }
        });
        marijanske.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,new MarijanskeMolitveFragment(itemList.get(1))).addToBackStack("marijanskeMolitveFragment").commit();
            }
        });
        devetnice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,new DevetniceFragment(itemList.get(3))).addToBackStack("devetniceFragment").commit();
            }
        });
        standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,new StandardneMolitveFragment(itemList.get(2))).addToBackStack("standardneMolitveFragment").commit();
            }
        });
        return molitvaFragmentView;
    }

}
