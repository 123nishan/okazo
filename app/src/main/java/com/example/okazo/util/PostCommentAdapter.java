package com.example.okazo.util;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.okazo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.MyViewHolder> {

    ArrayList<String> comment,userName,userImage,createdDate;
    Context context;
    public PostCommentAdapter(ArrayList<String> comment,ArrayList<String> userName,ArrayList<String> userImage,ArrayList<String> createdDate,Context context){
        this.comment=comment;
        this.userImage=userImage;
        this.userName=userName;
        this.createdDate=createdDate;
        this.context=context;
        //Log.d("recycelrCheck","const");
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.card_comment,parent,false);
        //Log.d("recycelrCheck","view");
        return new PostCommentAdapter.MyViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String imagePath=KEY_IMAGE_ADDRESS+(userImage.get(position));
       //Log.d("recycelrCheck","onBind");
        Glide.with(context)
                .load(Uri.parse(imagePath))
                .placeholder(R.drawable.ic_place_holder_background)
                //.error(R.drawable.ic_image_not_found_background)
                .centerCrop()
                .into(holder.circleImageView);

        holder.textViewUserName.setText(userName.get(position));
        holder.textViewComment.setText(comment.get(position));
        String dateTime=createdDate.get(position);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=null;
        try {
            date=simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milis=date.getTime();
        Date currentDateTimeString = Calendar.getInstance().getTime();
        Date date1=null;
//        try {
//             date1=simpleDateFormat.parse(currentDateTimeString);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        long testMilis=currentDateTimeString.getTime();
       // CharSequence postDate= DateUtils.getRelativeDateTimeString(context,milis,DateUtils.MINUTE_IN_MILLIS,DateUtils.WEEK_IN_MILLIS,0);
        CharSequence testDate= DateUtils.getRelativeTimeSpanString(milis,testMilis,DateUtils.MINUTE_IN_MILLIS);
        holder.textViewTime.setText(testDate);
    }

    @Override
    public int getItemCount() {
      //  Log.d("nishanCheck",userImage.size()+" asd");
        return userName.size();


    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView textViewUserName,textViewComment,textViewTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.card_comment_image);
            textViewUserName=itemView.findViewById(R.id.card_comment_user_name);
            textViewComment=itemView.findViewById(R.id.card_comment_comment);
            textViewTime=itemView.findViewById(R.id.card_comment_time);


        }
    }
}
