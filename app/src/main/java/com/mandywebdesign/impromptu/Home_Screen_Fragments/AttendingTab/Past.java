package com.mandywebdesign.impromptu.Home_Screen_Fragments.AttendingTab;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Adapters.Normal_pastbooked;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.Normal_past_booked;
import com.mandywebdesign.impromptu.Utils.Constants;
import com.mandywebdesign.impromptu.Utils.Util;
import com.mandywebdesign.impromptu.ui.DiscreteScrollViewOptions;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Past extends Fragment implements DiscreteScrollView.OnItemChangedListener {

    public static DiscreteScrollView recyclerView;
    FragmentManager fragmentManager;
    SharedPreferences sharedPreferences, itemPositionPref;
    View view;
    TextView noEvnets;
    private InfiniteScrollAdapter infiniteAdapter;
    public static String user, S_Token, itemPosition, formattedDate, getFormattedDate, timeFrom;
    Dialog progressDialog;
    Normal_pastbooked adapter;
    public static ArrayList<String> name1 = new ArrayList<>();
    public static ArrayList<String> title = new ArrayList<>();
    public static ArrayList<String> prices = new ArrayList<>();
    public static ArrayList<String> addres = new ArrayList<>();
    public static ArrayList<String> time = new ArrayList<>();
    public static ArrayList<String> categois = new ArrayList<>();
    public static ArrayList<String> images = new ArrayList<>();
    public static ArrayList<String> event_id = new ArrayList<>();
    public static ArrayList<String> rating = new ArrayList<>();
    public static ArrayList<String> overall_rating = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_past, container, false);
        fragmentManager = getFragmentManager();

        progressDialog = ProgressBarClass.showProgressDialog(getContext());
        progressDialog.dismiss();

        sharedPreferences = getContext().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        itemPositionPref = getContext().getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        S_Token = sharedPreferences.getString("Socailtoken", "");
        itemPosition = itemPositionPref.getString(Constants.itemPosition, String.valueOf(0));
        init();

        past_EVnets(S_Token);


        recyclerView = (DiscreteScrollView) view.findViewById(R.id.past_booked_recycler_view);
        recyclerView.setOrientation(DSVOrientation.HORIZONTAL);
        recyclerView.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new Normal_pastbooked(getContext(), fragmentManager, S_Token));
        recyclerView.setAdapter(infiniteAdapter);
        recyclerView.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        recyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)

                .build());


        return view;
    }

    private void past_EVnets(final String bToken) {
        name1.clear();
        title.clear();
        prices.clear();
        time.clear();
        addres.clear();
        categois.clear();
        images.clear();
        event_id.clear();
        rating.clear();
        overall_rating.clear();
        progressDialog.show();
        Call<Normal_past_booked> call = WebAPI.getInstance().getApi().past_booked("Bearer " + bToken, "application/json");
        call.enqueue(new Callback<Normal_past_booked>() {
            @Override
            public void onResponse(Call<Normal_past_booked> call, Response<Normal_past_booked> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {

                        Normal_past_booked data = response.body();
                        List<Normal_past_booked.Datum> datumArrayList = data.getData();


                        for (Normal_past_booked.Datum datum : datumArrayList) {

                            name1.add(datum.getBEventHostname());
                            title.add(datum.getTitle());
                            addres.add(datum.getAddressline1());

                            if (datum.getPrice() != null) {
                                if (datum.getPrice().equals("")) {

                                    prices.add("Free");
                                } else {
                                    prices.add(datum.getPrice());
                                }
                            } else {
                                prices.add("Paid");
                            }

                            rating.add(datum.getRating());
                            overall_rating.add(datum.getTotalRating());

                            Log.d("cates", "" + datum.getCategory());

                            String time_t = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM", "am").replaceFirst("PM", "pm");


                            if (time_t.startsWith("0")) {
                                timeFrom = time_t.substring(1);
                                if (time_t.contains(":00"))
                                {
                                    timeFrom = time_t.replace(":00","");

                                    if (timeFrom.startsWith("0"))
                                    {
                                        timeFrom = time_t.replace("0","");
                                        if (timeFrom.contains(":"))
                                        {
                                            timeFrom = time_t.replace(":","").replace("0","").replace("00","");
                                        }
                                    }
                                }

                            }else if(!time_t.startsWith("0"))
                            {
                                if (time_t.contains(":00"))
                                {
                                    timeFrom = time_t.replace(":00","");

                                    if (timeFrom.startsWith("0"))
                                    {
                                        timeFrom = time_t.replace("0","");
                                        if (timeFrom.contains(":"))
                                        {
                                            timeFrom = time_t.replace(":","").replace("0","").replace("00","");
                                        }
                                    }
                                }
                            }
                            else {
                                timeFrom = time_t.substring(0);
                            }

                            Calendar c = Calendar.getInstance();

                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            formattedDate = df.format(c.getTime());
                            c.add(Calendar.DATE, 1);

                            getFormattedDate = df.format(c.getTime());

                            System.out.println("Current time ==> " + c.getTime());

                            if (formattedDate.matches(Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt())))) {
                                time.add("Today at " + timeFrom);
                            } else if (getFormattedDate.matches(Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt())))) {
                                time.add("Tomorrow at " + timeFrom);
                            } else {
                                String date = Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt()));
                                /*to change server date formate*/
                                String s1 = date;
                                String[] str = s1.split("/");
                                String str1 = str[0];
                                String str2 = str[1];
                                String str3 = str[2];
                                time.add(str1 + "/" + str2 + "/" + str3 + " at " + timeFrom);

                                //time.add(datum.getDate()+" at "+timeFrom);
                            }

                            categois.add(datum.getCategory());
                            images.add(datum.getFile().get(0));
                            event_id.add(datum.getEventId().toString());


                            adapter = new Normal_pastbooked(getContext(), fragmentManager, S_Token);
                            recyclerView.setAdapter(adapter);
                            recyclerView.getLayoutManager().scrollToPosition(Integer.parseInt(itemPosition));

                            SharedPreferences.Editor editor = itemPositionPref.edit();
                            editor.clear();
                            editor.commit();
                            reverse();
                        }
                    } else if (response.body().getStatus().equals("400")) {
                        noEvnets.setVisibility(View.VISIBLE);
                    }
                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Normal_past_booked> call, Throwable t) {
                Log.d("++++", "t" + t);
                if (NoInternet.isOnline(getContext()) == false) {
                    progressDialog.dismiss();

                    NoInternet.dialog(getContext());
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.past_booked_recycler_view);
        noEvnets = view.findViewById(R.id.past_no_events);
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    private void reverse() {

        Collections.reverse(name1);
        Collections.reverse(title);
        Collections.reverse(prices);
        Collections.reverse(addres);
        Collections.reverse(time);
        Collections.reverse(categois);
        Collections.reverse(images);
        Collections.reverse(event_id);
        Collections.reverse(rating);
        Collections.reverse(overall_rating);
    }
}
