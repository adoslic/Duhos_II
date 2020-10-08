package com.duhos.duhosii;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import io.realm.Realm;


public class SplashScreenActivity extends Activity {

    protected int _splashTime = 5000;

    private Thread splashTread;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        final SplashScreenActivity sPlashScreen = this;

        // thread for displaying the SplashScreen
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized(this){
                        wait(_splashTime);
                    }

                } catch(InterruptedException e) {}
                finally {

                    if(!isFinishing()) // This pretty useful boolean val tells if
                    //user has pressed the back button. very useful.
                    {Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);

                        startActivity(i);
                        finish();
                    }
                    onStop();
                }
            }
        };

        splashTread.start();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Toast.makeText(this,"exec---",Toast.LENGTH_LONG).show();
            synchronized(splashTread){
                splashTread.notifyAll();
            }
        }
        return true;
    }
    @Override
    protected void onPause() {

        super.onPause();

        if(splashTread.getState()==Thread.State.TIMED_WAITING){
            //Thread is still waiting and Activity is paused. Means user has pressed Home. Bail out
            finish();
        }

    }
}