package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.MyEventTicketAdapter;
import com.example.okazo.util.SwipeToDeleteCallBack;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_ID;
import static com.example.okazo.util.constants.KEY_EVENT_TITLE;
import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;
import static com.example.okazo.util.constants.KEY_USER_ID;

public class CartActivity extends AppCompatActivity {
    private String userId;
    private RecyclerView recyclerView;
    private ApiInterface apiInterface;
    private MyEventTicketAdapter adapter;
    private TextView textViewError;
    private ArrayList<String> arrayListTitle=new ArrayList<>(),arrayListImage=new ArrayList<>(),
            arrayListStartDate=new ArrayList<>(),arrayListStartTime=new ArrayList<>(),arrayListTotalTicket=new ArrayList<>(),arrayListId=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        recyclerView=findViewById(R.id.cart_activity_recyclerview);
        getSupportActionBar().setTitle("Event Tickets");
        textViewError=findViewById(R.id.cart_activity_recyclerview_error);
        SharedPreferences sharedPreferences = CartActivity.this.getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences.getString("user_id","")!=null  && !sharedPreferences.getString("user_id","").isEmpty()){
            userId=sharedPreferences.getString("user_id","");
            apiInterface.allEventTicket(userId).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){
                        EventDetail eventDetail=apiResponse.getEvent();

                        if(Integer.valueOf(eventDetail.getTicketCount())>0){
                            arrayListImage.add(eventDetail.getImage());
                            arrayListStartDate.add(eventDetail.getStartDate());
                            arrayListStartTime.add(eventDetail.getStartTime());
                            arrayListTitle.add(eventDetail.getTitle());
                            arrayListTotalTicket.add(eventDetail.getTicketCount());
                            arrayListId.add(eventDetail.getId());
                            adapter=new MyEventTicketAdapter(arrayListTitle,arrayListStartDate,arrayListStartTime,arrayListImage,arrayListTotalTicket,CartActivity.this);

                            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(CartActivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                            recyclerView.setLayoutManager(linearLayoutManager);

                            recyclerView.setAdapter(adapter);
                            enableSwipeToDeleteAndUndo();

                            adapter.setOnCardClickListener(new MyEventTicketAdapter.OnCardClickListener() {
                                @Override
                                public void onCardClick(int position) {
                                    String eventId=arrayListId.get(position);
                                    Intent intent=new Intent(CartActivity.this,BuyTicketActivity.class);
                                    intent.putExtra(KEY_EVENT_TITLE,arrayListTitle.get(position));
                                    intent.putExtra(KEY_EVENT_ID,eventId);
                                    intent.putExtra(KEY_USER_ID,userId);
                                    startActivity(intent);

                                }
                            });

                        }else {
                            textViewError.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }



                    }else {
                        DynamicToast.makeError(CartActivity.this,"Problem loading data").show();
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });
        }else{
            DynamicToast.makeError(CartActivity.this,"Something went wrong").show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("user_email","");
//            editor.putString("user_id","");
            editor.remove("user_email");
            editor.remove("user_id");
            editor.commit();
            Intent intent=new Intent(CartActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(CartActivity.this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
//                Toast.makeText(CartActivity.this, arrayListId.get(position), Toast.LENGTH_SHORT).show();
                apiInterface.removeEventTickets(arrayListId.get(position)).enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        APIResponse apiResponse=response.body();
                        if(!apiResponse.getError()){
                            adapter.removeItem(position);
                            DynamicToast.makeSuccess(CartActivity.this,"All ticket removed of "+arrayListTitle.get(position)).show();
                        }else {
                            DynamicToast.makeError(CartActivity.this,apiResponse.getErrorMsg()).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {

                    }
                });
                // for undo
//                // final String item = adapter.getData().get(position);
//
//


//                Snackbar snackbar = Snackbar
//                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        adapter.restoreItem(item, position);
//                        recyclerView.scrollToPosition(position);
//                    }
//                });
//
//                snackbar.setActionTextColor(Color.YELLOW);
//                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}
