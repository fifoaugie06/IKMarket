package com.example.ikmarket.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import com.example.ikmarket.MainActivity;
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

import static com.example.ikmarket.admin.LoginActivity.ISLOGIN;
import static com.example.ikmarket.admin.LoginActivity.SHARED_PREFS;

public class DashboardActivity extends AppCompatActivity {
    private TextView tvGreeting, tvCountKomoditas, tvCountMarket, btnLogout, btnKomoditasLainnya, btnMarketLainnya;
    private ApiService apiService;
    private ProgressDialog progress;
    private KomoditasAdapter komoditasAdapter;
    private MarketAdapter marketsAdapter;
    private RecyclerView recyclerView, recyclerView2;
    private CardView btnAddKomoditas, btnAddMarkets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvGreeting = findViewById(R.id.tvgreeting);
        tvCountKomoditas = findViewById(R.id.tvcountproduct);
        tvCountMarket = findViewById(R.id.tvcountmarket);
        btnAddKomoditas = findViewById(R.id.btnaddkomoditas);
        btnAddMarkets = findViewById(R.id.btnaddmarkets);
        btnLogout = findViewById(R.id.btnlogout);
        btnKomoditasLainnya = findViewById(R.id.tvlainnya);
        btnMarketLainnya = findViewById(R.id.tvlainnya2);

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

        btnAddKomoditas.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, TambahKomoditasActivity.class));
            finish();
        });
        btnAddMarkets.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, TambahMarketActivity.class));
            finish();
        });
        btnKomoditasLainnya.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, KomoditasActivity.class));
            finish();
        });
        btnMarketLainnya.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, MarketActivity.class));
            finish();
        });

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        btnLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Konfirmasi");
            builder.setMessage("Anda yakin keluar dari Admin ?");
            builder.setPositiveButton("Confirm", (dialog, which) -> {
                preferences.edit().remove(ISLOGIN).apply();

                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                finish();
            });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {

            });

            AlertDialog dlg = builder.create();
            dlg.show();
        });

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

                    loadDataMarket();
                }

                if (responseProducts.size() >= 3) {
                    komoditasAdapter = new KomoditasAdapter(responseProducts.subList(0, 3));
                } else {
                    komoditasAdapter = new KomoditasAdapter(responseProducts);
                }
                recyclerView.setAdapter(komoditasAdapter);
                komoditasAdapter.notifyDataSetChanged();
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