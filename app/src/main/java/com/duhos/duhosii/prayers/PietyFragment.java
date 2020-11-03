package com.duhos.duhosii.prayers;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duhos.duhosii.MainActivity;
import com.duhos.duhosii.R;
import com.duhos.duhosii.models.Prayer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PietyFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    List<Prayer> itemList = new ArrayList<>();
    private View molitvaFragmentView;
    private PrayerItemAdapter adapter;
    private static final String TAG ="TAG";
    private FloatingActionButton casoslovButton;
    private FloatingActionButton svjedocanstvoButton;

    public PietyFragment(List<Prayer> itemList) {
        this.itemList=itemList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.show();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View viewActionBar=mActionBar.getCustomView();
        zaglavlje=viewActionBar.findViewById(R.id.naslov);
        zaglavlje.setText(getContext().getResources().getString(R.string.odSrcaKsrcuNaslov));

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);

        molitvaFragmentView = inflater.inflate(R.layout.fragment_molitva,container,false);
        casoslovButton=molitvaFragmentView.findViewById(R.id.casoslovButton);
        casoslovButton.hide();
        svjedocanstvoButton=molitvaFragmentView.findViewById(R.id.svjedocanstvoButton);
        svjedocanstvoButton.show();
        svjedocanstvoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,new SendTestimonyFragment()).addToBackStack("").commit();
            }
        });

        onInit();

        return molitvaFragmentView;
    }

    public void onInit() {
        //DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        //itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        recyclerView = molitvaFragmentView.findViewById(R.id.recyclerViewMolitva);
        adapter = new PrayerItemAdapter(itemList,getContext().getResources().getString(R.string.svjedocanstvaString));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(itemDecorator);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipePrayerToShareCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).SetNavItemChecked(2);
    }
}
