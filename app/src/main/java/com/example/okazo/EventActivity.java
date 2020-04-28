package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class EventActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
   private ImageView imageView,imageViewFirstCard;
   private TextView textViewFirstCard;
    private  AppBarLayout appBarLayout;
    private  Button buttonFollow,buttonFollowing;
    private CardView cardViewFirst,cardViewFirstInterested,cardViewSecondInterested,cardViewSecondTicket;
    private Boolean going;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        collapsingToolbarLayout=findViewById(R.id.event_activity_collapsing_tool_bar);
        appBarLayout=findViewById(R.id.event_activity_app_bar);
        imageView=findViewById(R.id.event_activity_image);
        buttonFollow=findViewById(R.id.event_activity_follow_button);
        buttonFollowing=findViewById(R.id.event_activity_following_button);
        cardViewFirstInterested=findViewById(R.id.event_activity_first_card_interested);
        cardViewSecondInterested=findViewById(R.id.event_activity_second_card_interested);
        cardViewSecondTicket=findViewById(R.id.event_activity_second_card_ticket);


        going=true;
        imageViewFirstCard=findViewById(R.id.event_activity_first_card_image);
        textViewFirstCard=findViewById(R.id.event_activity_first_card_text);

        cardViewFirst=findViewById(R.id.event_activity_first_card);
       if(going){
           cardViewFirst.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
           imageViewFirstCard.setBackground(getDrawable(R.drawable.ic_going));

           cardViewSecondInterested.setVisibility(View.GONE);
           cardViewSecondTicket.setVisibility(View.VISIBLE);


       }
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
                    collapsingToolbarLayout.setTitle("HELLO");
                    imageView.setVisibility(View.GONE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle(" ");

                }
            }
        });
    }
}
