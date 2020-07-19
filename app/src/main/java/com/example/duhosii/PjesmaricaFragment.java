package com.example.duhosii;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PjesmaricaFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    private boolean connectionFlag=false;
    private View connectionFragmentView;
    private ImageButton osvjeziButton;

    private RecyclerView recyclerView;
    private PjesmaricaItemAdapter adapter;
    private static final String TAG ="TAG";
    private View pjesmaricaFragmentView;
    List<Pjesma> itemList = new ArrayList<>();
    private DatabaseReference pjesmaricaReference;
    private FloatingActionButton searchButtonPjesma;
    private  EditText searchEditText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar_without_back);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View view=mActionBar.getCustomView();
        zaglavlje=view.findViewById(R.id.naslov);
        zaglavlje.setText("Pjesmarica");
        checkInternetConnection();

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);

        pjesmaricaFragmentView=inflater.inflate(R.layout.fragment_pjesmarica, container, false);

        searchEditText =pjesmaricaFragmentView.findViewById(R.id.searchEditText);

        KeyboardVisibilityEvent.setEventListener(getActivity(), new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if(isOpen){
                    searchEditText.setVisibility(View.VISIBLE);
                    searchEditText.getText().clear();
                    searchEditText.requestFocus();
                }
                else {
                    searchEditText.setVisibility(View.GONE);
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        if(connectionFlag==true) {
            pjesmaricaReference = FirebaseDatabase.getInstance().getReference("Pjesmarica");
            onInit();
            searchButtonPjesma=pjesmaricaFragmentView.findViewById(R.id.searchButtonPjesma);
            searchButtonPjesma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(searchEditText.getVisibility()==View.GONE){
                        searchEditText.setVisibility(View.VISIBLE);
                        searchEditText.getText().clear();
                        searchEditText.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                    else{
                        searchEditText.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    }

                }
            });
            return pjesmaricaFragmentView;
        } else {
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


    }


    public void onInit() {

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_15));
        recyclerView = pjesmaricaFragmentView.findViewById(R.id.recyclerViewPjesmarica);
        adapter = new PjesmaricaItemAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(itemDecorator);
        recyclerView.setAdapter(adapter);

        pjesmaricaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.exists()) {
                        final String naslov = snapshot.child("Naslov").getValue().toString();
                        final String bend = snapshot.child("Izvođač").getValue().toString();
                        final String tekstPjesme = snapshot.child("Tekst").getValue().toString();
                        final String link = snapshot.child("Link").getValue().toString();
                        itemList.add(new Pjesma(naslov,bend,tekstPjesme,link));
                    }
                }
                Collections.reverse(itemList);

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipePjesmaToShareCallback(adapter));
                itemTouchHelper.attachToRecyclerView(recyclerView);
                adapter.showShimmer = false;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Greška u čitanju iz baze podataka", databaseError.toException());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).SetNavItemChecked(0);
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
}
