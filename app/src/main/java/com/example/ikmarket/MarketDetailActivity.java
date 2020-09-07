package com.example.ikmarket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import static com.example.ikmarket.services.ApiClient.BASE_URL_STORAGE;

public class MarketDetailActivity extends AppCompatActivity {
    private TextView tvNama, tvProvincy, tvKabupaten, tvKecamatan, tvAddress, tvDeskripsi, tvCategory;
    private ImageView imgMarket;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detail);

        getSupportActionBar().setTitle("Detail Market");

        tvNama = findViewById(R.id.tvNamaMarket);
        tvProvincy = findViewById(R.id.tvProvinsi);
        tvKabupaten = findViewById(R.id.tvKabupaten);
        tvKecamatan = findViewById(R.id.tvKecamatan);
        tvAddress = findViewById(R.id.tvAddress);
        tvDeskripsi = findViewById(R.id.tvdesc);
        progressBar = findViewById(R.id.progress);
        imgMarket = findViewById(R.id.imgMarkets);
        tvCategory = findViewById(R.id.tvCategory);

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
                    .into(imgMarket);

            tvNama.setText(extras.getString("NAME"));
            tvCategory.setText(extras.getString("CATEGORY"));
            tvProvincy.setText(extras.getString("PROVINSI"));
            tvKabupaten.setText(extras.getString("KABUPATEN"));
            tvKecamatan.setText(extras.getString("KECAMATAN"));
            tvAddress.setText(extras.getString("ALAMAT"));
            tvDeskripsi.setText(extras.getString("DESKRIPSI"));
        }
    }
}