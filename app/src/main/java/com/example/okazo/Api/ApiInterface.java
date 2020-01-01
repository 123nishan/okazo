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
    @POST("save.php")
    Call<Note> saveNote(
            @Field("latitude") Double latitude,
            @Field("longitude") Double longitude,
            @Field("name") String name
    );

    @GET("notes.php")
     Call<List<Note>> getLocation();

}
