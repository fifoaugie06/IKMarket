package com.example.ikmarket.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ikmarket.MapsActivity;
import com.example.ikmarket.MarketDetailActivity;
import com.example.ikmarket.R;
import com.example.ikmarket.model.market.Datum;

import java.util.List;

import static com.example.ikmarket.services.ApiClient.BASE_URL_STORAGE;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> {
    private List<Datum> responseMarkets;
    private View view;
    private ImageView imgmarkets;

    public MarketAdapter(List<Datum> responseMarkets) {
        this.responseMarkets = responseMarkets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_market, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvname.setText(responseMarkets.get(position).getName());
        holder.tvopen.setText("Buka pada " + responseMarkets.get(position).getOpenAt());
        holder.tvdesc.setText(responseMarkets.get(position).getDescription());

        Glide.with(view.getContext())
                .load(BASE_URL_STORAGE + responseMarkets.get(position).getImage())
                .into(imgmarkets);

        holder.tvlihatlokasi.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), MapsActivity.class);
            String[] splitKoordinat = responseMarkets.get(position).getLonglat().split(",");

            intent.putExtra("NAMAPASAR", responseMarkets.get(position).getName());
            intent.putExtra("LATITUDE", splitKoordinat[0]);
            intent.putExtra("LONGITUDE", splitKoordinat[1]);
            view.getContext().startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), MarketDetailActivity.class);

            intent.putExtra("IMAGE", responseMarkets.get(position).getImage());
            intent.putExtra("NAME", responseMarkets.get(position).getName());
            intent.putExtra("CATEGORY", responseMarkets.get(position).getMarketCategory().getName());
            intent.putExtra("PROVINSI", responseMarkets.get(position).getProvincy().getName());
            intent.putExtra("KABUPATEN", responseMarkets.get(position).getRegency().getName());
            intent.putExtra("KECAMATAN", responseMarkets.get(position).getDistrict().getName());
            intent.putExtra("ALAMAT", responseMarkets.get(position).getFulladdress());
            intent.putExtra("DESKRIPSI", responseMarkets.get(position).getDescription());

            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return responseMarkets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvname, tvopen, tvdesc, tvlihatlokasi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgmarkets = itemView.findViewById(R.id.imgmarket);
            tvname = itemView.findViewById(R.id.tvname);
            tvopen = itemView.findViewById(R.id.tvopen);
            tvdesc = itemView.findViewById(R.id.tvdesc);
            tvlihatlokasi = itemView.findViewById(R.id.tvlihatlokasi);
        }
    }
}
