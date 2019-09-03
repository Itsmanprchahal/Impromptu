package com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Adapters.Business_History_adapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroHistoryEvents;
import com.mandywebdesign.impromptu.Utils.Constants;
import com.mandywebdesign.impromptu.Utils.Util;
import com.mandywebdesign.impromptu.ui.DiscreteScrollViewOptions;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.Join_us;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment  implements  DiscreteScrollView.OnItemChangedListener {

    View view;
    SharedPreferences sharedPreferences,itemPositionPref,sharedPreferences1,profileupdatedPref;
    FragmentManager manager;
    TextView noEvnets;
    String user,BToken,S_Token,itemPosition,formattedDate,getFormattedDate,timeFrom;
    DiscreteScrollView recyclerView;
    InfiniteScrollAdapter infiniteAdapter;
    ProgressDialog progressDialog;
    public  static ArrayList<String> name1=new ArrayList<>();
    public static ArrayList<String> title = new ArrayList<>();
    public static ArrayList<String> prices = new ArrayList<>();
    public static ArrayList<String> addres = new ArrayList<>();
    public static ArrayList<String> eventTime = new ArrayList<>();
    public  static ArrayList<String> categois = new ArrayList<>();
    public static ArrayList<String> images = new ArrayList<>();
    public static ArrayList<String> event_id = new ArrayList<>();
    public static ArrayList<String> ratingstatus = new ArrayList<>();
    public static ArrayList<String> rating_overall = new ArrayList<>();
    Business_History_adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);
        manager = getFragmentManager();
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        noEvnets = view.findViewById(R.id.noevents_history);

        progressDialog = ProgressBarClass.showProgressDialog(getContext(),"Please wait while we fetch your events");

        sharedPreferences1 = getActivity().getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        profileupdatedPref = getContext().getSharedPreferences("profileupdated", Context.MODE_PRIVATE);
        sharedPreferences = getContext().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        itemPositionPref = getContext().getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        user = "Bearer "+ sharedPreferences.getString("Usertoken", "");
        BToken = sharedPreferences.getString("Usertoken","");
        S_Token = sharedPreferences.getString("Socailtoken", "");
        itemPosition = itemPositionPref.getString(Constants.itemPosition, String.valueOf(0));



        recyclerView = (DiscreteScrollView) view.findViewById(R.id.business_history__recyclerview);
        recyclerView.setOrientation(DSVOrientation.HORIZONTAL);
        recyclerView.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new Business_History_adapter(getContext(),manager,S_Token));
        recyclerView.setAdapter(infiniteAdapter);
        recyclerView.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        recyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)

                .build());

        if (!BToken.equalsIgnoreCase("")) {

            histortEvnts(BToken);
        } else if (!S_Token.equalsIgnoreCase(""))
        {
            histortEvnts(S_Token);
        }else {
            progressDialog.dismiss();
        }
        return  view;
    }

    private void histortEvnts(String bToken) {

        Call<RetroHistoryEvents> call = WebAPI.getInstance().getApi().history("Bearer "+bToken,"application/json");
        call.enqueue(new Callback<RetroHistoryEvents>() {
            @Override
            public void onResponse(Call<RetroHistoryEvents> call, Response<RetroHistoryEvents> response) {
                RetroHistoryEvents historyEvents = response.body();
                List<RetroHistoryEvents.Datum> datumList = historyEvents.getData();

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("200")) {
                        name1.clear();
                        title.clear();
                        categois.clear();
                        prices.clear();
                        //images.clear();
                        event_id.clear();
                        eventTime.clear();
                        addres.clear();
                        ratingstatus.clear();
                        rating_overall.clear();
                        for (RetroHistoryEvents.Datum datum : datumList) {
                            name1.add(datum.getBEventHostname());
                            title.add(datum.getTitle());
                            addres.add(datum.getAddressline1());
                            categois.add(datum.getCategory());
                            images.add(datum.getFile());
                            event_id.add(datum.getEventId().toString());
                            ratingstatus.add(datum.getRating().toString());
                            rating_overall.add(datum.getTotalRating().toString());
                            if (datum.getPrice().equals("")) {

                                prices.add("Free");
                            } else {
                                prices.add(datum.getPrice());
                            }

                            String time_t = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.","am").replaceFirst("p.m.","pm").replaceFirst("AM","am").replaceFirst("PM","pm");


                                if (time_t.startsWith("0")) {
                                    timeFrom = time_t.substring(1);
                                } else {
                                    timeFrom = time_t.substring(0);
                                }

                                Calendar c = Calendar.getInstance();

                                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                                formattedDate = df.format(c.getTime());
                                c.add(Calendar.DATE, 1);

                                getFormattedDate = df.format(c.getTime());
                                // Toast.makeText(getContext(), "TOMORROW_DATE"+getFormattedDate, Toast.LENGTH_SHORT).show();

                                System.out.println("Current time ==> " + c.getTime());

                                if (formattedDate.matches(datum.getDate())) {
                                    eventTime.add("Today at " + timeFrom);
                                } else if (getFormattedDate.matches(datum.getDate())) {
                                    eventTime.add("Tomorrow at " + timeFrom);
                                } else {

                                    String date = datum.getDate();
                                    /*to change server date formate*/
                                    String s1 = date;
                                    String[] str = s1.split("/");
                                    String str1 = str[0];
                                    String str2 = str[1];
                                    String str3 = str[2];
                                    eventTime.add(str2 + "/" + str1 + "/" + str3 + " at " + timeFrom);
                                }


                            categois.add(datum.getCategory());
                            // Toast.makeText(getContext(), "Toast", Toast.LENGTH_SHORT).show();

                            adapter = new Business_History_adapter(getContext(), manager,S_Token);
                            recyclerView.setAdapter(adapter);
                            recyclerView.getLayoutManager().scrollToPosition(Integer.parseInt(itemPosition));

                            SharedPreferences.Editor editor = itemPositionPref.edit();
                            editor.clear();
                            editor.commit();

                        }
                    } else if (response.body().getStatus().equals("400")) {
                        noEvnets.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else if (response.body().getStatus().equals("401")) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.clear();
                        editor1.commit();
                        Toast.makeText(getContext(), "Business Logout", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor2 = profileupdatedPref.edit();
                        editor2.clear();
                        editor2.commit();

                        progressDialog.setMessage("login in another device");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        Intent intent = new Intent(getActivity(), Join_us.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                }else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                progressDialog.dismiss();

                reverse();
            }

            @Override
            public void onFailure(Call<RetroHistoryEvents> call, Throwable t) {
                if (NoInternet.isOnline(getContext())==false)
                {
                    progressDialog.dismiss();

                    NoInternet.dialog(getContext());
                }else {
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    private void reverse() {

        Collections.reverse(name1);
        Collections.reverse(title);
        Collections.reverse(prices);
        Collections.reverse(addres);
        Collections.reverse(categois);
        Collections.reverse(images);
        Collections.reverse(event_id);
        Collections.reverse(eventTime);
        Collections.reverse(rating_overall);
        Collections.reverse(ratingstatus);
    }
}
