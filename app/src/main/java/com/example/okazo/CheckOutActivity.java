package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.util.ConfirmationDialog;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_ID;
import static com.example.okazo.util.constants.KEY_TICKET_ID_ARRAY;
import static com.example.okazo.util.constants.KEY_TOTAL_AMOUNT;
import static com.example.okazo.util.constants.KEY_USER_ID;

public class CheckOutActivity extends AppCompatActivity implements ConfirmationDialog.orderConfirmationListener{

    private TextView textViewTotalAmount,textViewCardUserName,textViewCardTMoney;
    private Button buttonSubmit;
    private CardView cardViewFirst,cardViewSecond;
    private String paymentOption,eventId,userId,totalAmount,userName,token,ticketIdString=" ";
    private ArrayList<String>ticketId;
    private int userAmount;
    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        cardViewFirst=findViewById(R.id.check_out_activity_card_first);
        cardViewSecond=findViewById(R.id.check_out_activity_card_second);
        textViewTotalAmount=findViewById(R.id.check_out_activity_total_amount);

        textViewCardUserName=findViewById(R.id.credit_card_user_name);
        textViewCardTMoney=findViewById(R.id.credit_card_user_tmoney);
        buttonSubmit=findViewById(R.id.check_out_activity_submit);

        Bundle bundle=getIntent().getExtras();
        eventId=bundle.getString(KEY_EVENT_ID);
        userId=bundle.getString(KEY_USER_ID);
        totalAmount=bundle.getString(KEY_TOTAL_AMOUNT);
        ticketId=(ArrayList<String>) bundle.getSerializable(KEY_TICKET_ID_ARRAY);


     textViewTotalAmount.setText("Rs. "+totalAmount);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        //Toast.makeText(this, "a+  "+userId, Toast.LENGTH_SHORT).show();
        apiInterface.getPaymentInfo(userId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    userName=apiResponse.getUser().getName();
                    token=apiResponse.getUser().getToken();
                    userAmount=apiResponse.getUser().getAmount();
                    textViewCardUserName.setText(userName);
                    textViewCardTMoney.setText(String.valueOf(userAmount));
                    buttonSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(paymentOption!=null){
                                if(paymentOption.equals("first")) {
                                    if (Integer.valueOf(totalAmount) < userAmount) {
                                        ConfirmationDialog confirmationDialog=new ConfirmationDialog("Do you want to confirm?");
                                        confirmationDialog.show(getSupportFragmentManager(),"Ticket Confirmation");
                                    } else {
                                        textViewCardTMoney.setTextColor(getColor(R.color.red));
                                        DynamicToast.makeError(CheckOutActivity.this, "No sufficient amount").show();
                                    }
                                }else {

                                    ConfirmationDialog confirmationDialog=new ConfirmationDialog("Do you want to confirm?");
                                    confirmationDialog.show(getSupportFragmentManager(),"Ticket Confirmation");
                                }
                            }else {
                                DynamicToast.makeError(CheckOutActivity.this,"Please select payment option").show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(CheckOutActivity.this, apiResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(CheckOutActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });



     cardViewFirst.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             cardViewFirst.setCardBackgroundColor(getColor(R.color.colorPrimaryLight));
             cardViewSecond.setCardBackgroundColor(getColor(R.color.space_white));
             paymentOption="first";
         }
     });
     cardViewSecond.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             cardViewSecond.setCardBackgroundColor(getColor(R.color.colorPrimaryLight));
             cardViewFirst.setCardBackgroundColor(getColor(R.color.space_white));
             paymentOption="second";
         }
     });

    }

    @Override
    public void OnYesClicked() {
        for (String val:ticketId
             ) {
                    ticketIdString=ticketIdString+","+val;
        }
      
    }

    @Override
    public void OnNoClicked() {

    }
}
