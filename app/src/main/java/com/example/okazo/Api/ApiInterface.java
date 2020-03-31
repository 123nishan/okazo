package com.example.okazo.Api;

import com.example.okazo.Model.EventDetail;
import com.example.okazo.Model.Note;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
            @Field("verified") String verified,
            @Field("time") Timestamp time

    );
    @FormUrlEncoded
    @POST("otpVerification.php")
    Call<APIResponse>  otpVerification(
        @Field("email") String email,
        @Field("verified") String verified
    );
}
