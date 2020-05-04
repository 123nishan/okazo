package com.example.okazo.util;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{
    ArrayList<String> message,createdAt,senderId,receiverId,image;
    Context context;
    String currentUser;
    public MessageAdapter(ArrayList<String> message, ArrayList<String> createdAt, ArrayList<String> senderId, ArrayList<String> receiverId, String currentUser, Context applicationContext, ArrayList<String> image){
        this.message=message;
        this.senderId=senderId;
        this.receiverId=receiverId;
        this.createdAt=createdAt;
        this.currentUser=currentUser;
        this.context=applicationContext;
        this.image=image;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message,parent,false);

        return new MessageAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String sender=senderId.get(position);
        String imagePath=KEY_IMAGE_ADDRESS+(image.get(position));

        String created=createdAt.get(position);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=null;
        try{
            date=simpleDateFormat.parse(created);
        }catch (ParseException e){}
        long millis=date.getTime();
        Date currentDateTimeString = Calendar.getInstance().getTime();
        long currentMillis=currentDateTimeString.getTime();
        CharSequence sequence= DateUtils.getRelativeTimeSpanString(millis,currentMillis,DateUtils.MINUTE_IN_MILLIS);

            if(sender.equals(currentUser)){
                holder.linearLayoutLeft.setVisibility(View.GONE);
                holder.linearLayoutRight.setVisibility(View.VISIBLE);
                holder.textViewRightMessage.setText(message.get(position));
                Glide.with(context)
                        .load(Uri.parse(imagePath))
                        .placeholder(R.drawable.ic_place_holder_background)
                        .centerCrop()
                        .into(holder.circleImageViewRight);
                holder.textViewRightTime.setText(sequence);

            }else {
                holder.linearLayoutLeft.setVisibility(View.VISIBLE);
                holder.linearLayoutRight.setVisibility(View.GONE);
                holder.textViewLeftMessage.setText(message.get(position));
                Glide.with(context)
                        .load(Uri.parse(imagePath))
                        .placeholder(R.drawable.ic_place_holder_background)
                        .centerCrop()
                        .into(holder.circleImageViewLeft);
                holder.textViewLeftTime.setText(sequence);
            }





    }

    @Override
    public int getItemCount() {
        return message.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayoutLeft,linearLayoutRight;
        CircleImageView circleImageViewLeft,circleImageViewRight;
        TextView textViewLeftMessage,textViewLeftTime,textViewRightMessage,textViewRightTime;;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayoutLeft=itemView.findViewById(R.id.card_message_left_layout);
            linearLayoutRight=itemView.findViewById(R.id.card_message_right_layout);
            textViewLeftMessage=itemView.findViewById(R.id.card_message_left_message);
            textViewLeftTime=itemView.findViewById(R.id.card_message_left_time);
            textViewRightMessage=itemView.findViewById(R.id.card_message_right_message);
            textViewRightTime=itemView.findViewById(R.id.card_message_right_time);
            circleImageViewLeft=itemView.findViewById(R.id.card_message_left_image);
            circleImageViewRight=itemView.findViewById(R.id.card_message_right_image);
            linearLayoutRight.setVisibility(View.GONE);
            linearLayoutLeft.setVisibility(View.GONE);

        }
    }
}
