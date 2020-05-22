package com.example.okazo.util;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okazo.Model.EventDetail;
import com.example.okazo.R;

import java.util.ArrayList;

public class AdminEventAdapter extends RecyclerView.Adapter<AdminEventAdapter.MyViewHolder>{
    ArrayList<EventDetail> eventDetails;
    OnBlockClickListener onBlockClickListener;
    OnUnBlockClickListener onUnBlockClickListener;
    public  AdminEventAdapter(ArrayList<EventDetail> eventDetails){
        this.eventDetails=eventDetails;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_admin_event,parent,false);

        return new AdminEventAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        EventDetail object=eventDetails.get(position);
            holder.textViewTitle.setText(Html.fromHtml("<b>" + "Title: " + "</b> " + object.getTitle()));
            holder.textViewDetail.setText(Html.fromHtml("<b>" + "Detail: " + "</b> " + object.getDescription()));
            holder.textViewLocation.setText(Html.fromHtml("<b>" + "Place: " + "</b> " + object.getPlace()));
            holder.textViewStartDate.setText(Html.fromHtml("<b>" + "S.Date: " + "</b> " + object.getStartDate()));
            holder.textViewStartTime.setText(Html.fromHtml("<b>" + "S.Time: " + "</b> " + object.getStartTime()));
            holder.textViewEndDate.setText(Html.fromHtml("<b>" + "E.Date: " + "</b> " + object.getEndDate()));
            holder.textViewEndTime.setText(Html.fromHtml("<b>" + "E.Time: " + "</b> " + object.getEndTime()));
            holder.textViewHostName.setText(Html.fromHtml("<b>" + "Host Name: " + "</b> " + object.getHostName()));
            holder.textViewHostNumber.setText(Html.fromHtml("<b>" + "Host Number: " + "</b> " + object.getHostPhone()));
            if(object.getStatus().equals("1")){
                holder.buttonBlock.setVisibility(View.VISIBLE);
                holder.buttonUnblock.setVisibility(View.GONE);
            }else {
                holder.buttonBlock.setVisibility(View.GONE);
                holder.buttonUnblock.setVisibility(View.VISIBLE);
            }

    }

    @Override
    public int getItemCount() {
        return eventDetails.size();
    }

    public interface OnBlockClickListener{
        void onBlockClick(int position);
    }

    public interface OnUnBlockClickListener{
        void onUnBlockClick(int position);
    }

    public void setOnBlockClickListener(OnBlockClickListener onBlockClickListener){
        this.onBlockClickListener=onBlockClickListener;
    }

    public void setOnUnBlockClickListener(OnUnBlockClickListener onUnBlockClickListener){
        this.onUnBlockClickListener=onUnBlockClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,textViewDetail,textViewStartDate,textViewStartTime,textViewEndDate,textViewEndTime,textViewLocation,textViewHostName,textViewHostNumber;
        Button buttonBlock,buttonUnblock;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.card_admin_event_title);
            textViewDetail=itemView.findViewById(R.id.card_admin_event_detail);
            textViewStartDate=itemView.findViewById(R.id.card_admin_event_start_date);
            textViewStartTime=itemView.findViewById(R.id.card_admin_event_start_time);
            textViewEndDate=itemView.findViewById(R.id.card_admin_event_end_date);
            textViewEndTime=itemView.findViewById(R.id.card_admin_event_end_time);
            textViewLocation=itemView.findViewById(R.id.card_admin_event_location);
            textViewHostName=itemView.findViewById(R.id.card_admin_event_host_name);
            textViewHostNumber=itemView.findViewById(R.id.card_admin_event_host_number);
            buttonBlock=itemView.findViewById(R.id.card_admin_event_block);
            buttonUnblock=itemView.findViewById(R.id.card_admin_event_unblock);

            buttonBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onBlockClickListener!=null){
                        onBlockClickListener.onBlockClick(position);
                    }
                }
            });

            buttonUnblock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onUnBlockClickListener!=null){
                        onUnBlockClickListener.onUnBlockClick(position);
                    }
                }
            });
        }
    }
}
