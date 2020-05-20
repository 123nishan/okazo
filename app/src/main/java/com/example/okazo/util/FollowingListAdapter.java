package com.example.okazo.util;

import android.content.Context;
import android.net.Uri;
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

public class FollowingListAdapter extends RecyclerView.Adapter<FollowingListAdapter.MyViewHolder> {
   OnClickListener onClickListener;
   OnCardClickListener onCardClickListener;
    ArrayList<String> eventName,image;
    Context context;
    public FollowingListAdapter(ArrayList<String> eventName,ArrayList<String>image,Context context){
        this.eventName=eventName;
        this.image=image;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_following_list,parent,false);
        return new  FollowingListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String imagePath=KEY_IMAGE_ADDRESS+(image.get(position));

        Glide.with(context)
                .load(Uri.parse(imagePath))
                .placeholder(R.drawable.ic_place_holder_background)
                .centerCrop()
                .into(holder.circleImageViewProfile);
        holder.textViewName.setText(eventName.get(position));
    }

    @Override
    public int getItemCount() {
        return eventName.size();
    }
    public interface OnClickListener{
        void onClick(int position);
    }
    public interface OnCardClickListener{
        void onCardClick(int position);
    }
    public void setOnCardClickListener(OnCardClickListener onCardClickListener){
        this.onCardClickListener=onCardClickListener;
    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }
    public void removeItem(int position) {
        eventName.remove(position);
        image.remove(position);

        notifyItemRemoved(position);
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageViewProfile,circleImageViewLeave;
        CardView cardView;
        TextView textViewName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.card_following_list_card);
            circleImageViewLeave=itemView.findViewById(R.id.card_following_list_leave);
            circleImageViewProfile=itemView.findViewById(R.id.card_following_list_event_image);
            textViewName=itemView.findViewById(R.id.card_following_list_event_name);
            circleImageViewLeave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onClickListener!=null){
                        onClickListener.onClick(position);
                    }
                }
            });
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onCardClickListener!=null){
                        onCardClickListener.onCardClick(position);
                    }
                }
            });
        }
    }
}
