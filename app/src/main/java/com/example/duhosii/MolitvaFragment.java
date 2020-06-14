package com.example.duhosii;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.ArrayList;
import java.util.List;

public class MolitvaFragment extends Fragment {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;

    public MolitvaFragment() {
    }

    RecyclerView recyclerView;
    List<Model> itemList;
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

        View view = inflater.inflate(R.layout.fragment_molitva,container,false);

        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(new ItemAdapter(initData()));
        return view;
    }

    private List<Model> initData() {

        itemList=new ArrayList<>();
        itemList.add(new Model("Naslov1","Datum1"));
        itemList.add(new Model("Naslov2","Datum2"));
        itemList.add(new Model("Naslov3","Datum3"));
        itemList.add(new Model("Naslov4","Datum4"));
        itemList.add(new Model("Naslov5","Datum5"));
        itemList.add(new Model("Naslov6","Datum6"));
        itemList.add(new Model("Naslov7","Datum7"));

        return itemList;
    }


}
