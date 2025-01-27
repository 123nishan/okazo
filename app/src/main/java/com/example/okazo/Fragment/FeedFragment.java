package com.example.okazo.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.EventActivity;
import com.example.okazo.LoginActivity;
import com.example.okazo.MainActivity;
import com.example.okazo.Model.Comment;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.Model.Posts;
import com.example.okazo.R;
import com.example.okazo.util.FeedAdapter;
import com.example.okazo.util.PostCommentAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    public FeedFragment() {
        // Required empty public constructor
    }
private RecyclerView recyclerViewFeed;
    private ApiInterface apiInterface;
        private String userId;
        private TextView textViewTest;
        private ArrayList<String> arrayListEventTitle=new ArrayList<>(),arrayListProfileImage=new ArrayList<>(),
                arrayListDetail=new ArrayList<>(),arrayListCreatedDate=new ArrayList<>(),arrayListImage=new ArrayList<>(),
                arrayListPostId=new ArrayList<>(),arrayListLikes=new ArrayList<>(),arrayListUserLike=new ArrayList<>(),arrayListEventId=new ArrayList<>()
                ,arrayListComment=new ArrayList<>(),arrayListCommentDetail=new ArrayList<>(),arrayListCommnetUserImage=new ArrayList<>(),
    arrayListCommentUserName=new ArrayList<>(),arrayListCommentCreatedDate=new ArrayList<>();
        private FeedAdapter adapter;
        private int feedPosition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_feed, container, false);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        recyclerViewFeed=view.findViewById(R.id.feed_fragment_recyler_view);
        textViewTest=view.findViewById(R.id.feed_fragment_text_view);

        //textViewTest=view.findViewById(R.id.card_feed_text);
        //apiInterface.getFeed()
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences.getString("user_id","")!=null  && !sharedPreferences.getString("user_id","").isEmpty()){
            userId=sharedPreferences.getString("user_id","");
            apiInterface.getFeed(userId).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                   APIResponse apiResponse=response.body();
                   if(!apiResponse.getError()){
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
                                               ((TextView)recyclerViewFeed.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_feed_total_like)).setText(apiResponse1.getTotalLike());

                                           }else {

                                               ImageView imageView=((ImageView)recyclerViewFeed.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_feed_post_like_black));
                                               animation(getActivity().getApplicationContext(),imageView,R.drawable.ic_like_black);
                                               ((TextView)recyclerViewFeed.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_feed_total_like)).setText(apiResponse1.getTotalLike());
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
                       recyclerViewFeed.setVisibility(View.GONE);
                       textViewTest.setVisibility(View.VISIBLE);
                       textViewTest.setText("No feed to show");
                   }else {
                       DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse.getErrorMsg()).show();
                   }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });
        }else {
            DynamicToast.makeError(getActivity().getApplicationContext(),"Something went wrong").show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("user_email");
            editor.remove("user_id");
            editor.commit();
//            editor.putString("user_email","");
//            editor.putString("user_id","");
            Intent intent=new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        return view;

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
