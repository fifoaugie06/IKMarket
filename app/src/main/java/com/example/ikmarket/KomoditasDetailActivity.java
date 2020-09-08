package com.example.ikmarket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.example.ikmarket.services.ApiClient.BASE_URL_STORAGE;

public class KomoditasDetailActivity extends AppCompatActivity {
    private TextView tvNama, tvHarga, tvJenis, tvKualitas, tvTerakhirUpdate;
    private ProgressBar progressBar;
    private ImageView imgKomoditas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komoditas_detail);

        getSupportActionBar().setTitle("Detail Komoditas");

        tvNama = findViewById(R.id.tvNama);
        tvHarga = findViewById(R.id.tvHarga);
        tvJenis = findViewById(R.id.tvjenis);
        tvKualitas = findViewById(R.id.tvkualitas);
        tvTerakhirUpdate = findViewById(R.id.tvupdate);
        progressBar = findViewById(R.id.progress);
        imgKomoditas = findViewById(R.id.imgkomoditas);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Glide.with(this)
                    .load(BASE_URL_STORAGE + extras.getString("IMAGE"))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imgKomoditas);

            tvNama.setText(extras.getString("NAME"));
            tvHarga.setText(extras.getString("PRICE"));
            tvJenis.setText(extras.getString("TYPE"));
            tvKualitas.setText(extras.getString("QUALITY"));

            String strCurrentDate = extras.getString("UPDATE");
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
            Date date = null;
            try {
                date = inputFormat.parse(strCurrentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = outputFormat.format(date);
            tvTerakhirUpdate.setText(formattedDate);
        }
    }
}