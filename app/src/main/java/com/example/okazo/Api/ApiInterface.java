package com.example.okazo.Api;

import com.example.okazo.Model.Note;

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


    @GET("notes.php")
    Call<List<Note>> getLocation();


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
            @Field("status") String status,
            @Field("time") String time

    );
}
