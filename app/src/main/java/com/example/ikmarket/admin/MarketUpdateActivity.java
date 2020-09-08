package com.example.ikmarket.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ikmarket.R;
import com.example.ikmarket.model.ResponseGeneral;
import com.example.ikmarket.model.district.ResponseDistrict;
import com.example.ikmarket.model.marketcategory.ResponseMarketCategory;
import com.example.ikmarket.model.provincy.ResponseProvincy;
import com.example.ikmarket.model.regency.ResponseRegency;
import com.example.ikmarket.services.ApiClient;
import com.example.ikmarket.services.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketUpdateActivity extends AppCompatActivity {
    private EditText edtName, edtAddress, edtLonglat, edtOpenat, edtDesc;
    private ProgressDialog dialog;
    private ApiService apiService;
    private String ID, selectCategoryIntent, selectProvincyIntent, selectRegencyIntent, selectDistrictIntent;
    private Spinner spinnerMarketCategory, spinnerProvincy, spinnerRegency, spinnerDistrict;
    private List<String> spinnerNameMarketCategory, spinnerNameProvincy, spinnerNameRegency, spinnerNameDistrict;
    private List<Integer> spinnerCodeMarketCategory, spinnerCodeProvincy, spinnerCodeRegency, spinnerCodeDistrict;
    private int selectProvincyCode, selectRegencyCode, selectDistrictCode, selectMarketCategoryCode;
    private Button btnUpdateMarkets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_update);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Markets");

        spinnerMarketCategory = findViewById(R.id.spinnermarketcategory);
        spinnerProvincy = findViewById(R.id.spinnerprovince);
        spinnerRegency = findViewById(R.id.spinnerregency);
        spinnerDistrict = findViewById(R.id.spinnerdistrict);
        edtName = findViewById(R.id.edtnamamarket);
        edtAddress = findViewById(R.id.edtfulladdress);
        edtLonglat = findViewById(R.id.edtlonglat);
        edtOpenat = findViewById(R.id.edtopenat);
        edtDesc = findViewById(R.id.edtdesc);
        btnUpdateMarkets = findViewById(R.id.updatemarkets);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading ...");

        apiService = ApiClient.getClient().create(ApiService.class);

        loadSpinnerMarketCategory();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            ID = extras.getString("ID");
            edtName.setText(extras.getString("NAMA"));
            selectCategoryIntent = extras.getString("CATEGORY");
            selectProvincyIntent = extras.getString("PROVINCY");
            selectRegencyIntent = extras.getString("REGENCY");
            selectDistrictIntent = extras.getString("DISTRICT");
            edtAddress.setText(extras.getString("ADDRESS"));
            edtLonglat.setText(extras.getString("LONGLAT"));
            edtOpenat.setText(extras.getString("OPENAT"));
            edtDesc.setText(extras.getString("DESC"));
        }

        spinnerProvincy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectProvincyCode = spinnerCodeProvincy.get(position);

                loadSpinnerRegency(selectProvincyCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerRegency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectRegencyCode = spinnerCodeRegency.get(position);

                loadSpinnerDistrict(selectRegencyCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectDistrictCode = spinnerCodeDistrict.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMarketCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectMarketCategoryCode = spinnerCodeMarketCategory.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnUpdateMarkets.setOnClickListener(v -> {
            if (edtName != null && edtAddress != null && edtLonglat != null
                    && edtOpenat != null && edtDesc != null) {
                updateMarkets(edtName.getText().toString(), edtAddress.getText().toString(),
                        edtLonglat.getText().toString(), edtOpenat.getText().toString(), edtDesc.getText().toString());
            } else {
                Toast.makeText(MarketUpdateActivity.this, "Field wajib diisi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMarkets(String mrkt, String adrs, String longlt, String opnat, String dsc) {
        dialog.show();

        apiService.updateMarkets(ID, mrkt, String.valueOf(selectProvincyCode), String.valueOf(selectRegencyCode),
                String.valueOf(selectDistrictCode), adrs, longlt, opnat, dsc, String.valueOf(selectMarketCategoryCode))
                .enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                if (response.isSuccessful()){
                    Toast.makeText(MarketUpdateActivity.this, "Markets Berhasil Diupdate", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(MarketUpdateActivity.this, MarketActivity.class));
                    finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                Toast.makeText(MarketUpdateActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void loadSpinnerMarketCategory() {
        dialog.show();
        apiService.getMarketCategory().enqueue(new Callback<ResponseMarketCategory>() {
            @Override
            public void onResponse(Call<ResponseMarketCategory> call, Response<ResponseMarketCategory> response) {
                if (response.isSuccessful()) {
                    spinnerNameMarketCategory = new ArrayList<>();
                    spinnerCodeMarketCategory = new ArrayList<>();

                    for (int i = 0; i < response.body().getDataCount(); i++) {
                        spinnerNameMarketCategory.add(response.body().getData().get(i).getName());
                        spinnerCodeMarketCategory.add(response.body().getData().get(i).getId());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MarketUpdateActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameMarketCategory);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMarketCategory.setAdapter(adapter);

                    int spinnerPosition = adapter.getPosition(selectCategoryIntent);
                    spinnerMarketCategory.setSelection(spinnerPosition);

                    loadSpinnerProvincy();
                }
            }

            @Override
            public void onFailure(Call<ResponseMarketCategory> call, Throwable t) {
                Toast.makeText(MarketUpdateActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSpinnerProvincy() {
        apiService.getProvincy().enqueue(new Callback<ResponseProvincy>() {
            @Override
            public void onResponse(Call<ResponseProvincy> call, Response<ResponseProvincy> response) {
                if (response.isSuccessful()) {
                    spinnerNameProvincy = new ArrayList<>();
                    spinnerCodeProvincy = new ArrayList<>();

                    for (int i = 0; i < response.body().getDataCount(); i++) {
                        spinnerNameProvincy.add(response.body().getData().get(i).getName());
                        spinnerCodeProvincy.add(response.body().getData().get(i).getId());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MarketUpdateActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameProvincy);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerProvincy.setAdapter(adapter);

                    int spinnerPosition = adapter.getPosition(selectProvincyIntent);
                    spinnerProvincy.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onFailure(Call<ResponseProvincy> call, Throwable t) {

            }
        });
    }

    private void loadSpinnerRegency(int selectProvincyCode) {
        apiService.getRegency(String.valueOf(selectProvincyCode)).enqueue(new Callback<ResponseRegency>() {
            @Override
            public void onResponse(Call<ResponseRegency> call, Response<ResponseRegency> response) {
                if (response.isSuccessful()) {
                    spinnerNameRegency = new ArrayList<>();
                    spinnerCodeRegency = new ArrayList<>();

                    for (int i = 0; i < response.body().getData().getRegency().size(); i++) {
                        spinnerNameRegency.add(response.body().getData().getRegency().get(i).getName());
                        spinnerCodeRegency.add(response.body().getData().getRegency().get(i).getId());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MarketUpdateActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameRegency);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerRegency.setAdapter(adapter);

                    int spinnerPosition = adapter.getPosition(selectRegencyIntent);
                    spinnerRegency.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onFailure(Call<ResponseRegency> call, Throwable t) {

            }
        });
    }

    private void loadSpinnerDistrict(int selectRegencyCode) {
        apiService.getDistrict(String.valueOf(selectRegencyCode)).enqueue(new Callback<ResponseDistrict>() {
            @Override
            public void onResponse(Call<ResponseDistrict> call, Response<ResponseDistrict> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();

                    spinnerNameDistrict = new ArrayList<>();
                    spinnerCodeDistrict = new ArrayList<>();

                    for (int i = 0; i < response.body().getData().getDistrict().size(); i++) {
                        spinnerNameDistrict.add(response.body().getData().getDistrict().get(i).getName());
                        spinnerCodeDistrict.add(response.body().getData().getDistrict().get(i).getId());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MarketUpdateActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameDistrict);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDistrict.setAdapter(adapter);

                    int spinnerPosition = adapter.getPosition(selectDistrictIntent);
                    spinnerDistrict.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onFailure(Call<ResponseDistrict> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(MarketUpdateActivity.this, MarketActivity.class));
            finish();
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MarketUpdateActivity.this, MarketActivity.class));
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }
}