package com.example.okazo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Comment {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("count")
    @Expose
    private String count;

    @SerializedName("created_date")
    @Expose
    private String createdDate;

    @SerializedName("user_image")
    @Expose
    private String userImage;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("comment")
    @Expose
    private String comment;

    public String getId() {
        return id;
    }

    public String getCount() {
        return count;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getComment() {
        return comment;
    }
}
