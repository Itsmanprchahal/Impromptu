package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandywebdesign.impromptu.R;



public class Event_RecyclerAdapter extends RecyclerView.Adapter<Event_RecyclerAdapter.ViewHolder> {

    Context context;
    int images[];
    CardAdapterHelper cardAdapterHelper= new CardAdapterHelper();

    public Event_RecyclerAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
    }


    @NonNull
    @Override
    public Event_RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_events,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        cardAdapterHelper.onBindViewHolder(viewHolder.itemView,i,getItemCount());
        viewHolder.eventImage.setImageResource(images[i]);
//        viewHolder.eventName.setText(title[i]);


    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView eventImage;
        TextView eventName;

        public ViewHolder(final View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.event_image);
            eventName = itemView.findViewById(R.id.event_name);


        }
    }
}
