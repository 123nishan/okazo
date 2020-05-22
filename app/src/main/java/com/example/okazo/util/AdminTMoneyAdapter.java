package com.example.okazo.util;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okazo.Model.User;
import com.example.okazo.R;

import java.util.ArrayList;

public class AdminTMoneyAdapter extends RecyclerView.Adapter<AdminTMoneyAdapter.MyViewHolder> {
    ArrayList<User> userDetails;
    OnButtonClickListener onButtonClickListener;
    public AdminTMoneyAdapter(ArrayList<User> userDetails){
        this.userDetails=userDetails;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_admin_t_money,parent,false);
        return new AdminTMoneyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User object=userDetails.get(position);
            holder.textViewPhone.setText(Html.fromHtml("<b>" + "Phone: " + "</b> " + object.getPhone()));
        holder.textViewName.setText(Html.fromHtml("<b>" + "Name: " + "</b> " + object.getName()));
        holder.textViewEmail.setText(Html.fromHtml("<b>" + "Email: " + "</b> " + object.getEmail()));
        holder.textViewAmount.setText(Html.fromHtml("<b>" + "Amount: " + "</b> " + object.getAmount()));
    }

    @Override
    public int getItemCount() {
        return userDetails.size();
    }
    public interface OnButtonClickListener{
        void onClick(int position);
    }
    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener){
        this.onButtonClickListener=onButtonClickListener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName,textViewEmail,textViewAmount,textViewPhone;
        EditText editTextAmount;
        Button buttonConfirm;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAmount=itemView.findViewById(R.id.card_admin_tmoney_amount);
            textViewEmail=itemView.findViewById(R.id.card_admin_tmoney_email);
            textViewName=itemView.findViewById(R.id.card_admin_tmoney_name);
            textViewPhone=itemView.findViewById(R.id.card_admin_tmoney_phone);
            editTextAmount=itemView.findViewById(R.id.card_admin_t_money_amount);
            buttonConfirm=itemView.findViewById(R.id.card_admin_t_money_confirm);

            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onButtonClickListener!=null){
                        onButtonClickListener.onClick(position);
                    }
                }
            });
        }
    }
}
