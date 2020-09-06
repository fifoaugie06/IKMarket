package com.example.ikmarket.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ikmarket.R;
import com.example.ikmarket.adapter.KomoditasAdapter;
import com.example.ikmarket.adapter.MarketAdapter;
import com.example.ikmarket.model.market.ResponseMarket;
import com.example.ikmarket.model.product.ResponseProducts;
import com.example.ikmarket.services.ApiClient;
import com.example.ikmarket.services.ApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {
    private TextView tvGreeting, tvCountKomoditas, tvCountMarket, btnLogout;
    private ApiService apiService;
    private ProgressDialog progress;
    private KomoditasAdapter komoditasAdapter;
    private MarketAdapter marketsAdapter;
    private RecyclerView recyclerView, recyclerView2;
    private CardView btnAddKomoditas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvGreeting = findViewById(R.id.tvgreeting);
        tvCountKomoditas = findViewById(R.id.tvcountproduct);
        tvCountMarket = findViewById(R.id.tvcountmarket);
        btnAddKomoditas = findViewById(R.id.btnaddkomoditas);
        btnLogout = findViewById(R.id.btnlogout);

        recyclerView = findViewById(R.id.rvKomoditas);
        recyclerView2 = findViewById(R.id.rvMarket);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        Calendar currentTime = Calendar.getInstance();

        tvGreeting.setText(getGreeting(currentTime.get(Calendar.HOUR_OF_DAY)));

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");

        loadDataKomoditas();
        loadDataMarket();

        btnAddKomoditas.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, TambahKomoditasActivity.class)));

//        btnLogout.setOnClickListener(v ->
//
//        );

        getSupportActionBar().hide();
    }

    private void loadDataMarket() {
        progress.show();
        List<com.example.ikmarket.model.market.Datum> responseMakets = new ArrayList<>();

        apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getMarket().enqueue(new Callback<ResponseMarket>() {
            @Override
            public void onResponse(Call<ResponseMarket> call, Response<ResponseMarket> response) {
                if (response.isSuccessful()) {
                    tvCountMarket.setText(String.valueOf(response.body().getDataCount()));

                    responseMakets.addAll(response.body().getData());
                    progress.dismiss();
                }

                if (responseMakets.size() >= 3) {
                    marketsAdapter = new MarketAdapter(responseMakets.subList(0, 3));
                } else {
                    marketsAdapter = new MarketAdapter(responseMakets);
                }
                recyclerView2.setAdapter(marketsAdapter);
            }

            @Override
            public void onFailure(Call<ResponseMarket> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });
    }

    private void loadDataKomoditas() {
        progress.show();
        List<com.example.ikmarket.model.product.Datum> responseProducts = new ArrayList<>();

        apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProducts().enqueue(new Callback<ResponseProducts>() {
            @Override
            public void onResponse(Call<ResponseProducts> call, Response<ResponseProducts> response) {
                if (response.isSuccessful()) {
                    tvCountKomoditas.setText(String.valueOf(response.body().getDataCount()));

                    responseProducts.addAll(response.body().getData());
                    progress.dismiss();
                }

                if (responseProducts.size() >= 3) {
                    komoditasAdapter = new KomoditasAdapter(responseProducts.subList(0, 3));
                }else {
                    komoditasAdapter = new KomoditasAdapter(responseProducts);
                }
                recyclerView.setAdapter(komoditasAdapter);
            }

            @Override
            public void onFailure(Call<ResponseProducts> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });
    }

    private String getGreeting(int params) {
        if (params >= 0 && params <= 10) {
            return "Selamat Pagi";
        } else if (params >= 11 && params <= 14) {
            return "Selamat Siang";
        } else if (params >= 15 && params <= 16) {
            return "Selamat Sore";
        } else if (params == 17) {
            return "Selamat Petang";
        } else {
            return "Selamat Malam";
        }
    }
}