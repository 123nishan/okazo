package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.Model.Moderator;
import com.example.okazo.Model.Note;
import com.example.okazo.util.ConfirmationDialog;
import com.example.okazo.util.ModeratorListAdapter;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_DETAIL;
import static com.example.okazo.util.constants.KEY_USER_ID;

public class ModeratorListActivity extends AppCompatActivity implements ConfirmationDialog.orderConfirmationListener {
    private String userId;
    private RecyclerView recyclerView;
    private ModeratorListAdapter adapter;
    private ApiInterface apiInterface;
    private ArrayList<String> eventName=new ArrayList<>(),eventImage=new ArrayList<>(),eventId=new ArrayList<>();
    private int positionAdapter;
    private String moderatorId,moderatorType,moderatorStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator_list);
        Bundle bundle=getIntent().getExtras();
        userId=bundle.getString(KEY_USER_ID);
        recyclerView=findViewById(R.id.moderator_list_activity_recycler_view);

        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface.getModeratorListUser(userId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                        ArrayList<EventDetail> eventDetails=apiResponse.getEventArray();
                    for (EventDetail val:eventDetails
                         ) {
                        eventId.add(val.getId());
                        eventImage.add(val.getImage());
                        eventName.add(val.getTitle());
                    }
                    adapter=new ModeratorListAdapter(eventName,eventImage,getApplicationContext());
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnCardClickListener(new ModeratorListAdapter.OnCardClickListener() {
                        @Override
                        public void onCardClick(int position) {
                            String selectEventId=eventId.get(position);
                            apiInterface.primaryEventInfo(selectEventId,userId).enqueue(new Callback<APIResponse>() {
                                @Override
                                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                    APIResponse apiResponse1=response.body();
                                    if(!apiResponse1.getError()){
                                        Intent intent=new Intent(ModeratorListActivity.this,EventActivity.class);
                                        intent.putExtra(KEY_EVENT_DETAIL,apiResponse1.getEvent());
                                        intent.putExtra(KEY_USER_ID,userId);
                                        startActivity(intent);
                                    }else {
                                        DynamicToast.makeWarning(ModeratorListActivity.this,apiResponse1.getErrorMsg()).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<APIResponse> call, Throwable t) {

                                }
                            });
                        }
                    });
                    adapter.setOnClickListener(new ModeratorListAdapter.OnClickListener() {
                        @Override
                        public void onClick(int position) {
                            positionAdapter=position;
                            ConfirmationDialog confirmationDialog=new ConfirmationDialog("Do you want to leave this event?");
                            confirmationDialog.show(getSupportFragmentManager(),"Confirmation");
                        }
                    });
                }else {
                    DynamicToast.makeError(getApplicationContext(),apiResponse.getErrorMsg()).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });


    }

    @Override
    public void OnYesClicked() {
        apiInterface.getModerator(userId,eventId.get(positionAdapter)).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    Moderator moderator=apiResponse.getModerator();
                    moderatorId=moderator.getId();
                    moderatorType=moderator.getRole();
                    moderatorStatus=moderator.getStatus();
                    apiInterface.leaveEvent(moderatorId,eventId.get(positionAdapter),moderatorType).enqueue(new Callback<APIResponse>() {
                        @Override
                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                            APIResponse apiResponse1=response.body();
                            if(!apiResponse1.getError()){
                                    adapter.removeItem(positionAdapter);
                                    eventId.remove(positionAdapter);
                                    DynamicToast.makeSuccess(ModeratorListActivity.this,"Left the event").show();
                            }else {
                                DynamicToast.makeError(getApplicationContext(),apiResponse1.getErrorMsg()).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<APIResponse> call, Throwable t) {

                        }
                    });
                }else {
                    DynamicToast.makeError(ModeratorListActivity.this,apiResponse.getErrorMsg()).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void OnNoClicked() {

    }
}
