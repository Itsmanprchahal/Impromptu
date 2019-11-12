package com.mandywebdesign.impromptu.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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


public class HomeEventsAdapter extends RecyclerView.Adapter<HomeEventsAdapter.ViewHolder> {


    Context context;
    FragmentManager fragmentManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int row = -1;
    String token, value1;
    CardAdapterHelper cardAdapterHelper = new CardAdapterHelper();

    private OnItemClickListener itemClickListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }


    public HomeEventsAdapter(Context context, FragmentManager fragmentManager, String social_token) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.token = social_token;
    }

    @NonNull
    @Override
    public HomeEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_events, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        sharedPreferences = context.getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        viewHolder.eventCategoryname.setText(Home.cate.get(i));
        viewHolder.eventName.setText(Home.Title.get(i));
        viewHolder.date.setText(Home.Time.get(i));
        viewHolder.eventAddress.setText(Home.Address.get(i).toString());


        if (Home.Cost.get(i).equals("0")) {
            viewHolder.eventPrice.setText("Free");
        } else if (!Home.Cost.get(i).equals("0") && !Home.Cost.get(i).equals("Paid"))
        {
            viewHolder.eventPrice.setText("£ "+Home.Cost.get(i));
        }else {
            viewHolder.eventPrice.setText(Home.Cost.get(i));
        }


        Glide.with(context).load(Home.Image.get(i)).into(viewHolder.eventImage);
        viewHolder.eventCategoryname.setVisibility(View.VISIBLE);
        viewHolder.addtoFavCheck_box.setVisibility(View.VISIBLE);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = Home.event_id.get(i);
                value1 = Home.fav_id.get(i);
                String hostname = Home.event_host_username.get(i);
                Intent intent = new Intent(context, BookEventActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("event_id", value);
                intent.putExtra("fav_id", value1);
                intent.putExtra("hostname", hostname);
                editor.putString(Constants.itemPosition, String.valueOf(i));
                editor.putString(Constants.eventType, "");
                editor.commit();
                context.startActivity(intent);
                ((Activity) context).finish();

            }
        });

        if (Home.fav_id.get(i).equals("1")) {
            viewHolder.addtoFavCheck_box.setChecked(true);
        } else if (Home.fav_id.get(i).equals("0")) {
            viewHolder.addtoFavCheck_box.setChecked(false);
        }

        viewHolder.addtoFavCheck_box.setEnabled(true);
        viewHolder.addtoFavCheck_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if (!Home.fav_id.get(i).contains("1")) {
                        Call<NormalRetroFav> call = WebAPI.getInstance().getApi().fav(Home.social_token, "application/json", Home.event_id.get(i));
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
                    Call<NormalRetrodeleteFav> call = WebAPI.getInstance().getApi().deletefav(Home.social_token, "application/json", Home.event_id.get(i));
                    call.enqueue(new Callback<NormalRetrodeleteFav>() {
                        @Override
                        public void onResponse(Call<NormalRetrodeleteFav> call, Response<NormalRetrodeleteFav> response) {

                            if (response.body() != null) {

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
        return Home.Title.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName, date, eventCategoryname, relatedeventstext, eventAddress, eventPrice;
        CheckBox addtoFavCheck_box;

        public ViewHolder(final View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.event_image);
            eventName = itemView.findViewById(R.id.event_name);
            eventCategoryname = itemView.findViewById(R.id.custom_category_name);
            relatedeventstext = itemView.findViewById(R.id.related_events_text);
            eventAddress = itemView.findViewById(R.id.custom_text1);
            eventPrice = itemView.findViewById(R.id.event_price);
            date = itemView.findViewById(R.id.date);
            addtoFavCheck_box = itemView.findViewById(R.id.book_event_favourite_checkBox);
        }

    }
}