package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.SwipeToDeleteCallBack;
import com.example.okazo.util.UserAllTicketAdapter;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_ID;
import static com.example.okazo.util.constants.KEY_EVENT_TITLE;
import static com.example.okazo.util.constants.KEY_TICKET_ADD;
import static com.example.okazo.util.constants.KEY_TICKET_ID_ARRAY;
import static com.example.okazo.util.constants.KEY_TICKET_SUB;
import static com.example.okazo.util.constants.KEY_TOTAL_AMOUNT;
import static com.example.okazo.util.constants.KEY_USER_ID;
import static com.example.okazo.util.constants.KEY_USER_ROLE;

public class BuyTicketActivity extends AppCompatActivity {
    private String eventId,userId,eventTitle;
    private TextView textViewAmount;
    private RecyclerView recyclerView;
    private Button buttonConfirm;
    private ArrayList<String> arrayListId=new ArrayList<>(),arrayListName=new ArrayList<>(),arrayListPrice=new ArrayList<>(),arrayListQuantity=new ArrayList<>();
    private UserAllTicketAdapter adapter;
    private ApiInterface apiInterface;
    private EventDetail eventDetail;
    private Integer totalAmount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);
        Bundle bundle=getIntent().getExtras();
        eventId=bundle.getString(KEY_EVENT_ID);
        userId=bundle.getString(KEY_USER_ID);
        eventTitle=bundle.getString(KEY_EVENT_TITLE);
        recyclerView=findViewById(R.id.buy_ticket_activity_recycerview);
        textViewAmount=findViewById(R.id.buy_ticket_activity_total_amount);
        buttonConfirm=findViewById(R.id.buy_ticket_activity_confirm);
        getSupportActionBar().setTitle(eventTitle.toUpperCase());
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);

        apiInterface.getUserAllTicket(userId,eventId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                     eventDetail=apiResponse.getEvent();
                     int count=eventDetail.getTicketName().size();
                     for(int i=0;i<count;i++){
                         arrayListId.add(eventDetail.getTicketId().get(i));
                         arrayListName.add(eventDetail.getTicketName().get(i));
                         arrayListPrice.add(eventDetail.getTicketPrice().get(i));
                         arrayListQuantity.add(eventDetail.getQuantity().get(i));
                     }

                     totalAmount();

                    adapter=new UserAllTicketAdapter(arrayListName,arrayListPrice,arrayListQuantity,arrayListId);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(BuyTicketActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);
                    enableSwipeToDeleteAndUndo();

                    //on add qty
                    adapter.setOnAddClickListener(new UserAllTicketAdapter.OnAddClickListener() {
                        @Override
                        public void onAddClick(int position) {
                            Integer currentQty=Integer.valueOf(arrayListQuantity.get(position));
                            String addQty= String.valueOf((currentQty+1));
                            apiInterface.updateQuantity(arrayListId.get(position),KEY_TICKET_ADD,addQty).enqueue(new Callback<APIResponse>() {
                                @Override
                                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                    APIResponse apiResponse1=response.body();
                                    if(!apiResponse.getError()){
                                        arrayListQuantity.add(position,addQty);
                                        ((TextView)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_user_all_ticket_quantity)).setText(addQty);
                                        totalAmount=0;
                                        totalAmount();
                                    }else {
                                        DynamicToast.makeError(BuyTicketActivity.this,apiResponse1.getErrorMsg()).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<APIResponse> call, Throwable t) {

                                }
                            });



                        }
                    });
                    //on sub qty
                    adapter.setOnSubClickListener(new UserAllTicketAdapter.OnSubClickListener() {
                        @Override
                        public void onSubClick(int position) {
                            Integer currentQty=Integer.valueOf(arrayListQuantity.get(position));
                            if(currentQty==1){

                            }else {
                                String subQty = String.valueOf((currentQty - 1));
                                apiInterface.updateQuantity(arrayListId.get(position),KEY_TICKET_SUB,subQty).enqueue(new Callback<APIResponse>() {
                                    @Override
                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                        APIResponse apiResponse1=response.body();
                                        if(!apiResponse.getError()){
                                            arrayListQuantity.add(position, subQty);
                                            ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_user_all_ticket_quantity)).setText(subQty);
                                            totalAmount=0;
                                            totalAmount();
                                        }else {
                                            DynamicToast.makeError(BuyTicketActivity.this,apiResponse1.getErrorMsg()).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {

                                    }
                                });

                            }
                        }
                    });
                }else {
                    if(apiResponse.getErrorMsg().equals("Event Blocked")){
                        DynamicToast.makeError(BuyTicketActivity.this,"This event has been removed").show();


                    }else {
                        DynamicToast.makeError(BuyTicketActivity.this,"Problem getting data").show();
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });

    }

    private void totalAmount() {

        for(int j=0;j<arrayListPrice.size();j++){
            Integer price=Integer.valueOf(arrayListPrice.get(j));
            Integer quantity=Integer.valueOf(arrayListQuantity.get(j));
            totalAmount=totalAmount+(price*quantity);
        }
        textViewAmount.setText("Rs. "+(totalAmount));
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(BuyTicketActivity.this,CheckOutActivity.class);
                intent.putExtra(KEY_EVENT_ID,eventId);
                intent.putExtra(KEY_USER_ID,userId);
                intent.putExtra(KEY_TOTAL_AMOUNT,String.valueOf(totalAmount));
                intent.putExtra(KEY_TICKET_ID_ARRAY,arrayListId);
                startActivity(intent);
            }
        });
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(BuyTicketActivity.this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
//                Toast.makeText(CartActivity.this, arrayListId.get(position), Toast.LENGTH_SHORT).show();
                apiInterface.removeTicket(userId,arrayListId.get(position)).enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        APIResponse apiResponse=response.body();
                        if(!apiResponse.getError()){
                            adapter.removeItem(position);
                          //  DynamicToast.makeSuccess(BuyTicketActivity.this,"Ticket is removed").show();
//                            arrayListId.remove(position);
//                           // Toast.makeText(BuyTicketActivity.this, position+"||"+arrayListName.size(), Toast.LENGTH_SHORT).show();
//                            arrayListQuantity.remove(position);
//                            arrayListPrice.remove(position);
//                            arrayListName.remove(position);
                            totalAmount=0;
                            totalAmount();
                        }else {
                            DynamicToast.makeError(BuyTicketActivity.this,apiResponse.getErrorMsg()).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {

                    }
                });


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}
