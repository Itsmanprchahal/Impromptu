package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessUserProfile;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.Drafts;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.Live;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.ui.Home_Screen;

public class BusinessuserProfileLiveEvents extends RecyclerView.Adapter<BusinessuserProfileLiveEvents.ViewHolder> {

    Context context;
    FragmentManager manager;

    public BusinessuserProfileLiveEvents(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.business_vustom_event_thumbs,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.category.setText(BusinessUserProfile.profileliveevents.get(i));
        Glide.with(context).load(BusinessUserProfile.images.get(i)).apply(new RequestOptions().override(200,200)).into(viewHolder.imageView);
    }


    @Override
    public int getItemCount() {
        return BusinessUserProfile.profileliveevents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {


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
