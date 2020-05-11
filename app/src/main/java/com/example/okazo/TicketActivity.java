package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.FeedAdapter;
import com.example.okazo.util.TicketAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_ID;

public class TicketActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ApiInterface apiInterface;
    private TicketAdapter ticketAdapter;
    private String eventId,title,startDate,startTime;
    private EventDetail eventDetail;
    private ArrayList<String> arrayListPrice=new ArrayList<>(),arrayListName=new ArrayList<>();
    private TextView textViewStartDate,textViewStartTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /// ticket activity to buy ticket or explore that event ticket
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        Bundle bundle=getIntent().getExtras();
        eventId=bundle.getString(KEY_EVENT_ID);
        recyclerView=findViewById(R.id.ticket_activity_recycler_view);
        textViewStartDate=findViewById(R.id.ticket_activity_start_date);
        textViewStartTime=findViewById(R.id.ticket_activity_start_time);

        apiInterface.getAllTicket(eventId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){

                        eventDetail=apiResponse.getEvent();
                    arrayListPrice=eventDetail.getTicketPrice();
                    arrayListName=eventDetail.getTicketName();
                    title=eventDetail.getTitle();
                    startDate=eventDetail.getStartDate();
                    startTime=eventDetail.getStartTime();
                    getSupportActionBar().setTitle(title);
                    textViewStartDate.setText("Date: "+startDate);
                    if(startTime.length()==4){
                        startTime=startTime+"0";
                    }
                    textViewStartTime.setText("Time: "+startTime);
                    ticketAdapter=new TicketAdapter(arrayListName,arrayListPrice);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(TicketActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(ticketAdapter);

                    //add quantity
                    ticketAdapter.setOnAddQuantityListener(new TicketAdapter.OnAddQuantityListener() {
                        @Override
                        public void onAddQuantity(int position) {
                       Integer quantity= Integer.valueOf(((TextView)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_ticket_quantity)).getText().toString());
                        quantity=quantity+1;
                            ((TextView)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_ticket_quantity)).setText(String.valueOf(quantity));
                        }
                    });
                    //sub quanity
                    ticketAdapter.setOnSubQuantityListener(new TicketAdapter.OnSubQuantityListener() {
                        @Override
                        public void onSubQuantity(int position) {
                            Integer quantity= Integer.valueOf(((TextView)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_ticket_quantity)).getText().toString());
                           if(quantity==1){

                           }else {
                               quantity-=1;
                               ((TextView)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_ticket_quantity)).setText(String.valueOf(quantity));
                           }
                        }
                    });
                    //add cart
                    ticketAdapter.setOnAddToCartListener(new TicketAdapter.OnAddToCartListener() {
                        @Override
                        public void onAddToCart(int position) {
                            LinearLayout linearLayout=((LinearLayout)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_ticket_add_cart_layout));


                        }
                    });

                }else {
                    DynamicToast.makeError(TicketActivity.this,apiResponse.getErrorMsg()).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });




    }
}
