package com.example.okazo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventDetail {
    @SerializedName("event_type")
    @Expose
    private String eventType;
    public EventDetail(String eventType){
        this.eventType=eventType;
    }
    public String getEventType() {
        return eventType;
    }


}