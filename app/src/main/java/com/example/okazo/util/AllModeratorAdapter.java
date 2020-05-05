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

public class AllModeratorAdapter  extends RecyclerView.Adapter<AllModeratorAdapter.MyViewHolder>{
    ArrayList<String>name,email,image,id,status;
    String type;
    AllModeratorAdapter.OnRemoveClickListener onRemoveClickListener;
    Context context;
    public AllModeratorAdapter(ArrayList<String> name,ArrayList<String>email,ArrayList<String> image,ArrayList<String> id,Context context,String type,ArrayList<String> status){
        this.email=email;
        this.name=name;
        this.id=id;
        this.image=image;
        this.context=context;
        this.type=type;
        this.status=status;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_moderator,parent,false);
        return new AllModeratorAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.textViewName.setText(name.get(position));
            holder.textViewEmail.setText(email.get(position));
        String imagePath=KEY_IMAGE_ADDRESS+(image.get(position));
        Glide.with(context)
                .load(Uri.parse(imagePath))
                .placeholder(R.drawable.ic_place_holder_background)
                //.error(R.drawable.ic_image_not_found_background)
                .centerCrop()
                .into(holder.circleImageView);
        if(status.get(position).equals("Accepted")){
            holder.textViewStatus.setVisibility(View.GONE);
        }else {
            holder.textViewStatus.setVisibility(View.VISIBLE);
            holder.textViewStatus.setText("Request Pending");
        }
        if(type.equals("Admin") || type.equals("Editor")){

        }else {
            holder.cardView.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public interface OnRemoveClickListener{
        void onRemoveClick(int position);
    }
    public void setOnRemoveClickListener(OnRemoveClickListener onRemoveClickListener){
        this.onRemoveClickListener=onRemoveClickListener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName,textViewEmail,textViewStatus;
        CircleImageView circleImageView;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEmail=itemView.findViewById(R.id.card_moderator_email);
            textViewName=itemView.findViewById(R.id.card_moderator_name);
            circleImageView=itemView.findViewById(R.id.card_moderator_image);
            textViewStatus=itemView.findViewById(R.id.card_moderator_status);
            cardView=itemView.findViewById(R.id.card_moderator_remove_user);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onRemoveClickListener!=null){
                        onRemoveClickListener.onRemoveClick(position);
                    }
                }
            });
        }
    }
}
