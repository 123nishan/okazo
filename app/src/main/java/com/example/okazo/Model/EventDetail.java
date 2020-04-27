package com.example.okazo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventDetail implements Serializable {
    @SerializedName("event_type")
    @Expose
    private String eventType;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("start_time")
    @Expose
    private String startTime;

    @SerializedName("tag_count")
    @Expose
    private int tagCount;

    public int getTagCount() {
        return tagCount;
    }

    @SerializedName("event_type_image")
    @Expose
    private String eventTypeImage;

    public String getEventTypeImage() {
        return eventTypeImage;
    }

    @SerializedName("end_time")
    @Expose
    private String endTime;


    @SerializedName("start_date")
    @Expose
    private String startDate;

    @SerializedName("end_date")
    @Expose
    private String endDate;

    @SerializedName("place")
    @Expose
    private String place;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("ticket_status")
    @Expose
    private String ticketStatus;

    @SerializedName("page_status")
    @Expose
    private String pageStatus;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("tags")
    @Expose
    private String tags;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getPlace() {
        return place;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public String getPageStatus() {
        return pageStatus;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public String getTags() {
        return tags;
    }

    public EventDetail(String eventType){
        this.eventType=eventType;
    }
    public String getEventType() {
        return eventType;
    }


}
