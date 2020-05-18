package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.Model.User;
import com.example.okazo.util.MyEventTicketAdapter;
import com.example.okazo.util.MyTicketAdapter;
import com.example.okazo.util.PostCommentAdapter;
import com.example.okazo.util.SwipeToDeleteCallBack;
import com.example.okazo.util.TicketQrAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_TITLE;
import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;
import static com.example.okazo.util.constants.KEY_TICKET_ID_ARRAY;
import static com.example.okazo.util.constants.KEY_TICKET_NAME;
import static com.example.okazo.util.constants.KEY_TICKET_QUANTITY;

public class MyTicketActivity extends AppCompatActivity {
private String userId,boughtCount;
private RecyclerView recyclerView;
private ApiInterface apiInterface;
private MyEventTicketAdapter adapter;
private TextView textViewError;
private ArrayList<String> arrayListId=new ArrayList<>(),arrayListName=new ArrayList<>(),arrayListQuantity=new ArrayList<>(),arrayListAmount=new ArrayList<>(),
        arrayListPaymentOption=new ArrayList<>(),arrayListEventName=new ArrayList<>(),arrayListEventStartDate=new ArrayList<>(),arrayListEventStartTime=new ArrayList<>();
private ArrayList<String> arrayListBoughtDate=new ArrayList<>();
private ArrayList<String> finalId=new ArrayList<>(),finaName=new ArrayList<>(),finalQuantity=new ArrayList<>(),
        finalAmount=new ArrayList<>(),finalPaymentOption=new ArrayList<>(),finalBoughtDate=new ArrayList<>(),
    finalEventName=new ArrayList<>(),finalEventStartDate=new ArrayList<>(),finalEventStartTime=new ArrayList<>();
private MyTicketAdapter myTicketAdapter;
private User user;
private String tempId="",tempName="",tempQuantity="",tempAmount="",tempDate="",tempPaymentOption="",tempEventName="",tempEventStartDate="",tempEventStartTime="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        recyclerView=findViewById(R.id.my_ticket_activity_recycler_view);
        textViewError=findViewById(R.id.my_ticket_activity_recycler_error);
        getSupportActionBar().setTitle("Tickets");
        SharedPreferences sharedPreferences = MyTicketActivity.this.getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences.getString("user_id","")!=null  && !sharedPreferences.getString("user_id","").isEmpty()) {
            userId = sharedPreferences.getString("user_id", "");
            apiInterface.boughtTicket(userId).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){
                        user=apiResponse.getUser();
                        boughtCount=apiResponse.getUser().getBoughtCount();

                        if(Integer.valueOf(boughtCount)==0){
                            textViewError.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            textViewError.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            arrayListId=user.getTicketId();
                            arrayListName=user.getTicketName();
                            arrayListQuantity=user.getTicketQuantity();
                            arrayListAmount=user.getTicketAmount();
                            arrayListBoughtDate=user.getBoughtDate();
                            arrayListPaymentOption=user.getPaymentOption();
                            arrayListEventName=user.getEventName();
                            arrayListEventStartDate=user.getEventStartDate();
                            arrayListEventStartTime=user.getEventStartTime();


                            for(int i=0;i<arrayListId.size();i++){
                                    tempId=tempId+","+arrayListId.get(i);
                                    tempName=tempName+","+arrayListName.get(i);
                                    tempAmount=tempAmount+","+arrayListAmount.get(i);
                                    tempDate=tempDate+","+arrayListBoughtDate.get(i);
                                    tempPaymentOption=tempPaymentOption+","+arrayListPaymentOption.get(i);
                                    tempQuantity=tempQuantity+","+arrayListQuantity.get(i);
                                    tempEventName=tempEventName+","+arrayListEventName.get(i);
                                    tempEventStartDate=tempEventStartDate+","+arrayListEventStartDate.get(i);
                                    tempEventStartTime=tempEventStartTime+","+arrayListEventStartTime.get(i);



                              //
                            }
                            finalId=new ArrayList<>(Arrays.asList(tempId.split(",")));
                            finaName=new ArrayList<>(Arrays.asList(tempName.split(",")));
                            finalAmount=new ArrayList<>(Arrays.asList(tempAmount.split(",")));
                            finalBoughtDate=new ArrayList<>(Arrays.asList(tempDate.split(",")));
                            finalPaymentOption=new ArrayList<>(Arrays.asList(tempPaymentOption.split(",")));
                            finalQuantity=new ArrayList<>(Arrays.asList(tempQuantity.split(",")));

                            finalEventName=new ArrayList<>(Arrays.asList(tempEventName.split(",")));
                            finalEventStartDate=new ArrayList<>(Arrays.asList(tempEventStartDate.split(",")));
                            finalEventStartTime=new ArrayList<>(Arrays.asList(tempEventStartTime.split(",")));


                            finalId.removeAll(Collections.singleton(" "));
                            finalId.removeAll(Collections.singleton(""));
                            finaName.removeAll(Collections.singleton(" "));
                            finaName.removeAll(Collections.singleton(""));
                            finalAmount.removeAll(Collections.singleton(" "));
                            finalAmount.removeAll(Collections.singleton(""));
                            finalQuantity.removeAll(Collections.singleton(" "));
                            finalQuantity.removeAll(Collections.singleton(""));
                            finalBoughtDate.removeAll(Collections.singleton(" "));
                            finalBoughtDate.removeAll(Collections.singleton(""));
                            finalPaymentOption.removeAll(Collections.singleton(" "));
                            finalPaymentOption.removeAll(Collections.singleton(""));

                            finalEventName.removeAll(Collections.singleton(" "));
                            finalEventName.removeAll(Collections.singleton(""));

                            finalEventStartTime.removeAll(Collections.singleton(""));
                            finalEventStartTime.removeAll(Collections.singleton(" "));

                            finalEventStartDate.removeAll(Collections.singleton(" "));
                            finalEventStartDate.removeAll(Collections.singleton(""));

                            for (String val:finalEventName
                                 ) {
                                Log.d("EVENT",val);
                            }
                        myTicketAdapter=new MyTicketAdapter(finaName,finalQuantity,finalEventName,finalEventStartDate,finalEventStartTime);
                            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MyTicketActivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(myTicketAdapter);
                          myTicketAdapter.setOnClickListener(new MyTicketAdapter.OnClickListener() {
                              @Override
                              public void onClick(int position) {


                                  ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                  NetworkInfo nInfo = cm.getActiveNetworkInfo();
                                  boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                                  if(connected){
                                      Intent intent=new Intent(MyTicketActivity.this, TicketQrActivity.class);
                                      intent.putExtra(KEY_EVENT_TITLE,finalEventName.get(position));
                                      intent.putExtra(KEY_TICKET_ID_ARRAY,finalId.get(position));
                                      intent.putExtra(KEY_TICKET_NAME,finaName.get(position));
                                      intent.putExtra(KEY_TICKET_QUANTITY,finalQuantity.get(position));
                                      startActivity(intent);
                                  }else {
                                      DynamicToast.makeError(MyTicketActivity.this,"No internet connection").show();
                                  }



                              }
                          });

                           // Log.d("TEST",tempId);


                        }
                       // Log.d("TEST",apiResponse.getUser().getBoughtDate().get(0)+" ");

                    }else {
                        textViewError.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                       // DynamicToast.makeError(MyTicketActivity.this,"HERER").show();
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    DynamicToast.makeError(MyTicketActivity.this,t.getLocalizedMessage()).show();
                }
            });
        }else {
            DynamicToast.makeError(MyTicketActivity.this,"Something went wrong").show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("user_email","");
//            editor.putString("user_id","");
            editor.remove("user_email");
            editor.remove("user_id");
            editor.commit();
            Intent intent=new Intent(MyTicketActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.my_ticket_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart:
                Intent intent=new Intent(MyTicketActivity.this,CartActivity.class);
                startActivity(intent);
               // Toast.makeText(this, "CART", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
