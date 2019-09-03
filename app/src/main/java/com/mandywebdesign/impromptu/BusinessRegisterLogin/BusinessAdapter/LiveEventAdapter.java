package com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandywebdesign.impromptu.Adapters.HomeEventsAdapter;
import com.mandywebdesign.impromptu.Models.AllEventsPojo;
import com.mandywebdesign.impromptu.R;

import java.util.ArrayList;

public class LiveEventAdapter extends RecyclerView.Adapter<LiveEventAdapter.ViewHolder> {

    Context context;
    ArrayList<AllEventsPojo> liveEventThumbs;

    public LiveEventAdapter(Context context, ArrayList<AllEventsPojo> liveEventThumbs) {
        this.context = context;
        this.liveEventThumbs = liveEventThumbs;
    }

    @NonNull
    @Override
    public LiveEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.business_vustom_event_thumbs, viewGroup, false);
        return new LiveEventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            AllEventsPojo pojo = liveEventThumbs.get(i);
            viewHolder.eventTitle.setText(pojo.getTitle());
            viewHolder.thumbsImage.setImageResource(pojo.getImage());
    }





    @Override
    public int getItemCount() {
        return liveEventThumbs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView thumbsImage;
        TextView eventTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            thumbsImage = itemView.findViewById(R.id.business_event_thumb);
            eventTitle = itemView.findViewById(R.id.business_event_name_thumb);
        }
    }

}
