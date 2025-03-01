package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEventDetailAcitvity;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.NormalGetProfile;

public class NormalUserAttendingEvents extends RecyclerView.Adapter<NormalUserAttendingEvents.VeiewHolder> {

    Context context;

    public NormalUserAttendingEvents(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NormalUserAttendingEvents.VeiewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.business_vustom_event_thumbs,viewGroup,false);
        return  new VeiewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NormalUserAttendingEvents.VeiewHolder veiewHolder, final int i) {

        veiewHolder.category.setText(NormalGetProfile.attentingTietle.get(i));
        Glide.with(context).load(NormalGetProfile.attendingimage.get(i)).into(veiewHolder.imageView);

        veiewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessEventDetailAcitvity.class);
                intent.putExtra("eventType","past");
                intent.putExtra("event_id",NormalGetProfile.attentingevent_id.get(i));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return NormalGetProfile.attentingTietle.size();
    }

    public class VeiewHolder extends RecyclerView.ViewHolder {

        TextView category;
        ImageView imageView;

        public VeiewHolder(@NonNull View itemView) {
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
