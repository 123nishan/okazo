package com.example.okazo.util;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okazo.Model.User;
import com.example.okazo.R;

import java.util.ArrayList;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.MyViewHolder>{
    ArrayList<User> userDetail;
    OnBlockClickListener onBlockClickListener;
    OnUnBlockClickListener onUnBlockClickListener;
    public AdminUserAdapter( ArrayList<User> userDetail){
        this.userDetail=userDetail;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_admin_user,parent,false);
        return new AdminUserAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            User object=userDetail.get(position);
            holder.textViewName.setText(Html.fromHtml("<b>" + "Name: " + "</b> " + object.getName()));
            holder.textViewPhone.setText(Html.fromHtml("<b>" + "Phone: " + "</b> " + object.getPhone()));
            holder.textViewEmail.setText(Html.fromHtml("<b>" + "Email: " + "</b> " + object.getEmail()));
            holder.textViewCreatedAt.setText(Html.fromHtml("<b>" + "Account Created at: " + "</b> " + object.getCreatedAt()));
        if(object.getStatus().equals("1")){
            holder.buttonBlock.setVisibility(View.VISIBLE);
            holder.buttonUnBlock.setVisibility(View.GONE);
        }else {
            holder.buttonBlock.setVisibility(View.GONE);
            holder.buttonUnBlock.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        Log.d("ADA",userDetail.size()+"");
        return userDetail.size();
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
        TextView textViewName,textViewPhone,textViewEmail,textViewCreatedAt;
        Button buttonBlock,buttonUnBlock;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName=itemView.findViewById(R.id.card_admin_user_name);
            textViewCreatedAt=itemView.findViewById(R.id.card_admin_user_created_at);
            textViewEmail=itemView.findViewById(R.id.card_admin_user_email);
            textViewPhone=itemView.findViewById(R.id.card_admin_user_phone);
            buttonBlock=itemView.findViewById(R.id.card_admin_user_block);
            buttonUnBlock=itemView.findViewById(R.id.card_admin_user_unblock);

            buttonBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onBlockClickListener!=null){
                        onBlockClickListener.onBlockClick(position);
                    }
                }
            });

            buttonUnBlock.setOnClickListener(new View.OnClickListener() {
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
