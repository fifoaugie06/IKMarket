package com.example.ikmarket.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.ikmarket.R;
import com.example.ikmarket.model.ResponseGeneral;
import com.example.ikmarket.services.ApiClient;
import com.example.ikmarket.services.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edtusername, edtpassword;
    private Button btnLogin;
    private ApiService apiService;
    private ProgressDialog progress;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ISLOGIN = "IsLogin";
    public boolean stateLogin;

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        stateLogin = preferences.getBoolean(ISLOGIN, false);

        if (stateLogin) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtusername = findViewById(R.id.edtusername);
        edtpassword = findViewById(R.id.edtpassword);
        btnLogin = findViewById(R.id.btnMasuk);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        btnLogin.setOnClickListener(v -> {
            progress = new ProgressDialog(LoginActivity.this);
            progress.setCancelable(false);
            progress.setMessage("Loading ...");
            progress.show();

            apiService = ApiClient.getClient().create(ApiService.class);
            apiService.auth(edtusername.getText().toString(), edtpassword.getText().toString()).enqueue(new Callback<ResponseGeneral>() {
                @Override
                public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);

                        editor.putBoolean(ISLOGIN, true);
                        editor.apply();

                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Username atau Password Salah", Toast.LENGTH_SHORT).show();
                    }

                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            });
        });

        getSupportActionBar().hide();
    }
}
