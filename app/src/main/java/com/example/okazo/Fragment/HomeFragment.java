package com.example.okazo.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.EventActivity;
import com.example.okazo.EventDetailPreviewActivity;
import com.example.okazo.GeoFenceActivity;
import com.example.okazo.LoginActivity;
import com.example.okazo.MainActivity;
import com.example.okazo.Model.Comment;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.Model.Posts;
import com.example.okazo.R;
import com.example.okazo.RegisterActivity;
import com.example.okazo.TicketDetailActivity;
import com.example.okazo.eventDetail;
import com.example.okazo.util.FeedAdapter;
import com.example.okazo.util.PostCommentAdapter;
import com.example.okazo.util.constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.okazo.util.constants.KEY_EVENT_DETAIL;
import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;
import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;
import static com.example.okazo.util.constants.KEY_USER_ID;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
    private AppBarLayout appBarLayout;
    private  Button buttonLogOut,buttonAddEvent,buttonTicket;
    private  AppCompatImageButton imageButtonAdd;
    private  CollapsingToolbarLayout collapsingToolbarLayout;
    private CircleImageView circleImageView;
    private TextView textViewFirst,textViewSecond,textViewThird;
    private ApiInterface apiInterface;
    private String userId;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RelativeLayout relativeLayoutExtented;

    private ImageView imageViewProgress;
    private RecyclerView recyclerViewFeed;

    private TextView textViewTest;
    private ArrayList<String> arrayListEventTitle=new ArrayList<>(),arrayListProfileImage=new ArrayList<>(),
            arrayListDetail=new ArrayList<>(),arrayListCreatedDate=new ArrayList<>(),arrayListImage=new ArrayList<>(),
            arrayListPostId=new ArrayList<>(),arrayListLikes=new ArrayList<>(),arrayListUserLike=new ArrayList<>(),arrayListEventId=new ArrayList<>()
            ,arrayListComment=new ArrayList<>(),arrayListCommentDetail=new ArrayList<>(),arrayListCommnetUserImage=new ArrayList<>(),
            arrayListCommentUserName=new ArrayList<>(),arrayListCommentCreatedDate=new ArrayList<>();
    private FeedAdapter adapter;
    private int feedPosition;
    GifDrawable gifChecking = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
       // buttonLogOut=view.findViewById(R.id.home_fragment_log_out);
        appBarLayout= view.findViewById(R.id.fragment_home_app_bar);
        imageButtonAdd= view.findViewById(R.id.fragment_home_add);
        circleImageView=view.findViewById(R.id.home_fragment_circular_Image);
        textViewFirst=view.findViewById(R.id.home_fragment_first_textview);
        textViewSecond=view.findViewById(R.id.home_fragment_second_textview);
        textViewThird=view.findViewById(R.id.home_fragment_third_textview);
        relativeLayoutExtented=view.findViewById(R.id.home_fragment_extented_relative_layout);
        relativeLayoutExtented.setVisibility(View.GONE);

        imageViewProgress=view.findViewById(R.id.home_fragment_feed_progress);
        imageViewProgress.setVisibility(View.VISIBLE);
        try {
            gifChecking = new GifDrawable( getResources(), R.drawable.loading_animation );
            imageViewProgress.setBackground(gifChecking);
        } catch (IOException e) {
            e.printStackTrace();
        }

        recyclerViewFeed=view.findViewById(R.id.feed_fragment_recyler_view);
        recyclerViewFeed.setVisibility(View.GONE);
        textViewTest=view.findViewById(R.id.feed_fragment_text_view);
//        tabLayout=view.findViewById(R.id.home_fragment_tab_layout);
//        viewPager=view.findViewById(R.id.home_fragment_view_pager);
        collapsingToolbarLayout=view.findViewById(R.id.fragment_home_collapsing_tool_bar);

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        MainActivity mainActivity= (MainActivity) this.getActivity();
//        ActionBar bar=mainActivity.getSupportActionBar();
//        bar.hide();
       // ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        userId=sharedPreferences.getString("user_id","");

        String request="home";
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        if(sharedPreferences.getString("user_id","")!=null  && !sharedPreferences.getString("user_id","").isEmpty()){

            apiInterface.getFeed(userId).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){
                        imageViewProgress.setVisibility(View.GONE);
                        recyclerViewFeed.setVisibility(View.VISIBLE);
                        textViewTest.setVisibility(View.GONE);
                        // Log.d("checkHere","null "+apiResponse.getEventArray().get(0).getTitle());
                        ArrayList<Posts> eventDetails=apiResponse.getPostArray();
                        for (Posts value:eventDetails
                        ) {

                            arrayListEventTitle.add(value.getTitle());
                            arrayListCreatedDate.add(value.getCreatedDate());
                            arrayListDetail.add(value.getDetail());
                            arrayListProfileImage.add(value.getProfileImage());
                            arrayListPostId.add(value.getPostId());
                            arrayListLikes.add(value.getLikes());
                            arrayListUserLike.add(value.getUserLike());
                            arrayListEventId.add(value.getId());
                            arrayListComment.add(value.getComment());
                            arrayListImage.add(value.getImage());
                            //TODO left for image on post

                        }
                        //  Log.d("title",arrayListEventTitle.size()+"=="+apiResponse.getEventArray().size());
                        adapter=new FeedAdapter(arrayListEventTitle,arrayListProfileImage,arrayListDetail,arrayListCreatedDate,
                                getActivity().getApplicationContext(),arrayListPostId,arrayListLikes,arrayListUserLike,arrayListEventId,arrayListComment,arrayListImage);
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
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
                                BottomSheetDialog sheetDialog=new BottomSheetDialog(getContext() );
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
                                                                    DynamicToast.makeSuccess(getActivity().getApplicationContext(),"Added Successfully").show();
                                                                }else {
                                                                    DynamicToast.makeError(getActivity().getApplicationContext(),object.getErrorMsg()).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<APIResponse> call, Throwable t) {

                                                            }
                                                        });
                                                    }else {
                                                        DynamicToast.makeError(getActivity().getApplicationContext(),"Please write comment").show();
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
                                            DynamicToast.makeError(getActivity().getApplicationContext(),obj.getErrorMsg()).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {
                                        DynamicToast.makeError(getActivity().getApplicationContext(),t.getLocalizedMessage()).show();
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
                                apiInterface.setLike(userId,arrayListPostId.get(position)).enqueue(new Callback<APIResponse>() {
                                    @Override
                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                        APIResponse apiResponse1=response.body();
                                        if(!apiResponse1.getError()){
//                                           final Animation anim_out = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.fade_out);
//                                           final Animation anim_in  = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.fade_in);

                                            if(apiResponse1.getErrorMsg().equals("LIKED")){

                                                ImageView imageView=((ImageView)recyclerViewFeed.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_feed_post_like_black));

                                                animation(getActivity().getApplicationContext(),imageView,R.drawable.ic_like_red);
                                                ((TextView)recyclerViewFeed.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_feed_total_like)).setText(apiResponse1.getTotalLike()+" Likes");

                                            }else {

                                                ImageView imageView=((ImageView)recyclerViewFeed.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_feed_post_like_black));
                                                animation(getActivity().getApplicationContext(),imageView,R.drawable.ic_like_black);
                                                ((TextView)recyclerViewFeed.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_feed_total_like)).setText(apiResponse1.getTotalLike()+" Likes");
                                                // ((ImageView)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_feed_post_like_black)).setImageResource(R.drawable.ic_like_black);
                                            }

                                        }else {
                                            DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse1.getErrorMsg()).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {

                                    }
                                });

                            }
                        });
                    }else if(apiResponse.getErrorMsg().equals("NO FEED")) {
                        imageViewProgress.setVisibility(View.GONE);
                        recyclerViewFeed.setVisibility(View.GONE);
                        textViewTest.setVisibility(View.VISIBLE);
                        textViewTest.setText("No Feed");
                    }else {
                        DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse.getErrorMsg()).show();
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });



//            HomeFragment.ViewPagerAdapter viewPagerAdapter=new HomeFragment.ViewPagerAdapter(getChildFragmentManager());
//            viewPagerAdapter.addFragment(new FeedFragment() ,"Feed");
//            viewPagerAdapter.addFragment(new ProfileFragment(),"Events");
//            viewPager.setAdapter(viewPagerAdapter);
//            tabLayout.setupWithViewPager(viewPager);
//            tabLayout.setupWithViewPager(viewPager);


            //api for user name

            apiInterface.getUserName(userId,request).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){
                                textViewSecond.setText((apiResponse.getUser().getName().toUpperCase()));
                    }else {
                       DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse.getErrorMsg()).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("user_email");
                        editor.remove("user_id");
                        editor.commit();
//                        editor.putString("user_email","");
//                        editor.putString("user_id","");
                        Intent intent=new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });
            //api for event name
            apiInterface.checkEvent(userId).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){
                        relativeLayoutExtented.setVisibility(View.VISIBLE);
                        EventDetail eventDetail=apiResponse.getEvent();
                        textViewFirst.setVisibility(View.VISIBLE);
                        textViewThird.setVisibility(View.GONE);
                        textViewFirst.setText(apiResponse.getEvent().getTitle().toUpperCase());
                        String imagePath=KEY_IMAGE_ADDRESS+(apiResponse.getEvent().getImage());
                        Glide.with(getActivity().getApplicationContext())
                                .load(Uri.parse(imagePath))
                                .placeholder(R.drawable.ic_place_holder_background)
                                //.error(R.drawable.ic_image_not_found_background)
                                .centerCrop()
                                .into(circleImageView);
                        textViewFirst.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                apiInterface.primaryEventInfo(eventDetail.getId(),userId).enqueue(new Callback<APIResponse>() {
                                    @Override
                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                        APIResponse apiResponse1=response.body();
                                        if(!apiResponse1.getError()){
                                            Intent intent=new Intent(getActivity().getApplicationContext(), EventActivity.class);
                                            intent.putExtra(KEY_EVENT_DETAIL,apiResponse1.getEvent());
                                            intent.putExtra(KEY_USER_ID,userId);
                                            startActivity(intent);
                                        }else {
                                            DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse1.getErrorMsg()).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {
                                        DynamicToast.makeError(getActivity().getApplicationContext(),t.getLocalizedMessage()).show();
                                    }
                                });


                            }
                        });

                    }else {
                        textViewFirst.setVisibility(View.GONE);
                        textViewThird.setVisibility(View.VISIBLE);
                        Glide.with(getActivity().getApplicationContext())
                                .load(R.mipmap.ic_backimage)
                                .placeholder(R.drawable.ic_place_holder_background)
                                //.error(R.drawable.ic_image_not_found_background)
                                .centerCrop()
                                .into(circleImageView);
                        if(apiResponse.getErrorMsg().equals("NO EVENT")){
                                textViewThird.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(getActivity().getApplicationContext(), eventDetail.class);
                                        //Intent intent=new Intent(getActivity().getApplicationContext(), EventDetailPreviewActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        }else {
                            DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse.getErrorMsg()).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });
        }else {
            DynamicToast.makeError(getActivity().getApplicationContext(),"Something went wrong").show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("user_email","");
//            editor.putString("user_id","");
            editor.remove("user_email");
            editor.remove("user_id");
            editor.commit();
            Intent intent=new Intent(getActivity().getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }



//        buttonTicket=view.findViewById(R.id.ticket);
//        buttonTicket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity().getApplicationContext(), TicketDetailActivity.class);
//                startActivity(intent);
//            }
//        });

        //buttonAddEvent=view.findViewById(R.id.add_event);
//        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity().getApplicationContext(), eventDetail.class);
//                //Intent intent=new Intent(getActivity().getApplicationContext(), EventDetailPreviewActivity.class);
//                startActivity(intent);
//            }
//        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange=-1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if(scrollRange==-1){
                    scrollRange=appBarLayout.getTotalScrollRange();
                }

                if(scrollRange+i==0){

                    imageButtonAdd.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle("Okazo");
                    relativeLayoutExtented.setVisibility(View.GONE);

                }else {
                    //fully expanded
                    imageButtonAdd.setVisibility(View.GONE);
                    relativeLayoutExtented.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle("");

                }
            }
        });

//        buttonLogOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DynamicToast.makeWarning(getActivity(),"Logging Out").show();
//                SharedPreferences pref = getActivity().getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.remove("user_email");
//                editor.remove("user_id");
//                editor.commit();
//
//                Intent intent=new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
//
//            }
//        });
        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

         // Intent intent=new Intent(getActivity().getApplicationContext(), GeoFenceActivity.class);
          Intent intent=new Intent(getActivity().getApplicationContext(), eventDetail.class);
            startActivity(intent);
            }
        });
        return view;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter

    {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

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
