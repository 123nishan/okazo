package com.example.okazo.Model;


import com.google.gson.annotations.SerializedName;

public class Note {

    @SerializedName("id") private int id;

    @SerializedName("latitude") private Double latitude;

    @SerializedName("longitude") private Double longitude;

    @SerializedName("name") private String name;

    @SerializedName("date") private String date;

    @SerializedName("success") private Boolean success;

    @SerializedName("message") private String message;
    public Note(){


    }
    public Note(int id, Double latitude, Double longitude, String name, String date, Boolean success, String message) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.date = date;
        this.success = success;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
