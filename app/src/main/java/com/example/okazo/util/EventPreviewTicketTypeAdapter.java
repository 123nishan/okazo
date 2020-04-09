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

import com.example.okazo.EventDetailPreviewActivity;
import com.example.okazo.R;

import java.util.List;

public class EventPreviewTicketTypeAdapter extends ArrayAdapter<String>  {
    Context context;
    List<String> ticketTypeName,ticketTypePrice,ticketTypeNumber;
    public EventPreviewTicketTypeAdapter(Context context,List<String> ticketTypeName,List<String> ticketTypePrice,List<String> ticketTypeNumber){
        super(context, R.layout.card_ticket_type,R.id.card_ticket_type_name,ticketTypeName);
        this.context=context;
        this.ticketTypeName=ticketTypeName;
        this.ticketTypePrice=ticketTypePrice;
        this.ticketTypeNumber=ticketTypeNumber;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.card_ticket_type,parent,false);
        TextView textViewName=view.findViewById(R.id.card_ticket_type_name);
        Log.d("testList","done");
        TextView textViewPrice=view.findViewById(R.id.card_ticket_type_price);
        TextView textViewNumber=view.findViewById(R.id.card_ticket_type_Number);
        textViewName.setText(ticketTypeName.get(position));
        textViewPrice.setText(ticketTypePrice.get(position));
        textViewNumber.setText(ticketTypeNumber.get(position));
        return view;
    }
}
