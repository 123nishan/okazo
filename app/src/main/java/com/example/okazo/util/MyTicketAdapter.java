package com.example.okazo.util;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okazo.MyTicketActivity;
import com.example.okazo.R;

import java.util.ArrayList;

public class MyTicketAdapter extends RecyclerView.Adapter<MyTicketAdapter.MyViewHolder> {
    public OnClickListener onClickListener;
    ArrayList<String> name,quantity,eventTitle,eventStartDate,eventStartTime;
    public MyTicketAdapter( ArrayList<String> name, ArrayList<String> quantity, ArrayList<String> eventTitle, ArrayList<String> eventStartDate, ArrayList<String> eventStartTime){
        this.name=name;
        this.quantity=quantity;

        this.eventTitle=eventTitle;
        this.eventStartDate=eventStartDate;
        this.eventStartTime=eventStartTime;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_my_ticket,parent,false);

        return new MyTicketAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(name.get(position).equals("NA")){
            holder.textViewTicketName.setText("");
        }else {
            holder.textViewTicketName.setText(name.get(position).toUpperCase());
        }
        holder.textViewEventTitle.setText(eventTitle.get(position));
        holder.textViewStartTime.setText("Time: "+eventStartTime.get(position));
        holder.textViewStartDate.setText("Date: "+eventStartDate.get(position));
        holder.textViewQuantity.setText("Qty: "+quantity.get(position));

    }

    @Override
    public int getItemCount() {

        return name.size();
    }

    public interface OnClickListener{
        void onClick(int position);
    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textViewStartDate, textViewStartTime, textViewEventTitle,textViewTicketName,textViewQuantity;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.card_my_ticket_card);
            textViewEventTitle=itemView.findViewById(R.id.card_my_ticket_event_title);
            textViewQuantity=itemView.findViewById(R.id.card_my_ticket_ticket_quantity);
            textViewStartDate=itemView.findViewById(R.id.card_my_ticket_start_date);
            textViewStartTime=itemView.findViewById(R.id.card_my_ticket_start_time);
            textViewTicketName=itemView.findViewById(R.id.card_my_ticket_ticket_name);
            cardView.setOnClickListener(new View.OnClickListener() {
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
