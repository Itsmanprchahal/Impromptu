package com.mandywebdesign.impromptu.Filter;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Adapters.FilteredAdapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.NormalFilterEvents;
import com.mandywebdesign.impromptu.Utils.Constants;
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


public class FilteredScreen extends Fragment implements DiscreteScrollView.OnItemChangedListener {

    public DiscreteScrollView recyclerView;
    TextView noEvnets;
    FragmentManager manager;
    Dialog progressDialog;
    SharedPreferences sharedPreferences;
    String social_token, timeFrom, formattedDate, getFormattedDate;
    View view;
    ImageView back;
    String itemPosition;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_filtered_screen, container, false);

        manager = getFragmentManager();
        Bundle bundle = getArguments();
        String loc = bundle.getString("loc");
        String date = bundle.getString("date");
        final String price = bundle.getString("price");
        String gender = bundle.getString("gender");
        String lat = bundle.getString("lat");
        String lng = bundle.getString("lng");
        Log.d("filteredScreen", lat + "  " + lng);

        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        itemPositionPref = getContext().getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        social_token = "Bearer " + sharedPreferences.getString("Socailtoken", "");
        itemPosition = itemPositionPref.getString(Constants.itemPosition, String.valueOf(0));

        init();

        progressDialog = ProgressBarClass.showProgressDialog(getContext());
        progressDialog.show();


        recyclerView.setOrientation(DSVOrientation.HORIZONTAL);
        recyclerView.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new FilteredAdapter(getContext(), manager));
        recyclerView.setAdapter(infiniteAdapter);
        recyclerView.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        recyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)

                .build());


        listeners();

        Clear();
        Call<NormalFilterEvents> call = WebAPI.getInstance().getApi().filterEvnt(social_token, "application/json", lat, lng, gender, date, price);
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


                          /*  String time_t = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM", "am").replaceFirst("PM", "pm");

                            if (time_t.startsWith("0")) {
                                timeFrom = time_t.substring(1);
                            } else {
                                timeFrom = time_t.substring(0);
                            }
*/

                            String time_t = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM", "am").replaceFirst("PM", "pm");

                            Log.d("eventTIme", time_t);


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

                            adapter = new FilteredAdapter(getContext(), manager);
                            recyclerView.setAdapter(adapter);
                            recyclerView.getLayoutManager().scrollToPosition(Integer.parseInt(itemPosition));
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
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<NormalFilterEvents> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void listeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("back_pay", "filter");

                FilterScreen filterScreen = new FilterScreen();
                filterScreen.setArguments(bundle);
                manager.beginTransaction().replace(R.id.home_frame_layout, filterScreen).commit();
            }
        });
    }

    private void init() {
        recyclerView = view.findViewById(R.id.filtered__recyclerview);
        back = view.findViewById(R.id.back_filter);
        noEvnets = view.findViewById(R.id.noevents_filtered);
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
}
