package com.aeon.mymall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();

       /*
        SystemClock.sleep(3000);
        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();*/

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(SplashActivity.this, RegisterActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                finish();
            }
        }, 3000);*/

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent registerIntent = new Intent(SplashActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                    finish();
                }
            }, 3000);

        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, 3000);

        }

    }
}