package com.example.okazo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("verified")
    @Expose
    private String verified;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("mod_id")
    @Expose
    private String modId;

    @SerializedName("amount")
    @Expose
    private int amount;

    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public int getAmount() {
        return amount;
    }

    public String getModId() {
        return modId;
    }

    @SerializedName("mod_status")
    @Expose
    private String modStatus;

    public String getModStatus() {
        return modStatus;
    }

    public String getId() {
        return id;
    }

    public String getVerified() {
        return verified;
    }

    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;

    @SerializedName("image")
    @Expose
    private String image;

    public String getImage() {
        return image;
    }

    public User() {
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }
}
