package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.FollowingListAdapter;
import com.example.okazo.util.ModeratorListAdapter;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_DETAIL;
import static com.example.okazo.util.constants.KEY_USER_ID;

public class FollowingEventActivity extends AppCompatActivity {
private RecyclerView recyclerViewFollowing;
private FollowingListAdapter adapter;
private ApiInterface apiInterface;
    private ArrayList<String> eventName=new ArrayList<>(),eventImage=new ArrayList<>(),eventId=new ArrayList<>();
    private int positionAdapter;
    private  String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_event);
        recyclerViewFollowing=findViewById(R.id.following_activity_recyclerview);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        Bundle bundle=getIntent().getExtras();
        user_id=bundle.getString(KEY_USER_ID);
        apiInterface.getFollowingList(user_id).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    ArrayList<EventDetail> eventDetails=apiResponse.getEventArray();
                    for (EventDetail val:eventDetails
                         ) {
                        eventName.add(val.getTitle());
                        eventId.add(val.getId());
                        eventImage.add(val.getImage());
                        Log.d("image",val.getImage());
                    }

                    adapter=new FollowingListAdapter(eventName,eventImage,getApplicationContext());
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewFollowing.setLayoutManager(linearLayoutManager);
                    recyclerViewFollowing.setAdapter(adapter);
                    adapter.setOnClickListener(new FollowingListAdapter.OnClickListener() {
                        @Override
                        public void onClick(int position) {
                                apiInterface.eventFollow(user_id,eventId.get(position),"unfollow").enqueue(new Callback<APIResponse>() {
                                    @Override
                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                        APIResponse apiResponse1=response.body();
                                        if(!apiResponse1.getError()){
                                           adapter.removeItem(position);
                                            eventId.remove(position);


                                        }else {
                                            DynamicToast.makeWarning(FollowingEventActivity.this,apiResponse1.getErrorMsg()).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {

                                    }
                                });
                        }
                    });
                    adapter.setOnCardClickListener(new FollowingListAdapter.OnCardClickListener() {
                        @Override
                        public void onCardClick(int position) {
                                String selectEventId=eventId.get(position);
                                apiInterface.primaryEventInfo(selectEventId,user_id).enqueue(new Callback<APIResponse>() {
                                    @Override
                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                        APIResponse apiResponse1=response.body();
                                        if(!apiResponse1.getError()){
                                            Intent intent=new Intent(FollowingEventActivity.this,EventActivity.class);
                                            intent.putExtra(KEY_EVENT_DETAIL,apiResponse1.getEvent());
                                            intent.putExtra(KEY_USER_ID,user_id);
                                            startActivity(intent);
                                        }else {
                                            DynamicToast.makeWarning(FollowingEventActivity.this,apiResponse1.getErrorMsg()).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {

                                    }
                                });
                        }
                    });

                }else {

                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });

    }
}
