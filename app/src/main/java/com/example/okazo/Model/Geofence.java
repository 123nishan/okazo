package com.example.okazo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geofence {
    @SerializedName("status")
    @Expose
    private int status;

    public int getStatus() {
        return status;
    }

    public Geofence(int status) {
        this.status = status;
    }
}
