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
import com.example.ikmarket.MainActivity;
import com.example.ikmarket.R;
import com.example.ikmarket.admin.DashboardActivity;
import com.example.ikmarket.model.ResponseGeneral;
import com.example.ikmarket.model.product.Datum;
import com.example.ikmarket.services.ApiClient;
import com.example.ikmarket.services.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ikmarket.admin.LoginActivity.ISLOGIN;
import static com.example.ikmarket.services.ApiClient.BASE_URL_STORAGE;

public class KomoditasAdminAdapter extends RecyclerView.Adapter<KomoditasAdminAdapter.ViewHolder> {
    private List<Datum> responseProducts;
    private View view;
    private ImageView imgkomoditas;
    private ApiService apiService;

    public KomoditasAdminAdapter(List<Datum> responseProducts) {
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

        holder.itemView.setOnLongClickListener(v -> {
            final Dialog dial;
            dial = new Dialog(view.getContext());
            dial.setContentView(R.layout.dialog_update_delete);
            dial.show();
            final Button btnUpdate = dial.findViewById(R.id.btnupdate);
            final Button btnDelete = dial.findViewById(R.id.btndelete);

            Window window = dial.getWindow();
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            btnDelete.setOnClickListener(v1 -> {
                dial.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setCancelable(true);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Anda yakin Menghapus data berikut ?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProducts(responseProducts.get(position).getId());
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

            return false;
        });
    }

    private void deleteProducts(Integer id) {
        ProgressDialog progress = new ProgressDialog(view.getContext());
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        apiService = ApiClient.getClient().create(ApiService.class);
        apiService.deleteProducts(String.valueOf(id)).enqueue(new Callback<ResponseGeneral>() {
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
