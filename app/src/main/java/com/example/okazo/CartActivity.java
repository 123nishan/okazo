package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.MyEventTicketAdapter;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;

public class CartActivity extends AppCompatActivity {
    private String userId;
    private RecyclerView recyclerView;
    private ApiInterface apiInterface;
    private MyEventTicketAdapter adapter;
    private ArrayList<String> arrayListTitle=new ArrayList<>(),arrayListImage=new ArrayList<>(),arrayListStartDate=new ArrayList<>(),arrayListStartTime=new ArrayList<>(),arrayListTotalTicket=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        recyclerView=findViewById(R.id.cart_activity_recyclerview);
        getSupportActionBar().setTitle("Event Tickets");
        SharedPreferences sharedPreferences = CartActivity.this.getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences.getString("user_id","")!=null  && !sharedPreferences.getString("user_id","").isEmpty()){
            userId=sharedPreferences.getString("user_id","");
            apiInterface.allEventTicket(userId).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){
                        EventDetail eventDetail=apiResponse.getEvent();
                        arrayListImage.add(eventDetail.getImage());
                        arrayListStartDate.add(eventDetail.getStartDate());
                        arrayListStartTime.add(eventDetail.getStartTime());
                        arrayListTitle.add(eventDetail.getTitle());
                        arrayListTotalTicket.add(eventDetail.getTicketCount());


                        adapter=new MyEventTicketAdapter(arrayListTitle,arrayListStartDate,arrayListStartTime,arrayListImage,arrayListTotalTicket,CartActivity.this);

                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(CartActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                        recyclerView.setLayoutManager(linearLayoutManager);

                        recyclerView.setAdapter(adapter);

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
}
