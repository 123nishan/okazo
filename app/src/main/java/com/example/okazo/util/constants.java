package com.example.okazo.util;

import android.os.Environment;

import java.io.File;

public class constants {
    public static final int ERROR_DIALOG_REQUEST=9001;
    public static final int PERMISSION_REQUEST_ENABLE_GPS=9002;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=9003;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    //This is your from email
    public static final String EMAIL = "okazoevent@gmail.com";
    //shared preference  constant
    public static final String KEY_SHARED_PREFERENCE="hello";
    //constant ip for image locaiton
    public static final String KEY_IMAGE_ADDRESS="http://192.168.0.199/";
    //This is your from email password
    public static final String PASSWORD = "#Okazo123";

    public static final String  KEY_STAFF_ID="staffId";
    public static final String  KEY_STAFF_ROLE="staffRole";
    public static final String  KEY_STAFF_EMAIL="staffEmail";
    //for event detail
    public static final String  KEY_EVENT_TITLE="eventTitle";
    public static final String  KEY_ID_FOR_CHAT="id";
    public static final String KEY_EVENT_START_DATE="startDate";
    public static final String KEY_EVENT_END_DATE="endDate";
    public static final String KEY_EVENT_START_TIME="startTime";
    public static final String KEY_EVENT_END_TIME="endTime";
    public static final String KEY_PAGE_STATUS="pageStatus";
    public static final String KEY_EVENT_TICKET_STATUS="ticketStatus";
    public static final String KEY_TAG_ARRAY="eventTags";
    public static final String KEY_EVENT_SELECTED_LOCATION="selectedLocaiton";
    public static final String KEY_PLACE="place";
    public static final String KEY_IMAGE="image";
    public static final String KEY_EVENT_DESCRIPTION="description";
    public static final String KEY_LATITUDE="latitude";
    public static final String KEY_LONGITUDE="longitude";
    //for event ticket
    public static final String KEY_RADIO_TICKET_CATEGORY="ticketCategory";
    public static final String KEY_TICKET_TYPE_SINGLE_NAME="ticketTypeSingleName";
    public static final String KEY_TICKET_TYPE_SINGLE_PRICE="ticketTypeSinglePrice";
    public static final String KEY_TICKET_TYPE_SINGLE_NUMBER="ticketTypeSingleNumber";
    public static final String KEY_TICKET_NUMBER="ticketNumber";
    public static final String KEY_TICKET_PRICE="ticketPrice";
    public static final String KEY_BUNDLE_EVENT_DETAIL="eventDetailBundle";
    public static final String KEY_BUNDLE_TICKET_DETAIL="ticketDetailBundle";
    public static final String KEY_TICKET_TYPE_LIST="ticketTypeList";
    public static final String KEY_TICKET_TYPE_NAME_LIST="ticketTypeNameList";
    public static final String KEY_TICKET_TYPE_PRICE_LIST="ticketTypePriceList";
    public static final String KEY_TICKET_TYPE_NUMBER_LIST="ticketTypeNumberList";
    public static final String KEY_TICKET_ADD="ADD";
    public static final String KEY_TICKET_SUB="SUB";
    public static final String KEY_TICKET_ID_ARRAY="ticket_id";
    //public static final String KYE_EVENT_SELECTED_COUNTRY=""

    public static final String KEY_USER_ID="userId";
    public static final String KEY_EVENT_ID="eventId";
    public static final String KEY_EVENT_DETAIL="eventDetail";
    public static final String KEY_SENDER_ID="senderId";
    public static final String KEY_RECEIVER_ID="receiverId";
    public static final String KEY_SENDER_NAME="senderName";
    public static final String KEY_USER_ROLE="userRole";
    public static final String KEY_TOTAL_AMOUNT="totalAmount";
    public static final String KEY_TICKET_NAME="ticketName";
    public static final String KEY_TICKET_QUANTITY="ticketQuantity";
    public static final File FILELOCATION = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) ;



}
