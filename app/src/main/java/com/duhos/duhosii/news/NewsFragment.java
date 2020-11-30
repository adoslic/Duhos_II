package com.duhos.duhosii.news;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duhos.duhosii.MainActivity;
import com.duhos.duhosii.R;
import com.duhos.duhosii.models.News;
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


public class NewsFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    private boolean connectionFlag=false;
    private View connectionFragmentView,multimedijaFragmentView;
    private ImageButton osvjeziButton;
    private DatabaseReference multimedijaReference;
    private RecyclerView recyclerView;
    List<News> itemList = new ArrayList<>();
    private NewsItemAdapter adapter;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;
    GridLayoutManager gridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.show();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar_without_back);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View view=mActionBar.getCustomView();
        zaglavlje=view.findViewById(R.id.naslov);
        zaglavlje.setText(getContext().getResources().getString(R.string.novostiNaslov));
        checkInternetConnection();

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);
        bottomNavigationView.setBackground(getContext().getResources().getDrawable(R.color.white));

        if(connectionFlag) {
            multimedijaFragmentView=inflater.inflate(R.layout.fragment_multimedija, container, false);
            multimedijaReference = FirebaseDatabase.getInstance().getReference("Novosti");
            gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
            try {
                onInit();
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
            return multimedijaFragmentView;
        }
        else {
            connectionFragmentView = inflater.inflate(R.layout.no_internet_connection_fragment, container, false);
            osvjeziButton=connectionFragmentView.findViewById(R.id.osvjeziButton);
            osvjeziButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomNavigationView.findViewById(R.id.navigacija_multimedija).performClick();
                }
            });
            return connectionFragmentView;
        }
    }
    public void onInit() {
        recyclerView = multimedijaFragmentView.findViewById(R.id.multimedijarecyclerView);
        adapter = new NewsItemAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        multimedijaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.exists()) {
                        final String naslov = snapshot.child("Naslov").getValue().toString();
                        final String sadrzaj = snapshot.child("Sadržaj").getValue().toString();
                        final String medij = snapshot.child("Medij").getValue().toString();
                        final String link = snapshot.child("Link").getValue().toString();
                        itemList.add(new News(naslov,sadrzaj,medij,link));
                    }
                }
                Collections.reverse(itemList);

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeNewsToShareCallback(adapter));
                itemTouchHelper.attachToRecyclerView(recyclerView);
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
        ((MainActivity)getActivity()).SetNavItemChecked(3);

        MainActivity activity = (MainActivity) getActivity();
        Boolean subFragmentData = activity.getSubFragmentData();

        if (mBundleRecyclerViewState != null && connectionFlag && subFragmentData) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
                }
            }, 50);
        }

        if(connectionFlag)
            recyclerView.setLayoutManager(gridLayoutManager);

        activity.setSubFragmentData(false);
    }

        @Override
    public void onPause() {
        // Save ListView state @ onPause
            if(connectionFlag) {
                Log.d("TAG", "saving listview state");
                mBundleRecyclerViewState = new Bundle();
                try{
                    mListState = recyclerView.getLayoutManager().onSaveInstanceState();
                    mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
                }
                catch (Exception ignored){
                }
            }
        super.onPause();
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
