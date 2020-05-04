package com.example.okazo.util;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.zip.Inflater;

import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    Context context;
    ChatAdapter.OnCardClickListener onCardClickListener;
    ArrayList<String> senderId,createdAt,name,image,message,unseenCount;
    public ChatAdapter(ArrayList<String> senderId, ArrayList<String> createdAt, ArrayList<String> name, ArrayList<String> image, Context context, ArrayList<String> message, ArrayList<String> unseenCount){
        this.senderId=senderId;
        this.image=image;
        this.createdAt=createdAt;
        this.name=name;
        this.context=context;
        this.message=message;
        this.unseenCount=unseenCount;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_chat_room,parent,false);

        return new ChatAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewName.setText(name.get(position));
        holder.textViewMessage.setText(message.get(position));
        String unseen=unseenCount.get(position);
        if(unseen.equals("0")){
            holder.button.setVisibility(View.GONE);
        }else {
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setText(unseen);
        }

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
        holder.textViewTime.setText(sequence);


        String imagePath=KEY_IMAGE_ADDRESS+(image.get(position));

        Glide.with(context)
                .load(Uri.parse(imagePath))
                .placeholder(R.drawable.ic_place_holder_background)
                .centerCrop()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return senderId.size();
    }
    public   interface OnCardClickListener{
       void onClick(int position,ArrayList<String> sender_id);
    }
    public void setOnCardClickListener(OnCardClickListener onCardClickListener){
        this.onCardClickListener=onCardClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewName,textViewMessage,textViewTime;
        Button button;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.card_chat_room_image);
            textViewName=itemView.findViewById(R.id.card_chat_room_sender);
            textViewMessage=itemView.findViewById(R.id.card_chat_room_message);
            textViewTime=itemView.findViewById(R.id.card_chat_room_time);
            button=itemView.findViewById(R.id.card_chat_room_unseen);
            cardView=itemView.findViewById(R.id.card_chat_room_card);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onCardClickListener!=null){
                        onCardClickListener.onClick(position,senderId);
                    }
                }
            });
        }
    }
}
