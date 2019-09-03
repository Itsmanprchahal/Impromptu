package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEvent_detailsFragment;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.Live;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Utils.Constants;

public class Business_LiveEventAdapter extends RecyclerView.Adapter<Business_LiveEventAdapter.ViewHolder> {

    Context context;
    FragmentManager manager;
    String S_Token;
    CardAdapterHelper cardAdapterHelper= new CardAdapterHelper();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Business_LiveEventAdapter(Context context,FragmentManager manager) {

        this.context = context;
        this.manager = manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_events,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
      //  cardAdapterHelper.onBindViewHolder(viewHolder.itemView,i,getItemCount());

        sharedPreferences = context.getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        viewHolder.eventName.setText(Live.title.get(i));
        if (Live.prices.get(i).equals("0"))
        {
            viewHolder.evetPrice.setText("Free");
        }else {
            viewHolder.evetPrice.setText("£ "+Live.prices.get(i));
        }


        String s = Live.addres.get(i);
        Log.e("addre",s );

        if (s.contains(" NearBy "))
        {
            String[] arrayString = s.split(" NearBy ");

            String add1 = arrayString[1];

            Log.e("add1",add1);
            viewHolder.eventAddress.setText("Landmark "+add1);

        }else {
            viewHolder.eventAddress.setText(Live.addres.get(i));
        }


        viewHolder.date.setText(Live.event_time.get(i));
        viewHolder.category.setText(Live.categois.get(i));
        Glide.with(context).load(Live.images.get(i)).into(viewHolder.eventImage);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String value = Live.event_id.get(i);
                bundle.putString("event_id", value);
                bundle.putString("eventType","live");
                bundle.putString("event_method","hosting");

                editor.putString(Constants.itemPosition, String.valueOf(i));
                editor.commit();

                BusinessEvent_detailsFragment businessEvent_detailsFragment = new BusinessEvent_detailsFragment();
                businessEvent_detailsFragment.setArguments(bundle);

                FragmentTransaction transaction2 = manager.beginTransaction();
                transaction2.replace(R.id.home_frame_layout, businessEvent_detailsFragment);
                transaction2.addToBackStack(null);
                transaction2.commit();

            }
        });

    }




    @Override
    public int getItemCount() {
        return Live.name1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView eventImage;
        TextView eventName,eventAddress;
        TextView evetPrice,category,date;

        public ViewHolder(final View itemView) {
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
