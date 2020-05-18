package com.example.okazo.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okazo.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAllTicketAdapter extends RecyclerView.Adapter<UserAllTicketAdapter.MyViewHolder> {
    ArrayList<String> name,price,quanity,id;
    OnAddClickListener onAddClickListener;
    OnSubClickListener onSubClickListener;
    public UserAllTicketAdapter(ArrayList<String>name, ArrayList<String> price,ArrayList<String> quanity,ArrayList<String> id){
        this.name=name;
        this.quanity=quanity;
        this.price=price;
        this.id=id;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_all_ticket,parent,false);
        return new UserAllTicketAdapter.MyViewHolder(view);
    }

    public void removeItem(int position) {
        name.remove(position);
        price.remove(position);
        quanity.remove(position);
        id.remove(position);


        notifyItemRemoved(position);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(getItemCount()==1){
            holder.textViewName.setText("Ticket Price: ");
        }else {
            holder.textViewName.setText(name.get(position).toUpperCase());
        }
        holder.textViewPrice.setText("Rs. "+price.get(position));
        holder.textViewQuantity.setText(quanity.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public interface OnAddClickListener{
        void onAddClick(int position);
    }
    public interface OnSubClickListener{
        void onSubClick(int position);
    }
    public void setOnAddClickListener (OnAddClickListener onAddClickListener){
        this.onAddClickListener=onAddClickListener;
    }
    public void setOnSubClickListener(OnSubClickListener onSubClickListener){
        this.onSubClickListener=onSubClickListener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageViewAdd,circleImageViewSub;
        TextView textViewName,textViewPrice,textViewQuantity;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageViewAdd=itemView.findViewById(R.id.card_user_all_ticket_add);
            circleImageViewSub=itemView.findViewById(R.id.card_user_all_ticket_sub);
            textViewName=itemView.findViewById(R.id.card_user_all_ticket_name);
            textViewPrice=itemView.findViewById(R.id.card_user_all_ticket_price);
            textViewQuantity=itemView.findViewById(R.id.card_user_all_ticket_quantity);
            circleImageViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onAddClickListener!=null){
                        onAddClickListener.onAddClick(position);
                    }
                }
            });

            circleImageViewSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onSubClickListener!=null){
                        onSubClickListener.onSubClick(position);
                    }
                }
            });
        }
    }
}
