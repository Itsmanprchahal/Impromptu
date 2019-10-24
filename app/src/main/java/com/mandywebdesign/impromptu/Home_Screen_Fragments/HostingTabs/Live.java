package com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs;


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

import com.mandywebdesign.impromptu.Adapters.Business_LiveEventAdapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroLiveEvents;
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
public class Live extends Fragment implements DiscreteScrollView.OnItemChangedListener {

    public static DiscreteScrollView recyclerView;
    FragmentManager fragmentManager;
    SharedPreferences sharedPreferences, sharedPreferences1, profileupdatedPref, itemPositionPref;
    View view;
    TextView noEvnets;
    public static String user, BToken, S_Token, itemPosition, formattedDate, getFormattedDate, timeFrom;
    Dialog progressDialog;
    Business_LiveEventAdapter adapter;
    private InfiniteScrollAdapter infiniteAdapter;
    public static ArrayList<String> name1 = new ArrayList<>();
    public static ArrayList<String> title = new ArrayList<>();
    public static ArrayList<String> prices = new ArrayList<>();
    public static ArrayList<String> addres = new ArrayList<>();
    public static ArrayList<String> event_time = new ArrayList<>();
    public static ArrayList<String> categois = new ArrayList<>();
    public static ArrayList<String> images = new ArrayList<>();
    public static ArrayList<String> event_id = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_live, container, false);

        fragmentManager = getFragmentManager();

        progressDialog = ProgressBarClass.showProgressDialog(getContext());
        progressDialog.dismiss();

        sharedPreferences = getContext().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        sharedPreferences1 = getActivity().getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        profileupdatedPref = getContext().getSharedPreferences("profileupdated", Context.MODE_PRIVATE);
        itemPositionPref = getContext().getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        user = "Bearer " + sharedPreferences.getString("Usertoken", "");
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");
        itemPosition = itemPositionPref.getString(Constants.itemPosition, String.valueOf(0));

        init();





        recyclerView = (DiscreteScrollView) view.findViewById(R.id.business_events__recyclerview);
        recyclerView.setOrientation(DSVOrientation.HORIZONTAL);
        recyclerView.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new Business_LiveEventAdapter(getContext(), fragmentManager));
        recyclerView.setAdapter(infiniteAdapter);
        recyclerView.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        recyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)

                .build());

        progressDialog.show();

        return view;
    }

    private void init() {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.business_events__recyclerview);
        noEvnets = view.findViewById(R.id.noevents_live);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!BToken.equalsIgnoreCase("")) {

            liveEvnts(BToken);
        } else if (!S_Token.equalsIgnoreCase("")) {
            liveEvnts(S_Token);
        } else {
            progressDialog.dismiss();
        }
    }

    public void liveEvnts(final String token) {
        name1.clear();
        title.clear();
        prices.clear();
        addres.clear();
        categois.clear();
        images.clear();
        event_id.clear();
        event_time.clear();

        Call<RetroLiveEvents> call = WebAPI.getInstance().getApi().liveEvents("Bearer " + token, "application/json");
        call.enqueue(new Callback<RetroLiveEvents>() {
            @Override
            public void onResponse(Call<RetroLiveEvents> call, Response<RetroLiveEvents> response) {

                if (response.body() !=null) {

                    if (response.body().getStatus().equals("200")) {

                        RetroLiveEvents data = response.body();
                        List<RetroLiveEvents.Datum> datumArrayList = data.getData();

                        for (RetroLiveEvents.Datum datum : datumArrayList) {

                            name1.add(datum.getBEventHostname());
                            title.add(datum.getTitle());
                            addres.add(datum.getAddressline1());
                            if (datum.getPrice().equals("")) {

                                prices.add("Free");
                            } else {
                                prices.add(datum.getPrice());
                            }
                            Log.d("cates", "" + datum.getCategory());

                            String time_t = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.","am").replaceFirst("p.m.","pm").replaceFirst("AM","am").replaceFirst("PM","pm");

                            Log.d("TIME",time_t);



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
                                    event_time.add(str2 + "/" + str1 + "/" + str3 + " at " + timeFrom);
                                }



                            categois.add(datum.getCategory());
                            images.add(datum.getFile());

                            event_id.add(datum.getEventId().toString());

                            adapter = new Business_LiveEventAdapter(getContext(), fragmentManager);
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

                        progressDialog.show();

                        Intent intent = new Intent(getActivity(), Join_us.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                } else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                progressDialog.dismiss();

                reverse();
            }

            @Override
            public void onFailure(Call<RetroLiveEvents> call, Throwable t) {
                Log.d("++++", "t" + t);
                if (NoInternet.isOnline(getContext()) == false) {
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
        Collections.reverse(event_time);
    }
}
