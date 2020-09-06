package com.example.ikmarket.services;

import com.example.ikmarket.model.ResponseGeneral;
import com.example.ikmarket.model.market.ResponseMarket;
import com.example.ikmarket.model.product.ResponseProducts;
import com.example.ikmarket.model.type.ResponseType;
import com.example.ikmarket.model.unit.ResponseUnit;
import com.example.ikmarket.quality.ResponseQuality;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("products")
    Call<ResponseProducts> getProducts();

    @GET("markets")
    Call<ResponseMarket> getMarket();

    @FormUrlEncoded
    @POST("adm1n/auth")
    Call<ResponseGeneral> auth(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("type")
    Call<ResponseType> getType();

    @GET("quality")
    Call<ResponseQuality> getQuality();

    @GET("unit")
    Call<ResponseUnit> getUnit();
}
