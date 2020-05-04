package com.example.okazo.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.okazo.R;

import java.util.ArrayList;

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
            if(sender.equals(currentUser)){
                holder.cardViewLeft.setVisibility(View.GONE);
                holder.cardViewRight.setVisibility(View.VISIBLE);
                holder.textViewRightMessage.setText(message.get(position));
                Glide.with(context)
                        .load(Uri.parse(imagePath))
                        .placeholder(R.drawable.ic_place_holder_background)
                        .centerCrop()
                        .into(holder.circleImageViewRight);

            }else {
                holder.cardViewLeft.setVisibility(View.VISIBLE);
                holder.cardViewRight.setVisibility(View.GONE);
                holder.textViewLeftMessage.setText(message.get(position));
                Glide.with(context)
                        .load(Uri.parse(imagePath))
                        .placeholder(R.drawable.ic_place_holder_background)
                        .centerCrop()
                        .into(holder.circleImageViewLeft);
            }
    }

    @Override
    public int getItemCount() {
        return message.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cardViewLeft,cardViewRight;
        CircleImageView circleImageViewLeft,circleImageViewRight;
        TextView textViewLeftMessage,textViewLeftTime,textViewRightMessage,textViewRightTime;;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewLeft=itemView.findViewById(R.id.card_message_left_layout);
            cardViewRight=itemView.findViewById(R.id.card_message_right_layout);
            textViewLeftMessage=itemView.findViewById(R.id.card_message_left_message);
            textViewLeftTime=itemView.findViewById(R.id.card_message_left_time);
            textViewRightMessage=itemView.findViewById(R.id.card_message_right_message);
            textViewRightTime=itemView.findViewById(R.id.card_message_right_time);
            circleImageViewLeft=itemView.findViewById(R.id.card_message_left_image);
            circleImageViewRight=itemView.findViewById(R.id.card_message_right_image);
            cardViewRight.setVisibility(View.GONE);
            cardViewLeft.setVisibility(View.GONE);

        }
    }
}
