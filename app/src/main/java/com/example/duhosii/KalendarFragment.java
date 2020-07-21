package com.example.duhosii;

import android.content.Context;
import android.graphics.Color;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class KalendarFragment extends Fragment implements DatePickerListener {

    TextView zaglavlje;
    BottomNavigationView bottomNavigationView;
    private boolean connectionFlag=false;
    private View connectionFragmentView,kalendarFragmentView;
    private ImageButton osvjeziButton;
    private HorizontalPicker picker;
    private DatabaseReference kalendarReference;
    private RecyclerView recyclerView;
    private KalendarItemAdapter adapter;
    private static final String TAG ="TAG";
    List<Dogadjaj> itemList = new ArrayList<>();
    List<String> listaDatuma=new ArrayList<>();
    String datumBezGodine;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActionBar mActionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.toolbar_without_back);
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        View view=mActionBar.getCustomView();
        zaglavlje=view.findViewById(R.id.naslov);
        zaglavlje.setText("Kalendar");
        checkInternetConnection();

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);
        if(connectionFlag==true) {
            kalendarFragmentView=inflater.inflate(R.layout.fragment_kalendar, container, false);
            kalendarReference = FirebaseDatabase.getInstance().getReference("Kalendar");

            picker=(HorizontalPicker)kalendarFragmentView.findViewById(R.id.datePicker);
            picker.setListener(this)
                    .setDays(365)
                    .setOffset(7)
                    .setDateSelectedColor(Color.parseColor("#899FB0"))
                    .setDateSelectedTextColor(Color.WHITE)
                    .setMonthAndYearTextColor(Color.parseColor("#143F61"))
                    .setTodayButtonTextColor(Color.parseColor("#143F61"))
                    .setTodayDateTextColor(Color.parseColor("#899FB0"))
                    .setUnselectedDayTextColor(Color.parseColor("#143F61"))
                    .setDayOfWeekTextColor(Color.parseColor("#899FB0"))
                    .setUnselectedDayTextColor(Color.parseColor("#143F61"))
                    .showTodayButton(false)
                    .init();
            picker.setBackgroundColor(Color.WHITE);
            picker.setDate(new DateTime());
            onInit();
            return kalendarFragmentView;
        }
        else {
            connectionFragmentView = inflater.inflate(R.layout.no_internet_connection_fragment, container, false);
            osvjeziButton=connectionFragmentView.findViewById(R.id.osvjeziButton);
            osvjeziButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomNavigationView.findViewById(R.id.navigacija_kalendar).performClick();
                }
            });
            return connectionFragmentView;
        }
    }

    public void onInit() {
        kalendarReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if(snapshot.exists()) {
                        final String naslov = snapshot.child("Naslov").getValue().toString();
                        final String opis = snapshot.child("Opis").getValue().toString();
                        final String datum = snapshot.child("Datum").getValue().toString();
                        final String lokacija = snapshot.child("Lokacija").getValue().toString();
                        itemList.add(new Dogadjaj(naslov,opis,datum,lokacija));

                    }
                }
                Collections.shuffle(itemList);
                Collections.sort(itemList, new Comparator<Dogadjaj>() {
                    public int compare(Dogadjaj d1, Dogadjaj d2) {
                        return d1.getDatum().compareTo(d2.getDatum());
                    }
                });
                DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_15));
                recyclerView = kalendarFragmentView.findViewById(R.id.recyclerViewKalendar);
                adapter = new KalendarItemAdapter(itemList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);
                recyclerView.addItemDecoration(itemDecorator);
                recyclerView.setAdapter(adapter);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeKalendarToShareCallback(adapter));
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
        ((MainActivity)getActivity()).SetNavItemChecked(1);
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
    public void onDateSelected(DateTime dateSelected) {
       String dan= String.valueOf(dateSelected.getDayOfWeek());
       String danUMjesecu=String.valueOf(dateSelected.getDayOfMonth());
       String mjesec=String.valueOf(dateSelected.getMonthOfYear());
       String godina=String.valueOf(dateSelected.getYear());

       if(Integer.parseInt(danUMjesecu)<10) {
           danUMjesecu="0"+danUMjesecu;
       }
        if(Integer.parseInt(mjesec)<10) {
            mjesec="0"+mjesec;
        }
        datumBezGodine=danUMjesecu+"/"+mjesec;
        int position = 0;
        for(int i=0;i<itemList.size();i++) {
            String[] parts = adapter.getDatum(i).getDatum().split("/");
            String danIzAdaptera = parts[0];
            String mjesecIzAdaptera = parts[1];
            if ((danIzAdaptera+"/"+mjesecIzAdaptera).equals(datumBezGodine))
                position = i;
        }
        recyclerView.scrollToPosition(position);
    }
}
