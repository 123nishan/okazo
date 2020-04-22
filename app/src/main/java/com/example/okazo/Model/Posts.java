package com.example.okazo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Posts {
    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("profile_image")
    @Expose
    private String profileImage;

    @SerializedName("post_id")
    @Expose
    private String postId;

    @SerializedName("detail")
    @Expose
    private String detail;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getPostId() {
        return postId;
    }

    public String getDetail() {
        return detail;
    }

    public String getImage() {
        return image;
    }

    public String getCreatedDate() {
        return createdDate;
    }
}
