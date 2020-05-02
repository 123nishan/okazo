package com.example.okazo.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okazo.EventDetailPreviewActivity;
import com.example.okazo.R;

import java.util.ArrayList;
import java.util.List;




public class EventPreviewTicketTypeAdapter extends RecyclerView.Adapter<EventPreviewTicketTypeAdapter.MyViewHolder>  {
    Context context;
    ArrayList<String> ticketTypeName,ticketTypePrice,ticketTypeNumber;
    public EventPreviewTicketTypeAdapter(ArrayList<String> ticketTypeName, ArrayList<String> ticketTypeNumber, ArrayList<String>ticketTypePrice){
        this.ticketTypeName=ticketTypeName;
        this.ticketTypeNumber=ticketTypeNumber;
        this.ticketTypePrice=ticketTypePrice;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ticket_type,parent,false);

        return new EventPreviewTicketTypeAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.textViewName.setText(ticketTypeName.get(position));
            holder.textViewPrice.setText(ticketTypePrice.get(position));
            holder.textViewNumber.setText(ticketTypeNumber.get(position));
    }

    @Override
    public int getItemCount() {
        return ticketTypeName.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
TextView textViewName,textViewPrice,textViewNumber;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

         textViewPrice=itemView.findViewById(R.id.card_ticket_type_price);
         textViewNumber=itemView.findViewById(R.id.card_ticket_type_Number);
         textViewName=itemView.findViewById(R.id.card_ticket_type_name);
    }
}

}
