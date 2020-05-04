package com.example.okazo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chat {
    @SerializedName("sender_id")
    @Expose
    private String senderId;

    @SerializedName("receiver_id")
    @Expose
    private String receiverId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("unseen_count")
    @Expose
    private String unseenCount;




    public String getUnseenCount() {
        return unseenCount;
    }

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getName() {
        return name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getImage() {
        return image;
    }

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("image")
    @Expose
    private String image;

}
