package com.example.okazo.util;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.okazo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {
ArrayList<String> eventTitle,eventProfile,postDetail,postCreatedDate;
Context context;
public FeedAdapter(ArrayList<String> eventTitle,ArrayList<String> eventProfile,
                   ArrayList<String> postDetail,ArrayList<String> postCreatedDate,Context context){
    this.eventTitle=eventTitle;
    this.eventProfile=eventProfile;
    this.postCreatedDate=postCreatedDate;
    this.postDetail=postDetail;
    this.context=context;
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

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,textViewDate,textViewDescription,textViewTotalLike,textViewTotalComment;
        CircleImageView imageViewProfile;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile=itemView.findViewById(R.id.card_feed_profile_image);
            textViewDate=itemView.findViewById(R.id.card_feed_date_time);
            textViewDescription=itemView.findViewById(R.id.card_feed_description);
            textViewTotalLike=itemView.findViewById(R.id.card_feed_total_like);
            textViewTotalComment=itemView.findViewById(R.id.card_feed_total_comment);
            textViewTitle=itemView.findViewById(R.id.card_feed_title);
        }
    }
}
