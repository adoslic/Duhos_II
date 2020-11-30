package com.duhos.duhosii.questions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duhos.duhosii.MainActivity;
import com.duhos.duhosii.R;
import com.duhos.duhosii.models.Question;
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


public class QuestionsFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    private boolean connectionFlag = false;
    private View connectionFragmentView, pitanjaFragmentView;
    private ImageButton osvjeziButton;
    private DatabaseReference pitanjaReference;
    private RecyclerView recyclerView;
    List<Question> itemList = new ArrayList<>();
    private FloatingActionButton pitajKapelanaButton;
    private QuestionsItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.show();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar_without_back);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View view = mActionBar.getCustomView();
        zaglavlje = view.findViewById(R.id.naslov);
        zaglavlje.setText(getContext().getResources().getString(R.string.pitanjaNaslov));
        checkInternetConnection();

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);
        bottomNavigationView.setBackground(getContext().getResources().getDrawable(R.color.white));

        if (connectionFlag) {
            pitanjaFragmentView = inflater.inflate(R.layout.fragment_pitanja, container, false);
            pitanjaReference = FirebaseDatabase.getInstance().getReference("Pitanja");
            try {
                onInit();
            } catch (Exception e) {
                connectionFragmentView = inflater.inflate(R.layout.no_internet_connection_fragment, container, false);
                osvjeziButton = connectionFragmentView.findViewById(R.id.osvjeziButton);
                osvjeziButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomNavigationView.findViewById(R.id.navigacija_pjesmarica).performClick();
                    }
                });
                return connectionFragmentView;
            }
            pitajKapelanaButton = pitanjaFragmentView.findViewById(R.id.pitajKapelanaButton);
            pitajKapelanaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new AskChaplainFragment()).addToBackStack("").commit();

                }
            });
            return pitanjaFragmentView;
        } else {
            connectionFragmentView = inflater.inflate(R.layout.no_internet_connection_fragment, container, false);
            osvjeziButton = connectionFragmentView.findViewById(R.id.osvjeziButton);
            osvjeziButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomNavigationView.findViewById(R.id.navigacija_pitanja).performClick();
                }
            });
            return connectionFragmentView;
        }
    }

    public void onInit() {
        recyclerView = pitanjaFragmentView.findViewById(R.id.pitanjarecyclerView);
        adapter = new QuestionsItemAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        /*recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position,0);

                    }
                })
        );
         */

        pitanjaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.exists()) {
                        final String pitanje = snapshot.child("Pitanje").getValue().toString();
                        final String odgovor = snapshot.child("Odgovor").getValue().toString();
                        itemList.add(new Question(pitanje, odgovor));
                    }
                }
                Collections.reverse(itemList);
                adapter.showShimmer = false;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", getContext().getResources().getString(R.string.greskaUbaziString), databaseError.toException());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).SetNavItemChecked(4);
    }

    private void checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                connectionFlag = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                connectionFlag = true;
            }
        } else {
            connectionFlag = false;
        }
    }

    public boolean checkHolder() {
        return adapter.checkHolder();
    }
}
