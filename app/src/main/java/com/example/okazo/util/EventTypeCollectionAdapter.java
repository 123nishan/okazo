package com.example.okazo.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.okazo.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;

public class EventTypeCollectionAdapter extends RecyclerView.Adapter<EventTypeCollectionAdapter.MyViewHolder> {
    private ArrayList<String> eventType=new ArrayList<>();
    private ArrayList<String> eventTypeImage=new ArrayList<>();
    private Context context;
    public EventTypeCollectionAdapter.OnClickListener onClickListener;
    public  EventTypeCollectionAdapter(ArrayList<String> eventType,ArrayList<String> eventTypeImage,Context context){
        this.eventType=eventType;
        this.eventTypeImage=eventTypeImage;
        this.context=context;


    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_map_event_type,parent,false);
        return new EventTypeCollectionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String imagePath=KEY_IMAGE_ADDRESS+(eventTypeImage.get(position));

        holder.textView.setText(eventType.get(position));
        Glide.with(context)
                .load(Uri.parse(imagePath))
                .placeholder(R.drawable.ic_place_holder_background)
                .centerCrop()
                .into(holder.imageView);
    }
    public interface OnClickListener{
        void OnClick(int position,ArrayList<String> eventType);
    }
    public void setOnClickListener(EventTypeCollectionAdapter.OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }
    @Override
    public int getItemCount() {
        return eventType.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        CardView cardView;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.card_event_fragment_event_type);
            cardView=itemView.findViewById(R.id.card_event_fragment_card);
            imageView=itemView.findViewById(R.id.card_event_fragment_image);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onClickListener!=null){
                        onClickListener.OnClick(position,eventType);
                    }
                }
            });
        }
    }
}
