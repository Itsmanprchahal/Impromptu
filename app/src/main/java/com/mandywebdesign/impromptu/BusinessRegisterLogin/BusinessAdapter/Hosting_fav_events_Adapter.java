package com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mandywebdesign.impromptu.Adapters.CardAdapterHelper;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEvent_detailsFragment;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.Live;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.favouriteTab.Hosts;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Utils.Constants;

public class Hosting_fav_events_Adapter extends RecyclerView.Adapter<Hosting_fav_events_Adapter.ViewHolder> {

    Context context;
    FragmentManager manager;
    CardAdapterHelper cardAdapterHelper= new CardAdapterHelper();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Hosting_fav_events_Adapter(Context context, FragmentManager manager) {
        this.context = context;
        this.manager = manager;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_events,viewGroup,false);

        return new Hosting_fav_events_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
       // cardAdapterHelper.onBindViewHolder(viewHolder.itemView,i,getItemCount());
        sharedPreferences = context.getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        viewHolder.eventName.setText(Hosts.title_fav.get(i));
        viewHolder.date.setText(Hosts.Time_fav.get(i));
        viewHolder.eventAddress.setText(Hosts.addres_fav.get(i));

        if (Hosts.prices_fav.get(i).equals("0"))
        {
            viewHolder.evetPrice.setText("Free");
        }else
        {
            viewHolder.evetPrice.setText("£ "+Hosts.prices_fav.get(i));
        }

        String s = Hosts.addres_fav.get(i);
        Log.e("addre",s );

        if (s.contains(" NearBy "))
        {
            String[] arrayString = s.split(" NearBy ");

            String add1 = arrayString[1];

            Log.e("add1",add1);
            viewHolder.eventAddress.setText("Landmark "+add1);

        }else {
            viewHolder.eventAddress.setText(Hosts.addres_fav.get(i));
        }


        viewHolder.category.setText(Hosts.categois_fav.get(i));
        Glide.with(context).load(Hosts.images_fav.get(i)).into(viewHolder.eventImage);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String value = Hosts.event_id_fav.get(i);
                bundle.putString("event_id", value);
                bundle.putString("eventType","fav");
                bundle.putString("other_events","");
                editor.putString(Constants.itemPosition, String.valueOf(i));
                editor.commit();


                BusinessEvent_detailsFragment businessEvent_detailsFragment = new BusinessEvent_detailsFragment();
                businessEvent_detailsFragment.setArguments(bundle);
//
                manager.beginTransaction().replace(R.id.home_frame_layout, businessEvent_detailsFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Hosts.addres_fav.size();
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
