package com.example.okazo.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okazo.R;

import java.util.ArrayList;

public class TicketAdapter  extends RecyclerView.Adapter<TicketAdapter.MyViewHolder>{
    ArrayList<String>name,price;

    public TicketAdapter.OnBuyClickListener onBuyClickListener;
    public OnAddQuantityListener onAddQuantityListener;
    public OnSubQuantityListener onSubQuantityListener;
    public OnAddToCartListener onAddToCartListener;

    public TicketAdapter(ArrayList<String>name,ArrayList<String> price){
        this.name=name;
        this.price=price;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ticket,parent,false);
        return new TicketAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(name.size()>1){
            holder.linearLayout.setVisibility(View.GONE);
            holder.linearLayoutAddToCart.setVisibility(View.VISIBLE);
        }
        if(name.size()==1){
            holder.textViewTicketName.setText("Ticket Price: ");

        }else {

            holder.textViewTicketName.setText(name.get(position));
        }
//        holder.textViewTitle.setText(title);
//        holder.textViewStartDate.setText(startDate);
//        holder.textViewStartTime.setText(startTime);
        holder.textViewPrice.setText("Rs. "+price.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }
    public interface OnBuyClickListener{
        void onBuyClick(int position);
    }
    public interface OnAddQuantityListener{
        void onAddQuantity(int position);
    }
    public interface OnSubQuantityListener{
        void onSubQuantity(int position);
    }
    public interface OnAddToCartListener{
        void onAddToCart(int position);
    }
    public void setOnBuyClickListener (OnBuyClickListener onBuyClickListener){
        this.onBuyClickListener=onBuyClickListener;
    }
    public void setOnAddQuantityListener(OnAddQuantityListener onAddQuantityListener){
        this.onAddQuantityListener=onAddQuantityListener;
    }
    public void setOnAddToCartListener (OnAddToCartListener onAddToCartListener){
        this.onAddToCartListener=onAddToCartListener;
    }
    public void setOnSubQuantityListener(OnSubQuantityListener onSubQuantityListener){
        this.onSubQuantityListener=onSubQuantityListener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTicketName,textViewPrice,textViewQuantity;
        LinearLayout linearLayout,linearLayoutAddToCart;
        CardView cardViewAdd,cardViewSub;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPrice=itemView.findViewById(R.id.card_ticket_price);
//            textViewStartDate=itemView.findViewById(R.id.card_ticket_start_date);
//            textViewStartTime=itemView.findViewById(R.id.card_ticket_start_time);
//            textViewTitle=itemView.findViewById(R.id.card_ticket_title);
            cardViewAdd=itemView.findViewById(R.id.card_ticket_add_quantity);
            cardViewSub=itemView.findViewById(R.id.card_ticket_sub_quantity);
            textViewQuantity=itemView.findViewById(R.id.card_ticket_quantity);
            textViewTicketName=itemView.findViewById(R.id.card_ticket_ticket_name);
            linearLayout=itemView.findViewById(R.id.card_ticket_buy_layout);
            linearLayoutAddToCart=itemView.findViewById(R.id.card_ticket_add_cart_layout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onBuyClickListener!=null){
                        onBuyClickListener.onBuyClick(position);
                    }
                }
            });
            //add
            cardViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onAddQuantityListener!=null){
                        onAddQuantityListener.onAddQuantity(position);
                    }
                }
            });
            //sub
            cardViewSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onSubQuantityListener!=null){
                        onSubQuantityListener.onSubQuantity(position);
                    }
                }
            });
            //add to cart
            linearLayoutAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onAddToCartListener!=null){
                        onAddToCartListener.onAddToCart(position);
                    }
                }
            });
        }
    }
}
