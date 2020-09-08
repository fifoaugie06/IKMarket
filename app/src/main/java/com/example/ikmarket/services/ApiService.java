package com.example.ikmarket.services;

import com.example.ikmarket.model.ResponseGeneral;
import com.example.ikmarket.model.district.ResponseDistrict;
import com.example.ikmarket.model.market.ResponseMarket;
import com.example.ikmarket.model.marketcategory.ResponseMarketCategory;
import com.example.ikmarket.model.product.ResponseProducts;
import com.example.ikmarket.model.provincy.ResponseProvincy;
import com.example.ikmarket.model.regency.ResponseRegency;
import com.example.ikmarket.model.type.ResponseType;
import com.example.ikmarket.model.unit.ResponseUnit;
import com.example.ikmarket.model.quality.ResponseQuality;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @GET("products")
    Call<ResponseProducts> getProducts();

    @GET("products/getByPriceMax")
    Call<ResponseProducts> getProductsByPriceMax();

    @GET("products/getByPriceMin")
    Call<ResponseProducts> getProductsByPriceMin();

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

    @FormUrlEncoded
    @PUT("products/{id}")
    Call<ResponseGeneral> updateProducts(
            @Path("id") String id,
            @Field("name") String namakomoditas,
            @Field("type_id") String typeid,
            @Field("price") String price,
            @Field("quality_id") String qualityid,
            @Field("unit_id") String unitid
    );

    @DELETE("products/{id}")
    Call<ResponseGeneral> deleteProducts(
            @Path("id") String id
    );

    @FormUrlEncoded
    @POST("quality")
    Call<ResponseGeneral> createQuality(
            @Field("name") String name
    );

    @GET("markets")
    Call<ResponseMarket> getMarket();

    @DELETE("markets/{id}")
    Call<ResponseGeneral> deleteMarkets(
            @Path("id") String id
    );

    @Multipart
    @POST("markets")
    Call<ResponseGeneral> createMarkets(
            @Part MultipartBody.Part image,
            @Part("image") RequestBody imagename,
            @Part("name") RequestBody namamarket,
            @Part("province_id") RequestBody provinceid,
            @Part("regency_id") RequestBody regencyid,
            @Part("district_id") RequestBody districtid,
            @Part("fulladdress") RequestBody fulladdress,
            @Part("longlat") RequestBody longlat,
            @Part("open_at") RequestBody open_at,
            @Part("description") RequestBody description,
            @Part("market_category_id") RequestBody market_category_id
    );

    @FormUrlEncoded
    @PUT("markets/{id}")
    Call<ResponseGeneral> updateMarkets(
            @Path("id") String id,
            @Field("name") String namamarket,
            @Field("province_id") String provinceid,
            @Field("regency_id") String regencyid,
            @Field("district_id") String districtid,
            @Field("fulladdress") String fulladdress,
            @Field("longlat") String longlat,
            @Field("open_at") String open_at,
            @Field("description") String description,
            @Field("market_category_id") String market_category_id
    );

    @GET("markets/category")
    Call<ResponseMarketCategory> getMarketCategory();

    @GET("provincy")
    Call<ResponseProvincy> getProvincy();

    @GET("provincy/{id}")
    Call<ResponseRegency> getRegency(
            @Path("id") String id
    );

    @GET("regency/{id}")
    Call<ResponseDistrict> getDistrict(
            @Path("id") String id
    );

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
