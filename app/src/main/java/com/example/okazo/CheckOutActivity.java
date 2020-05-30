package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_ID;
import static com.example.okazo.util.constants.KEY_TICKET_ID_ARRAY;
import static com.example.okazo.util.constants.KEY_TICKET_NAME;
import static com.example.okazo.util.constants.KEY_TICKET_PRICE;
import static com.example.okazo.util.constants.KEY_TICKET_QUANTITY;
import static com.example.okazo.util.constants.KEY_TOTAL_AMOUNT;
import static com.example.okazo.util.constants.KEY_USER_ID;

public class CheckOutActivity extends AppCompatActivity implements ConfirmationDialog.orderConfirmationListener{

    private TextView textViewTotalAmount,textViewCardUserName,textViewCardTMoney;
    private Button buttonSubmit;
    private CardView cardViewFirst,cardViewSecond;
    private String paymentOption,eventId,userId,totalAmount,userName,token,ticketIdString=" ",ticketNameString=" ",ticketPriceString=" ",ticketQuantityString=" ";
    private ArrayList<String>ticketId,ticketName,ticketPrice,ticketQuantity;
    private int userAmount=0;
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
        ticketName= (ArrayList<String>) bundle.getSerializable(KEY_TICKET_NAME);
        ticketPrice= (ArrayList<String>) bundle.getSerializable(KEY_TICKET_PRICE);
        ticketQuantity= (ArrayList<String>) bundle.getSerializable(KEY_TICKET_QUANTITY);


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
                                if(paymentOption.equals("t money")) {
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
             paymentOption="t money";
         }
     });
     cardViewSecond.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             cardViewSecond.setCardBackgroundColor(getColor(R.color.colorPrimaryLight));
             cardViewFirst.setCardBackgroundColor(getColor(R.color.space_white));
             paymentOption="cash";
         }
     });

    }

    @Override
    public void OnYesClicked() {
        ticketIdString=" ";ticketNameString=" ";ticketPriceString=" ";ticketQuantityString=" ";
     for(int i=0;i<ticketId.size();i++)
              {
                    ticketIdString=ticketIdString+","+ticketId.get(i);
                    ticketQuantityString=ticketQuantityString+","+ticketQuantity.get(i);
                    ticketNameString=ticketNameString+","+ticketName.get(i);
                    ticketPriceString=ticketPriceString+","+ticketPrice.get(i);

        }

     //   Log.d("TESTCHECK",totalAmount+"||"+userId+"||"+ticketPriceString+"||"+paymentOption+"||"+userAmount+"||"+ticketQuantityString+"||"+ticketPriceString+"||"+ticketNameString+" ||"+ticketIdString);
     String tMoney=String.valueOf(userAmount);
     apiInterface.buyTicket(totalAmount,userId,ticketIdString,paymentOption,tMoney,ticketQuantityString,ticketPriceString,ticketNameString,eventId).enqueue(new Callback<APIResponse>() {
         @Override
         public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
             APIResponse apiResponse=response.body();
             if(!apiResponse.getError()){
                 if(paymentOption.equals("cash")){
                     sendNotification(paymentOption);
                 }else {
                     sendNotification(paymentOption);
                 }

                 DynamicToast.makeSuccess(CheckOutActivity.this,"ticket purchased").show();
                 Intent intent=new Intent(CheckOutActivity.this,MainActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                 startActivity(intent);
             }else {
                 DynamicToast.makeError(CheckOutActivity.this,apiResponse.getErrorMsg()).show();
             }
         }

         @Override
         public void onFailure(Call<APIResponse> call, Throwable t) {
             DynamicToast.makeError(CheckOutActivity.this,t.getLocalizedMessage()).show();
         }
     });

    }

    @Override
    public void OnNoClicked() {

    }
    private void sendNotification(String paymentOption) {
        String CHANNEL_ID = "Okazo";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("chaneel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);

            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);


        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANNEL_ID);
        if(paymentOption.toLowerCase().equals("cash")){
            builder.setContentTitle("Tickets Booked")
                    .setContentText("Tickets has been booked successfully")
                    .setAutoCancel(false)

                    .setSmallIcon(R.mipmap.ic_okazo_logo);

        }else {
            builder.setContentTitle("Tickets Confirmed")
                    .setContentText("Tickets has been purchased")
                    .setAutoCancel(false)

                    .setSmallIcon(R.mipmap.ic_okazo_logo);
        }


        Notification notification=builder.build();
        notification.flags= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(new Random().nextInt(),notification);
    }
}
