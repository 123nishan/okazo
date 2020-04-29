package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.Comment;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.Model.Posts;
import com.example.okazo.util.FeedAdapter;
import com.example.okazo.util.PostCommentAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
   private TextView textViewFirstCard,textViewCountDown,textViewTitle, textViewGoing,
           textViewInterested,textViewLocation,textViewHost,textViewDate,textViewtime,textViewErrorRecyclerView,
    textViewPhone,textViewTicket,textViewDetail;
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
    private String ticketStatus;
    private RecyclerView recyclerViewFeed;
    private FeedAdapter adapter;
    private int feedPosition;

    private ArrayList<String>
            arrayListDetail=new ArrayList<>(),arrayListCreatedDate=new ArrayList<>(),arrayListImage=new ArrayList<>(),
            arrayListPostId=new ArrayList<>(),arrayListLikes=new ArrayList<>(),arrayListUserLike=new ArrayList<>(),arrayListComment=new ArrayList<>(),
            arrayListEventTitle=new ArrayList<>(),arrayListProfileImage=new ArrayList<>(),arrayListCommentDetail=new ArrayList<>(),arrayListCommnetUserImage=new ArrayList<>(),
            arrayListCommentUserName=new ArrayList<>(),arrayListCommentCreatedDate=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
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
        textViewTitle=findViewById(R.id.event_activity_title);
        cardViewFirst=findViewById(R.id.event_activity_first_card);
        recyclerViewFeed=findViewById(R.id.event_activity_recycler_view);
        textViewErrorRecyclerView=findViewById(R.id.event_activity_error_recycler_view);

        textViewLocation=findViewById(R.id.event_activity_location);
        textViewHost=findViewById(R.id.event_activity_host);
        textViewDate=findViewById(R.id.event_activity_date);
        textViewtime=findViewById(R.id.event_activity_time);
        textViewPhone=findViewById(R.id.event_activity_phone);
        textViewTicket=findViewById(R.id.event_activity_ticket);
        textViewGoing=findViewById(R.id.event_activity_going);
        textViewInterested=findViewById(R.id.event_activity_interested);
        textViewDetail=findViewById(R.id.event_activity_detail);

         eventDetail= (EventDetail) getIntent().getSerializableExtra(KEY_EVENT_DETAIL);
        userId=getIntent().getExtras().getString(KEY_USER_ID);
        eventId=eventDetail.getId();

        //post recyclerView
        apiInterface.getEventPost(eventId,userId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    recyclerViewFeed.setVisibility(View.VISIBLE);
                    textViewErrorRecyclerView.setVisibility(View.GONE);
                    ArrayList<Posts> postDetail=apiResponse.getPostArray();
                    for (Posts value:postDetail
                         ) {
                        arrayListEventTitle.add(eventDetail.getTitle());
                        arrayListProfileImage.add(eventDetail.getImage());

                        arrayListCreatedDate.add(value.getCreatedDate());
                        arrayListDetail.add(value.getDetail());

                        arrayListPostId.add(value.getPostId());
                        arrayListLikes.add(value.getLikes());
                        arrayListUserLike.add(value.getUserLike());
                        arrayListComment.add(value.getComment());
                       // arrayListEventId.add(value.getId());

                    }
                    adapter=new FeedAdapter(arrayListEventTitle,arrayListProfileImage,arrayListDetail,arrayListCreatedDate,
                            EventActivity.this,arrayListPostId,arrayListLikes,arrayListUserLike,null,arrayListComment);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(EventActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewFeed.setLayoutManager(linearLayoutManager);
                    recyclerViewFeed.setAdapter(adapter);

                    adapter.setOnCommentClickListener(new FeedAdapter.OnCommentClickListener() {
                        @Override
                        public void onCommentClick(int position) {
                            String postId=arrayListPostId.get(position);
                            feedPosition=position;
                            //  Toast.makeText(getActivity().getApplicationContext(), "a"+postId, Toast.LENGTH_SHORT).show();
                            View dialog=getLayoutInflater().inflate(R.layout.bottom_sheet,null);
                            BottomSheetDialog sheetDialog=new BottomSheetDialog(EventActivity.this );
                            sheetDialog.setContentView(dialog);
                            sheetDialog.show();
                            if(arrayListCommentDetail.size()>0){
                                arrayListCommentDetail.clear();
                                arrayListCommentCreatedDate.clear();
                                arrayListCommentUserName.clear();
                                arrayListCommnetUserImage.clear();
                            }
                            apiInterface.allComment(postId).enqueue(new Callback<APIResponse>() {
                                @Override
                                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                    APIResponse obj=response.body();
                                    RecyclerView recyclerView=dialog.findViewById(R.id.bottom_sheet_comment_recyclerview);
                                    TextView textViewError=dialog.findViewById(R.id.bottom_sheet_recyclerview_error);
                                    if(!obj.getError()){
                                        PostCommentAdapter commentAdapter;
                                        ArrayList<Comment> comment=obj.getCommentArray();
                                        String commentCount=comment.get(0).getCount();
                                        EditText editTextComment=dialog.findViewById(R.id.bottom_sheet_write_comment);
                                        ImageView imageView=dialog.findViewById(R.id.bottom_sheet_comment_button);
                                        imageView.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View view) {
                                                if(editTextComment.getText().toString()!=null && !editTextComment.getText().toString().equals("")){
                                                    String writeComment=editTextComment.getText().toString();
                                                    apiInterface.addComment(userId,postId,writeComment).enqueue(new Callback<APIResponse>() {
                                                        @Override
                                                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                                            APIResponse object=response.body();
                                                            if(!object.getError()){
                                                                ((TextView)recyclerViewFeed.findViewHolderForAdapterPosition(feedPosition).itemView.findViewById(R.id.card_feed_total_comment)).setText(object.getTotalComment()+" Comments");
                                                                DynamicToast.makeSuccess(EventActivity.this,"Added Successfully").show();
                                                            }else {
                                                                DynamicToast.makeError(EventActivity.this,object.getErrorMsg()).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<APIResponse> call, Throwable t) {

                                                        }
                                                    });
                                                }else {
                                                    DynamicToast.makeError(EventActivity.this,"Please write comment").show();
                                                }
                                            }
                                        });


                                        if(commentCount.equals("0")){
                                            recyclerView.setVisibility(View.GONE);
                                            textViewError.setVisibility(View.VISIBLE);

                                        }else {
                                            textViewError.setVisibility(View.GONE);

                                            for (Comment value : comment
                                            ) {
                                                arrayListCommentDetail.add(value.getComment());
                                                arrayListCommentCreatedDate.add(value.getCreatedDate());
                                                arrayListCommentUserName.add(value.getUser_name());
                                                arrayListCommnetUserImage.add(value.getUserImage());

                                            }
                                            recyclerView.setVisibility(View.VISIBLE);
                                            commentAdapter=new PostCommentAdapter(arrayListCommentDetail,arrayListCommentUserName,arrayListCommnetUserImage,arrayListCommentCreatedDate,dialog.getContext());
                                            LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(dialog.getContext());
                                            linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);

                                            recyclerView.setLayoutManager(linearLayoutManager1);

                                            recyclerView.setAdapter(commentAdapter);


                                        }

                                    }else {
                                        DynamicToast.makeError(EventActivity.this,obj.getErrorMsg()).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<APIResponse> call, Throwable t) {
                                    DynamicToast.makeError(EventActivity.this,t.getLocalizedMessage()).show();
                                }
                            });




//                               LinearLayout linearLayout=getView().findViewById(R.id.bottom_sheet);
//                               BottomSheetBehavior sheetBehavior=BottomSheetBehavior.from(linearLayout);
//                           if(sheetBehavior.getState()!=BottomSheetBehavior.STATE_EXPANDED){
//                               sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//                           }else {
//                               sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                           }
//                               BottomSheetFragment bottomSheetFragment=new BottomSheetFragment();
//                               bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
                        }
                    });


                    adapter.setOnLikeClickListener(new FeedAdapter.OnLikeClickListener() {
                        @Override
                        public void OnLikeClick(int position) {
                            Log.d("nishanCheck",userId+" "+arrayListPostId.get(position));
                            apiInterface.setLike(userId,arrayListPostId.get(position)).enqueue(new Callback<APIResponse>() {
                                @Override
                                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                    APIResponse apiResponse1=response.body();
                                    if(!apiResponse1.getError()){
//                                           final Animation anim_out = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.fade_out);
//                                           final Animation anim_in  = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.fade_in);

                                        if(apiResponse1.getErrorMsg().equals("LIKED")){

                                            ImageView imageView=((ImageView)recyclerViewFeed.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_feed_post_like_black));

                                            animation(EventActivity.this,imageView,R.drawable.ic_like_red);
                                            ((TextView)recyclerViewFeed.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_feed_total_like)).setText(apiResponse1.getTotalLike());

                                        }else {

                                            ImageView imageView=((ImageView)recyclerViewFeed.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_feed_post_like_black));
                                            animation(EventActivity.this,imageView,R.drawable.ic_like_black);
                                            ((TextView)recyclerViewFeed.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_feed_total_like)).setText(apiResponse1.getTotalLike());
                                            // ((ImageView)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_feed_post_like_black)).setImageResource(R.drawable.ic_like_black);
                                        }

                                    }else {
                                        DynamicToast.makeError(EventActivity.this,apiResponse1.getErrorMsg()).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<APIResponse> call, Throwable t) {

                                }
                            });

                        }
                    });
                }else {
                    DynamicToast.makeError(EventActivity.this,"COULD'NT LOAD POSTS").show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
        //post recyclerView



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

        textViewLocation.setTextColor(getColor(R.color.colorPrimary));


        textViewCountDown.setText(sequence);
        textViewTitle.setText(eventDetail.getTitle());
        textViewLocation.setText(eventDetail.getPlace());
        textViewHost.setText(eventDetail.getHostName());
        if(eventDetail.getEndDate().equals(eventDetail.getStartDate()) || eventDetail.getEndDate()!=null || !eventDetail.getEndDate().isEmpty() ){
            textViewDate.setText(eventDetail.getStartDate());
        }else {
            textViewDate.setText(eventDetail.getStartDate()+" - "+eventDetail.getEndDate() );
        }
        textViewtime.setText(eventDetail.getStartTime()+" - "+eventDetail.getEndTime() );
        textViewPhone.setTextColor(getColor(R.color.colorPrimary));
        textViewPhone.setText(eventDetail.getHostPhone());
        ticketStatus=eventDetail.getTicketStatus();
        if(ticketStatus.equals("1")){
            textViewTicket.setText("Click for ticket");
            textViewTicket.setTextColor(getColor(R.color.colorPrimary));
        }else {
            textViewTicket.setText("Free Entry");
        }
        textViewDetail.setText(eventDetail.getDescription());
    apiInterface.getResponseCount(eventId).enqueue(new Callback<APIResponse>() {
        @Override
        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
            APIResponse object=response.body();
            if(!object.getError()){
                    textViewGoing.setText(object.getEvent().getGoingCount());
                    textViewInterested.setText(object.getEvent().getInterestedCount());
            }else {
                DynamicToast.makeError(EventActivity.this,object.getErrorMsg()).show();
                finish();
            }
        }

        @Override
        public void onFailure(Call<APIResponse> call, Throwable t) {
            DynamicToast.makeError(EventActivity.this,"There was error loading data").show();
            finish();
        }
    });
textViewPhone.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(textViewPhone.getText().toString()!="Call On"){
            Intent intent=new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+textViewPhone.getText().toString()));
            startActivity(intent);
        }
    }
});


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
    private void animation(Context c, ImageView v, int r){
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageResource(r);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
}
