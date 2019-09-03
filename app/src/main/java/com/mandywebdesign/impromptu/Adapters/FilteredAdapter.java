package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mandywebdesign.impromptu.Filter.FilteredScreen;
import com.mandywebdesign.impromptu.Utils.Constants;
import com.mandywebdesign.impromptu.ui.BookEventFragment;
import com.mandywebdesign.impromptu.R;

public class FilteredAdapter extends RecyclerView.Adapter<FilteredAdapter.ViewHolder> {

    Context context;
    CardAdapterHelper cardAdapterHelper= new CardAdapterHelper();
    FragmentManager manager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public FilteredAdapter(Context context,FragmentManager manager) {
        this.context = context;
        this.manager = manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_events,viewGroup,false);

        return new FilteredAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
//        cardAdapterHelper.onBindViewHolder(viewHolder.itemView,i,getItemCount());
        sharedPreferences = context.getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        viewHolder.eventName.setText(FilteredScreen.title.get(i));
        viewHolder.evetPrice.setText(FilteredScreen.prices.get(i));
        viewHolder.eventAddress.setText(FilteredScreen.addres.get(i));
        viewHolder.category.setText(FilteredScreen.categois.get(i));
        Glide.with(context).load(FilteredScreen.images.get(i)).into(viewHolder.eventImage);
        viewHolder.date.setText(FilteredScreen.event_time.get(i));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String value = FilteredScreen.event_id.get(i);
                bundle.putString("event_id", value);
                bundle.putString("fav_id","0");
                bundle.putString("hostname",FilteredScreen.name1.get(i));

                editor.putString(Constants.itemPosition, String.valueOf(i));
                editor.commit();

                BookEventFragment bookEventFragment = new BookEventFragment();
                bookEventFragment.setArguments(bundle);
//
                manager.beginTransaction().replace(R.id.home_frame_layout,bookEventFragment).addToBackStack(null).commit();
            }
        });
    }



    @Override
    public int getItemCount() {
        return FilteredScreen.name1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView eventImage;
        TextView eventName,eventAddress,date;
        TextView evetPrice,category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.event_image);
            eventName = itemView.findViewById(R.id.event_name);
            evetPrice = itemView.findViewById(R.id.event_price);
            eventAddress = itemView.findViewById(R.id.custom_text1);
            category = itemView.findViewById(R.id.custom_category_name);
            date = itemView.findViewById(R.id.date);
        }
    }
}