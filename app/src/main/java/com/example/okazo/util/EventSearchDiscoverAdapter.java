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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;

public class EventSearchDiscoverAdapter  extends RecyclerView.Adapter<EventSearchDiscoverAdapter.MyViewHolder>{
    ArrayList<String> id, title,location,image,host,date,time,distance;
    OnCardClickListener onCardClickListener;
    Context context;
    public EventSearchDiscoverAdapter(ArrayList<String> id,ArrayList<String> title,ArrayList<String> location,ArrayList<String> image,ArrayList<String> host,ArrayList<String> date,ArrayList<String> time,Context context,ArrayList<String> distance){
        this.id=id;
        this.title=title;
        this.host=host;
        this.image=image;
        this.location=location;
        this.date=date;
        this.context=context;
        this.time=time;
        this.distance=distance;
        Log.d("REC",distance.size()+"");

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event_search_discover,parent,false);

        return new EventSearchDiscoverAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.textViewTitle.setText(title.get(position));
            holder.textViewLocation.setText(location.get(position));
            holder.textViewHost.setText(host.get(position));
            holder.textViewStartTime.setText(time.get(position));
            holder.textViewStartDate.setText(date.get(position));
            holder.textViewDistance.setText(distance.get(position)+" Km far");
        String imagePath=KEY_IMAGE_ADDRESS+(image.get(position));
        Glide.with(context)
                .load(Uri.parse(imagePath))
                .placeholder(R.drawable.ic_place_holder_background)
                //.error(R.drawable.ic_image_not_found_background)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return id.size();
    }
    public interface OnCardClickListener{
        void onCardClick(int position);
    }
    public void setOnCardClickListener(OnCardClickListener onCardClickListener){
        this.onCardClickListener=onCardClickListener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        CircleImageView imageView;
        TextView textViewTitle,textViewLocation,textViewStartDate,textViewStartTime,textViewHost,textViewDistance;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.card_event_search_card);
            textViewTitle=itemView.findViewById(R.id.card_event_search_title);
            textViewLocation=itemView.findViewById(R.id.card_event_search_location);
            textViewStartDate=itemView.findViewById(R.id.card_event_search_date);
            textViewStartTime=itemView.findViewById(R.id.card_event_search_time);
            textViewHost=itemView.findViewById(R.id.card_event_search_host);
            imageView=itemView.findViewById(R.id.card_event_search_image_view);
            textViewDistance=itemView.findViewById(R.id.card_event_search_distance);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && onCardClickListener!=null){
                        onCardClickListener.onCardClick(position);
                    }
                }
            });

        }
    }
}
