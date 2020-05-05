package com.example.okazo.util;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.okazo.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;

public class AddModeratorAdapter extends RecyclerView.Adapter<AddModeratorAdapter.MyViewHolder>{
    AddModeratorAdapter.OnButtonClickListener onButtonClickListener;
    ArrayList<String>name,email,image,id,status;
    Context context;
    public AddModeratorAdapter(ArrayList<String> name, ArrayList<String> email, ArrayList<String> image, Context context, ArrayList<String> id,ArrayList<String> status){
        this.name=name;
        this.email=email;
        this.image=image;
        this.context=context;
        this.id=id;
        this.status=status;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_add_moderator,parent,false);
        return new AddModeratorAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(status.get(position).equals("Accepted")){
            //no thing
        }else {
            holder.textViewName.setText(name.get(position));
            holder.textViewEmail.setText(email.get(position));
            String imagePath = KEY_IMAGE_ADDRESS + (image.get(position));

            Glide.with(context)
                    .load(Uri.parse(imagePath))
                    .placeholder(R.drawable.ic_place_holder_background)
                    .centerCrop()
                    .into(holder.circleImageView);
            if(status.get(position).equals("Pending")){
                holder.button.setVisibility(View.GONE);
                holder.textViewStatus.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    public int getItemCount() {
        return name.size();
    }
    public interface OnButtonClickListener{
        void onButtonClick(int position);
    }
    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener){
        this.onButtonClickListener=onButtonClickListener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView textViewName,textViewEmail,textViewStatus;
        Button button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.card_add_moderator_image);
            textViewEmail=itemView.findViewById(R.id.card_add_moderator_email);
            textViewName=itemView.findViewById(R.id.card_add_moderator_name);
            button=itemView.findViewById(R.id.card_add_moderator_add_button);
            textViewStatus=itemView.findViewById(R.id.card_add_moderator_status);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onButtonClickListener!=null){
                        onButtonClickListener.onButtonClick(position);
                    }
                }
            });
        }
    }
}
