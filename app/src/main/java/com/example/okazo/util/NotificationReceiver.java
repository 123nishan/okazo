package com.example.okazo.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_ID;
import static com.example.okazo.util.constants.KEY_USER_ID;

public class NotificationReceiver extends BroadcastReceiver {
    private ApiInterface apiInterface;
    private String userId,eventId;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        Bundle bundle=intent.getExtras();
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        userId=bundle.getString(KEY_USER_ID);
        eventId=bundle.getString(KEY_EVENT_ID);
        apiInterface.assignRewardUser(userId,eventId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    DynamicToast.makeSuccess(context,"Reward Claimed").show();
                }else {
                    DynamicToast.makeError(context,"problem claiming").show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                DynamicToast.makeError(context,t.getLocalizedMessage()).show();
            }
        });

    }
}
