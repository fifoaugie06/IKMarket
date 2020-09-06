package com.example.ikmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.ikmarket.admin.DashboardActivity;

import static com.example.ikmarket.admin.LoginActivity.ISLOGIN;
import static com.example.ikmarket.admin.LoginActivity.SHARED_PREFS;

public class SplashScreenActivity extends AppCompatActivity {
    public boolean stateLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        new Handler().postDelayed(() -> {
            SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            stateLogin = preferences.getBoolean(ISLOGIN, false);

            if (stateLogin) {
                startActivity(new Intent(SplashScreenActivity.this, DashboardActivity.class));
            }else {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            }
            finish();

        }, 1500);
    }
}
