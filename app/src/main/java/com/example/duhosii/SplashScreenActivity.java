package com.example.duhosii;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;


public class SplashScreenActivity extends AppCompatActivity{

    GifImageView gifImageView;
    private DatabaseReference molitvaReference,devetniceReference,marijanskeReference,standardneReference;
    private RecyclerView recyclerView;
    ArrayList<Molitva> itemListMolitva = new ArrayList<>();
    ArrayList<Molitva> itemListMarijanske = new ArrayList<>();
    ArrayList<Molitva> itemListStandardne = new ArrayList<>();
    ArrayList<Molitva> itemListDevetnice = new ArrayList<>();

    private static final String TAG ="TAG";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide app name bar
        //wait for 5 seconds
        molitvaReference = FirebaseDatabase.getInstance().getReference("Molitve/Molitva");
        devetniceReference = FirebaseDatabase.getInstance().getReference("Molitve/Devetnice");
        marijanskeReference = FirebaseDatabase.getInstance().getReference("Molitve/Marijanske_molitve");
        standardneReference = FirebaseDatabase.getInstance().getReference("Molitve/Standardne_molitve");

        onInit();





    }

    public void onInit() {
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {

            }
        },7600);
        molitvaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemListMolitva.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.exists()) {
                        final String naziv = snapshot.child("Naziv").getValue().toString();
                        final String datum = snapshot.child("Datum").getValue().toString();
                        final String tekst = snapshot.child("Tekst").getValue().toString();
                        itemListMolitva.add(new Molitva(naziv,datum,tekst));
                    }
                }
                Collections.reverse(itemListMolitva);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Greška u čitanju iz baze podataka", databaseError.toException());
            }
        });


        marijanskeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemListMarijanske.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.exists()) {
                        final String naziv = snapshot.child("Naziv").getValue().toString();
                        final String datum = snapshot.child("Datum").getValue().toString();
                        final String tekst = snapshot.child("Tekst").getValue().toString();
                        itemListMarijanske.add(new Molitva(naziv,datum,tekst));
                    }
                }
                Collections.reverse(itemListMarijanske);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Greška u čitanju iz baze podataka", databaseError.toException());
            }
        });

        standardneReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemListStandardne.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.exists()) {
                        final String naziv = snapshot.child("Naziv").getValue().toString();
                        final String datum = snapshot.child("Datum").getValue().toString();
                        final String tekst = snapshot.child("Tekst").getValue().toString();
                        itemListStandardne.add(new Molitva(naziv,datum,tekst));
                    }
                }
                Collections.reverse(itemListStandardne);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Greška u čitanju iz baze podataka", databaseError.toException());
            }
        });


        devetniceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemListDevetnice.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.exists()) {
                        final String naziv = snapshot.child("Naziv").getValue().toString();
                        final String datum = snapshot.child("Datum").getValue().toString();
                        final String tekst = snapshot.child("Tekst").getValue().toString();
                        itemListDevetnice.add(new Molitva(naziv,datum,tekst));
                    }
                }
                Collections.reverse(itemListDevetnice);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Greška u čitanju iz baze podataka", databaseError.toException());
            }
        });
        Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
        intent.putExtra("listaMolitva", (Serializable) itemListMolitva);
        intent.putExtra("listaMarijanske", (Serializable) itemListMarijanske);
        intent.putExtra("listaStandardne", (Serializable) itemListStandardne);
        intent.putExtra("listaDevetnice", (Serializable) itemListDevetnice);
        SplashScreenActivity.this.startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        SplashScreenActivity.this.finish();
    }

}