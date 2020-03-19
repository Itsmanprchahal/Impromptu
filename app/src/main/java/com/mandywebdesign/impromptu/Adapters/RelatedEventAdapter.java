package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Retrofit.NormalRetroFav;
import com.mandywebdesign.impromptu.Retrofit.NormalRetrodeleteFav;
import com.mandywebdesign.impromptu.Utils.Constants;
import com.mandywebdesign.impromptu.ui.BookEventActivity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Home;
import com.mandywebdesign.impromptu.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelatedEventAdapter extends RecyclerView.Adapter<RelatedEventAdapter.ViewHolder> {

    Context context;
    FragmentManager manager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String token;
    String category;
    public View view;


    public RelatedEventAdapter(Context context, FragmentManager manager, String social_token,String category) {
        this.context = context;
        this.manager = manager;
        this.token = social_token;
        this.category = category;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_events, viewGroup, false);
        // cardAdapterHelper.onCreateViewHolder(viewGroup,view);
        return new RelatedEventAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {


        sharedPreferences = context.getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        viewHolder.eventName.setText(Home.rel_Title.get(i));
        Glide.with(context).load(Home.rel_image.get(i)).into(viewHolder.eventImage);
        viewHolder.event_category.setText(Home.rel_category_name.get(i));
        viewHolder.date.setText(Home.rel_time.get(i));

        String s = Home.rel_address1.get(i);
        Log.e("addre", s);


        viewHolder.eventAddress.setText(Home.rel_address1.get(i));

        if (Home.rel_cost.get(i).equals("0")) {
            viewHolder.event_price.setText("Free");
        } else if (!Home.rel_cost.get(i).equals("0") && !Home.rel_cost.get(i).equals("Paid"))
        {
            viewHolder.event_price.setText("£ "+Home.rel_cost.get(i));
        }else {
            viewHolder.event_price.setText(Home.rel_cost.get(i));
        }


        viewHolder.relatedText.setVisibility(View.GONE);
        viewHolder.event_category.setVisibility(View.VISIBLE);
        viewHolder.addtoFavCheck_box.setVisibility(View.VISIBLE);

        viewHolder.relatedText.setText("Back to your events");
        if (i > -1) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.recylerfirstitem);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
             viewHolder.itemView.setAnimation(animation);
            animation.start();
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = Home.rel_eventID.get(i);
                String fav_id = Home.rel_fav_id.get(i);
                String hostname = Home.rel_hostname.get(i);
                Intent intent = new Intent(context, BookEventActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("event_id",value);
                intent.putExtra("fav_id",fav_id);
                intent.putExtra("lat",Home.lat);
                intent.putExtra("lng",Home.lng);
                intent.putExtra("hostname",hostname);
                editor.putString(Constants.eventType, String.valueOf(i));
                editor.putString(Constants.Category,category);
                editor.commit();
                context.startActivity(intent);

            }
        });


        if (viewHolder.getAdapterPosition() == 0 ) {
            viewHolder.relatedText.setVisibility(View.VISIBLE);
        }


        viewHolder.relatedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.clear();

                Home.recyclerView.setVisibility(View.VISIBLE);
                Home.relatedEventsRecyclerView.setVisibility(View.GONE);
                Home.textView.setVisibility(View.GONE);
                Home.see_related.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.addtoFavCheck_box.setEnabled(false);
        if (Home.rel_fav_id.get(i).equals("1")) {
            viewHolder.addtoFavCheck_box.setChecked(true);
        } else if (Home.rel_fav_id.get(i).equals("0")) {
            viewHolder.addtoFavCheck_box.setChecked(false);
        }
        viewHolder.addtoFavCheck_box.setEnabled(true);
        viewHolder.addtoFavCheck_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if (!Home.rel_fav_id.get(i).contains("1")) {
                        Call<NormalRetroFav> call = WebAPI.getInstance().getApi().fav(Home.social_token, "application/json", Home.rel_eventID.get(i));
                        call.enqueue(new Callback<NormalRetroFav>() {
                            @Override
                            public void onResponse(Call<NormalRetroFav> call, Response<NormalRetroFav> response) {
                                //Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<NormalRetroFav> call, Throwable t) {

                            }
                        });
                    }

                } else {
                    Call<NormalRetrodeleteFav> call = WebAPI.getInstance().getApi().deletefav(Home.social_token, "application/json", Home.rel_eventID.get(i));
                    call.enqueue(new Callback<NormalRetrodeleteFav>() {
                        @Override
                        public void onResponse(Call<NormalRetrodeleteFav> call, Response<NormalRetrodeleteFav> response) {
                        if (response.body()!=null)
                        {

                        }
                            //  Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<NormalRetrodeleteFav> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return Home.rel_Title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName, event_category, date, eventAddress, relatedText, event_price;
        CheckBox addtoFavCheck_box;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            eventImage = itemView.findViewById(R.id.event_image);
            eventName = itemView.findViewById(R.id.event_name);
            event_category = itemView.findViewById(R.id.custom_category_name);
            relatedText = itemView.findViewById(R.id.related_events_text);
            event_price = itemView.findViewById(R.id.event_price);
            eventAddress = itemView.findViewById(R.id.custom_text1);
            date = itemView.findViewById(R.id.date);
            addtoFavCheck_box = itemView.findViewById(R.id.book_event_favourite_checkBox);
        }
    }
}