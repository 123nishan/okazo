package com.example.okazo.util;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.okazo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {
ArrayList<String> eventTitle,eventProfile,postDetail,postCreatedDate,postId,postLikes,userPostLike,eventId,postComment,postImage;
Context context;
public FeedAdapter.OnLikeClickListener onLikeClickListener;
private FeedAdapter.OnCommentClickListener onCommentClickListener;
public FeedAdapter(ArrayList<String> eventTitle, ArrayList<String> eventProfile,
                   ArrayList<String> postDetail, ArrayList<String> postCreatedDate, Context context,
                   ArrayList<String> postId,ArrayList<String> postLikes,ArrayList<String> userPostLike,ArrayList<String> eventId,ArrayList<String> postComment,ArrayList<String> postImage){
    this.eventTitle=eventTitle;
    this.eventProfile=eventProfile;
    this.postCreatedDate=postCreatedDate;
    this.postDetail=postDetail;
    this.context=context;
    this.postId=postId;
    this.postLikes=postLikes;
    this.userPostLike=userPostLike;
    this.eventId=eventId;
    this.postComment=postComment;
    this.postImage=postImage;
}
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_feed,parent,false);

        return new FeedAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewTitle.setText(eventTitle.get(position));
        holder.textViewDescription.setText(postDetail.get(position));
       holder.textViewTotalLike.setText(postLikes.get(position)+" Likes");
       holder.textViewTotalComment.setText(postComment.get(position)+" Comments");
       if(postImage.get(position).equals("null") || postImage==null){
           holder.imageViewPostImage.setVisibility(View.GONE);
       }else {
           holder.imageViewPostImage.setVisibility(View.VISIBLE);
           String postImagePath=KEY_IMAGE_ADDRESS+(postImage.get(position));
           Glide.with(context)
                   .load(Uri.parse(postImagePath))
                   .placeholder(R.drawable.ic_place_holder_background)
                   //.error(R.drawable.ic_image_not_found_background)
                   .fitCenter()
                   .into(holder.imageViewPostImage);
       }
        if(userPostLike.get(position).equals("1")){
            holder.imageViewLike.setImageResource(R.drawable.ic_like_red);
        }else {
            holder.imageViewLike.setImageResource(R.drawable.ic_like_black);
        }
       String date=postCreatedDate.get(position);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1=null;
        try {
             date1=simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milis=date1.getTime();
        CharSequence postDate=DateUtils.getRelativeDateTimeString(context,milis,DateUtils.MINUTE_IN_MILLIS,DateUtils.WEEK_IN_MILLIS,0);
        //Log.d("timeCheck",postDate+"null");
        holder.textViewDate.setText(postDate);
        String imagePath=KEY_IMAGE_ADDRESS+(eventProfile.get(position));

        Glide.with(context)
                .load(Uri.parse(imagePath))
                .placeholder(R.drawable.ic_place_holder_background)
                //.error(R.drawable.ic_image_not_found_background)
                .centerCrop()
                .into(holder.imageViewProfile);

    }

    @Override
    public int getItemCount() {
        return eventTitle.size();
    }

    public interface OnLikeClickListener{
            void OnLikeClick(int position);
    }

    public void setOnLikeClickListener(OnLikeClickListener onLikeClickListener){
    this.onLikeClickListener=onLikeClickListener;
    }

    public interface OnCommentClickListener{
         void onCommentClick(int position);
    }
    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener){
    this.onCommentClickListener=onCommentClickListener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,textViewDate,textViewDescription,textViewTotalLike,textViewTotalComment;
        CircleImageView imageViewProfile;
        ImageView imageViewLike,imageViewPostImage;
        LinearLayout linearLayout,linearLayoutCommnet;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile=itemView.findViewById(R.id.card_feed_profile_image);
            textViewDate=itemView.findViewById(R.id.card_feed_date_time);
            textViewDescription=itemView.findViewById(R.id.card_feed_description);
            textViewTotalLike=itemView.findViewById(R.id.card_feed_total_like);
            textViewTotalComment=itemView.findViewById(R.id.card_feed_total_comment);
            textViewTitle=itemView.findViewById(R.id.card_feed_title);
            imageViewLike=itemView.findViewById(R.id.card_feed_post_like_black);
            linearLayout=itemView.findViewById(R.id.card_feed_post_like_layout);
            linearLayoutCommnet=itemView.findViewById(R.id.card_feed_post_comment_layout);
            imageViewPostImage=itemView.findViewById(R.id.card_feed_post_image);
            linearLayoutCommnet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onCommentClickListener!=null){
                        onCommentClickListener.onCommentClick(position);
                    }
                }
            });
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onLikeClickListener!=null){
                        onLikeClickListener.OnLikeClick(position);
                    }
                }
            });
        }
    }
}
