package com.example.ikmarket.model.market;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Regency {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("province_id")
    @Expose
    private String provinceId;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
