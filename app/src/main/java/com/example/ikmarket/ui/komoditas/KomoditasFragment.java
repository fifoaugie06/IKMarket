package com.example.ikmarket.ui.komoditas;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText edtsearch;
    private TextView tvnull;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_komoditas, container, false);

        tvnull = view.findViewById(R.id.tvnull);
        edtsearch = view.findViewById(R.id.edtsearch);
        recyclerView = view.findViewById(R.id.rvKomoditas);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        loadData();

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void filter(String params) {
        List<Datum> dataProductList = new ArrayList<>();
        try {
            for (Datum s : responseProducts) {
                if (s.getName().toLowerCase().contains(params.toLowerCase()) || s.getQuality().getName().toLowerCase().contains(params.toLowerCase())) {
                    dataProductList.add(s);
                }
            }

            if (dataProductList.size() == 0) {
                tvnull.setVisibility(View.VISIBLE);
            } else {
                tvnull.setVisibility(View.GONE);
            }

            adapter = new KomoditasAdapter(dataProductList);
            recyclerView.setAdapter(adapter);
        } catch (NullPointerException e) {
            Log.i("e", String.valueOf(e));
        }
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
                if (response.isSuccessful()) {
                    responseProducts = new ArrayList<>();

                    responseProducts.addAll(response.body().getData());
                }
                progress.dismiss();
                adapter = new KomoditasAdapter(responseProducts);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseProducts> call, Throwable t) {
                Toast.makeText(view.getContext(), "Jaringan Error", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });
    }
}
