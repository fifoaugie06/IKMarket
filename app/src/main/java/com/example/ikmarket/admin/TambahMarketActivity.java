package com.example.ikmarket.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahMarketActivity extends AppCompatActivity {
    private Spinner spinnerMarketCategory, spinnerProvincy, spinnerRegency, spinnerDistrict;
    private ApiService apiService;
    private ProgressDialog dialog;
    private List<String> spinnerNameMarketCategory, spinnerNameProvincy, spinnerNameRegency, spinnerNameDistrict;
    private List<Integer> spinnerCodeMarketCategory, spinnerCodeProvincy, spinnerCodeRegency, spinnerCodeDistrict;
    private int selectProvincyCode, selectRegencyCode, selectDistrictCode, selectMarketCategoryCode;
    private ImageButton btnLongLat;
    private EditText edtLongLat, edtOpenAt, edtNamaMarket, edtFullAddress, edtDescription;
    private ImageView icCamera, imgMarket;
    private String mediaPath;
    private Button btnTambahMarket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_market);

        getSupportActionBar().setTitle("Tambah Markets");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerMarketCategory = findViewById(R.id.spinnermarketcategory);
        spinnerProvincy = findViewById(R.id.spinnerprovince);
        spinnerRegency = findViewById(R.id.spinnerregency);
        spinnerDistrict = findViewById(R.id.spinnerdistrict);
        btnLongLat = findViewById(R.id.btngetlonglat);
        edtLongLat = findViewById(R.id.edtlonglat);
        edtOpenAt = findViewById(R.id.edtopenat);
        icCamera = findViewById(R.id.ic_Camera);
        imgMarket = findViewById(R.id.imgMarket);
        btnTambahMarket = findViewById(R.id.tambahmarkets);
        edtNamaMarket = findViewById(R.id.edtnamamarket);
        edtFullAddress = findViewById(R.id.edtfulladdress);
        edtDescription = findViewById(R.id.edtdesc);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading ...");

        apiService = ApiClient.getClient().create(ApiService.class);

        loadSpinnerMarketCategory();

        icCamera.setOnClickListener(view -> {
            String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

            if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
                Intent galeryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeryIntent, 0);
            } else {
                EasyPermissions.requestPermissions(this, "Access for storage",
                        101, galleryPermissions);
            }
        });

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

        btnLongLat.setOnClickListener(v -> {
            String[] mapsPermissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            if (EasyPermissions.hasPermissions(this, mapsPermissions)) {
                getlocation();
            } else {
                EasyPermissions.requestPermissions(this, "Access for maps",
                        101, mapsPermissions);
            }
        });

        edtOpenAt.setClickable(true);
        edtOpenAt.setLongClickable(false);
        edtOpenAt.setInputType(InputType.TYPE_NULL);
        edtOpenAt.setOnClickListener(v -> showTimePicker());

        btnTambahMarket.setOnClickListener(v -> {
            if (edtNamaMarket != null && mediaPath != null && edtFullAddress != null && edtLongLat != null
                    && edtOpenAt != null && edtDescription != null) {
                tambahMarket(edtNamaMarket.getText().toString(), edtFullAddress.getText().toString(),
                        edtLongLat.getText().toString(), edtOpenAt.getText().toString(), edtDescription.getText().toString());
            } else {
                Toast.makeText(TambahMarketActivity.this, "Field wajib diisi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tambahMarket(String mrkt, String adrs, String longlt, String opnat, String dsc) {
        dialog.show();

        File file = new File(mediaPath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody namaMarket = RequestBody.create(MediaType.parse("text/plain"), mrkt);
        RequestBody provinceID = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectProvincyCode));
        RequestBody regencyID = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectRegencyCode));
        RequestBody districtID = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectDistrictCode));
        RequestBody fullAddress = RequestBody.create(MediaType.parse("text/plain"), adrs);
        RequestBody longLat = RequestBody.create(MediaType.parse("text/plain"), longlt);
        RequestBody openAt = RequestBody.create(MediaType.parse("text/plain"), opnat);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), dsc);
        RequestBody marketCategoryID = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectMarketCategoryCode));

        apiService.createMarkets(fileToUpload, filename, namaMarket, provinceID, regencyID, districtID,
                fullAddress, longLat, openAt, description, marketCategoryID).enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TambahMarketActivity.this, "Market Baru Ditambahkan", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(TambahMarketActivity.this, DashboardActivity.class));
                    finish();
                } else if (response.code() == 400) {
                    Toast.makeText(TambahMarketActivity.this, "Gambar melebihi 2Mb", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                Toast.makeText(TambahMarketActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void showTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(TambahMarketActivity.this, (
                timePicker, selectedHour, selectedMinute) -> edtOpenAt.setText(selectedHour + ":" + selectedMinute)
                , hour, minute, true);
        timePickerDialog.show();
    }

    private void getlocation() {
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocation.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {

                edtLongLat.setText(location.getLatitude() + ", " + location.getLongitude());
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TambahMarketActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameMarketCategory);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMarketCategory.setAdapter(adapter);

                    loadSpinnerProvincy();
                }
            }

            @Override
            public void onFailure(Call<ResponseMarketCategory> call, Throwable t) {
                Toast.makeText(TambahMarketActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TambahMarketActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameProvincy);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerProvincy.setAdapter(adapter);
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TambahMarketActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameRegency);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerRegency.setAdapter(adapter);
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TambahMarketActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameDistrict);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDistrict.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseDistrict> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);

                imgMarket.setImageBitmap(BitmapFactory.decodeFile(mediaPath));

                cursor.close();
            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(TambahMarketActivity.this, DashboardActivity.class));
            finish();
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TambahMarketActivity.this, DashboardActivity.class));
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }
}