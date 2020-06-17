package com.example.duhosii;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarijanskeMolitveFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    List<Molitva> itemList = new ArrayList<>();
    private View molitvaFragmentView;
    private DatabaseReference molitvaReference;
    private MolitvaItemAdapter adapter;
    private static final String TAG ="TAG";
    public MarijanskeMolitveFragment() {
    }

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

        molitvaFragmentView = inflater.inflate(R.layout.fragment_molitva,container,false);

        molitvaReference = FirebaseDatabase.getInstance().getReference("Molitve").child("Marijanske_molitve");

        onInit();

        return molitvaFragmentView;
    }

    public void onInit() {

        molitvaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.exists()) {
                        final String naziv = snapshot.child("Naziv").getValue().toString();
                        final String datum = snapshot.child("Datum").getValue().toString();
                        final String slika = snapshot.child("Slika").getValue().toString();
                        final String tekst = snapshot.child("Tekst").getValue().toString();
                        itemList.add(new Molitva(naziv,datum,slika,tekst));
                    }
                }
                Collections.reverse(itemList);

                recyclerView = molitvaFragmentView.findViewById(R.id.recyclerView);

                recyclerView.setHasFixedSize(true);
                adapter = new MolitvaItemAdapter(itemList,"Marijanske");
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToShareCallback(adapter));
                itemTouchHelper.attachToRecyclerView(recyclerView);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Greška u čitanju iz baze podataka", databaseError.toException());
            }
        });

    }

}
