package com.example.okazo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class EventDetail implements Serializable {
    @SerializedName("event_type")
    @Expose
    private String eventType;

    @SerializedName("role_name")
    @Expose
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    @SerializedName("total_event")
    @Expose
    private String totalEvent;

    @SerializedName("following_count")
    @Expose
    private String followingCount;

    @SerializedName("moderator_count")
    @Expose
    private String moderatorCount;

    public String getTotalEvent() {
        return totalEvent;
    }

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("going_count")
    @Expose
    private String goingCount;




    @SerializedName("interested_count")
    @Expose
    private String interestedCount;

    public String getGoingCount() {
        return goingCount;
    }

    public String getInterestedCount() {
        return interestedCount;
    }

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("host_phone")
    @Expose
    private String hostPhone;

    public String getHostPhone() {
        return hostPhone;
    }

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

    @SerializedName("host_name")
    @Expose
    private String hostName;

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("following")
    @Expose
    private String following;



    @SerializedName("ticket_count")
    @Expose
    private String ticketCount;

    public String getTicketCount() {
        return ticketCount;
    }

    @SerializedName("ticket_price")
    @Expose
    private ArrayList<String> ticketPrice;

    @SerializedName("total_sold_type")
    @Expose
    private ArrayList<String> ticketSoldType;
//for geofence
    @SerializedName("start_date_array")
    @Expose
    private ArrayList<String> startDateArray;

    @SerializedName("image_array")
    @Expose
    private ArrayList<String> imageArray;


    @SerializedName("geo_id_array")
    @Expose
    private ArrayList<String> geoIdArray;

    @SerializedName("event_id_array")
    @Expose
    private ArrayList<String> eventIdArray;

    public ArrayList<String> getStartDateArray() {
        return startDateArray;
    }

    public ArrayList<String> getImageArray() {
        return imageArray;
    }

    @SerializedName("reward_array")
    @Expose
    private ArrayList<String> rewardArray;

    @SerializedName("title_array")
    @Expose
    private ArrayList<String> titleArray;

    @SerializedName("latitude_array")
    @Expose
    private ArrayList<String> latitudeArray;

    @SerializedName("longitude_array")
    @Expose
    private ArrayList<String> longitudeArray;

    public ArrayList<String> getGeoIdArray() {
        return geoIdArray;
    }

    public ArrayList<String> getEventIdArray() {
        return eventIdArray;
    }

    public ArrayList<String> getRewardArray() {
        return rewardArray;
    }

    public ArrayList<String> getTitleArray() {
        return titleArray;
    }

    public ArrayList<String> getLatitudeArray() {
        return latitudeArray;
    }

    public ArrayList<String> getLongitudeArray() {
        return longitudeArray;
    }
    //for gofence

    public String getFollowingCount() {
        return followingCount;
    }

    public String getModeratorCount() {
        return moderatorCount;
    }

    public ArrayList<String> getTicketSoldType() {
        return ticketSoldType;
    }

    @SerializedName("ticket_quantity")
    @Expose
    private ArrayList<String> ticketQuantity;



    @SerializedName("ticket_name")
    @Expose
    private ArrayList<String> ticketName;

    @SerializedName("ticket_id")
    @Expose
    private ArrayList<String> ticketId;

    @SerializedName("quantity")
    @Expose
    private ArrayList<String> quantity;

    @SerializedName("distance")
    @Expose
    private String distance;

    public String getDistance() {
        return distance;
    }

    public ArrayList<String> getQuantity() {
        return quantity;
    }

    public ArrayList<String> getTicketId() {
        return ticketId;
    }

    public ArrayList<String> getTicketPrice() {
        return ticketPrice;
    }

    public ArrayList<String> getTicketQuantity() {
        return ticketQuantity;
    }

    public ArrayList<String> getTicketName() {
        return ticketName;
    }

    public String getFollowing() {
        return following;
    }

    public String getResponse() {
        return response;
    }

    public String getHostName() {
        return hostName;
    }

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
