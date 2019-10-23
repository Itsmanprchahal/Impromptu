package com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEventDetailAcitvity;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessUserProfile;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.UsersLiveEvent;
import com.mandywebdesign.impromptu.Retrofit.UsersPastEvent;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.NormalGetProfile;
import com.mandywebdesign.impromptu.ui.BookEventActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersLiveEventsAdapter extends RecyclerView.Adapter<UsersLiveEventsAdapter.ViewHolder> {
    Context context;
    FragmentManager manager;
    List<UsersLiveEvent.Datum> datumArrayList;

    public UsersLiveEventsAdapter(Context context, List<UsersLiveEvent.Datum> datumArrayList) {
        this.context = context;
        this.datumArrayList = datumArrayList;
    }

    @NonNull
    @Override
    public UsersLiveEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.business_vustom_event_thumbs,parent,false);

        return new UsersLiveEventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersLiveEventsAdapter.ViewHolder holder, final int position) {
        holder.category.setText(datumArrayList.get(position).getTitle());
        Glide.with(context).load(datumArrayList.get(position).getFile()).apply(new RequestOptions().override(200,200)).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookEventActivity.class);
                intent.putExtra("eventType","live");
                intent.putExtra("event_id",datumArrayList.get(position).getEventId().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datumArrayList.size();
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
