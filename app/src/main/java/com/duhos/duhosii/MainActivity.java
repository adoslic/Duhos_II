package com.duhos.duhosii;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.duhos.duhosii.calendar.CalendarFragment;
import com.duhos.duhosii.info.ApplicationInfoFragment;
import com.duhos.duhosii.info.dialog.Dialog;
import com.duhos.duhosii.news.NewsFragment;
import com.duhos.duhosii.prayers.PrayerGroupsFragment;
import com.duhos.duhosii.questions.QuestionsFragment;
import com.duhos.duhosii.songs.SongsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;
    private Boolean subFragment = false;
    BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()){
                        case R.id.navigacija_pjesmarica:
                            selectedFragment = new SongsFragment();
                            break;
                        case R.id.navigacija_kalendar:
                            selectedFragment=new CalendarFragment();
                            break;
                        case R.id.navigacija_molitva:
                            selectedFragment=new PrayerGroupsFragment();
                            break;
                        case R.id.navigacija_multimedija:
                            selectedFragment=new NewsFragment();
                            break;
                        case R.id.navigacija_pitanja:
                            selectedFragment=new QuestionsFragment();
                            break;
                    }
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,selectedFragment).addToBackStack("").commit();
                    return true;
                }
            };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("com.duhosii", MODE_PRIVATE);

        //remove default action bar and set different action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.setVisibility(View.VISIBLE);
        onNewIntent(getIntent());
    }

    public void otvoriDialog(View view) {
        final Dialog dialog=new Dialog();
        dialog.show(getSupportFragmentManager(),"dialog");
    }

    public void idiNatrag(View view) {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if(getSupportFragmentManager().getBackStackEntryAt(count-1).getName().equals("subFragment")) {
            subFragment = true;
        }
        getSupportFragmentManager().popBackStack();
    }

    public void SetNavItemChecked(int id) {
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(id);
        menuItem.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 1) {
            super.onBackPressed();
            finish();
        } else if(getSupportFragmentManager().getBackStackEntryAt(count-1).getName().equals("subFragment")) {
            subFragment = true;
            getSupportFragmentManager().popBackStack();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences.getBoolean("firstRun", true)) {
            //You can perform anything over here. This will call only first time
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new ApplicationInfoFragment()).addToBackStack("").commit();
            editor = sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();
        }
        String menuFragment = getIntent().getStringExtra("notification");
        if (menuFragment != null) {
            if (menuFragment.equals("openCalendar")) {
                findViewById(R.id.navigacija_kalendar).performClick();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String menuFragment = intent.getStringExtra("notification");
        if (menuFragment != null) {
            if (menuFragment.equals("openCalendar")) {
                findViewById(R.id.navigacija_kalendar).performClick();
            }
        } else {
            //neka pocetni fragment bude molitva
            findViewById(R.id.navigacija_molitva).performClick();
        }
    }

    public Boolean getSubFragmentData() {
        return subFragment;
    }

    public void setSubFragmentData(Boolean subFragment) {
        this.subFragment = subFragment;
    }

}
