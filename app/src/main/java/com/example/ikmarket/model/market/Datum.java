package com.example.ikmarket.model.market;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("province_id")
    @Expose
    private String provinceId;
    @SerializedName("regency_id")
    @Expose
    private String regencyId;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("fulladdress")
    @Expose
    private String fulladdress;
    @SerializedName("longlat")
    @Expose
    private String longlat;
    @SerializedName("open_at")
    @Expose
    private String openAt;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("market_category_id")
    @Expose
    private String marketCategoryId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("provincy")
    @Expose
    private Provincy provincy;
    @SerializedName("regency")
    @Expose
    private Regency regency;
    @SerializedName("district")
    @Expose
    private District district;
    @SerializedName("market_category")
    @Expose
    private MarketCategory marketCategory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getRegencyId() {
        return regencyId;
    }

    public void setRegencyId(String regencyId) {
        this.regencyId = regencyId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getFulladdress() {
        return fulladdress;
    }

    public void setFulladdress(String fulladdress) {
        this.fulladdress = fulladdress;
    }

    public String getLonglat() {
        return longlat;
    }

    public void setLonglat(String longlat) {
        this.longlat = longlat;
    }

    public String getOpenAt() {
        return openAt;
    }

    public void setOpenAt(String openAt) {
        this.openAt = openAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMarketCategoryId() {
        return marketCategoryId;
    }

    public void setMarketCategoryId(String marketCategoryId) {
        this.marketCategoryId = marketCategoryId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Provincy getProvincy() {
        return provincy;
    }

    public void setProvincy(Provincy provincy) {
        this.provincy = provincy;
    }

    public Regency getRegency() {
        return regency;
    }

    public void setRegency(Regency regency) {
        this.regency = regency;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public MarketCategory getMarketCategory() {
        return marketCategory;
    }

    public void setMarketCategory(MarketCategory marketCategory) {
        this.marketCategory = marketCategory;
    }

}
