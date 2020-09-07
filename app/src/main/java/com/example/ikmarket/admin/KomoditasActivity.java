package com.example.ikmarket.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.ikmarket.adapter.KomoditasAdminAdapter;
import com.example.ikmarket.model.product.Datum;
import com.example.ikmarket.model.product.ResponseProducts;
import com.example.ikmarket.services.ApiClient;
import com.example.ikmarket.services.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KomoditasActivity extends AppCompatActivity {
    private List<Datum> responseProducts;
    private ProgressDialog progress;
    private KomoditasAdminAdapter adapter;
    private RecyclerView recyclerView;
    private EditText edtsearch;
    private TextView tvnull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_komoditas);

        tvnull = findViewById(R.id.tvnull);
        edtsearch = findViewById(R.id.edtsearch);
        recyclerView = findViewById(R.id.rvKomoditas);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        getSupportActionBar().setTitle("Komoditas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        List<Datum> dataProductList = new ArrayList<>();
        try {
            for (Datum s : responseProducts) {
                if (s.getName().toLowerCase().contains(params.toLowerCase()) || s.getQuality().getName().toLowerCase().contains(params.toLowerCase())) {
                    dataProductList.add(s);
                }
            }

            if (dataProductList.size() == 0) {
                tvnull.setVisibility(View.VISIBLE);
            } else {
                tvnull.setVisibility(View.GONE);
            }

            adapter = new KomoditasAdminAdapter(dataProductList);
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
        apiService.getProducts().enqueue(new Callback<ResponseProducts>() {
            @Override
            public void onResponse(Call<ResponseProducts> call, Response<ResponseProducts> response) {
                if (response.isSuccessful()) {
                    responseProducts = new ArrayList<>();

                    responseProducts.addAll(response.body().getData());
                }
                progress.dismiss();
                adapter = new KomoditasAdminAdapter(responseProducts);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseProducts> call, Throwable t) {
                Toast.makeText(KomoditasActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(KomoditasActivity.this, DashboardActivity.class));
            finish();
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(KomoditasActivity.this, DashboardActivity.class));
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }
}