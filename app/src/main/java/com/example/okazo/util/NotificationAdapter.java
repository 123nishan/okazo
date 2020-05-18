package com.example.okazo.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okazo.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{
    ArrayList<String>name,type;
    OnClickListener onClickListener;
    public NotificationAdapter(ArrayList<String> name,ArrayList<String> type){
        this.name=name;
        this.type=type;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notification,parent,false);
        return new NotificationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.textViewModeratorType.setText("For "+type.get(position));
            holder.textViewEventName.setText("From "+name.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public void removeItem(int position) {
        name.remove(position);
        type.remove(position);



        notifyItemRemoved(position);
    }

    public interface  OnClickListener{
        void onClick(int position);
    }
    public void setOnClickListener(OnClickListener onClickListener)
    {
        this.onClickListener=onClickListener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        Button buttonAccept;
        TextView textViewEventName,textViewModeratorType;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonAccept=itemView.findViewById(R.id.card_notification_accept);
            textViewEventName=itemView.findViewById(R.id.card_notification_event_title);
            textViewModeratorType=itemView.findViewById(R.id.card_notification_event_post);
            buttonAccept.setOnClickListener(new View.OnClickListener() {
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
