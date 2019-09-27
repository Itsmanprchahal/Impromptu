package com.mandywebdesign.impromptu.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEventDetailAcitvity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AddEvents.Add_Event_Activity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.History;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.Rating;
import com.mandywebdesign.impromptu.Utils.Constants;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Business_History_adapter extends RecyclerView.Adapter<Business_History_adapter.ViewHolder> {

    CardAdapterHelper cardAdapterHelper = new CardAdapterHelper();
    Context context;
    FragmentManager manager;
    SharedPreferences sharedPreferences;
    String token;
    SharedPreferences.Editor editor;
    Dialog progressDialog;

    public Business_History_adapter(Context context, FragmentManager manager, String token) {
        this.context = context;
        this.manager = manager;
        this.token = token;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_events, viewGroup, false);
        progressDialog = ProgressBarClass.showProgressDialog(context);
        progressDialog.dismiss();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        sharedPreferences = context.getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        viewHolder.overall_rating.setVisibility(View.GONE);
        viewHolder.relist.setVisibility(View.VISIBLE);
        viewHolder.date.setText(History.eventTime.get(i));
        viewHolder.eventName.setText(History.title.get(i));

        if (History.prices.get(i).equals("0")) {
            viewHolder.evetPrice.setText("Free");
        } else {
            viewHolder.evetPrice.setText("£ " + History.prices.get(i));
        }


        String s = History.addres.get(i);
        Log.e("addre", s);

        if (s.contains(" NearBy ")) {
            String[] arrayString = s.split(" NearBy ");

            String add1 = arrayString[1];

            Log.e("add1", add1);
            viewHolder.eventAddress.setText("Landmark " + add1);

        } else {
            viewHolder.eventAddress.setText(History.addres.get(i));
        }
        viewHolder.category.setText(History.categois.get(i));
        Glide.with(context).load(History.images.get(i)).into(viewHolder.eventImage);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = History.event_id.get(i);
                Intent intent = new Intent(context, BusinessEventDetailAcitvity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("event_id", value);
                intent.putExtra("eventType", "history");
                editor.putString(Constants.itemPosition, String.valueOf(i));
                editor.commit();

                context.startActivity(intent);


            }
        });

        viewHolder.relist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = History.event_id.get(i);
                Intent intent = new Intent(context, Add_Event_Activity.class);
                intent.putExtra("editevent", "republish");
                intent.putExtra("value", value);
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return History.name1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView eventImage;
        TextView eventName, eventAddress;
        TextView evetPrice, category, date;
        Button relist;
        RatingBar overall_rating;

        public ViewHolder(final View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.event_image);
            eventName = itemView.findViewById(R.id.event_name);
            evetPrice = itemView.findViewById(R.id.event_price);
            eventAddress = itemView.findViewById(R.id.custom_text1);
            category = itemView.findViewById(R.id.custom_category_name);
            date = itemView.findViewById(R.id.date);
            relist = itemView.findViewById(R.id.relist);
            overall_rating = itemView.findViewById(R.id.overall_rating);
        }
    }
}
