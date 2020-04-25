package com.example.okazo.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okazo.Model.EventDetail;
import com.example.okazo.R;

import java.util.ArrayList;

public class EventTypeAdapter extends RecyclerView.Adapter<EventTypeAdapter.MyViewHolder> {
    private ArrayList<EventDetail> eventDetails=new ArrayList<>();
    private String parentClass;

    public EventTypeAdapter.OnRemoveClickListner removeClickListner;

    public interface OnRemoveClickListner{
        void onRemoveClick(int position,ArrayList<EventDetail> eventDetails);
    }
    public void setRemoveClickListner(OnRemoveClickListner removeClickListner){
        this.removeClickListner=removeClickListner;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event_type,parent,false);
        return new MyViewHolder(view);
    }
    public  EventTypeAdapter(ArrayList<EventDetail> eventDetails,String parentClass){
        this.eventDetails=eventDetails;

        this.parentClass=parentClass;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EventDetail detail=eventDetails.get(position);
        holder.textViewEventType.setText(detail.getEventType());


        if(parentClass.equals("preview") || parentClass.equals("eventFragment")){
            holder.removeEventType.setVisibility(View.GONE);

        }
        else {
            holder.removeEventType.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {


        return eventDetails.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewEventType;
        ImageButton removeEventType;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewEventType=itemView.findViewById(R.id.card_event_detail_event_type);
            removeEventType=itemView.findViewById(R.id.card_event_detail_remove);
            removeEventType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && removeClickListner!=null){
                        removeClickListner.onRemoveClick(position,eventDetails);
                    }

                }
            });

        }
    }
}
