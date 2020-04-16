package com.example.okazo.Api;

import com.example.okazo.Model.EventDetail;
import com.example.okazo.Model.Geofence;
import com.example.okazo.Model.Note;

import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("register.php")
    Call<APIResponse> registerUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name,
            @Field("phone") String phone


    );
    @FormUrlEncoded
    @POST("login.php")
    Call<APIResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );


    @GET("eventDetail/eventType.php")
    Call<ArrayList<EventDetail>> getEventType();

    @GET("geofence/activate_geofence.php")
    Call<ArrayList<Geofence>> getGeofenceStatus();

    @FormUrlEncoded
    @POST("save.php")
    Call<Note> saveNote(
            @Field("latitude") Double latitude,
            @Field("longitude") Double longitude,
            @Field("name") String name
    );
    @FormUrlEncoded
    @POST("otp.php")
    Call<APIResponse> otp(
            @Field("email") String email,
            @Field("code") String code,

            @Field("time") Timestamp time

    );
    @FormUrlEncoded
    @POST("otpVerification.php")
    Call<APIResponse>  otpVerification(
        @Field("email") String email,
        @Field("verified") String verified
    );
    //@FormUrlEncoded
    @Multipart
    @POST("eventDetail/event_creation.php")
    Call<APIResponse> eventCreation(
//            @Field("title") String title,
//            @Field("id") String id,
//            @Field("description") String description,
//            @Field("start_time") String start_time,
//            @Field("end_time") String end_time,
//            @Field("start_date") String start_date,
//            @Field("end_date") String end_date,
//            @Field("place") String place,
//            @Field("latitude") String latitude,
//            @Field("longitude") String longitude,
//            @Field("image") String image,
//            @Field("ticket_status") String ticket_status,
//            @Field("page_status") String page_status,
//            @Field("user_id") String user_id,
//            @Field("ticket_category") String ticket_category,
//            @Field("moderator_id") String moderator_id,
//            @Field("ticket_price") String ticket_price,
//            @Field("ticket_quantity") String ticket_quantity,
//            @Field("ticket_price_array") String[] ticket_price_array,
//            @Field("ticket_name_array") String[] ticket_name_array,
//            @Field("ticket_quantity_array") String[] ticket_quantity_array
//          @Part ("image\"; filename=\"myfile.jpg\" ") RequestBody file, @Part ("image")RequestBody image

            @Part MultipartBody.Part file,@Part ("filename") RequestBody name

            );

}
