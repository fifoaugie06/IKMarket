package com.example.ikmarket.services;

import com.example.ikmarket.model.product.ResponseProducts;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("products")
    Call<ResponseProducts> getProducts();
}
