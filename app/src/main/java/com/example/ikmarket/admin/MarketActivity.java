package com.example.ikmarket.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ikmarket.R;
import com.example.ikmarket.adapter.MarketAdminAdapter;
import com.example.ikmarket.model.market.Datum;
import com.example.ikmarket.model.market.ResponseMarket;
import com.example.ikmarket.services.ApiClient;
import com.example.ikmarket.services.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketActivity extends AppCompatActivity {
    private List<Datum> responseMarkets;
    private ProgressDialog progress;
    private MarketAdminAdapter adapter;
    private RecyclerView recyclerView;
    private EditText edtsearch;
    private TextView tvnull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_market);

        tvnull = findViewById(R.id.tvnull);
        edtsearch = findViewById(R.id.edtsearch);
        recyclerView = findViewById(R.id.rvMarket);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Market");

        loadData();

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filter(String params) {
        List<Datum> dataMarketList = new ArrayList<>();
        try {
            for (Datum s : responseMarkets) {
                if (s.getName().toLowerCase().contains(params.toLowerCase())) {
                    dataMarketList.add(s);
                }
            }

            if (dataMarketList.size() == 0) {
                tvnull.setVisibility(View.VISIBLE);
            } else {
                tvnull.setVisibility(View.GONE);
            }

            adapter = new MarketAdminAdapter(dataMarketList);
            recyclerView.setAdapter(adapter);
        } catch (NullPointerException e) {
            Log.i("e", String.valueOf(e));
        }
    }


    private void loadData() {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getMarket().enqueue(new Callback<ResponseMarket>() {
            @Override
            public void onResponse(Call<ResponseMarket> call, Response<ResponseMarket> response) {
                if (response.isSuccessful()) {
                    responseMarkets = new ArrayList<>();

                    responseMarkets.addAll(response.body().getData());
                }
                progress.dismiss();
                adapter = new MarketAdminAdapter(responseMarkets);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseMarket> call, Throwable t) {
                Toast.makeText(MarketActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(MarketActivity.this, DashboardActivity.class));
            finish();
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MarketActivity.this, DashboardActivity.class));
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }
}