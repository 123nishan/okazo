package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.FeedAdapter;
import com.example.okazo.util.TicketAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_ID;
import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;

public class TicketActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ApiInterface apiInterface;
    private TicketAdapter ticketAdapter;
    private String eventId,title,startDate,startTime,userId;
    private EventDetail eventDetail;
    private ArrayList<String> arrayListPrice=new ArrayList<>(),arrayListName=new ArrayList<>(),arrayListTicketId=new ArrayList<>();
    private TextView textViewStartDate,textViewStartTime;
    private Button buttonProceedPayment;
    private View layoutBottomSheeet;
    private BottomSheetBehavior sheetBehavior;
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
        buttonProceedPayment=findViewById(R.id.buy_bottom_sheet_proceed);
        layoutBottomSheeet=findViewById(R.id.buy_bottom_sheet_layout);
        layoutBottomSheeet.setVisibility(View.GONE);
        //TODO remove bottom sheet


        SharedPreferences sharedPreferences = TicketActivity.this.getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences.getString("user_id","")!=null  && !sharedPreferences.getString("user_id","").isEmpty()){
            userId=sharedPreferences.getString("user_id","");
        }else {
            DynamicToast.makeError(TicketActivity.this,"Something went wrong").show();
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.remove("user_email");
            editor.remove("user_id");
            editor.commit();
            Intent intent=new Intent(TicketActivity.this,LoginActivity.class);
            startActivity(intent);
        }


        apiInterface.getAllTicket(eventId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){

                        eventDetail=apiResponse.getEvent();
                    arrayListPrice=eventDetail.getTicketPrice();
                    arrayListName=eventDetail.getTicketName();
                    arrayListTicketId=eventDetail.getTicketId();
                    title=eventDetail.getTitle();
                    startDate=eventDetail.getStartDate();
                    startTime=eventDetail.getStartTime();
                    getSupportActionBar().setTitle(title);
                    textViewStartDate.setText("Event Date: "+startDate);
                    if(startTime.length()==4){
                        startTime=startTime+"0";
                    }
                    textViewStartTime.setText("Event Time: "+startTime);
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
                            String quantity= (((TextView)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_ticket_quantity)).getText().toString());
                            String ticketId=arrayListTicketId.get(position);
                            apiInterface.addToCart(userId,ticketId,quantity).enqueue(new Callback<APIResponse>() {
                                @Override
                                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                    APIResponse apiResponse1=response.body();
                                    if(!apiResponse1.getError()){
                                        DynamicToast.makeSuccess(TicketActivity.this,"Ticket Added to Cart").show();
                                    }else {
                                        DynamicToast.makeError(TicketActivity.this,"Please try later").show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<APIResponse> call, Throwable t) {

                                }
                            });
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
                Intent intent=new Intent(TicketActivity.this,CartActivity.class);
                startActivity(intent);
                // Toast.makeText(this, "CART", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
