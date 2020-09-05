package com.example.ikmarket.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ikmarket.R;
import com.example.ikmarket.model.product.Datum;

import java.util.List;

import static com.example.ikmarket.services.ApiClient.BASE_URL_STORAGE;

public class KomoditasAdapter extends RecyclerView.Adapter<KomoditasAdapter.ViewHolder> {
    private List<Datum> responseProducts;
    private View view;
    private ImageView imgkomoditas;

    public KomoditasAdapter(List<Datum> responseProducts) {
        this.responseProducts = responseProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_komoditas, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvname.setText(responseProducts.get(position).getName());
        holder.tvdesc.setText("Kualitas " + responseProducts.get(position).getQuality().getName());
        holder.tvprice.setText("Rp " + responseProducts.get(position).getPrice() + "/" + responseProducts.get(position).getUnit().getShortname());

        Glide.with(view.getContext())
                .load(BASE_URL_STORAGE + responseProducts.get(position).getImage())
                .into(imgkomoditas);
    }

    @Override
    public int getItemCount() {
        return responseProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvname, tvdesc, tvprice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvname = itemView.findViewById(R.id.tvname);
            tvdesc = itemView.findViewById(R.id.tvdesc);
            tvprice = itemView.findViewById(R.id.tvprice);
            imgkomoditas = itemView.findViewById(R.id.imgkomoditas);
        }
    }
}
