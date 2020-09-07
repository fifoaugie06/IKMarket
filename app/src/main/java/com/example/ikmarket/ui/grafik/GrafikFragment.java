package com.example.ikmarket.ui.grafik;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.ikmarket.R;
import com.example.ikmarket.model.product.Datum;
import com.example.ikmarket.model.product.ResponseProducts;
import com.example.ikmarket.services.ApiClient;
import com.example.ikmarket.services.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrafikFragment extends Fragment {
    private View view;
    private ApiService apiService;
    private ProgressDialog progress;
    private List<Datum> responseProducts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_grafik, container, false);

        progress = new ProgressDialog(view.getContext());
        progress.setCancelable(false);
        progress.setMessage("Loading ...");

        apiService = ApiClient.getClient().create(ApiService.class);

        progress.show();
        apiService.getProductsByPriceMax().enqueue(new Callback<ResponseProducts>() {
            @Override
            public void onResponse(Call<ResponseProducts> call, Response<ResponseProducts> response) {
                if (response.isSuccessful()){
                    progress.dismiss();

                    responseProducts = new ArrayList<>();

                    responseProducts.addAll(response.body().getData());

                    loadChart(responseProducts);
                }
            }

            @Override
            public void onFailure(Call<ResponseProducts> call, Throwable t) {
                Toast.makeText(view.getContext(), "Jaringan Error", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });

        progress.show();
        apiService.getProductsByPriceMin().enqueue(new Callback<ResponseProducts>() {
            @Override
            public void onResponse(Call<ResponseProducts> call, Response<ResponseProducts> response) {
                if (response.isSuccessful()){
                    progress.dismiss();

                    responseProducts = new ArrayList<>();

                    responseProducts.addAll(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ResponseProducts> call, Throwable t) {
                Toast.makeText(view.getContext(), "Jaringan Error", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });

        return view;
    }

    private void loadChart(List<Datum> responseProducts) {
        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < responseProducts.size(); i++) {
            data.add(new ValueDataEntry(responseProducts.get(i).getName(), responseProducts.get(i).getPrice()));
        }

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("Rp {%Value}{groupsSeparator: }");

        column.color("#43658B");

        cartesian.animation(false);
        cartesian.title("Komoditas dengan Harga Tertinggi");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("Rp {%Value}{groupsSeparator: }");
        cartesian.yAxis(0).labels().rotation(-90);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Product");

        anyChartView.setChart(cartesian);
    }
}
