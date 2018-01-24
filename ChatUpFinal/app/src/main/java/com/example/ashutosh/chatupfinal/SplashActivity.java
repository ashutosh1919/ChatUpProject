package com.example.ashutosh.chatupfinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.ashutosh.chatupfinal.modal.UserDetails;
import com.example.ashutosh.chatupfinal.preferences.MyPreferenceManager;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                UserDetails userDetails = MyPreferenceManager.getUserDetail(SplashActivity.this);

                if(userDetails==null)
                {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(SplashActivity.this, BaseDashboardActivity.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
