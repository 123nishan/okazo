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

    @FormUrlEncoded
    @POST("moderator/check_moderator.php")
    Call<APIResponse> getModerator(
            @Field("user_id") String userId,
            @Field("event_id") String eventId
    );

    @GET("eventDetail/eventType.php")
    Call<ArrayList<EventDetail>> getEventType();

    @GET("geofence/activate_geofence.php")
    Call<ArrayList<Geofence>> getGeofenceStatus();

    @FormUrlEncoded
    @POST("map/event_location.php")
    Call<ArrayList<EventDetail>> getEventLocation(
            @Field("user_id") String userId,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("map/check_event_type.php")
    Call<APIResponse> checkEventType(

            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("moderator/leave_event.php")
    Call<APIResponse> leaveEvent(

            @Field("moderator_id") String moderatorId,
            @Field("event_id") String eventId,
            @Field("moderator_type") String moderatorType
    );

    @GET("map/event_type.php")
    Call<ArrayList<EventDetail>> getMapEventType();

    @FormUrlEncoded
    @POST("save.php")
    Call<Note> saveNote(
            @Field("latitude") Double latitude,
            @Field("longitude") Double longitude,
            @Field("name") String name
    );

    @FormUrlEncoded
    @POST("eventDetail/event_response_count.php")
    Call<APIResponse> getResponseCount(
            @Field("event_id") String eventId

    );

    @FormUrlEncoded
    @POST("eventDetail/all_comment.php")
    Call<APIResponse> allComment(

            @Field("post_id") String postId


    );

    @FormUrlEncoded
    @POST("user/comment.php")
    Call<APIResponse> addComment(
            @Field("user_id") String userId,
            @Field("post_id") String postId,
            @Field("comment") String comment

    );



    @FormUrlEncoded
    @POST("eventDetail/event_post.php")
    Call<APIResponse> getEventPost(
            @Field("event_id") String eventId,
            @Field("user_id") String userId

    );

    @FormUrlEncoded
    @POST("eventDetail/check_event_status.php")
    Call<APIResponse> getEventStatus(
            @Field("event_id") String eventId


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

    @FormUrlEncoded
    @POST("eventDetail/moderator.php")
    Call<APIResponse> checkModerator(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("eventDetail/check_event.php")
    Call<APIResponse> checkEvent(
            @Field("user_id") String userId
    );
@FormUrlEncoded
@POST("user/user_detail.php")
Call<APIResponse> getUserName(
  @Field("id") String userId,
  @Field("request") String request
);

@FormUrlEncoded
@POST("user/feed.php")
Call<APIResponse> getFeed(
        @Field("user_id") String userId
);

@FormUrlEncoded
@POST("user/event_response.php")
Call<APIResponse> setEventResponse(
        @Field("user_id") String userId,
        @Field("event_id") String eventId,
        @Field("response") String response
);


    @FormUrlEncoded
    @POST("user/event_info.php")
    Call<APIResponse> getFollowing(
            @Field("user_id") String userId,
            @Field("event_id") String eventId
    );

@FormUrlEncoded
@POST("user/like.php")
Call<APIResponse> setLike(
        @Field("user_id") String userId,
        @Field("post_id") String postId
);
    @FormUrlEncoded
    @POST("eventDetail/event_creation.php")
    Call<APIResponse> eventCreation(
            @Field("title") String title,

            @Field("description") String description,
            @Field("start_time") String start_time,
            @Field("end_time") String end_time,
            @Field("start_date") String start_date,
            @Field("end_date") String end_date,
            @Field("place") String place,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
//            @Field("image") String image,
            @Field("ticket_status") String ticket_status,
            @Field("page_status") String page_status,
            @Field("user_id") String user_id,
            @Field("ticket_category") String ticket_category,
            @Field("moderator_id") String moderator_id,
            @Field("ticket_price") String ticket_price,
            @Field("ticket_quantity") String ticket_quantity,
            @Field("ticket_price_array") String ticket_price_array,
            @Field("ticket_name_array") String ticket_name_array,
            @Field("ticket_quantity_array") String ticket_quantity_array,
            @Field("mod_status") String mod_status,
            @Field("tags") String allTag
            // mod status 1 is true  there is mod and 0 is false there is no mod

           // @Part MultipartBody.Part file,@Part ("file") RequestBody name



            );


    @Multipart
    @POST("eventDetail/event_profile_image.php")
    Call<APIResponse> uploadEventProfileImage(
            @Part MultipartBody.Part file,@Part ("file") RequestBody name
    );



}
