package com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEventDetailAcitvity;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.UsersPastEvent;

import java.util.List;

public class UsersPastEventsAdapter extends RecyclerView.Adapter<UsersPastEventsAdapter.ViewHolder> {
    Context context;
    FragmentManager manager;
    List<UsersPastEvent.Datum> datumArrayList;

    public UsersPastEventsAdapter(Context context, List<UsersPastEvent.Datum> datumArrayList) {
        this.context = context;
        this.datumArrayList = datumArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.business_vustom_event_thumbs, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.category.setText(datumArrayList.get(position).getTitle());
        Log.d("imgUrl+++", "" + datumArrayList.get(position).getFile().get(0));

        Glide.with(context)
                .load(datumArrayList.get(position).getFile().get(0))
                .centerCrop()
                .into(holder.business_event_thumb);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessEventDetailAcitvity.class);
                intent.putExtra("event_id", datumArrayList.get(position).getEventId().toString());
                intent.putExtra("eventType", "past");
                 context.startActivity(intent);
                Log.d("image+++++++", datumArrayList.get(position).getFile().toString());
            }
        });
    }


    @Override
    public int getItemCount() {
        return datumArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        ImageView business_event_thumb;

        public ViewHolder(final View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.business_event_name_thumb);
            business_event_thumb = itemView.findViewById(R.id.business_event_thumb);

        }
    }
}
