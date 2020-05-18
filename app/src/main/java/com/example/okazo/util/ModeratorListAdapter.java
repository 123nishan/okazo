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

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;

public class ModeratorListAdapter extends RecyclerView.Adapter<ModeratorListAdapter.MyViewHolder> {
    OnClickListener onClickListener;
    ArrayList<String> eventName,image;
    Context context;
    public ModeratorListAdapter(ArrayList<String> eventName,ArrayList<String>image,Context context){
        this.eventName=eventName;
        this.image=image;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_moderator_list,parent,false);
        return new ModeratorListAdapter.MyViewHolder(view);
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
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
            CircleImageView circleImageViewProfile,circleImageViewLeave;
            TextView textViewName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageViewLeave=itemView.findViewById(R.id.card_moderator_list_leave);
            circleImageViewProfile=itemView.findViewById(R.id.card_moderator_list_event_image);
            textViewName=itemView.findViewById(R.id.card_moderator_list_event_name);
            circleImageViewLeave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onClickListener!=null){
                        onClickListener.onClick(position);
                    }
                }
            });
        }
    }
}
