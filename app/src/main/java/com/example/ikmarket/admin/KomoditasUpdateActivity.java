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
import com.example.ikmarket.model.quality.ResponseQuality;
import com.example.ikmarket.model.type.ResponseType;
import com.example.ikmarket.model.unit.ResponseUnit;
import com.example.ikmarket.services.ApiClient;
import com.example.ikmarket.services.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KomoditasUpdateActivity extends AppCompatActivity {
    private EditText edtName, edtPrice;
    private Spinner spinnerType, spinnerQuality, spinnerUnit;
    private ProgressDialog dialog;
    private ApiService apiService;
    private List<String> spinnerNameType, spinnerNameQuality, spinnerNameUnit;
    private List<Integer> spinnerCodeType, spinnerCodeQuality, spinnerCodeUnit;
    private String ID, selectTypeIntent, selectQualityIntent, selectUnitIntent;
    private int selectTypeCode, selectQualityCode, selectUnitCode;
    private Button btnUpdateKomoditas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komoditas_update);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Komoditas");

        edtName = findViewById(R.id.edtnamaproduct);
        edtPrice = findViewById(R.id.edtharga);
        spinnerType = findViewById(R.id.spinnermarketcategory);
        spinnerQuality = findViewById(R.id.spinnerquality);
        spinnerUnit = findViewById(R.id.spinnerunit);
        btnUpdateKomoditas = findViewById(R.id.updatekomoditas);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading ...");

        apiService = ApiClient.getClient().create(ApiService.class);

        loadSpinnerType();
        loadSpinnerQuality();
        loadSpinnerUnit();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            ID = extras.getString("ID");
            edtName.setText(extras.getString("NAME"));
            edtPrice.setText(extras.getString("PRICE"));
            selectTypeIntent = extras.getString("TYPE");
            selectQualityIntent = extras.getString("QUALITY");
            selectUnitIntent = extras.getString("UNIT");
        }

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectTypeCode = spinnerCodeType.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerQuality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectQualityCode = spinnerCodeQuality.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectUnitCode = spinnerCodeUnit.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnUpdateKomoditas.setOnClickListener(v -> {
            if (selectTypeCode != 0 && selectQualityCode != 0 && selectUnitCode != 0
                    && edtName != null && edtPrice != null) {
                updateKomoditas(edtName.getText().toString(), selectTypeCode, edtPrice.getText().toString(),
                        selectQualityCode, selectUnitCode);
            } else {
                Toast.makeText(KomoditasUpdateActivity.this, "Isi Semua Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateKomoditas(String nama, int selectTypeCode, String hrg, int selectQualityCode, int selectUnitCode) {
        dialog.show();

        apiService.updateProducts(ID, nama, String.valueOf(selectTypeCode), hrg, String.valueOf(selectQualityCode),
                String.valueOf(selectUnitCode)).enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                if (response.isSuccessful()){
                    Toast.makeText(KomoditasUpdateActivity.this, "Komoditas Berhasil Diupdate", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(KomoditasUpdateActivity.this, KomoditasActivity.class));
                    finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                Toast.makeText(KomoditasUpdateActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void loadSpinnerType() {
        dialog.show();
        apiService.getType().enqueue(new Callback<ResponseType>() {
            @Override
            public void onResponse(Call<ResponseType> call, Response<ResponseType> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();

                    spinnerNameType = new ArrayList<>();
                    spinnerCodeType = new ArrayList<>();

                    for (int i = 0; i < response.body().getDataCount(); i++) {
                        spinnerNameType.add(response.body().getData().get(i).getName());
                        spinnerCodeType.add(response.body().getData().get(i).getId());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(KomoditasUpdateActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameType);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapter);

                    int spinnerPosition = adapter.getPosition(selectTypeIntent);
                    spinnerType.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onFailure(Call<ResponseType> call, Throwable t) {
                Toast.makeText(KomoditasUpdateActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSpinnerQuality() {
        dialog.show();
        apiService.getQuality().enqueue(new Callback<ResponseQuality>() {
            @Override
            public void onResponse(Call<ResponseQuality> call, Response<ResponseQuality> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();

                    spinnerNameQuality = new ArrayList<>();
                    spinnerCodeQuality = new ArrayList<>();

                    for (int i = 0; i < response.body().getDataCount(); i++) {
                        spinnerNameQuality.add(response.body().getData().get(i).getName());
                        spinnerCodeQuality.add(response.body().getData().get(i).getId());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(KomoditasUpdateActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameQuality);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerQuality.setAdapter(adapter);

                    int spinnerPosition = adapter.getPosition(selectQualityIntent);
                    spinnerQuality.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onFailure(Call<ResponseQuality> call, Throwable t) {
                Toast.makeText(KomoditasUpdateActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSpinnerUnit() {
        dialog.show();
        apiService.getUnit().enqueue(new Callback<ResponseUnit>() {
            @Override
            public void onResponse(Call<ResponseUnit> call, Response<ResponseUnit> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();

                    spinnerNameUnit = new ArrayList<>();
                    spinnerCodeUnit = new ArrayList<>();

                    for (int i = 0; i < response.body().getDataCount(); i++) {
                        spinnerNameUnit.add(response.body().getData().get(i).getFullname());
                        spinnerCodeUnit.add(response.body().getData().get(i).getId());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(KomoditasUpdateActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameUnit);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerUnit.setAdapter(adapter);

                    int spinnerPosition = adapter.getPosition(selectUnitIntent);
                    spinnerUnit.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onFailure(Call<ResponseUnit> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(KomoditasUpdateActivity.this, KomoditasActivity.class));
            finish();
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(KomoditasUpdateActivity.this, KomoditasActivity.class));
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }
}