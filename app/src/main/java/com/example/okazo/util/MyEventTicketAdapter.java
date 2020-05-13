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

public class MyEventTicketAdapter extends RecyclerView.Adapter<MyEventTicketAdapter.MyViewHolder> {
    ArrayList<String>title,startDate,startTime,image,totalCount;
    Context context;
    public MyEventTicketAdapter(ArrayList<String> title,ArrayList<String> startDate,ArrayList<String> startTime,ArrayList<String> image,ArrayList<String>totalCount,Context context){
        this.title=title;
        this.startDate=startDate;
        this.startTime=startTime;
        this.image=image;
        this.totalCount=totalCount;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ticket_event,parent,false);
        return new MyEventTicketAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.textViewTitle.setText("Event: "+title.get(position));
            holder.textViewTotalTicket.setText("Total Tickets: "+totalCount.get(position));
            holder.textViewStartTime.setText("Time:  "+startTime.get(position));
            holder.textViewStartDate.setText("Date: "+startDate.get(position));
        String imagePath=KEY_IMAGE_ADDRESS+(image.get(position));

        Glide.with(context)
                .load(Uri.parse(imagePath))
                .placeholder(R.drawable.ic_place_holder_background)
                .centerCrop()
                .into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView textViewTitle,textViewStartDate,textViewStartTime,textViewTotalTicket;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.card_ticket_event_image);
            textViewStartDate=itemView.findViewById(R.id.card_ticket_event_start_date);
            textViewStartTime=itemView.findViewById(R.id.card_ticket_event_start_time);
            textViewTitle=itemView.findViewById(R.id.card_ticket_event_title);
            textViewTotalTicket=itemView.findViewById(R.id.card_ticket_event_total_quantity);
        }
    }
}
