package com.example.okazo.Api;

import com.example.okazo.Model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("uid")
    @Expose
    private String uid;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("error_msg")
    @Expose
    private String errorMsg;

    public String getMod_Id() {
        return mod_Id;
    }

    public String getEvent_id() {
        return event_id;
    }

    @SerializedName("event_id")
@Expose
private String event_id;

    @SerializedName("mod_id")
    @Expose
    private String mod_Id;


    public APIResponse() {
    }

    public Boolean getError() {
        return error;
    }


    public void setError(Boolean error) {
        this.error = error;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
