package com.mandywebdesign.impromptu.Home_Screen_Fragments.favouriteTab;


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

import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessAdapter.Hosting_fav_events_Adapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.FollowerPublish;
import com.mandywebdesign.impromptu.Retrofit.NormalretroHosting_fav_evnts;
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
public class Hosts extends Fragment implements DiscreteScrollView.OnItemChangedListener {

    DiscreteScrollView recyclerView;
    FragmentManager fragmentManager;
    private InfiniteScrollAdapter infiniteAdapter;
    Hosting_fav_events_Adapter adapter;
    View view;
    TextView noEvnets;
    SharedPreferences sharedPreferences, itemPositionPref;
    String S_Token, itemPosition, formattedDate, getFormattedDate, timeFrom;
    Dialog progressDialog;
    public static ArrayList<String> name1_fav = new ArrayList<>();
    public static ArrayList<String> title_fav = new ArrayList<>();
    public static ArrayList<String> prices_fav = new ArrayList<>();
    public static ArrayList<String> addres_fav = new ArrayList<>();
    public static ArrayList<String> Time_fav = new ArrayList<>();
    public static ArrayList<String> categois_fav = new ArrayList<>();
    public static ArrayList<String> images_fav = new ArrayList<>();
    public static ArrayList<String> event_id_fav = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hosts, container, false);
        fragmentManager = getFragmentManager();

        progressDialog = ProgressBarClass.showProgressDialog(getContext());
        progressDialog.dismiss();
        sharedPreferences = getContext().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        itemPositionPref = getContext().getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        S_Token = sharedPreferences.getString("Socailtoken", "");
        itemPosition = itemPositionPref.getString(Constants.itemPosition, String.valueOf(0));

        initi();


        GetFavEvents(S_Token);


        recyclerView = (DiscreteScrollView) view.findViewById(R.id.host_favourite_event_recycler_view);
        recyclerView.setOrientation(DSVOrientation.HORIZONTAL);
        recyclerView.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new Hosting_fav_events_Adapter(getContext(), fragmentManager));
        recyclerView.setAdapter(infiniteAdapter);
        recyclerView.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        recyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)

                .build());


        return view;
    }

    private void initi() {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.host_favourite_event_recycler_view);
        noEvnets = (TextView) view.findViewById(R.id.noEvetntshost);
    }

    private void GetFavEvents(final String s_token) {
        progressDialog.show();
        name1_fav.clear();
        title_fav.clear();
        prices_fav.clear();
        addres_fav.clear();
        Time_fav.clear();
        categois_fav.clear();
        images_fav.clear();
        event_id_fav.clear();
        Call<FollowerPublish> publishCall = WebAPI.getInstance().getApi().followersPublish("Bearer " + s_token);
        publishCall.enqueue(new Callback<FollowerPublish>() {
            @Override
            public void onResponse(Call<FollowerPublish> call, Response<FollowerPublish> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals("200")) {
                        FollowerPublish followerPublish = response.body();
                        List<FollowerPublish.Datum> followerPublishList = followerPublish.getData();

                        for (FollowerPublish.Datum datum : followerPublishList) {
                            name1_fav.add(datum.getBEventHostname());
                            title_fav.add(datum.getTitle());
                            addres_fav.add(datum.getAddressline1());

                            if (datum.getPrice()!=null)
                            {
                                if (datum.getPrice().equals("")) {

                                    prices_fav.add("Free");
                                } else {
                                    prices_fav.add(datum.getPrice());
                                }
                            }else {
                                prices_fav.add("Paid");
                            }

                            String time_t = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM", "am").replaceFirst("PM", "pm");


                            if (time_t.startsWith("0")) {
                                timeFrom = time_t.substring(1);
                            } else {
                                timeFrom = time_t.substring(0);
                            }

                            Calendar c = Calendar.getInstance();

                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            formattedDate = df.format(c.getTime());
                            c.add(Calendar.DATE, 1);

                            getFormattedDate = df.format(c.getTime());
                            // Toast.makeText(getContext(), "TOMORROW_DATE"+getFormattedDate, Toast.LENGTH_SHORT).show();

                            System.out.println("Current time ==> " + c.getTime());

                            if (formattedDate.matches(Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt())))) {
                                Time_fav.add("Today at " + timeFrom);
                            } else if (getFormattedDate.matches(Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt())))) {
                                Time_fav.add("Tomorrow at " + timeFrom);
                            } else {
                                String date = Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt()));
                                /*to change server date formate*/
                                String s1 = date;
                                String[] str = s1.split("/");
                                String str1 = str[0];
                                String str2 = str[1];
                                String str3 = str[2];
                                Time_fav.add(str2 + "/" + str1 + "/" + str3 + " at " + timeFrom);
                            }

                            categois_fav.add(datum.getCategory());
                            images_fav.add(datum.getFile());
                            event_id_fav.add(datum.getEventId().toString());


                            adapter = new Hosting_fav_events_Adapter(getContext(), fragmentManager);
                            recyclerView.setAdapter(adapter);
                            recyclerView.getLayoutManager().scrollToPosition(Integer.parseInt(itemPosition));

                            SharedPreferences.Editor editor = itemPositionPref.edit();
                            editor.clear();
                            editor.commit();
                        }

                    } else if (response.body().getStatus().equals("400")) {
                        noEvnets.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);

                    } else {
                        progressDialog.dismiss();
                        Intent intent = new Intent(getContext(), NoInternetScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                progressDialog.dismiss();
                reverse();
            }

            @Override
            public void onFailure(Call<FollowerPublish> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    private void reverse() {

        Collections.reverse(name1_fav);
        Collections.reverse(title_fav);
        Collections.reverse(prices_fav);
        Collections.reverse(addres_fav);
        Collections.reverse(Time_fav);
        Collections.reverse(categois_fav);
        Collections.reverse(images_fav);
        Collections.reverse(event_id_fav);
    }
}
