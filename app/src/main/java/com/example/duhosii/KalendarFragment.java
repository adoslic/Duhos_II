package com.example.duhosii;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


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
    private List<AlarmDate> listaAlarma=new ArrayList<>();
    private List<AlarmDate> konacnaListaAlarma=new ArrayList<>();
    private ArrayList<Boolean> alarmVisibility = new ArrayList<>();
    private boolean flagClicked=false;
    List<String> exDatesString=new ArrayList<String>();

    private int size=0;
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
        zaglavlje.setText("Kalendar");
        checkInternetConnection();

        flagClicked=false;

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_AUTO);
        if(connectionFlag==true) {
            kalendarFragmentView=inflater.inflate(R.layout.fragment_kalendar, container, false);

            final ArrayList<AlarmDate> list = new ArrayList<>();
            Realm.init(getContext());
            final Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm1) {
                    List<AlarmDate> resultList = realm.where(AlarmDate.class).findAll();
                    list.addAll(resultList);
                }
            });

            if (!list.isEmpty()) {
                konacnaListaAlarma=list;

                for(int i=0;i<konacnaListaAlarma.size();i++){
                    String vrijemeSdatumom=konacnaListaAlarma.get(i).getDatum() + " " + konacnaListaAlarma.get(i).getVrijeme()+":00";
                    Date vrijemeAlarma=null;
                    try {
                        vrijemeAlarma=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(vrijemeSdatumom);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(vrijemeAlarma.before(new Date())){
                        final RealmResults<AlarmDate> results = realm.where(AlarmDate.class).equalTo("datum",konacnaListaAlarma.get(i).datum.toString()).equalTo("vrijeme",konacnaListaAlarma.get(i).vrijeme.toString()).equalTo("naslov",konacnaListaAlarma.get(i).naslov.toString()).findAll();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                results.deleteAllFromRealm();
                            }
                        });
                        konacnaListaAlarma.remove(i);
                        i=0;
                    }
                }
            }


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            for(int i=1;i<366;i++) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(new Date());
                calendar1.add(Calendar.DATE, -i);
                String yesterdayAsString = dateFormat.format(calendar1.getTime());
                exDatesString.add(yesterdayAsString);
            }

            kalendarReference = FirebaseDatabase.getInstance().getReference("Kalendar");
            kalendarReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        if(snapshot.exists()) {
                            final String datum = snapshot.child("Datum").getValue().toString();
                            if(exDatesString.contains(datum))
                                snapshot.getRef().removeValue();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



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
                    .showTodayButton(true)
                    .init();
            picker.setBackgroundColor(Color.WHITE);
            picker.setDate(new DateTime());
            View view1=picker.getRootView();

            Typeface firaSansBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/firasans_bold.ttf");
            Typeface monseratBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/montserrat_light.ttf");

            TextView today=view1.findViewById(R.id.tvToday);
            today.setText("Danas");
            today.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            today.setTypeface(monseratBold);

            TextView mjesec=view1.findViewById(R.id.tvMonth);
            mjesec.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            mjesec.setTypeface(firaSansBold);

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
                        final String vrijeme = snapshot.child("Vrijeme").getValue().toString();

                        itemList.add(new Dogadjaj(naslov,opis,datum,vrijeme,lokacija));


                    }
                }

                for(int i=0;i<itemList.size()-1;i++){
                    for(int j=0;j<itemList.size()-i-1;j++) {
                        String date1String=itemList.get(j).datum+" "+itemList.get(j).vrijeme+":00";
                        Date date1 = null;
                        try {
                            date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date1String);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date date2 = null;
                        String date2String=itemList.get(j+1).datum+" "+itemList.get(j+1).vrijeme+":00";

                        try {
                            date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date2String);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(date2.before(date1)){
                            Collections.swap(itemList,j+1,j);
                        }
                    }
                }

                if(alarmVisibility.size()==0){
                    for (int i = 0; i < itemList.size(); i++) {
                        alarmVisibility.add(false);
                    }
                }

                for (int i = 0; i < itemList.size(); i++) {
                    for(int j=0;j<konacnaListaAlarma.size();j++){
                        if(konacnaListaAlarma.get(j).getNaslov().toString().equals(itemList.get(i).getNaslov().toString()) && konacnaListaAlarma.get(j).getDatum().toString().equals(itemList.get(i).getDatum().toString())) {
                            alarmVisibility.set(i, true);
                            break;
                        }
                        else
                            alarmVisibility.set(i,false);
                    }
                }


                recyclerView = kalendarFragmentView.findViewById(R.id.recyclerViewKalendar);
                adapter = new KalendarItemAdapter(itemList,konacnaListaAlarma,alarmVisibility);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);
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
        boolean flag=false;
        for(int i=0;i<itemList.size();i++) {
            String[] parts = adapter.getDatum(i).getDatum().split("/");
            String danIzAdaptera = parts[0];
            String mjesecIzAdaptera = parts[1];
            if ((danIzAdaptera+"/"+mjesecIzAdaptera).equals(datumBezGodine)) {
                position = i;
                flag=true;
                break;
            }
        }
        if(flag==false && flagClicked==true) {
            Toast.makeText(getContext(), "Nema predviđenih događaja za odabrani datum!", Toast.LENGTH_SHORT).show();
        }
        flagClicked=true;
        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position,0);
    }


}
