package com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mandywebdesign.impromptu.Adapters.CardAdapterHelper;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEventDetailAcitvity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AttendingTab.Upcoming;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Home;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.favouriteTab.Hosts;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Utils.Constants;
import com.mandywebdesign.impromptu.ui.BookEventActivity;

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
        } else if (!Hosts.prices_fav.get(i).equals("0") && !Hosts.prices_fav.get(i).equals("Paid"))
        {
            viewHolder.evetPrice.setText("£ "+Hosts.prices_fav.get(i));
        }else
        {
            viewHolder.evetPrice.setText(Hosts.prices_fav.get(i));
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

                String value = Hosts.event_id_fav.get(i);
                Intent intent = new Intent(context, BookEventActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("event_id",value);
                intent.putExtra("eventType","fav");
                intent.putExtra("other_events","");
                intent.putExtra("lat", Home.lat);
                intent.putExtra("lng",Home.lng);
                intent.putExtra("hostname", Hosts.name1_fav.get(i));
                editor.putString(Constants.itemPosition, String.valueOf(i));
                editor.commit();
                context.startActivity(intent);

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
