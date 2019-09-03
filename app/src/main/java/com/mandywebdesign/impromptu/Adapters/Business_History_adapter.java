package com.mandywebdesign.impromptu.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
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
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEvent_detailsFragment;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.Drafts;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.History;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.Live;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.Rating;
import com.mandywebdesign.impromptu.Utils.Constants;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Business_History_adapter extends RecyclerView.Adapter<Business_History_adapter.ViewHolder> {

    CardAdapterHelper cardAdapterHelper= new CardAdapterHelper();
    Context context;
    FragmentManager manager;
    SharedPreferences sharedPreferences;
    String token;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    public Business_History_adapter(Context context,FragmentManager manager,String token) {
        this.context = context;
        this.manager = manager;
        this.token = token;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_events,viewGroup,false);
        progressDialog = ProgressBarClass.showProgressDialog( context,"Please wait...");
        progressDialog.dismiss();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        sharedPreferences = context.getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        viewHolder.overall_rating.setVisibility(View.VISIBLE);
        viewHolder.ratenow_bt.setVisibility(View.VISIBLE);
        viewHolder.date.setText(History.eventTime.get(i));
        viewHolder.eventName.setText(History.title.get(i));
        if (History.prices.get(i).equals("0"))
        {
            viewHolder.evetPrice.setText("Free");
        }else {
            viewHolder.evetPrice.setText("£ "+History.prices.get(i));
        }

        if (History.ratingstatus.get(i).equals("no"))
        {
            viewHolder.ratenow_bt.setVisibility(View.VISIBLE);
        }else {
            viewHolder.ratenow_bt.setVisibility(View.GONE);
        }


            viewHolder.overall_rating.setRating(Float.parseFloat(History.rating_overall.get(i)));



        String s = History.addres.get(i);
        Log.e("addre",s );

        if (s.contains(" NearBy "))
        {
            String[] arrayString = s.split(" NearBy ");

            String add1 = arrayString[1];

            Log.e("add1",add1);
            viewHolder.eventAddress.setText("Landmark "+add1);

        }else {
            viewHolder.eventAddress.setText(History.addres.get(i));
        }
        viewHolder.category.setText(History.categois.get(i));
        Glide.with(context).load(History.images.get(i)).into(viewHolder.eventImage);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String value = History.event_id.get(i);
                bundle.putString("event_id", value);
                bundle.putString("eventType","history");

                editor.putString(Constants.itemPosition, String.valueOf(i));
                editor.commit();

                BusinessEvent_detailsFragment businessEvent_detailsFragment = new BusinessEvent_detailsFragment();
                businessEvent_detailsFragment.setArguments(bundle);
                manager.beginTransaction().replace(R.id.home_frame_layout,businessEvent_detailsFragment).commit();
                Home_Screen.countt=1;

            }
        });

        viewHolder.ratenow_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                progressDialog.show();
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_rating_box);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);

                final RatingBar ratingBar = dialog.findViewById(R.id.rating_bar);
                final EditText feedback = dialog.findViewById(R.id.feedback);
                Button dialogratingshare_button = dialog.findViewById(R.id.dialogratingshare_button);
                dialog.show();

                dialogratingshare_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String rating = String.valueOf(ratingBar.getRating());
                        String feedbck = feedback.getText().toString();
                        if (rating.equals("") | feedbck.equals("")) {
                            Toast.makeText(context, "Add Rating  and reviews", Toast.LENGTH_SHORT).show();
                        } else {
                            Call<Rating> call = WebAPI.getInstance().getApi().rating("Bearer " + token, History.event_id.get(i), rating, feedbck);
                            call.enqueue(new Callback<Rating>() {
                                @Override
                                public void onResponse(Call<Rating> call, Response<Rating> response) {
                                    if (response.body()!=null) {
                                        progressDialog.dismiss();
                                        dialog.dismiss();
                                        if (response.body().getStatus().equals("200"))
                                        {
                                            viewHolder.ratenow_bt.setVisibility(View.GONE);
                                        }
//                                        Toast.makeText(context, ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Rating> call, Throwable t) {
                                        progressDialog.dismiss();
                                    Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return History.name1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView eventImage;
        TextView eventName,eventAddress;
        TextView evetPrice,category,date;
        Button ratenow_bt;
        RatingBar overall_rating;

        public ViewHolder(final View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.event_image);
            eventName = itemView.findViewById(R.id.event_name);
            evetPrice = itemView.findViewById(R.id.event_price);
            eventAddress = itemView.findViewById(R.id.custom_text1);
            category = itemView.findViewById(R.id.custom_category_name);
            date = itemView.findViewById(R.id.date);
            ratenow_bt = itemView.findViewById(R.id.ratenow_bt);
            overall_rating = itemView.findViewById(R.id.overall_rating);
        }
    }
}
