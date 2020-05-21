package com.example.okazo.util;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.okazo.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.MyViewHolder>{
    ArrayList<String> image,title,amount,date;
    Context context;
    public RewardAdapter(ArrayList<String> image,ArrayList<String> title,ArrayList<String> date,ArrayList<String> amount,Context context){
        this.image=image;
        this.amount=amount;
        this.title=title;
        this.context=context;
        this.date
                =date;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reward,parent,false);
        return new RewardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String postImagePath=KEY_IMAGE_ADDRESS+(image.get(position));
        Glide.with(context)
                .load(Uri.parse(postImagePath))
                .placeholder(R.drawable.ic_place_holder_background)
                //.error(R.drawable.ic_image_not_found_background)
                .fitCenter()
                .into(holder.circleImageView);
        holder.textViewTitle.setText(title.get(position));
        holder.textViewDate.setText(date.get(position));
        holder.textViewAmount.setText(amount.get(position));

    }

    @Override
    public int getItemCount() {
        return amount.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView textViewTitle,textViewDate,textViewAmount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.card_reward_image);
            textViewAmount=itemView.findViewById(R.id.card_reward_amount);
            textViewDate=itemView.findViewById(R.id.card_reward_date);
            textViewTitle=itemView.findViewById(R.id.card_reward_title);
        }
    }
}
