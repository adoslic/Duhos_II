package com.duhos.duhosii;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.duhos.duhosii.calendar.CalendarFragment;
import com.duhos.duhosii.info.ApplicationInfoFragment;
import com.duhos.duhosii.info.dialog.Dialog;
import com.duhos.duhosii.info.dialog.WhatsNewDialog;
import com.duhos.duhosii.news.NewsFragment;
import com.duhos.duhosii.prayers.PrayerGroupsFragment;
import com.duhos.duhosii.questions.QuestionsFragment;
import com.duhos.duhosii.songs.SongsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import static android.content.ContentValues.TAG;
import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;
    private Boolean subFragment = false;
    private boolean songAbout = false;
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    int MY_REQUEST_CODE = 777;
    private AppUpdateManager appUpdateManager;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    String fragment = "";
                    switch (menuItem.getItemId()) {
                        case R.id.navigacija_pjesmarica:
                            selectedFragment = new SongsFragment();
                            fragment = "pjesmarica";
                            break;
                        case R.id.navigacija_kalendar:
                            selectedFragment = new CalendarFragment();
                            fragment = "kalendar";
                            break;
                        case R.id.navigacija_molitva:
                            selectedFragment = new PrayerGroupsFragment();
                            fragment = "molitva";
                            break;
                        case R.id.navigacija_multimedija:
                            selectedFragment = new NewsFragment();
                            fragment = "multimedija";
                            break;
                        case R.id.navigacija_pitanja:
                            selectedFragment = new QuestionsFragment();
                            fragment = "pitanja";
                            break;
                    }
                    int count = getSupportFragmentManager().getBackStackEntryCount();
                    if (count != 0) {
                        String stack = getSupportFragmentManager().getBackStackEntryAt(count - 1).getName();
                        if (!stack.equals(fragment)) {
                            if (count > 1) {
                                getSupportFragmentManager().popBackStack();
                            }
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, selectedFragment).addToBackStack(fragment).commit();
                        }
                    } else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, selectedFragment).addToBackStack(fragment).commit();
                    }
                    return true;
                }
            };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("com.duhosii", MODE_PRIVATE);
        //micanje default action bara i postavljanje posebnog
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));

        appUpdateManager = AppUpdateManagerFactory.create(this);
        checkUpdate();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setBackground(this.getResources().getDrawable(R.color.white));
        onNewIntent(getIntent());
        mAuth = FirebaseAuth.getInstance();
        connectToFirebase();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void connectToFirebase() {
        String email = "duhos.com@gmail.com";
        String password = "adminDuhos20";
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                        } else {
                            Log.d(TAG, "signInWithEmail:fail");
                        }
                    }
                });
    }

    public void otvoriDialog(View view) {
        final Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void idiNatrag(View view) {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (getSupportFragmentManager().getBackStackEntryAt(count - 1).getName().equals("subFragment")) {
            subFragment = true;
        }
        getSupportFragmentManager().popBackStack();
    }

    public void SetNavItemChecked(int id) {
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(id);
        menuItem.setChecked(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        switch (Objects.requireNonNull(getSupportFragmentManager().getBackStackEntryAt(count - 1).getName())) {
            case "molitva":
                super.onBackPressed();
                finish();
                break;
            case "pjesmarica": {
                SongsFragment fragment = (SongsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_containter);
                assert fragment != null;
                fragment.checkKeyboard();
                break;
            }
            case "pitanja": {
                QuestionsFragment fragment = (QuestionsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_containter);
                assert fragment != null;
                boolean isExpanded = fragment.checkHolder();
                if (!isExpanded) {
                    getSupportFragmentManager().popBackStack();
                }
                break;
            }
            case "subFragment":
                subFragment = true;
                getSupportFragmentManager().popBackStack();
                break;
            default:
                getSupportFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(appUpdateInfo -> {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, resume the update.
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo,
                                    IMMEDIATE,
                                    this,
                                    MY_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                });

        if (sharedPreferences.getBoolean("firstRun", true)) {
            //You can perform anything over here. This will call only first time
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new ApplicationInfoFragment()).addToBackStack("").commit();
            //final WhatsNewDialog whatsNewDialog = new WhatsNewDialog();
            //whatsNewDialog.show(getSupportFragmentManager(), "dialog");
            editor = sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.putLong("lastRunVersionCode", 0);
            editor.apply();
        } else {
            PackageInfo pInfo = null;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            assert pInfo != null;

            if (!sharedPreferences.getAll().containsKey("lastRunVersionCode")
                    && sharedPreferences.getLong( "lastRunVersionCode", 2) < pInfo.versionCode
                    && pInfo.versionCode == 3) {
                final WhatsNewDialog dialog = new WhatsNewDialog();
                dialog.show(getSupportFragmentManager(), "whatsIsNew");

                editor = sharedPreferences.edit();
                editor.putLong("lastRunVersionCode", pInfo.versionCode);
                editor.apply();
            }
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

    public boolean getSongAbout() {
        return songAbout;
    }

    public void setSongAbout(Boolean songAbout) {
        this.songAbout = songAbout;
    }

    private void checkUpdate() {
        // Returns an intent object that you use to check for an update.
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        Log.d(TAG, "Checking for updates");

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                Log.d(TAG, "Update available");
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "No Update available");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.d(TAG, "Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

}
