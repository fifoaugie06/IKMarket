package com.example.ikmarket.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ikmarket.R;
import com.example.ikmarket.model.ResponseGeneral;
import com.example.ikmarket.model.type.Datum;
import com.example.ikmarket.model.type.ResponseType;
import com.example.ikmarket.model.unit.ResponseUnit;
import com.example.ikmarket.quality.ResponseQuality;
import com.example.ikmarket.services.ApiClient;
import com.example.ikmarket.services.ApiService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKomoditasActivity extends AppCompatActivity {
    private Spinner spinnerType, spinnerQuality, spinnerUnit;
    private ApiService apiService;
    private ProgressDialog dialog;
    private List<String> spinnerNameType, spinnerNameQuality, spinnerNameUnit;
    private List<Integer> spinnerCodeType, spinnerCodeQuality, spinnerCodeUnit;
    private ImageView imgKomoditas;
    private String mediaPath;
    private Button btnTambahKomoditas;
    private ImageButton btnTambahQuality;
    private int selectTypeCode, selectQualityCode, selectUnitCode;
    private EditText edtNamaProduct, edtHarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_komoditas);

        getSupportActionBar().setTitle("Tambah Komoditas");

        spinnerType = findViewById(R.id.spinnertype);
        spinnerQuality = findViewById(R.id.spinnerquality);
        spinnerUnit = findViewById(R.id.spinnerunit);
        imgKomoditas = findViewById(R.id.imgKomoditas);
        btnTambahKomoditas = findViewById(R.id.tambahkomoditas);
        edtNamaProduct = findViewById(R.id.edtnamaproduct);
        edtHarga = findViewById(R.id.edtharga);
        btnTambahQuality = findViewById(R.id.btntambahquality);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading ...");

        apiService = ApiClient.getClient().create(ApiService.class);

        loadSpinnerType();
        loadSpinnerQuality();
        loadSpinnerUnit();

        btnTambahQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dial;
                dial = new Dialog(TambahKomoditasActivity.this);
                dial.setContentView(R.layout.dialog_create_quality);
                dial.show();
                final Button btnTambah = dial.findViewById(R.id.btnadd);
                final EditText edtName = dial.findViewById(R.id.edtname);

                Window window = dial.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                btnTambah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtName.getText().length() == 0){
                            edtName.setError("Wajib diisi");
                        }else {
                            tambahQuality(edtName.getText().toString());
                            dial.dismiss();
                        }
                    }
                });
            }
        });

        imgKomoditas.setOnClickListener(v -> {
            String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

            if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
                Intent galeryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeryIntent, 0);
            } else {
                EasyPermissions.requestPermissions(this, "Access for storage",
                        101, galleryPermissions);
            }
        });

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

        btnTambahKomoditas.setOnClickListener(v -> {
//            Toast.makeText(TambahKomoditasActivity.this, selectTypeCode + " " + selectQualityCode + " " + selectUnitCode, Toast.LENGTH_LONG).show();
            if (mediaPath != null && selectTypeCode != 0 && selectQualityCode != 0 && selectUnitCode != 0
                    && edtNamaProduct != null && edtHarga != null){
                tambahKomoditas(edtNamaProduct.getText().toString(), selectTypeCode, edtHarga.getText().toString(),
                        selectQualityCode, selectUnitCode);
            }else {
                Toast.makeText(TambahKomoditasActivity.this, "Isi Semua Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tambahQuality(String qualityname) {
        dialog.show();

        apiService = ApiClient.getClient().create(ApiService.class);
        apiService.createQuality(qualityname).enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                if (response.isSuccessful()){
                    Toast.makeText(TambahKomoditasActivity.this, "Kualitas Baru Ditambahkan", Toast.LENGTH_SHORT).show();

                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                Toast.makeText(TambahKomoditasActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void tambahKomoditas(String nama, int selectTypeCode, String hrg, int selectQualityCode, int selectUnitCode) {
        dialog.show();

        File file = new File(mediaPath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody namaKomoditas = RequestBody.create(MediaType.parse("text/plain"), nama);
        RequestBody selectType = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectTypeCode));
        RequestBody harga = RequestBody.create(MediaType.parse("text/plain"), hrg);
        RequestBody selectQuality = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectQualityCode));
        RequestBody selectUnit = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectUnitCode));

        apiService = ApiClient.getClient().create(ApiService.class);
        apiService.createProducts(fileToUpload, filename, namaKomoditas, selectType, harga, selectQuality, selectUnit).enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                if (response.isSuccessful()){
                    Toast.makeText(TambahKomoditasActivity.this, "Komoditas Baru Ditambahkan", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(TambahKomoditasActivity.this, DashboardActivity.class));
                    finish();
                }else if (response.code() == 400){
                    Toast.makeText(TambahKomoditasActivity.this, "Gambar melebihi 2Mb", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                Toast.makeText(TambahKomoditasActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);

                imgKomoditas.setImageBitmap(BitmapFactory.decodeFile(mediaPath));

                cursor.close();
            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    private void loadSpinnerUnit() {
        dialog.show();
        apiService.getUnit().enqueue(new Callback<ResponseUnit>() {
            @Override
            public void onResponse(Call<ResponseUnit> call, Response<ResponseUnit> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();

                    spinnerNameUnit = new ArrayList<>();
                    spinnerCodeUnit = new ArrayList<>();

                    for (int i = 0; i < response.body().getDataCount(); i++) {
                        spinnerNameUnit.add(response.body().getData().get(i).getFullname());
                        spinnerCodeUnit.add(response.body().getData().get(i).getId());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TambahKomoditasActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameUnit);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerUnit.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseUnit> call, Throwable t) {

            }
        });
    }

    private void loadSpinnerQuality() {
        dialog.show();
        apiService.getQuality().enqueue(new Callback<ResponseQuality>() {
            @Override
            public void onResponse(Call<ResponseQuality> call, Response<ResponseQuality> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();

                    spinnerNameQuality = new ArrayList<>();
                    spinnerCodeQuality = new ArrayList<>();

                    for (int i = 0; i < response.body().getDataCount(); i++) {
                        spinnerNameQuality.add(response.body().getData().get(i).getName());
                        spinnerCodeQuality.add(response.body().getData().get(i).getId());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TambahKomoditasActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameQuality);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerQuality.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseQuality> call, Throwable t) {
                Toast.makeText(TambahKomoditasActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSpinnerType() {
        dialog.show();
        apiService.getType().enqueue(new Callback<ResponseType>() {
            @Override
            public void onResponse(Call<ResponseType> call, Response<ResponseType> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();

                    spinnerNameType = new ArrayList<>();
                    spinnerCodeType = new ArrayList<>();

                    for (int i = 0; i < response.body().getDataCount(); i++) {
                        spinnerNameType.add(response.body().getData().get(i).getName());
                        spinnerCodeType.add(response.body().getData().get(i).getId());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TambahKomoditasActivity.this, R.layout.support_simple_spinner_dropdown_item, spinnerNameType);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerType.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseType> call, Throwable t) {
                Toast.makeText(TambahKomoditasActivity.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}