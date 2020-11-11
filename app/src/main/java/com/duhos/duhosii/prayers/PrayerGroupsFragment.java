package com.duhos.duhosii.prayers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.duhos.duhosii.MainActivity;
import com.duhos.duhosii.R;
import com.duhos.duhosii.models.Prayer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class PrayerGroupsFragment extends Fragment {

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
    private FloatingActionButton posaljiMolitvenuNakanu,fabMenu,casoslovButton,svjedocanstvoButton;
    private TextView svjedocanstvoFabText,nakanaFabText,casoslovFabText;
    private boolean fabIsOpen;
    private DatabaseReference marijanskeReference,opceReference,poboznostiReference,nadahnucaReference;
    List<Prayer> itemListMarijanske = new ArrayList<>();
    List<Prayer> itemListOpce = new ArrayList<>();
    List<Prayer> itemListNadahnuca = new ArrayList<>();
    List<Prayer> itemListPoboznosti = new ArrayList<>();
    String marijanskeZadnjiDatum;
    String opceZadnjiDatum;
    String nadahnucaZadnjiDatum;
    String poboznostiZadnjiDatum;
    View viewActionBar;
    private View mShadowView;
    ActionBar mActionBar;
    TextView naslov;
    RelativeLayout toolBar;
    ImageButton menu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.show();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar_without_back);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        viewActionBar=mActionBar.getCustomView();

        toolBar=viewActionBar.findViewById(R.id.toolBar);
        naslov=viewActionBar.findViewById(R.id.naslov);
        menu=viewActionBar.findViewById(R.id.menu);

        zaglavlje=viewActionBar.findViewById(R.id.naslov);
        zaglavlje.setText(getContext().getResources().getString(R.string.odSrcaKsrcuNaslov));

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);
        bottomNavigationView.setBackground(getContext().getResources().getDrawable(R.color.white));

        checkInternetConnection();

        if(connectionFlag) {
            molitvaFragmentView = inflater.inflate(R.layout.fragment_grupe_molitvi, container, false);

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

            svjedocanstvoButton=molitvaFragmentView.findViewById(R.id.svjedocanstvoButton);
            posaljiMolitvenuNakanu=molitvaFragmentView.findViewById(R.id.posaljiMolitvenuNakanu);
            casoslovButton=molitvaFragmentView.findViewById(R.id.casoslovButton);
            fabMenu=molitvaFragmentView.findViewById(R.id.fabMenu);

            svjedocanstvoFabText=molitvaFragmentView.findViewById(R.id.svjedocanstvoFabText);
            casoslovFabText=molitvaFragmentView.findViewById(R.id.casoslovFabText);
            nakanaFabText=molitvaFragmentView.findViewById(R.id.nakanaFabText);

            mShadowView=molitvaFragmentView.findViewById(R.id.shadowView);

            svjedocanstvoButton.hide();
            posaljiMolitvenuNakanu.hide();
            casoslovButton.hide();

            svjedocanstvoFabText.setVisibility(View.INVISIBLE);
            casoslovFabText.setVisibility(View.INVISIBLE);
            nakanaFabText.setVisibility(View.INVISIBLE);
            fabIsOpen=false;

            fabMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fabIsOpen){
                        fabIsOpen=false;
                        svjedocanstvoButton.hide();
                        posaljiMolitvenuNakanu.hide();
                        casoslovButton.hide();

                        final OvershootInterpolator interpolator = new OvershootInterpolator();
                        ViewCompat.animate(fabMenu).
                                rotation(0f).
                                withLayer().
                                setDuration(300).
                                setInterpolator(interpolator).
                                start();

                        svjedocanstvoFabText.setVisibility(View.INVISIBLE);
                        casoslovFabText.setVisibility(View.INVISIBLE);
                        nakanaFabText.setVisibility(View.INVISIBLE);

                        mShadowView.setVisibility(View.GONE);
                        //mActionBar.show();

                        mActionBar.setBackgroundDrawable(getContext().getResources().getDrawable(R.color.grey));
                        naslov.setBackgroundDrawable(getContext().getResources().getDrawable(R.color.grey));
                        toolBar.setBackground(getContext().getResources().getDrawable(R.color.grey));
                        menu.setBackground(getContext().getResources().getDrawable(R.color.grey));

                        bottomNavigationView.setBackground(getContext().getResources().getDrawable(R.color.white));
                    }
                    else{
                        svjedocanstvoButton.show();
                        posaljiMolitvenuNakanu.show();
                        casoslovButton.show();
                        final OvershootInterpolator interpolator = new OvershootInterpolator();
                        ViewCompat.animate(fabMenu).
                                rotation(135f).
                                withLayer().
                                setDuration(300).
                                setInterpolator(interpolator).
                                start();
                        svjedocanstvoFabText.setVisibility(View.VISIBLE);
                        casoslovFabText.setVisibility(View.VISIBLE);
                        nakanaFabText.setVisibility(View.VISIBLE);

                        //mActionBar.hide();
                        mActionBar.setBackgroundDrawable(getContext().getResources().getDrawable(R.color.actionBarFABShadow));
                        naslov.setBackgroundDrawable(getContext().getResources().getDrawable(R.color.actionBarFABShadow));
                        toolBar.setBackground(getContext().getResources().getDrawable(R.color.actionBarFABShadow));
                        menu.setBackground(getContext().getResources().getDrawable(R.color.actionBarFABShadow));

                        mShadowView.setVisibility(View.VISIBLE);

                        mShadowView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fabMenu.performClick();
                            }
                        });
                        bottomNavigationView.setBackground(getContext().getResources().getDrawable(R.color.shadowFABgrey));


                        fabIsOpen=true;

                        casoslovButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,new BreviaryFragment()).addToBackStack("").commit();
                            }
                        });

                        svjedocanstvoButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,new SendTestimonyFragment()).addToBackStack("").commit();
                            }
                        });

                        posaljiMolitvenuNakanu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,new IntentionFragment()).addToBackStack("").commit();
                            }
                        });
                    }
                }
            });

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
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new GeneralPrayersFragment(itemListOpce)).addToBackStack("").commit();
                }
            });
            marijanske.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new MarianPrayersFragment(itemListMarijanske)).addToBackStack("").commit();
                }
            });
            devetnice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new InspirationFragment(itemListNadahnuca)).addToBackStack("").commit();
                }
            });
            standard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new PietyFragment(itemListPoboznosti)).addToBackStack("").commit();
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
                        itemListOpce.add(new Prayer(nazivOpce,datumOpce,tekstOpce));
                    }
                }
                Collections.reverse(itemListOpce);
                for(int i=0;i<itemListOpce.size()-1;i++){
                    for(int j=0;j<itemListOpce.size()-i-1;j++) {
                        String date1String=itemListOpce.get(j).getDatum()+" "+"00:00:00";
                        Date date1 = null;
                        try {
                            date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date1String);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date date2 = null;
                        String date2String=itemListOpce.get(j+1).getDatum()+" "+"00:00:00";

                        try {
                            date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date2String);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(date1.before(date2)){
                            Collections.swap(itemListOpce,j+1,j);
                        }
                    }
                }
                if(!itemListOpce.isEmpty()){
                    opceZadnjiDatum=itemListOpce.get(0).getDatum();
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
                        itemListMarijanske.add(new Prayer(nazivMarijanske,datumMarijanske,tekstMarijanske));
                    }
                }
                Collections.reverse(itemListMarijanske);
                for(int i=0;i<itemListMarijanske.size()-1;i++){
                    for(int j=0;j<itemListMarijanske.size()-i-1;j++) {
                        String date1String=itemListMarijanske.get(j).getDatum()+" "+"00:00:00";
                        Date date1 = null;
                        try {
                            date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date1String);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date date2 = null;
                        String date2String=itemListMarijanske.get(j+1).getDatum()+" "+"00:00:00";

                        try {
                            date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date2String);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(date1.before(date2)){
                            Collections.swap(itemListMarijanske,j+1,j);
                        }
                    }
                }
                if(!itemListMarijanske.isEmpty()){
                    marijanskeZadnjiDatum=itemListMarijanske.get(0).getDatum();
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
                        itemListNadahnuca.add(new Prayer(nazivNadahnuca,datumNadahnuca,tekstNadahnuca));
                    }
                }
                Collections.reverse(itemListNadahnuca);
                for(int i=0;i<itemListNadahnuca.size()-1;i++){
                    for(int j=0;j<itemListNadahnuca.size()-i-1;j++) {
                        String date1String=itemListNadahnuca.get(j).getDatum()+" "+"00:00:00";
                        Date date1 = null;
                        try {
                            date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date1String);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date date2 = null;
                        String date2String=itemListNadahnuca.get(j+1).getDatum()+" "+"00:00:00";

                        try {
                            date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date2String);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(date1.before(date2)){
                            Collections.swap(itemListNadahnuca,j+1,j);
                        }
                    }
                }
                if(!itemListNadahnuca.isEmpty()){
                    nadahnucaZadnjiDatum=itemListNadahnuca.get(0).getDatum();
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
                        itemListPoboznosti.add(new Prayer(nazivPoboznosti,datumPoboznosti,tekstPoboznosti));
                    }
                }
                Collections.reverse(itemListPoboznosti);
                for(int i=0;i<itemListPoboznosti.size()-1;i++){
                    for(int j=0;j<itemListPoboznosti.size()-i-1;j++) {
                        String date1String=itemListPoboznosti.get(j).getDatum()+" "+"00:00:00";
                        Date date1 = null;
                        try {
                            date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date1String);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date date2 = null;
                        String date2String=itemListPoboznosti.get(j+1).getDatum()+" "+"00:00:00";

                        try {
                            date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date2String);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(date1.before(date2)){
                            Collections.swap(itemListPoboznosti,j+1,j);
                        }
                    }
                }
                if(!itemListPoboznosti.isEmpty()){
                    poboznostiZadnjiDatum=itemListPoboznosti.get(0).getDatum();
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
