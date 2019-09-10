package com.mandywebdesign.impromptu.Adapters;

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
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEventDetailAcitvity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.Drafts;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Utils.Constants;

public class Business_DraftsEventAdapter extends RecyclerView.Adapter<Business_DraftsEventAdapter.ViewHolder> {

    Context context;
    FragmentManager manager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CardAdapterHelper cardAdapterHelper= new CardAdapterHelper();

    public Business_DraftsEventAdapter(Context context,FragmentManager manager) {
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

//        cardAdapterHelper.onBindViewHolder(viewHolder.itemView,i,getItemCount());
        sharedPreferences = context.getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        viewHolder.eventName.setText(Drafts.title.get(i));

        if (Drafts.prices.get(i).equals("0"))
        {
            viewHolder.evetPrice.setText("Free");
        }else
        {
            viewHolder.evetPrice.setText("£ "+Drafts.prices.get(i));
        }

        String s = Drafts.addres.get(i);
        Log.e("addre",s );

        if (s.contains(" NearBy "))
        {
            String[] arrayString = s.split(" NearBy ");

            String add1 = arrayString[1];

            Log.e("add1",add1);
            viewHolder.eventAddress.setText("Landmark "+add1);

        }else {
            viewHolder.eventAddress.setText(Drafts.addres.get(i));
        }

        viewHolder.category.setText(Drafts.categois.get(i));
        viewHolder.date.setText(Drafts.eventTIme.get(i));
        Glide.with(context).load(Drafts.draftsimages.get(i)).into(viewHolder.eventImage);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = Drafts.event_id.get(i);
                Intent intent = new Intent(context, BusinessEventDetailAcitvity.class);
                intent.putExtra("event_id",value);
                intent.putExtra("eventType","draft");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                editor.putString(Constants.itemPosition, String.valueOf(i));
                editor.commit();
                context.startActivity(intent);

//                Bundle bundle = new Bundle();
//                String value = Drafts.event_id.get(i);
//                bundle.putString("event_id", value);
//                bundle.putString("eventType","draft");
//
//                editor.putString(Constants.itemPosition, String.valueOf(i));
//                editor.commit();
//
//                BusinessEvent_detailsFragment businessEvent_detailsFragment = new BusinessEvent_detailsFragment();
//                businessEvent_detailsFragment.setArguments(bundle);
//
//                manager.beginTransaction().replace(R.id.home_frame_layout,businessEvent_detailsFragment).addToBackStack(null).commit();
////                Toast.makeText(context, ""+Live.event_id.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return Drafts.title.size();
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
