package com.example.ikmarket.model.regency;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("regency")
    @Expose
    private List<Regency> regency = null;

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

    public List<Regency> getRegency() {
        return regency;
    }

    public void setRegency(List<Regency> regency) {
        this.regency = regency;
    }
}
