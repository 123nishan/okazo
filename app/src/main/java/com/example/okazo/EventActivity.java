package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_DETAIL;
import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;
import static com.example.okazo.util.constants.KEY_USER_ID;

public class EventActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
   private ImageView imageView,imageViewFirstCard;
   private TextView textViewFirstCard,textViewCountDown;
    private  AppBarLayout appBarLayout;
    private  Button buttonFollow,buttonFollowing;
    private CardView cardViewFirst,cardViewFirstInterested,cardViewSecondInterested,cardViewSecondTicket;
    private Boolean going,interested;
    private EventDetail eventDetail;
    private String userId;
    private ApiInterface apiInterface;
    private String eventId;
    private String following;
    private String eventResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        collapsingToolbarLayout=findViewById(R.id.event_activity_collapsing_tool_bar);
        textViewCountDown=findViewById(R.id.event_activity_countdown);
        appBarLayout=findViewById(R.id.event_activity_app_bar);
        imageView=findViewById(R.id.event_activity_image);
        buttonFollow=findViewById(R.id.event_activity_follow_button);
        buttonFollowing=findViewById(R.id.event_activity_following_button);
        cardViewFirstInterested=findViewById(R.id.event_activity_first_card_interested);
        cardViewSecondInterested=findViewById(R.id.event_activity_second_card_interested);
        cardViewSecondTicket=findViewById(R.id.event_activity_second_card_ticket);
        imageViewFirstCard=findViewById(R.id.event_activity_first_card_image);
        textViewFirstCard=findViewById(R.id.event_activity_first_card_text);

        cardViewFirst=findViewById(R.id.event_activity_first_card);


         eventDetail= (EventDetail) getIntent().getSerializableExtra(KEY_EVENT_DETAIL);
        userId=getIntent().getExtras().getString(KEY_USER_ID);
        eventId=eventDetail.getId();
        String imagePath=KEY_IMAGE_ADDRESS+(eventDetail.getImage());
        Glide.with(EventActivity.this)
                .load(Uri.parse(imagePath))
                .placeholder(R.drawable.ic_okazo_logo_background)
                .error(R.drawable.ic_okazo_logo_background)
                .centerCrop()
                .into(imageView);
    String date=eventDetail.getStartDate();
    String time=eventDetail.getStartTime();


        SimpleDateFormat  simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");;
    if(time.length()==4){
         time=time+0;
    }else {

    }

        String dateTime=date+" "+time;
    Date date1=null;
        try {
            date1=simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milis=date1.getTime();
        CharSequence sequence= DateUtils.getRelativeDateTimeString(EventActivity.this,milis,DateUtils.MINUTE_IN_MILLIS,DateUtils.WEEK_IN_MILLIS,0);
        textViewCountDown.setText(sequence);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface.getFollowing(userId,eventId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                  // following=apiResponse.getCollection().getFollowing();
                    following=apiResponse.getEvent().getFollowing();
                    eventResponse=apiResponse.getEvent().getResponse();
                    if(following.equals("1")){
                        buttonFollow.setVisibility(View.GONE);
                        buttonFollowing.setVisibility(View.VISIBLE);
                    }else {
                        buttonFollow.setVisibility(View.GONE);
                        buttonFollowing.setVisibility(View.VISIBLE);
                    }

                    if(eventResponse.equals("3")){
                        cardViewFirst.setVisibility(View.VISIBLE);
                        cardViewFirstInterested.setVisibility(View.GONE);
                        cardViewSecondInterested.setVisibility(View.VISIBLE);
                        cardViewSecondTicket.setVisibility(View.GONE);

                    }else if(eventResponse.equals("1")){

                        cardViewFirst.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        imageViewFirstCard.setBackground(getDrawable(R.drawable.ic_going));
                        cardViewSecondInterested.setVisibility(View.GONE);
                        cardViewSecondTicket.setVisibility(View.VISIBLE);
                    }else {
                        cardViewFirst.setVisibility(View.GONE);
                        cardViewFirstInterested.setVisibility(View.VISIBLE);
                        cardViewSecondInterested.setVisibility(View.GONE);
                        cardViewSecondTicket.setVisibility(View.VISIBLE);
                    }
                   // Toast.makeText(EventActivity.this, "c"+" "+apiResponse.getCollection().getFollowing(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                DynamicToast.makeError(EventActivity.this,"There was an error").show();
                finish();
            }
        });




//
//       if(going){
//           cardViewFirst.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
//           imageViewFirstCard.setBackground(getDrawable(R.drawable.ic_going));
//
//           cardViewSecondInterested.setVisibility(View.GONE);
//           cardViewSecondTicket.setVisibility(View.VISIBLE);
//
//
//       }
        cardViewFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(going){
                    cardViewFirst.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    imageViewFirstCard.setBackground(getDrawable(R.drawable.ic_going_not_filled));
                    going=false;
                    cardViewSecondInterested.setVisibility(View.VISIBLE);
                    cardViewSecondTicket.setVisibility(View.GONE);
                }else {
                    going=true;
                    cardViewFirst.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                    imageViewFirstCard.setBackground(getDrawable(R.drawable.ic_going));


                    cardViewSecondInterested.setVisibility(View.GONE);
                    cardViewSecondTicket.setVisibility(View.VISIBLE);
                }



            }
        });

       cardViewSecondInterested.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               cardViewFirst.setVisibility(View.GONE);
               cardViewFirstInterested.setVisibility(View.VISIBLE);
               cardViewSecondInterested.setVisibility(View.GONE);
               cardViewSecondTicket.setVisibility(View.VISIBLE);
           }
       });

        cardViewFirstInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardViewFirstInterested.setVisibility(View.GONE);
                cardViewFirst.setVisibility(View.VISIBLE);
                cardViewSecondInterested.setVisibility(View.VISIBLE);
                cardViewSecondTicket.setVisibility(View.GONE);
            }
        });

        buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonFollow.setVisibility(View.GONE);
                buttonFollowing.setVisibility(View.VISIBLE);
            }
        });
        buttonFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonFollowing.setVisibility(View.GONE);
                buttonFollow.setVisibility(View.VISIBLE);
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange=-1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange==-1){
                    scrollRange=appBarLayout.getTotalScrollRange();
                }
                if(scrollRange+verticalOffset==0){
                    collapsingToolbarLayout.setTitle("Okazo");
                    imageView.setVisibility(View.GONE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle(" ");

                }
            }
        });
    }
}
