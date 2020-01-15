package com.mandywebdesign.impromptu.Filter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Adapters.FilteredAdapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.NormalFilterEvents;
import com.mandywebdesign.impromptu.Utils.Util;
import com.mandywebdesign.impromptu.ui.DiscreteScrollViewOptions;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilteredActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener{


    public DiscreteScrollView recyclerView;
    TextView noEvnets;
    FragmentManager manager;
    Dialog progressDialog;
    SharedPreferences sharedPreferences;
    String social_token, timeFrom, formattedDate, getFormattedDate;
    View view;
    ImageView back;
    static String itemPosition="0";
    FilteredAdapter adapter;
    SharedPreferences itemPositionPref;
    private InfiniteScrollAdapter infiniteAdapter;
    public static ArrayList<String> name1 = new ArrayList<>();
    public static ArrayList<String> title = new ArrayList<>();
    public static ArrayList<String> prices = new ArrayList<>();
    public static ArrayList<String> addres = new ArrayList<>();
    public static ArrayList<String> categois = new ArrayList<>();
    public static ArrayList<String> images = new ArrayList<>();
    public static ArrayList<String> event_time = new ArrayList<>();
    public static ArrayList<String> event_id = new ArrayList<>();
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered);

        intent = getIntent();
        String loc = intent.getStringExtra("loc");
        String date = intent.getStringExtra("date");
        final String price = intent.getStringExtra("price");
        String gender = intent.getStringExtra("gender");
        String lat = intent.getStringExtra("lat");
        String lng = intent.getStringExtra("lng");
        String eDate = intent.getStringExtra("edate");
        Log.d("filteredScreen", lat + "  " + lng);

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        social_token = "Bearer " + sharedPreferences.getString("Socailtoken", "");


        init();

        progressDialog = ProgressBarClass.showProgressDialog(FilteredActivity.this);
        progressDialog.show();


        recyclerView.setOrientation(DSVOrientation.HORIZONTAL);
        recyclerView.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new FilteredAdapter(FilteredActivity.this));
        recyclerView.setAdapter(infiniteAdapter);
        recyclerView.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        recyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)

                .build());


        listeners();

        Clear();
        Call<NormalFilterEvents> call = WebAPI.getInstance().getApi().filterEvnt(social_token, "application/json", lat, lng, gender, date,eDate, price);
        call.enqueue(new Callback<NormalFilterEvents>() {
            @Override
            public void onResponse(Call<NormalFilterEvents> call, Response<NormalFilterEvents> response) {

                if (response.body() != null) {

                    if (response.body().getStatus().equals("200")) {
                        progressDialog.dismiss();
                        NormalFilterEvents data = response.body();
                        List<NormalFilterEvents.Datum> datumList = data.getData();

                        if (datumList.size() == 0) {
                            noEvnets.setVisibility(View.VISIBLE);
                        }
                        for (NormalFilterEvents.Datum datum : datumList) {
                            name1.add(datum.getBEventHostname());
                            title.add(datum.getTitle());

                            if (datum.getPrice() != null) {
                                if (datum.getPrice().equals("0")) {

                                    prices.add("Free");
                                } else {
                                    prices.add("£ " + datum.getPrice());
                                }
                            } else {
                                prices.add("Paid");
                            }

                            String time_t = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM", "am").replaceFirst("PM", "pm");

                            Log.d("eventTIme", time_t);


                            timeFrom = removeLeadingZeroes(time_t);
                            if (timeFrom.contains(":00"))
                            {
                                timeFrom = removeLeadingZeroes(time_t).replace(":00","");
                            }else {
                                timeFrom = removeLeadingZeroes(time_t);
                            }

                            Calendar c = Calendar.getInstance();

                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            formattedDate = df.format(c.getTime());
                            c.add(Calendar.DATE, 1);

                            getFormattedDate = df.format(c.getTime());

                            System.out.println("Current time ==> " + c.getTime());

                            if (formattedDate.matches(Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt())))) {
                                event_time.add("Today at " + timeFrom);
                            } else if (getFormattedDate.matches(Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt())))) {
                                event_time.add("Tomorrow at " + timeFrom);
                            } else {
                                String date = Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt()));
                                /*to change server date formate*/
                                String s1 = date;
                                String[] str = s1.split("/");
                                String str1 = str[0];
                                String str2 = str[1];
                                String str3 = str[2];
                                event_time.add(str1 + "/" + str2 + "/" + str3 + " at " + timeFrom);
                            }


                            addres.add(datum.getAddressline1());
                            categois.add(datum.getCategory());
                            images.add(datum.getFile());
                            event_id.add(datum.getEventId().toString());

                            adapter = new FilteredAdapter(FilteredActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                    } else if (response.body().getStatus().equals("400")) {
                        Clear();
                        noEvnets.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    }
                } else {
                    Clear();
                    recyclerView.setVisibility(View.GONE);
                    Intent intent = new Intent(FilteredActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<NormalFilterEvents> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(FilteredActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString("back_pay", "filter");
//
//                FilterScreen filterScreen = new FilterScreen();
//                filterScreen.setArguments(bundle);
//                manager.beginTransaction().replace(R.id.home_frame_layout, filterScreen).commit();
                onBackPressed();
            }
        });
    }

    private void init() {
        recyclerView = findViewById(R.id.filtered__recyclerview);
        back = findViewById(R.id.back_filter);
        noEvnets = findViewById(R.id.noevents_filtered);
    }

    public void Clear() {
        name1.clear();
        title.clear();
        prices.clear();
        categois.clear();
        addres.clear();
        images.clear();
        event_id.clear();
        event_time.clear();
    }


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    String removeLeadingZeroes(String s) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() > 0 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }

        return sb.toString();
    }
}
