package com.example.okazo.util;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okazo.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditTicketAdapter  extends RecyclerView.Adapter<EditTicketAdapter.MyViewHolder>{
    public OnRemoveClickListener onRemoveClickListener;
    ArrayList<String>id,name,price,quanity;
    public EditTicketAdapter(ArrayList<String>id,ArrayList<String>name,ArrayList<String>price,ArrayList<String>quanity){
        this.id=id;
        this.name=name;
        this.quanity=quanity;
        this.price=price;


    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ticket_type,parent,false);

        return new EditTicketAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       // holder.textInputEditTextPrice.setInputType(num);
        if(id.get(position).equals("Empty")){
            holder.linearLayoutPreview.setVisibility(View.GONE);
            holder.linearLayoutEdit.setVisibility(View.VISIBLE);
            holder.circleImageView.setVisibility(View.VISIBLE);
            holder.textInputEditTextName.setText("");
            holder.textInputEditTextQuantity.setText("");
            holder.textInputEditTextPrice.setText("");
        }else {
            holder.linearLayoutPreview.setVisibility(View.GONE);
            holder.linearLayoutEdit.setVisibility(View.VISIBLE);
            holder.circleImageView.setVisibility(View.VISIBLE);
            holder.textInputEditTextName.setText(name.get(position));
            holder.textInputEditTextQuantity.setText(quanity.get(position));
            holder.textInputEditTextPrice.setText(price.get(position));
        }


    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public interface OnRemoveClickListener
    {
        void onRemoveClick(int position,ArrayList<String> id);
    }
    public void setOnRemoveClickListener(OnRemoveClickListener onRemoveClickListener){
        this.onRemoveClickListener=onRemoveClickListener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
    TextInputEditText textInputEditTextPrice,textInputEditTextName,textInputEditTextQuantity;
    CircleImageView circleImageView;
    LinearLayout linearLayoutPreview,linearLayoutEdit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutEdit=itemView.findViewById(R.id.card_ticket_type_Edit);
            linearLayoutPreview=itemView.findViewById(R.id.card_ticket_type_preview);

            circleImageView=itemView.findViewById(R.id.card_ticket_type_remove);

            textInputEditTextName=itemView.findViewById(R.id.card_ticket_type_edit_name);
            textInputEditTextPrice=itemView.findViewById(R.id.card_ticket_type_edit_price);
            textInputEditTextQuantity=itemView.findViewById(R.id.card_ticket_type_edit_quantity);
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onRemoveClickListener!=null){
                        onRemoveClickListener.onRemoveClick(position,id);
                    }
                }
            });
        }
    }
}
