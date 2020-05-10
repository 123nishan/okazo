package com.example.okazo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.Comment;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.Model.Posts;
import com.example.okazo.util.ConfirmationDialog;
import com.example.okazo.util.FeedAdapter;
import com.example.okazo.util.PostCommentAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_DETAIL;

import static com.example.okazo.util.constants.KEY_EVENT_ID;
import static com.example.okazo.util.constants.KEY_ID_FOR_CHAT;
import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;
import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;
import static com.example.okazo.util.constants.KEY_USER_ID;
import static com.example.okazo.util.constants.KEY_USER_ROLE;

public class EventActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener,ConfirmationDialog.orderConfirmationListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
   private ImageView imageView,imageViewFirstCard;
   private TextView textViewFirstCard,textViewCountDown,textViewTitle, textViewGoing,
           textViewInterested,textViewLocation,textViewHost,textViewDate,textViewtime,textViewErrorRecyclerView,
    textViewPhone,textViewTicket,textViewDetail,textViewEventClosed,textViewEventClosedAdmin;
    private  AppBarLayout appBarLayout;
    private  Button buttonFollow,buttonFollowing;
    private CardView cardViewFirst,cardViewFirstInterested,cardViewSecondInterested,cardViewSecondTicket,cardViewMessage;
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
    private String eventStatus,path;
    private Uri uriProfileImage;
private LinearLayout linearLayout,linearLayoutResponseLayout;
    private static final int CHOOSE_IMAGE = 505;
    private ArrayList<String>
            arrayListDetail=new ArrayList<>(),arrayListCreatedDate=new ArrayList<>(),arrayListImage=new ArrayList<>(),
            arrayListPostId=new ArrayList<>(),arrayListLikes=new ArrayList<>(),arrayListUserLike=new ArrayList<>(),arrayListComment=new ArrayList<>(),
            arrayListEventTitle=new ArrayList<>(),arrayListProfileImage=new ArrayList<>(),arrayListCommentDetail=new ArrayList<>(),arrayListCommnetUserImage=new ArrayList<>(),
            arrayListCommentUserName=new ArrayList<>(),arrayListCommentCreatedDate=new ArrayList<>();
// for admin side and moderator
    private String userRole;
    private EditText editTextPostDetail;
    private ImageView imageViewPost,imageViewMenu,imageViewBack;
    private Button buttonPost,buttonMessage,buttonModerator,buttonNotification,buttonReport;
    private CardView cardViewPost;
    private String moderatorId;
    private Boolean followButtonFlag=false;
    private HorizontalScrollView horizontalScrollView;
    private CircleImageView circleImageViewDot;
        private int temp=0;
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
        linearLayout=findViewById(R.id.event_activity_about_layout);
        textViewEventClosed=findViewById(R.id.event_activity_event_closed);
        textViewLocation=findViewById(R.id.event_activity_location);
        textViewHost=findViewById(R.id.event_activity_host);
        textViewDate=findViewById(R.id.event_activity_date);
        textViewtime=findViewById(R.id.event_activity_time);
        textViewPhone=findViewById(R.id.event_activity_phone);
        textViewTicket=findViewById(R.id.event_activity_ticket);
        textViewGoing=findViewById(R.id.event_activity_going);
        textViewInterested=findViewById(R.id.event_activity_interested);
        textViewDetail=findViewById(R.id.event_activity_detail);
        cardViewMessage=findViewById(R.id.event_activity_third_card);
        textViewEventClosedAdmin=findViewById(R.id.event_activity_event_closed_admin);
        linearLayoutResponseLayout=findViewById(R.id.event_activity_response_button_layout);
        imageViewMenu=findViewById(R.id.event_activity_menu_admin);
        imageViewBack=findViewById(R.id.event_activity_back);
         eventDetail= (EventDetail) getIntent().getSerializableExtra(KEY_EVENT_DETAIL);
        userId=getIntent().getExtras().getString(KEY_USER_ID);
        circleImageViewDot=findViewById(R.id.event_activity_unseen_dot);
        eventId=eventDetail.getId();
        horizontalScrollView=findViewById(R.id.event_activity_moderator_action_layout);
        buttonMessage=findViewById(R.id.event_activity_moderator_action_message);
        buttonModerator=findViewById(R.id.event_activity_moderator_action_moderator);
        buttonNotification=findViewById(R.id.event_activity_moderator_action_notification);
        ticketStatus=eventDetail.getTicketStatus();
    //for admin side and moderator
        cardViewPost=findViewById(R.id.event_activity_create_post_card);
        editTextPostDetail=findViewById(R.id.event_activity_create_post_detail);
        buttonPost=findViewById(R.id.event_activity_create_post_button);
        imageViewPost=findViewById(R.id.event_activity_create_post_image);
        buttonReport=findViewById(R.id.event_activity_moderator_action_report);

    imageViewBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });


        SharedPreferences sharedPreferences = EventActivity.this.getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        apiInterface.getModerator(userId,eventId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                   String status=apiResponse.getModerator().getStatus();
                   String role=apiResponse.getModerator().getRole();
                   moderatorId=apiResponse.getModerator().getId();

                   if(status.equals("Accepted")){
                       apiInterface.checkInbox(eventId).enqueue(new Callback<APIResponse>() {
                           @Override
                           public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                               APIResponse apiResponse1=response.body();
                               if(!apiResponse1.getError()){
                                   if(Integer.valueOf(apiResponse1.getInboxCount())>0){
                                       circleImageViewDot.setVisibility(View.VISIBLE);
                                   }else {
                                       circleImageViewDot.setVisibility(View.GONE);
                                   }
                               }
                           }

                           @Override
                           public void onFailure(Call<APIResponse> call, Throwable t) {

                           }
                       });
                       horizontalScrollView.setVisibility(View.VISIBLE);
                       linearLayoutResponseLayout.setVisibility(View.GONE);
                       if(ticketStatus.equals("1")){
                           textViewTicket.setText("Click for ticket");
                           textViewTicket.setTextColor(getColor(R.color.colorPrimary));

                       }else {
                           textViewTicket.setText("Free Entry");
                       }
                       followButtonFlag=true;
                       buttonFollowing.setVisibility(View.GONE);
                       buttonFollow.setVisibility(View.GONE);
                      //userRole="Moderator";
                      imageViewMenu.setVisibility(View.VISIBLE);
                      imageViewMenu.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              PopupMenu menu=new PopupMenu(EventActivity.this,view);
                              MenuInflater inflater=menu.getMenuInflater();
                              inflater.inflate(R.menu.event_setting,menu.getMenu());
                              menu.setOnMenuItemClickListener(EventActivity.this::onMenuItemClick);
                              menu.show();
                          }
                      });
                      //for admin only
                       if(role.equals("Admin")){

                           buttonMessage.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                                   intent.putExtra(KEY_ID_FOR_CHAT,eventId);
                                   startActivity(intent);
                               }
                           });
                           userRole="Admin";
                           checkeventStatus();

                       }
                       else if(role.equals("Editor")){
                           userRole="Editor";
                           checkeventStatus();
                           Toast.makeText(EventActivity.this, "Editor", Toast.LENGTH_SHORT).show();
                       }else {

                           userRole="Moderator";
                           checkeventStatus();
                            buttonReport.setClickable(false);
                           buttonReport.setBackground(getDrawable(R.drawable.disable_round_button));
                           buttonModerator.setClickable(false);
                           buttonModerator.setBackground(getDrawable(R.drawable.disable_round_button));

                       }
                       //for non admin moderator only
                   }else {
                       NoModerator();
                       DynamicToast.makeSuccess(EventActivity.this,"YOu have moderator request from this event").show();
                   }
                }else {
                    String message=apiResponse.getErrorMsg();
                    if(message.equals("banned")){
                        DynamicToast.makeError(EventActivity.this,"Your account has been compromised").show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("user_email");
                        editor.remove("user_id");
                        editor.commit();
                        Intent intent=new Intent(EventActivity.this,LoginActivity.class);

                        startActivity(intent);
                    }else if(message.equals("no moderator")){
                        NoModerator();
                    }else {
                        DynamicToast.makeError(EventActivity.this,message).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                DynamicToast.makeError(EventActivity.this,t.getLocalizedMessage()).show();
            }
        });

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
                            //Log.d("nishanCheck",userId+" "+arrayListPostId.get(position));
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
                }
                else {
                    DynamicToast.makeError(EventActivity.this,"There was problem loading content please contact service").show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
        //post recyclerView○


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

//        if(!followButtonFlag){
//
//
//
//        }else {
//            buttonFollow.setVisibility(View.GONE);
//            buttonFollowing.setVisibility(View.GONE);
//        }




going=false;
        cardViewFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onCLick",eventResponse);
             if(eventResponse.equals("3")){
                 apiInterface.setEventResponse(userId,eventId,"1").enqueue(new Callback<APIResponse>() {
                     @Override
                     public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                         APIResponse apiResponse=response.body();
                         if(!apiResponse.getError()){
                             cardViewFirst.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                                imageViewFirstCard.setBackground(getDrawable(R.drawable.ic_going));
                                cardViewSecondInterested.setVisibility(View.GONE);
                                cardViewSecondTicket.setVisibility(View.VISIBLE);
                                eventResponse="1";
                                textViewGoing.setText(String.valueOf(Integer.valueOf(textViewGoing.getText().toString())+1));
                               // getFollowing();
                         }
                     }

                     @Override
                     public void onFailure(Call<APIResponse> call, Throwable t) {

                     }
                 });
             }else if(eventResponse.equals("1")){
                 apiInterface.setEventResponse(userId,eventId,"3").enqueue(new Callback<APIResponse>() {
                     @Override
                     public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                         APIResponse apiResponse=response.body();
                         if(!apiResponse.getError()){
                             cardViewFirst.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                             imageViewFirstCard.setBackground(getDrawable(R.drawable.ic_going_not_filled));
                             cardViewSecondInterested.setVisibility(View.VISIBLE);
                             cardViewSecondTicket.setVisibility(View.GONE);
                             eventResponse="3";
                             textViewGoing.setText(String.valueOf(Integer.valueOf(textViewGoing.getText().toString())-1));
                            // getFollowing();
                         }
                     }

                     @Override
                     public void onFailure(Call<APIResponse> call, Throwable t) {

                     }
                 });
             }else {

             }
//                if(going){
//                    cardViewFirst.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                    imageViewFirstCard.setBackground(getDrawable(R.drawable.ic_going_not_filled));
//                    going=false;
//                    cardViewSecondInterested.setVisibility(View.VISIBLE);
//                    cardViewSecondTicket.setVisibility(View.GONE);
//                }else {
//                    going=true;
//                    cardViewFirst.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
//                    imageViewFirstCard.setBackground(getDrawable(R.drawable.ic_going));
//
//
//                    cardViewSecondInterested.setVisibility(View.GONE);
//                    cardViewSecondTicket.setVisibility(View.VISIBLE);
//                }



            }
        });

       cardViewSecondInterested.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {

              // getFollowing();
//               cardViewFirst.setVisibility(View.GONE);
//               cardViewFirstInterested.setVisibility(View.VISIBLE);
//               cardViewSecondInterested.setVisibility(View.GONE);
//               cardViewSecondTicket.setVisibility(View.VISIBLE);

               if(eventResponse.equals("3")){
                   apiInterface.setEventResponse(userId,eventId,"2").enqueue(new Callback<APIResponse>() {
                       @Override
                       public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                           APIResponse apiResponse=response.body();
                           if(!apiResponse.getError()){
                               cardViewFirst.setVisibility(View.GONE);
                               cardViewFirstInterested.setVisibility(View.VISIBLE);
//                               cardViewFirstInterested.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
//                               imageViewFirstCard.setBackground(getDrawable(R.drawable.ic_interested));
                               cardViewSecondInterested.setVisibility(View.GONE);
                               cardViewSecondTicket.setVisibility(View.VISIBLE);
                               eventResponse="2";
                               textViewInterested.setText(String.valueOf(Integer.valueOf(textViewInterested.getText().toString())+1));
                               //getFollowing();
                           }
                       }

                       @Override
                       public void onFailure(Call<APIResponse> call, Throwable t) {

                       }
                   });
               }else if(eventResponse.equals("2")){
                   apiInterface.setEventResponse(userId,eventId,"3").enqueue(new Callback<APIResponse>() {
                       @Override
                       public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                           APIResponse apiResponse=response.body();
                           if(!apiResponse.getError()){
                               cardViewFirst.setVisibility(View.VISIBLE);
                               cardViewFirstInterested.setVisibility(View.GONE);
                               cardViewFirst.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                               imageViewFirstCard.setBackground(getDrawable(R.drawable.ic_going_not_filled));
                               cardViewSecondInterested.setVisibility(View.VISIBLE);
                               cardViewSecondTicket.setVisibility(View.GONE);
                               eventResponse="3";
                               textViewInterested.setText(String.valueOf(Integer.valueOf(textViewInterested.getText().toString())-1));

                           }
                       }

                       @Override
                       public void onFailure(Call<APIResponse> call, Throwable t) {

                       }
                   });
               }


           }
       });

        cardViewFirstInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cardViewFirstInterested.setVisibility(View.GONE);
//                cardViewFirst.setVisibility(View.VISIBLE);
//                cardViewSecondInterested.setVisibility(View.VISIBLE);
//                cardViewSecondTicket.setVisibility(View.GONE);

//                if(eventResponse.equals("3")){
//                    apiInterface.setEventResponse(userId,eventId,"2").enqueue(new Callback<APIResponse>() {
//                        @Override
//                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//                            APIResponse apiResponse=response.body();
//                            if(!apiResponse.getError()){
//                                cardViewFirst.setVisibility(View.VISIBLE);
//                                cardViewFirstInterested.setVisibility(View.GONE);
//                                cardViewFirst.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                                imageViewFirstCard.setBackground(getDrawable(R.drawable.ic_going_not_filled));
//                                cardViewSecondInterested.setVisibility(View.VISIBLE);
//                                cardViewSecondTicket.setVisibility(View.GONE);
//                                eventResponse="2";
//                                //getFollowing();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<APIResponse> call, Throwable t) {
//
//                        }
//                    });
//                }else if(eventResponse.equals("2")){
                    apiInterface.setEventResponse(userId,eventId,"3").enqueue(new Callback<APIResponse>() {
                        @Override
                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                            APIResponse apiResponse=response.body();
                            if(!apiResponse.getError()){
                                cardViewFirst.setVisibility(View.VISIBLE);
                                cardViewFirstInterested.setVisibility(View.GONE);
                                cardViewFirst.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                                imageViewFirstCard.setBackground(getDrawable(R.drawable.ic_going_not_filled));
                                cardViewSecondInterested.setVisibility(View.VISIBLE);
                                cardViewSecondTicket.setVisibility(View.GONE);
                                eventResponse="3";
                                textViewInterested.setText(String.valueOf(Integer.valueOf(textViewInterested.getText().toString())-1));

                            }
                        }

                        @Override
                        public void onFailure(Call<APIResponse> call, Throwable t) {

                        }
                    });
              //  }


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


                    collapsingToolbarLayout.setTitle("");

                    imageView.setVisibility(View.GONE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle(" ");

                }
            }
        });

        textViewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ticketStatus.equals("1")) {
                    Toast.makeText(EventActivity.this, "TCIKET", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void NoModerator() {
        if(ticketStatus.equals("1")){
            textViewTicket.setText("Click for ticket");
            textViewTicket.setTextColor(getColor(R.color.colorPrimary));

        }else {
            textViewTicket.setText("Free Entry");
        }

    getFollowing();

        userRole="User";
        apiInterface.getEventStatus(eventId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    eventStatus=apiResponse.getEvent().getStatus();
                    if(eventStatus.equals("2")){

                        linearLayout.setVisibility(View.GONE);
                        textViewEventClosed.setVisibility(View.VISIBLE);
                        buttonFollow.setClickable(false);
                        linearLayoutResponseLayout.setVisibility(View.GONE);
//                                        cardViewFirst.setClickable(false);
//                                        cardViewFirstInterested.setClickable(false);
//                                        cardViewSecondInterested.setClickable(false);
//                                        cardViewSecondTicket.setClickable(false);
//                                        cardViewMessage.setClickable(false);

                    }else if(eventStatus.equals("4")){
                        DynamicToast.makeError(EventActivity.this,"This event has been removed").show();
                        finish();
                    }else {

                    }
                }else {
                    DynamicToast.makeError(EventActivity.this,"Problem loading event").show();

                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

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
    private void showImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_IMAGE);
        //startActivityForResult(Intent.createChooser(intent, "Select image"), CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            uriProfileImage = data.getData();

            Glide.with(EventActivity.this)
                    .load(uriProfileImage)
                    .placeholder(R.drawable.ic_place_holder_background)
                    //.error(R.drawable.ic_image_not_found_background)
                    .centerCrop()
                    .into(imageViewPost);
            // Bitmap bitmap = null;
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor=getContentResolver().query(uriProfileImage,filePathColumn,null,null,null);
            assert cursor!=null;
            cursor.moveToFirst();

            int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
            path=cursor.getString(columnIndex);
            cursor.close();



        }
    }

    private void createPost(){
        cardViewPost.setVisibility(View.VISIBLE);
        buttonPost.setVisibility(View.GONE);
        editTextPostDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>10){
                    buttonPost.setVisibility(View.VISIBLE);
                }else {
                    buttonPost.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        imageViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postDetail=editTextPostDetail.getText().toString();
                Log.d("postCheck",postDetail+"||||||"+path);
            }
        });
    }

    private void checkeventStatus(){
        apiInterface.getEventStatus(eventId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    String statusCheck=apiResponse.getEvent().getStatus();
                    if(statusCheck.equals("2")){
                        textViewEventClosedAdmin.setVisibility(View.VISIBLE);
                    }else if(statusCheck.equals("4")) {
                        // linearLayout.setVisibility(View.GONE);
                        textViewEventClosedAdmin.setVisibility(View.VISIBLE);




                        buttonFollow.setClickable(false);
                        buttonFollowing.setClickable(false);
                        linearLayoutResponseLayout.setVisibility(View.GONE);

                        textViewEventClosedAdmin.setText("Your event was removed due to false information,please contact service");
                    }else {
                        //if admin and all status is fine
                        if(userRole.equals("Admin")){
                            createPost();
                            addModerator(userRole);
                        }else if(userRole.equals("Editor")){
                            addModerator(userRole);
                            createPost();
                        }else {
                            addModerator(userRole);
                            cardViewPost.setVisibility(View.GONE);
                        }

                        linearLayoutResponseLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.event_setting_1:
                if(userRole.equals("Moderator")){
                        DynamicToast.makeError(EventActivity.this,"You dont have permission to this feature").show();
                }else {
                   temp+=1;
                    Intent intent = new Intent(EventActivity.this, EventSettingActivity.class);
                    intent.putExtra(KEY_EVENT_ID,eventId);
                    intent.putExtra(KEY_USER_ROLE, userRole);

                    startActivity(intent);
                }
                break;
            case R.id.event_setting_2:
                String message;
                if(userRole.equals("Admin")){
                    message="Do you want to leave group?\nAs admin leaving will close the event ";
                }else {
                    message="Do you want to leave group?";
                }
                ConfirmationDialog confirmationDialog=new ConfirmationDialog(message);
                confirmationDialog.show(getSupportFragmentManager(),"Confirmation");

                break;

        }
        return false;
    }

    @Override
    public void OnYesClicked() {
        if(checkConnection()) {
            apiInterface.leaveEvent(moderatorId,eventId,userRole).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        DynamicToast.makeSuccess(getApplicationContext(),"You left the event").show();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }else{
                        DynamicToast.makeError(EventActivity.this,apiResponse.getErrorMsg()).show();
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });

        }
        else {
            DynamicToast.makeWarning(this,"No internet connection").show();

        }

    }

    @Override
    public void OnNoClicked() {

    }
    private boolean checkConnection(){
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isConnected();
            return connected;
        }catch (Exception e){


        }
        return connected;



    }
    private void getFollowing(){
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
    }

    @Override
    protected void onResume() {
        super.onResume();
                if(temp==1) {
            temp=0;
                    apiInterface.getEventAllDetail(eventId).enqueue(new Callback<APIResponse>() {
                        @Override
                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                            APIResponse apiResponse=response.body();
                            if(!apiResponse.getError()){
                                eventDetail=apiResponse.getEvent();
                                textViewTitle.setText(eventDetail.getTitle());
                                textViewDetail.setText(eventDetail.getDescription());
                                textViewLocation.setText(eventDetail.getPlace());
                                if(eventDetail.getEndDate().equals(eventDetail.getStartDate()) || eventDetail.getEndDate()!=null || !eventDetail.getEndDate().isEmpty() ){
                                    textViewDate.setText(eventDetail.getStartDate());
                                }else {
                                    textViewDate.setText(eventDetail.getStartDate()+" - "+eventDetail.getEndDate() );
                                }
                                textViewtime.setText(eventDetail.getStartTime()+" - "+eventDetail.getEndTime() );
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
                                ticketStatus=eventDetail.getTicketStatus();



                            }else {
                                DynamicToast.makeError(EventActivity.this,"Problem Loading data").show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<APIResponse> call, Throwable t) {

                        }
                    });
        }



        apiInterface.checkInbox(eventId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse response1=response.body();
                if(!response1.getError()){
                    if(Integer.valueOf(response1.getInboxCount())>0){
                        circleImageViewDot.setVisibility(View.VISIBLE);
                    }else {
                        circleImageViewDot.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });

    }
    private void addModerator(String moderatorType){
        buttonModerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ModeratorActivity.class);
                intent.putExtra(KEY_EVENT_ID,eventId);
                intent.putExtra(KEY_USER_ROLE,moderatorType);
                startActivity(intent);
            }
        });
    }

}
