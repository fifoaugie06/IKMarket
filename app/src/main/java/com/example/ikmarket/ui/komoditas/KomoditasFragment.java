package com.example.ikmarket.ui.komoditas;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ikmarket.R;
import com.example.ikmarket.adapter.KomoditasAdapter;
import com.example.ikmarket.model.product.Datum;
import com.example.ikmarket.model.product.ResponseProducts;
import com.example.ikmarket.services.ApiClient;
import com.example.ikmarket.services.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KomoditasFragment extends Fragment {
    private View view;
    private List<Datum> responseProducts;
    private ProgressDialog progress;
    private KomoditasAdapter adapter;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_komoditas, container, false);

        recyclerView = view.findViewById(R.id.rvKomoditas);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        loadData();

        return view;
    }

    private void loadData() {
        progress = new ProgressDialog(view.getContext());
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProducts().enqueue(new Callback<ResponseProducts>() {
            @Override
            public void onResponse(Call<ResponseProducts> call, Response<ResponseProducts> response) {
                if (response.isSuccessful()){
                    responseProducts = new ArrayList<>();

                    responseProducts.addAll(response.body().getData());
                }
                progress.dismiss();
                adapter = new KomoditasAdapter(responseProducts);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseProducts> call, Throwable t) {
                Log.i("qwe", call.request().url().toString());
            }
        });
    }
}
