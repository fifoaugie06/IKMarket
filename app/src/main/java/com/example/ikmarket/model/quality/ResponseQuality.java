package com.example.ikmarket.model.quality;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseQuality {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data_count")
    @Expose
    private Integer dataCount;
    @SerializedName("data")
    @Expose
    private List<DatumQuality> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getDataCount() {
        return dataCount;
    }

    public void setDataCount(Integer dataCount) {
        this.dataCount = dataCount;
    }

    public List<DatumQuality> getData() {
        return data;
    }

    public void setData(List<DatumQuality> data) {
        this.data = data;
    }
}
