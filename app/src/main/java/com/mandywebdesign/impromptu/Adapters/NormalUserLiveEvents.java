package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.NormalGetProfile;

public class NormalUserLiveEvents extends RecyclerView.Adapter<NormalUserLiveEvents.ViewHolder> {

    Context context;

    public NormalUserLiveEvents(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.business_vustom_event_thumbs,viewGroup,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.category.setText(NormalGetProfile.eventTitle.get(i));
        Glide.with(context).load(NormalGetProfile.images.get(i)).into(viewHolder.imageView);

    }


    @Override
    public int getItemCount() {
        return NormalGetProfile.eventTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        ImageView imageView;

        public ViewHolder(final View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.business_event_name_thumb);
            imageView = itemView.findViewById(R.id.business_event_thumb);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //manager.beginTransaction().replace(R.id.home_frame_layout,new BusinessEvent_detailsFragment()).addToBackStack("Live").commit();
                }
            });
        }
    }
}
