package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.ModeratorListAdapter;
import com.example.okazo.util.NotificationAdapter;
import com.example.okazo.util.SwipeToDeleteCallBack;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_USER_ID;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private ApiInterface apiInterface;
    private String userId;
    private TextView textViewError;
    private ArrayList<String> eventTitle=new ArrayList<>(),eventId=new ArrayList<>(),eventRoleName=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView=findViewById(R.id.notification_activity_recycler_view);
        textViewError=findViewById(R.id.notification_activity_error);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        Bundle bundle=getIntent().getExtras();
        userId=bundle.getString(KEY_USER_ID);

        apiInterface.getRequestNotification(userId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    textViewError.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    ArrayList<EventDetail> eventDetails=apiResponse.getEventArray();
                    for (EventDetail val:eventDetails
                         ) {
                        eventId.add(val.getId());
                        eventTitle.add(val.getTitle());
                        eventRoleName.add(val.getRoleName());
                    }
                    adapter=new NotificationAdapter(eventTitle,eventRoleName);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnClickListener(new NotificationAdapter.OnClickListener() {
                        @Override
                        public void onClick(int position) {
                            Log.d("TEST",userId+"||"+eventId.get(position));
                            apiInterface.moderatorResponse(userId,"accept",eventId.get(position)).enqueue(new Callback<APIResponse>() {
                                @Override
                                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                    APIResponse apiResponse1=response.body();
                                    if(!apiResponse1.getError()){
                                            DynamicToast.makeSuccess(getApplicationContext(),"Successfully accepted request").show();
                                            adapter.removeItem(position);
                                            adapter.notifyDataSetChanged();
                                    }else {
                                        DynamicToast.makeError(getApplicationContext(),apiResponse1.getErrorMsg()).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<APIResponse> call, Throwable t) {
                                    DynamicToast.makeError(getApplicationContext(),t.getLocalizedMessage()).show();
                                }
                            });
                        }
                    });
                    enableSwipeToDeleteAndUndo();
                }else {

                    textViewError.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });

    }
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(NotificationActivity.this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
//                Toast.makeText(CartActivity.this, arrayListId.get(position), Toast.LENGTH_SHORT).show();
                apiInterface.moderatorResponse(userId,"reject",eventId.get(position)).enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        APIResponse apiResponse=response.body();
                        if(!apiResponse.getError()){
                                DynamicToast.makeError(getApplicationContext(),"Rejected request").show();
                                adapter.removeItem(position);

                        }else {
                            DynamicToast.makeError(getApplicationContext() ,apiResponse.getErrorMsg()).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {

                    }
                });
//                apiInterface.removeTicket(userId,arrayListId.get(position)).enqueue(new Callback<APIResponse>() {
//                    @Override
//                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//                        APIResponse apiResponse=response.body();
//                        if(!apiResponse.getError()){
   //                        adapter.removeItem(position);
//                            //  DynamicToast.makeSuccess(BuyTicketActivity.this,"Ticket is removed").show();
////                            arrayListId.remove(position);
////                           // Toast.makeText(BuyTicketActivity.this, position+"||"+arrayListName.size(), Toast.LENGTH_SHORT).show();
////                            arrayListQuantity.remove(position);
////                            arrayListPrice.remove(position);
////                            arrayListName.remove(position);
//
//                        }else {
//                            DynamicToast.makeError(NotificationActivity.this,apiResponse.getErrorMsg()).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<APIResponse> call, Throwable t) {
//
//                    }
//                });


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}
