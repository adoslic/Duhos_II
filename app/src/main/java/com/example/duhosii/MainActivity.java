package com.example.duhosii;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //micanje default action bara i postavljanje posebnog
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        //neka pocetni fragment bude molitva
        findViewById(R.id.navigacija_molitva).performClick();
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment=null;

                    switch (menuItem.getItemId()){
                        case R.id.navigacija_pjesmarica:
                            selectedFragment=new PjesmaricaFragment();
                            break;
                        case R.id.navigacija_kalendar:
                            selectedFragment=new KalendarFragment();
                            break;
                        case R.id.navigacija_molitva:
                            selectedFragment=new MolitvaFragment();
                            break;
                        case R.id.navigacija_multimedija:
                            selectedFragment=new MultimedijaFragment();
                            break;
                        case R.id.navigacija_pitanja:
                            selectedFragment=new PitanjaFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,selectedFragment).commit();
                    return true;
                }
            };

    public void otvoriDialog(View view) {
        final Dialog dialog=new Dialog();
        dialog.show(getSupportFragmentManager(),"dialog");

    }

}
