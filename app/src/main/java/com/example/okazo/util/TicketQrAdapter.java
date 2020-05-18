package com.example.okazo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.okazo.R;


import java.util.ArrayList;



public class TicketQrAdapter extends RecyclerView.Adapter<TicketQrAdapter.MyViewHolder> {

    Context context;
    String [] count=new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

    ArrayList<String> id,name;
    public TicketQrAdapter(ArrayList<String> id,ArrayList<String>name,Context context){
        this.id=id;
        this.name=name;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ticket_qr,parent,false);
        return new TicketQrAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        String imagePath="https://api.qrserver.com/v1/create-qr-code/?size=150x150&data="+name.get(position)+id.get(position)+count[position];
        Glide.with(context)
                .load(Uri.parse(imagePath))
                .placeholder(R.drawable.ic_place_holder_background)
                .centerCrop()
                .into(holder.imageViewQr);
        if(name.get(position).equals("NA")){
            holder.textViewName.setText("Normal");
        }else {
            holder.textViewName.setText(name.get(position));
        }
        holder.textViewId.setText("Ticket Id: "+id.get(position)+count[position]);
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewQr;
        TextView textViewId,textViewName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewQr=itemView.findViewById(R.id.card_ticket_qr_image);
            textViewId=itemView.findViewById(R.id.card_ticket_qr_id);
            textViewName=itemView.findViewById(R.id.card_ticket_qr_name);
        }
    }
}
