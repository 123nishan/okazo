package com.example.okazo.util;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.R;

import java.util.ArrayList;

import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;

public class MapEventTypeAdapter extends RecyclerView.Adapter<MapEventTypeAdapter.MyViewHolder> {
    private ArrayList<String> eventDetails=new ArrayList<>();
    private ArrayList<String> eventTypeImage=new ArrayList<>();
    private Context context;
    public MapEventTypeAdapter.OnClickListener onClickListener;
    public  MapEventTypeAdapter(ArrayList<String> eventDetails,ArrayList<String> eventTypeImage,Context context){
        this.eventDetails=eventDetails;
        this.eventTypeImage=eventTypeImage;
        this.context=context;


    }
    public interface OnClickListener{
        void OnClick(int position,ArrayList<String> eventDetail);
    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_map_event_type,parent,false);
        return new MapEventTypeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        detail=eventDetails.get(position);
        String imagePath=KEY_IMAGE_ADDRESS+(eventTypeImage.get(position));
        holder.textView.setText(eventDetails.get(position));
        Glide.with(context)
                .load(Uri.parse(imagePath))
                .placeholder(R.drawable.ic_place_holder_background)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return eventDetails.size();
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
                        onClickListener.OnClick(position,eventDetails);
                    }
                }
            });
        }
    }
}
