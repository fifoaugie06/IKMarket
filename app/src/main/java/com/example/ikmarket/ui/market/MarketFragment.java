package com.example.ikmarket.ui.market;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ikmarket.R;
import com.example.ikmarket.adapter.MarketAdapter;
import com.example.ikmarket.model.market.Datum;
import com.example.ikmarket.model.market.ResponseMarket;
import com.example.ikmarket.services.ApiClient;
import com.example.ikmarket.services.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketFragment extends Fragment {
    private View view;
    private List<Datum> responseMarkets;
    private ProgressDialog progress;
    private MarketAdapter adapter;
    private RecyclerView recyclerView;
    private EditText edtsearch;
    private TextView tvnull;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_market, container, false);

        tvnull = view.findViewById(R.id.tvnull);
        edtsearch = view.findViewById(R.id.edtsearch);
        recyclerView = view.findViewById(R.id.rvMarket);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
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
        List<Datum> dataMarketList = new ArrayList<>();
        try {
            for (Datum s : responseMarkets) {
                if (s.getName().toLowerCase().contains(params.toLowerCase())) {
                    dataMarketList.add(s);
                }
            }

            if (dataMarketList.size() == 0) {
                tvnull.setVisibility(View.VISIBLE);
            } else {
                tvnull.setVisibility(View.GONE);
            }

            adapter = new MarketAdapter(dataMarketList);
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
        apiService.getMarket().enqueue(new Callback<ResponseMarket>() {
            @Override
            public void onResponse(Call<ResponseMarket> call, Response<ResponseMarket> response) {
                if (response.isSuccessful()) {
                    responseMarkets = new ArrayList<>();

                    responseMarkets.addAll(response.body().getData());
                }
                progress.dismiss();
                adapter = new MarketAdapter(responseMarkets);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseMarket> call, Throwable t) {
                Toast.makeText(view.getContext(), "Jaringan Error", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });
    }
}
