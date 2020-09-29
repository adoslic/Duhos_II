package com.example.duhosii;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MolitveneGrupeFragment extends Fragment {

    TextView zaglavlje,marijanskeDatum,opceDatum,poboznostiDatum,nadahnucaDatum;
    BottomNavigationView bottomNavigationView;
    private View molitvaFragmentView;
    private View connectionFragmentView;
    private ImageButton osvjeziButton;
    private static final String TAG ="TAG";
    private LinearLayout molitva,marijanske,devetnice,standard;
    private RelativeLayout molitvaTextLayout,marijanskeTextLayout,devetniceTextLayout,standardTextLayout;
    private RelativeLayout molitvaImageLayout,marijanskeImageLayout,devetniceImageLayout,standardImageLayout;
    private boolean connectionFlag=false;
    private FloatingActionButton posaljiMolitvenuNakanu;
    private DatabaseReference marijanskeReference,opceReference,poboznostiReference,nadahnucaReference;
    List<Molitva> itemListMarijanske = new ArrayList<>();
    List<Molitva> itemListOpce = new ArrayList<>();
    List<Molitva> itemListNadahnuca = new ArrayList<>();
    List<Molitva> itemListPoboznosti = new ArrayList<>();
    String marijanskeZadnjiDatum;
    String opceZadnjiDatum;
    String nadahnucaZadnjiDatum;
    String poboznostiZadnjiDatum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.show();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar_without_back);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View viewActionBar=mActionBar.getCustomView();



        zaglavlje=viewActionBar.findViewById(R.id.naslov);
        zaglavlje.setText(getContext().getResources().getString(R.string.odSrcaKsrcuNaslov));

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);
        checkInternetConnection();

        if(connectionFlag==true) {
            molitvaFragmentView = inflater.inflate(R.layout.fragment_grupe_molitvi, container, false);

            posaljiMolitvenuNakanu=molitvaFragmentView.findViewById(R.id.posaljiMolitvenuNakanu);

            posaljiMolitvenuNakanu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new NakanaFragment()).addToBackStack("").commit();

                }
            });
            marijanskeDatum=molitvaFragmentView.findViewById(R.id.datumMarije);
            opceDatum=molitvaFragmentView.findViewById(R.id.datumMolitve);
            poboznostiDatum=molitvaFragmentView.findViewById(R.id.datumStandard);
            nadahnucaDatum=molitvaFragmentView.findViewById(R.id.datumDevetnice);

            try {
                getFirebaseData();
            }
            catch(Exception e){
                connectionFragmentView = inflater.inflate(R.layout.no_internet_connection_fragment, container, false);
                osvjeziButton=connectionFragmentView.findViewById(R.id.osvjeziButton);
                osvjeziButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomNavigationView.findViewById(R.id.navigacija_pjesmarica).performClick();
                    }
                });
                return connectionFragmentView;
            }

            molitva = molitvaFragmentView.findViewById(R.id.grupaMolitve);
            marijanske = molitvaFragmentView.findViewById(R.id.grupaMarijanskeMolitve);
            devetnice = molitvaFragmentView.findViewById(R.id.grupaDevetnice);
            standard = molitvaFragmentView.findViewById(R.id.grupaStandard);

            molitvaTextLayout = molitvaFragmentView.findViewById(R.id.molitva_relativeLayout);
            marijanskeTextLayout = molitvaFragmentView.findViewById(R.id.marijanske_relativeLayout);
            devetniceTextLayout = molitvaFragmentView.findViewById(R.id.devetnice_relativeLayout);
            standardTextLayout = molitvaFragmentView.findViewById(R.id.standardne_relativeLayout);

            molitvaImageLayout = molitvaFragmentView.findViewById(R.id.slikaBiblijeLayout);
            marijanskeImageLayout = molitvaFragmentView.findViewById(R.id.slikaMarijeLayout);
            devetniceImageLayout = molitvaFragmentView.findViewById(R.id.slikaKruniceLayout);
            standardImageLayout = molitvaFragmentView.findViewById(R.id.slikaStandardLayout);

            molitvaImageLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_transition_animation));
            molitvaTextLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_transition_animation));
            marijanskeImageLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_transition_animation));
            marijanskeTextLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_transition_animation));
            devetniceImageLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_transition_animation));
            devetniceTextLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_transition_animation));
            standardImageLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_transition_animation));
            standardTextLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_transition_animation));


            molitva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new OpceMolitveFragment(itemListOpce)).addToBackStack("").commit();
                }
            });
            marijanske.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new MarijanskeMolitveFragment(itemListMarijanske)).addToBackStack("").commit();
                }
            });
            devetnice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new NadahnucaFragment(itemListNadahnuca)).addToBackStack("").commit();
                }
            });
            standard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new PoboznostiFragment(itemListPoboznosti)).addToBackStack("").commit();
                }
            });
            return molitvaFragmentView;
        }
        else {
            connectionFragmentView = inflater.inflate(R.layout.no_internet_connection_fragment, container, false);
            osvjeziButton=connectionFragmentView.findViewById(R.id.osvjeziButton);
            osvjeziButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomNavigationView.findViewById(R.id.navigacija_molitva).performClick();
                }
            });
            return connectionFragmentView;
        }

    }

    private void getFirebaseData() {
        opceReference = FirebaseDatabase.getInstance().getReference("Molitve").child("Molitve i pobožnosti");
        opceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemListOpce.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.exists()) {
                        final String nazivOpce = snapshot.child("Naziv").getValue().toString();
                        final String datumOpce = snapshot.child("Datum").getValue().toString();
                        final String tekstOpce = snapshot.child("Tekst").getValue().toString();
                        itemListOpce.add(new Molitva(nazivOpce,datumOpce,tekstOpce));
                    }
                }
                Collections.reverse(itemListOpce);
                if(!itemListOpce.isEmpty()){
                    opceZadnjiDatum=itemListOpce.get(0).datum;
                    opceDatum.setText(opceZadnjiDatum);
                    //opceDatum.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_transition_animation));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, getContext().getResources().getString(R.string.greskaUbaziString), databaseError.toException());
            }
        });

        marijanskeReference = FirebaseDatabase.getInstance().getReference("Molitve").child("Marijanske molitve");
        marijanskeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemListMarijanske.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.exists()) {
                        final String nazivMarijanske = snapshot.child("Naziv").getValue().toString();
                        final String datumMarijanske = snapshot.child("Datum").getValue().toString();
                        final String tekstMarijanske = snapshot.child("Tekst").getValue().toString();
                        itemListMarijanske.add(new Molitva(nazivMarijanske,datumMarijanske,tekstMarijanske));
                    }
                }
                Collections.reverse(itemListMarijanske);
                if(!itemListMarijanske.isEmpty()){
                    marijanskeZadnjiDatum=itemListMarijanske.get(0).datum;
                    marijanskeDatum.setText(marijanskeZadnjiDatum);
                    //marijanskeDatum.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_transition_animation));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, getContext().getResources().getString(R.string.greskaUbaziString), databaseError.toException());
            }
        });

        nadahnucaReference = FirebaseDatabase.getInstance().getReference("Molitve").child("Nadahnuća");
        nadahnucaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemListNadahnuca.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.exists()) {
                        final String nazivNadahnuca = snapshot.child("Naziv").getValue().toString();
                        final String datumNadahnuca = snapshot.child("Datum").getValue().toString();
                        final String tekstNadahnuca = snapshot.child("Tekst").getValue().toString();
                        itemListNadahnuca.add(new Molitva(nazivNadahnuca,datumNadahnuca,tekstNadahnuca));
                    }
                }
                Collections.reverse(itemListNadahnuca);
                if(!itemListNadahnuca.isEmpty()){
                    nadahnucaZadnjiDatum=itemListNadahnuca.get(0).datum;
                    nadahnucaDatum.setText(nadahnucaZadnjiDatum);
                    //nadahnucaDatum.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_transition_animation));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, getContext().getResources().getString(R.string.greskaUbaziString), databaseError.toException());
            }
        });


        poboznostiReference = FirebaseDatabase.getInstance().getReference("Molitve").child("Svjedočanstva");
        poboznostiReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemListPoboznosti.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.exists()) {
                        final String nazivPoboznosti = snapshot.child("Naziv").getValue().toString();
                        final String datumPoboznosti = snapshot.child("Datum").getValue().toString();
                        final String tekstPoboznosti = snapshot.child("Tekst").getValue().toString();
                        itemListPoboznosti.add(new Molitva(nazivPoboznosti,datumPoboznosti,tekstPoboznosti));
                    }
                }
                Collections.reverse(itemListPoboznosti);
                if(!itemListPoboznosti.isEmpty()){
                    poboznostiZadnjiDatum=itemListPoboznosti.get(0).datum;
                    poboznostiDatum.setText(poboznostiZadnjiDatum);
                    //poboznostiDatum.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_transition_animation));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, getContext().getResources().getString(R.string.greskaUbaziString), databaseError.toException());
            }
        });

    }

    private void checkInternetConnection() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=connectivityManager.getActiveNetworkInfo();
        if(null!=activeNetwork){
            if(activeNetwork.getType()==ConnectivityManager.TYPE_WIFI){
                connectionFlag=true;
            }
            else if(activeNetwork.getType()==ConnectivityManager.TYPE_MOBILE){
                connectionFlag=true;
            }

        }
        else
        {
            connectionFlag=false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).SetNavItemChecked(2);
    }


}
