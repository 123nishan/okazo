package com.example.okazo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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

    @SerializedName("following_count")
    @Expose
    private String followingCount;

    @SerializedName("money")
    @Expose
    private String money;

    public String getMoney() {
        return money;
    }

    @SerializedName("moderator_count")
    @Expose
    private String moderatorCount;

    public String getFollowingCount() {
        return followingCount;
    }

    public String getModeratorCount() {
        return moderatorCount;
    }

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("moderator_request")
    @Expose
    private String moderatorRequest;

    public String getModeratorRequest() {
        return moderatorRequest;
    }

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

    @SerializedName("payment_option")
    @Expose
    private ArrayList<String> paymentOption;

    @SerializedName("bought_date")
    @Expose
    private ArrayList<String> boughtDate;

    @SerializedName("bought_count")
    @Expose
    private String boughtCount;

    @SerializedName("ticket_id")
    @Expose
    private ArrayList<String> ticketId;

    @SerializedName("ticket_name")
    @Expose
    private ArrayList<String> ticketName;

    @SerializedName("ticket_quantity")
    @Expose
    private ArrayList<String> ticketQuantity;

    @SerializedName("ticket_amount")
    @Expose
    private ArrayList<String> ticketAmount;

    @SerializedName("event_name")
    @Expose
    private ArrayList<String> eventName;

    @SerializedName("event_start_date")
    @Expose
    private ArrayList<String> eventStartDate;

    @SerializedName("event_start_time")
    @Expose
    private ArrayList<String> eventStartTime;



    public ArrayList<String> getEventStartDate() {
        return eventStartDate;
    }

    public ArrayList<String> getEventStartTime() {
        return eventStartTime;
    }

    public ArrayList<String> getEventName() {
        return eventName;
    }

    public ArrayList<String> getPaymentOption() {
        return paymentOption;
    }

    public ArrayList<String> getBoughtDate() {
        return boughtDate;
    }

    public ArrayList<String> getTicketId() {
        return ticketId;
    }

    public ArrayList<String> getTicketName() {
        return ticketName;
    }

    public ArrayList<String> getTicketQuantity() {
        return ticketQuantity;
    }

    public ArrayList<String> getTicketAmount() {
        return ticketAmount;
    }

    public String getBoughtCount() {
        return boughtCount;
    }


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
