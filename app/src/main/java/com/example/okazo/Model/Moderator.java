package com.example.okazo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Moderator {
    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public String getRole() {
        return role;
    }
}
