package com.example.ikmarket.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ikmarket.MapsActivity;
import com.example.ikmarket.MarketDetailActivity;
import com.example.ikmarket.R;
import com.example.ikmarket.admin.MarketUpdateActivity;
import com.example.ikmarket.model.ResponseGeneral;
import com.example.ikmarket.model.market.Datum;
import com.example.ikmarket.services.ApiClient;
import com.example.ikmarket.services.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ikmarket.services.ApiClient.BASE_URL_STORAGE;

public class MarketAdminAdapter extends RecyclerView.Adapter<MarketAdminAdapter.ViewHolder> {
    private List<Datum> responseMarkets;
    private View view;
    private ImageView imgmarkets;

    public MarketAdminAdapter(List<Datum> responseMarkets) {
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

        holder.itemView.setOnLongClickListener(v -> {
            final Dialog dialog;
            dialog = new Dialog(view.getContext());
            dialog.setContentView(R.layout.dialog_update_delete);
            dialog.show();
            final Button btnUpdate = dialog.findViewById(R.id.btnupdate);
            final Button btnDelete = dialog.findViewById(R.id.btndelete);

            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            btnDelete.setOnClickListener(v1 -> {
                dialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setCancelable(true);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Anda yakin Menghapus data berikut ?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMarkets(responseMarkets.get(position).getId());
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dlg = builder.create();
                dlg.show();
            });

            btnUpdate.setOnClickListener(v12 -> {
                dialog.dismiss();

                Intent intent = new Intent(view.getContext(), MarketUpdateActivity.class);

                intent.putExtra("ID", String.valueOf(responseMarkets.get(position).getId()));
                intent.putExtra("NAMA", responseMarkets.get(position).getName());
                intent.putExtra("CATEGORY", responseMarkets.get(position).getMarketCategory().getName());
                intent.putExtra("PROVINCY", responseMarkets.get(position).getProvincy().getName());
                intent.putExtra("REGENCY", responseMarkets.get(position).getRegency().getName());
                intent.putExtra("DISTRICT", responseMarkets.get(position).getDistrict().getName());
                intent.putExtra("ADDRESS", responseMarkets.get(position).getFulladdress());
                intent.putExtra("LONGLAT", responseMarkets.get(position).getLonglat());
                intent.putExtra("OPENAT", responseMarkets.get(position).getOpenAt());
                intent.putExtra("DESC", responseMarkets.get(position).getDescription());

                view.getContext().startActivity(intent);
                ((Activity)view.getContext()).finish();
            });

            return false;
        });
    }

    private void deleteMarkets(Integer id) {
        ProgressDialog progress = new ProgressDialog(view.getContext());
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.deleteMarkets(String.valueOf(id)).enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                if (response.isSuccessful()){
                    Toast.makeText(view.getContext(), "Berhasil Dihapus", Toast.LENGTH_SHORT).show();

                    ((Activity)view.getContext()).finish();
                    ((Activity) view.getContext()).overridePendingTransition(0,0);
                    view.getContext().startActivity(((Activity) view.getContext()).getIntent());
                    ((Activity) view.getContext()).overridePendingTransition(0,0);
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                progress.dismiss();
            }
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
