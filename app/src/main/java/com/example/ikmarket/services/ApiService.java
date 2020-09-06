package com.example.ikmarket.services;

import com.example.ikmarket.model.ResponseGeneral;
import com.example.ikmarket.model.market.ResponseMarket;
import com.example.ikmarket.model.product.ResponseProducts;
import com.example.ikmarket.model.type.ResponseType;
import com.example.ikmarket.model.unit.ResponseUnit;
import com.example.ikmarket.quality.ResponseQuality;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @GET("products")
    Call<ResponseProducts> getProducts();

    @Multipart
    @POST("products")
    Call<ResponseGeneral> createProducts(
            @Part MultipartBody.Part image,
            @Part("image") RequestBody imagename,
            @Part("name") RequestBody namakomoditas,
            @Part("type_id") RequestBody typeid,
            @Part("price") RequestBody price,
            @Part("quality_id") RequestBody qualityid,
            @Part("unit_id") RequestBody unitid
    );

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
