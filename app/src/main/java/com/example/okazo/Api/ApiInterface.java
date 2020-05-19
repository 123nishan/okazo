package com.example.okazo.Api;

import android.text.Editable;

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
            @Field("phone") String phone,
            @Field("token") String token

    );

    @FormUrlEncoded
    @POST("chat/check_inbox.php")
    Call<APIResponse> checkInbox(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("eventDetail/all_moderator.php")
    Call<APIResponse> getAllModerator(
            @Field("event_id") String event_id,
            @Field("mod_type") String modType
    );

    @FormUrlEncoded
    @POST("moderator/all_user.php")
    Call<APIResponse> getAllUser(
            @Field("search") Editable search,
            @Field("event_id") String eventId
    );

    @FormUrlEncoded
    @POST("eventDetail/all_detail.php")
    Call<APIResponse> getEventAllDetail(

            @Field("event_id") String eventId
    );

    @FormUrlEncoded
    @POST("moderator/remove_moderator.php")
    Call<APIResponse> removeModerator(
            @Field("event_id") String eventId,
            @Field("moderator_id") String moderatorId
    );

    @FormUrlEncoded
    @POST("user/current_user_info.php")
    Call<APIResponse> currentUserInfo(
            @Field("user_id") String userId

    );

    @FormUrlEncoded
    @POST("fcm.php")
    Call<APIResponse> sendInboxNotification(
            @Field("message") String message,
            @Field("send_to") String id,
            @Field("send_from") String sendfrom
    );

    @FormUrlEncoded
    @POST("moderator/add_moderator.php")
    Call<APIResponse> requestModerator(
            @Field("user_id") String userId,
            @Field("event_id") String eventId,
            @Field("role") String role
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
    @POST("eventDetail/ticket.php")
    Call<APIResponse> getAllTicket(
            @Field("event_id") String eventId

    );


    @FormUrlEncoded
    @POST("user/profile_info.php")
    Call<APIResponse> getProfileInfo(
            @Field("user_id") String userId

    );

    @FormUrlEncoded
    @POST("user/update_image.php")
    Call<APIResponse> updateUserImage(
            @Field("user_id") String userId,
            @Field("file_name") String fileName

    );

    @FormUrlEncoded
    @POST("user/primary_event_info.php")
    Call<APIResponse> primaryEventInfo(
            @Field("event_id") String eventId,
            @Field("user_id") String userId

    );

    @FormUrlEncoded
    @POST("map/check_event_type.php")
    Call<APIResponse> checkEventType(

            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("ticket/add_to_cart.php")
    Call<APIResponse> addToCart(

            @Field("user_id") String userId,
            @Field("ticket_id") String ticketId,
            @Field("quantity") String quantity
    );

    @FormUrlEncoded
    @POST("ticket/all_event_ticket.php")
    Call<APIResponse> allEventTicket(

            @Field("user_id") String userId

    );

    @FormUrlEncoded
    @POST("ticket/remove_event_ticket.php")
    Call<APIResponse> removeEventTickets(

            @Field("event_id") String eventId

    );

    @FormUrlEncoded
    @POST("chat/chat_room.php")
    Call<APIResponse> getChatRoom(

            @Field("receiver_id") String receiverId
    );

    @FormUrlEncoded
    @POST("chat/message.php")
    Call<APIResponse> getMessage(

            @Field("receiver_id") String receiverId,
            @Field("sender_id") String senderId
    );

    @FormUrlEncoded
    @POST("user/payment_info.php")
    Call<APIResponse> getPaymentInfo(

            @Field("user_id") String userId

    );

    @FormUrlEncoded
    @POST("ticket/all_user_ticket.php")
    Call<APIResponse> getUserAllTicket(

            @Field("user_id") String userId,
            @Field("event_id") String eventId
    );

    @FormUrlEncoded
    @POST("user/buy_ticket.php")
    Call<APIResponse> buyTicket(
            @Field("amount") String amount,
            @Field("user_id") String userId,
            @Field("ticket_id") String ticketIdString,
            @Field("option") String paymentOption,
            @Field("t_money") String tMoney,
            @Field("quantity") String ticketQuantityString,
            @Field("per_amount") String ticketPriceString,
            @Field("ticket_name") String ticketNameString,
            @Field("event_id") String eventId
    );


    @FormUrlEncoded
    @POST("ticket/update_quantity.php")
    Call<APIResponse> updateQuantity(

            @Field("ticket_id") String ticketId,
            @Field("type") String type,
            @Field("quantity") String quantity
    );

    @FormUrlEncoded
    @POST("ticket/remove_ticket.php")
    Call<APIResponse> removeTicket(
            @Field("user_id") String userId,
            @Field("ticket_id") String ticketId


    );

    @FormUrlEncoded
    @POST("chat/send_message.php")
    Call<APIResponse> sendMessgae(

            @Field("receiver_id") String receiverId,
            @Field("sender_id") String senderId,
            @Field("message") String message
    );

    @FormUrlEncoded
    @POST("eventDetail/update_detail.php")
    Call<APIResponse> updateEventDetail(

            @Field("title") String title,
            @Field("description") String description,
            @Field("start_time") String startTime,
            @Field("end_time") String endTime,
            @Field("start_date") String startDate,
            @Field("end_date") String endDate,
            @Field("place") String place,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("ticket_status") String ticketStatus,
            @Field("page_status") String pageStatus,
            @Field("event_id") String eventId,
            @Field("ticket_price_array") String ticketPrice,
            @Field("ticket_name_array") String ticketName,
            @Field("ticket_quantity_array") String ticketQuantity
    );

    @FormUrlEncoded
    @POST("chat/make_seen.php")
    Call<APIResponse> makeSeen(

            @Field("receiver_id") String receiverId,
            @Field("sender_id") String senderId

    );

    @FormUrlEncoded
    @POST("eventDetail/close_event.php")
    Call<APIResponse> closeEvent(

            @Field("event_id") String eventId


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
    @POST("user/list_moderator.php")
    Call<APIResponse> getModeratorListUser(
            @Field("user_id") String userId

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
    @POST("eventDetail/create_post.php")
    Call<APIResponse> createPost(
            @Field("event_id") String eventId,
            @Field("user_id") String user_id,
            @Field("detail") String detail,
            @Field("image") String image


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
    @POST("user/moderator_request.php")
    Call<APIResponse> getRequestNotification(
            @Field("user_id") String userId

    );

    @FormUrlEncoded
    @POST("user/moderator_response.php")
    Call<APIResponse> moderatorResponse(
            @Field("user_id") String userId,
            @Field("type") String type,
            @Field("event_id") String eventId

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
    @POST("ticket/bought_user_ticket.php")
    Call<APIResponse> boughtTicket(
            @Field("user_id") String userId

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

    @Multipart
    @POST("eventDetail/event_profile_image.php")
    Call<APIResponse> uploadPostImage(
            @Part MultipartBody.Part file,@Part ("file") RequestBody name
    );

}
