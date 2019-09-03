package com.mandywebdesign.impromptu.Home_Screen_Fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mandywebdesign.impromptu.Adapters.HomeEventsAdapter;
import com.mandywebdesign.impromptu.Adapters.RelatedEventAdapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Filter.FilterScreen;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.NormalGetEvent;
import com.mandywebdesign.impromptu.Retrofit.NormalrelatedEvents;
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

public class Home extends Fragment implements DiscreteScrollView.OnItemChangedListener {


    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 111;
    public static DiscreteScrollView relatedEventsRecyclerView;
    public static DiscreteScrollView recyclerView;
    public static TextView textView, see_related;
    ImageView filter, swipeUp;
    InfiniteScrollAdapter infiniteAdapter;
    View view;
    TextView noevents;
    String adapterPositn, lat, lng;
    public static String social_token, category, itemPosition, releatedposition, getCategory, formattedDate, getFormattedDate, timeFrom;
    CheckBox shuffle;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences, itemPositionPref;
    FragmentManager fragmentManager;
    public static ArrayList<String> Title = new ArrayList<>();
    public static ArrayList<Integer> event_count = new ArrayList<>();
    public static ArrayList<String> host_image = new ArrayList<>();
    public static ArrayList<String> rel_Title = new ArrayList<>();
    public static ArrayList<String> Address = new ArrayList<>();
    public static ArrayList<String> rel_address1 = new ArrayList<>();
    public static ArrayList<String> Time = new ArrayList<>();
    public static ArrayList<String> rel_date = new ArrayList<>();
    public static ArrayList<String> date = new ArrayList<>();
    public static ArrayList<String> rel_time = new ArrayList<>();
    public static ArrayList<String> rel_cost = new ArrayList<>();
    public static ArrayList<String> rel_image = new ArrayList<>();
    public static ArrayList<String> rel_eventID = new ArrayList<>();
    public static ArrayList<String> rel_category_name = new ArrayList<>();
    public static ArrayList<String> rel_fav_id = new ArrayList<>();
    public static ArrayList<String> Cost = new ArrayList<>();
    public static ArrayList<String> Image = new ArrayList<>();
    public static ArrayList<String> event_id = new ArrayList<>();
    public static ArrayList<String> cate = new ArrayList<>();
    public static ArrayList<String> cate_id = new ArrayList<>();
    public static ArrayList<String> fav_id = new ArrayList<>();
    public static ArrayList<String> event_host_username = new ArrayList<>();
    public static ArrayList<String> rel_hostname = new ArrayList<>();
    FusedLocationProviderClient locationProviderClient;
    HomeEventsAdapter adapter;
    int current_Position;


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        fragmentManager = getActivity().getSupportFragmentManager();
        progressDialog = ProgressBarClass.showProgressDialog(getContext(), "Please wait...");
        progressDialog.dismiss();

        itemPositionPref = getContext().getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);

        itemPosition = itemPositionPref.getString(Constants.itemPosition, "0");
        releatedposition = itemPositionPref.getString(Constants.eventType, String.valueOf(0));
        getCategory = itemPositionPref.getString(Constants.Category, "");
        recyclerView = view.findViewById(R.id.home_feed_recyclerview);
        relatedEventsRecyclerView = view.findViewById(R.id.home_frag_related_items);
//        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout1);


        init();
        shuffle = (CheckBox) view.findViewById(R.id.home_shuffle);

        statusCheck();
        lisnerters();


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releatedposition = "0";

                relatedEventsRecyclerView.smoothScrollToPosition(0);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        statusCheck();
//        if (Home_Screen.newCount == 1) {
//            Home_Screen.newCount = 0;
//            statusCheck();
//        } else {
//            statusCheck();
//        }
    }

    private void init() {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        textView = (TextView) view.findViewById(R.id.home_related);
        filter = (ImageView) view.findViewById(R.id.filter);
        noevents = (TextView) view.findViewById(R.id.noevents);
        see_related = (TextView) view.findViewById(R.id.see_related);
    }


    private void lisnerters() {
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.home_frame_layout, new FilterScreen()).addToBackStack(null).commit();
            }
        });

        shuffle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (shuffle.isChecked()) {
                    shuffleEvents();
                } else if (!shuffle.isChecked()) {
                    getAllEvent(lat, lng);
                }

            }
        });

    }


    public void HomeEventsSetAdapter(String social_token) {

        if (current_Position == 1) {
            see_related.setVisibility(View.GONE);
        }

        if (!itemPosition.equalsIgnoreCase("") && !itemPosition.equalsIgnoreCase("0")) {

            releatedposition = "0";
            see_related.setVisibility(View.VISIBLE);
            see_related.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    relatedEventsRecyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                    clear();
                    getRelated(category);

                }
            });
        } else if (current_Position == 1) {
            see_related.setVisibility(View.INVISIBLE);
        } else {
            see_related.setVisibility(View.INVISIBLE);
        }

        if (!releatedposition.equalsIgnoreCase("") && itemPosition.equalsIgnoreCase("0")) {
            see_related.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relatedEventsRecyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    clearPref();
                    getRelated(category);
                }
            });
        } else if (current_Position == 1) {
            see_related.setVisibility(View.INVISIBLE);
        }

        if (!getCategory.equalsIgnoreCase("") && !releatedposition.equalsIgnoreCase("")) {

            recyclerView.setVisibility(View.GONE);
            relatedEventsRecyclerView.setVisibility(View.VISIBLE);
            clearPref();
            getRelated(getCategory);
        }


        recyclerView.addScrollStateChangeListener(new DiscreteScrollView.ScrollStateChangeListener<RecyclerView.ViewHolder>() {
            @Override
            public void onScrollStart(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

            }

            @Override
            public void onScrollEnd(@NonNull RecyclerView.ViewHolder currentItemHolder, final int adapterPosition) {


                adapterPositn = String.valueOf(adapterPosition);
                if (adapterPosition == -1 || current_Position == 1) {
                    see_related.setVisibility(View.INVISIBLE);
                } else if (adapterPosition == 0 || current_Position == 1) {
                    see_related.setVisibility(View.INVISIBLE);

                } else {
                    see_related.setVisibility(View.VISIBLE);
                    see_related.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            category = cate_id.get(adapterPosition);
                            relatedEventsRecyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);

                            releatedposition = "0";

                            getRelated(category);

                        }
                    });
                }
            }

            @Override
            public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {

                if (event_count.get(newPosition) == 1) {
                    see_related.setVisibility(View.INVISIBLE);
                }

                current_Position = event_count.get(newPosition);

            }
        });

        adapter = new HomeEventsAdapter(getContext(), fragmentManager, social_token);


        recyclerView.setOrientation(DSVOrientation.HORIZONTAL);
        recyclerView.getCurrentItem();

        recyclerView.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new HomeEventsAdapter(getContext(), fragmentManager, social_token));
        recyclerView.setAdapter(infiniteAdapter);
        recyclerView.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        recyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)

                .build());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        clearPref();

        if (releatedposition.equalsIgnoreCase("")) {
            recyclerView.getLayoutManager().scrollToPosition(Integer.parseInt(itemPosition));
        } else if (itemPosition.equals("0") && releatedposition.equals("0")) {
            recyclerView.getLayoutManager().scrollToPosition(0);
        } else if (adapterPositn != null) {
            recyclerView.getLayoutManager().scrollToPosition(Integer.parseInt(adapterPositn));
        }
    }

    private void clearPref() {
        SharedPreferences.Editor editor = itemPositionPref.edit();
        editor.clear();
        editor.commit();
    }


    private void getRelated(final String category) {
        progressDialog.show();

        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        social_token = "Bearer " + sharedPreferences.getString("Socailtoken", "");
        rel_clear();
        Call<NormalrelatedEvents> call = WebAPI.getInstance().getApi().getrelatedEvents(social_token, category, lat, lng);
        call.enqueue(new Callback<NormalrelatedEvents>() {
            @Override
            public void onResponse(Call<NormalrelatedEvents> call, Response<NormalrelatedEvents> response) {

                if (response.body() != null) {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("200")) {
                        NormalrelatedEvents data = response.body();
                        List<NormalrelatedEvents.Datum> datumArrayList = data.getData();

                        rel_clear();

                        for (NormalrelatedEvents.Datum datum : datumArrayList) {

                            rel_cost.add(datum.getPrice());
                            rel_Title.add(datum.getTitle());
                            rel_address1.add(datum.getAddressline1());
                            rel_cost.add(datum.getPrice());
                            rel_hostname.add(datum.getBEventHostname());


                            //split date format
                            rel_date.add(datum.getDate());
                            /*to change server date formate*/
                            String s1 = rel_date.get(0);
                            String[] str = s1.split("/");
                            String str1 = str[0];
                            String str2 = str[1];
                            String str3 = str[2];

                            // rel_time.add(datum.getTimeTo());

                            rel_image.add(datum.getFile().get(0));


                            rel_category_name.add(datum.getCategory());
                            rel_eventID.add(datum.getEventId().toString());
                            rel_fav_id.add(datum.getFavourite().toString());
                            Log.d("123456789", "");

                            String time_t = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM","am").replaceFirst("PM","pm");


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

                            System.out.println("Current time ==> " + c.getTime());

                            if (formattedDate.matches(rel_date.get(0))) {
                                rel_time.add("Today at " + timeFrom);
                            } else if (getFormattedDate.matches(rel_date.get(0))) {
                                rel_time.add("Tomorrow at " + timeFrom);
                            } else {
                                rel_time.add(str2 + "/" + str1 + "/" + str3 + " at " + timeFrom);
                            }


                            progressDialog.dismiss();
                            RelatedEventsSetAdapter(social_token);
                        }
                    } else {
                        progressDialog.dismiss();
                    }
                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<NormalrelatedEvents> call, Throwable t) {
                if (!NoInternet.isOnline(getContext()) == false) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void RelatedEventsSetAdapter(String social_token) {

        RelatedEventAdapter relatedEventAdapter = new RelatedEventAdapter(getActivity(), fragmentManager, social_token, category);

        relatedEventsRecyclerView.setOrientation(DSVOrientation.VERTICAL);
        relatedEventsRecyclerView.getCurrentItem();

        relatedEventsRecyclerView.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new HomeEventsAdapter(getContext(), fragmentManager, social_token));
        relatedEventsRecyclerView.setAdapter(infiniteAdapter);
        relatedEventsRecyclerView.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        relatedEventsRecyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(1.0f)

                .build());
        relatedEventsRecyclerView.setAdapter(relatedEventAdapter);
        relatedEventAdapter.notifyDataSetChanged();


        if (!releatedposition.equalsIgnoreCase("")) {

            if (releatedposition.equalsIgnoreCase("0")) {
                textView.setVisibility(View.INVISIBLE);
            } else {
                textView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

            relatedEventsRecyclerView.getLayoutManager().scrollToPosition(Integer.parseInt(releatedposition));


            clearPref();

            getReleatedScorllposition();
        }
    }

    private void getReleatedScorllposition() {

        relatedEventsRecyclerView.addScrollStateChangeListener(new DiscreteScrollView.ScrollStateChangeListener<RecyclerView.ViewHolder>() {
            @Override
            public void onScrollStart(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

            }

            @Override
            public void onScrollEnd(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

                adapterPositn = String.valueOf(adapterPosition);
                if (adapterPosition == -1 || adapterPosition == 0 || adapterPosition == 1) {
                    textView.setVisibility(View.INVISIBLE);
                } else {
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {
            }
        });
    }


    public void clear() {
        Title.clear();
        host_image.clear();
        Image.clear();
        Cost.clear();
        Time.clear();
        Address.clear();
        cate.clear();
        cate_id.clear();
        date.clear();
        fav_id.clear();
        event_host_username.clear();
        event_id.clear();
    }

    public void rel_clear() {
        rel_Title.clear();
        rel_address1.clear();
        rel_time.clear();
        rel_date.clear();
        rel_cost.clear();
        rel_image.clear();
        rel_fav_id.clear();
        rel_eventID.clear();
        rel_category_name.clear();
    }

    public void getAllEvent(String lat, String lng) {

        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        social_token = "Bearer " + sharedPreferences.getString("Socailtoken", "");

        progressDialog.show();

        Call<NormalGetEvent> call = WebAPI.getInstance().getApi().normalGetEvent(social_token, lat, lng);
        call.enqueue(new Callback<NormalGetEvent>() {
            @Override
            public void onResponse(Call<NormalGetEvent> call, Response<NormalGetEvent> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {

                        NormalGetEvent data = response.body();
                        List<NormalGetEvent.Datum> datumArrayList = data.getData();

                        if (data.getData().size() == 0) {
                            noevents.setVisibility(View.VISIBLE);
                            see_related.setVisibility(View.GONE);
                        }

                        clear();
                        for (NormalGetEvent.Datum normalGetEvent : datumArrayList) {

                            event_count.add(normalGetEvent.getCategoriesCount());
                            Cost.add(normalGetEvent.getPrice().toString());
                            Title.add(normalGetEvent.getTitle());
                            cate.add(normalGetEvent.getCategory());
                            Address.add(normalGetEvent.getAddressline1().toString());
                            Log.d("11212121", "  " + normalGetEvent.getAddressline1());
//                            Time.add(normalGetEvent.getTimeFrom());
                            cate_id.add(normalGetEvent.getCategoryId().toString());
                            Image.add(normalGetEvent.getFile());
                            date.add(normalGetEvent.getDate());
                            event_id.add(normalGetEvent.getEventId().toString());
                            fav_id.add(normalGetEvent.getFavourite().toString());
                            event_host_username.add(normalGetEvent.getUsername().toString());

                            String time_t = Util.convertTimeStampToTime(Long.parseLong(normalGetEvent.getEventStartDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM","am").replaceFirst("PM","pm");


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

                            System.out.println("Current time ==> " + c.getTime());

                            if (formattedDate.matches(normalGetEvent.getDate())) {
                                Time.add("Today at " + timeFrom);
                            } else if (getFormattedDate.matches(normalGetEvent.getDate())) {
                                Time.add("Tomorrow at " + timeFrom);
                            } else {

                                String dateformat = normalGetEvent.getDate();
                                /*to change server date formate*/
                                String s1 = dateformat;
                                String[] str = s1.split("/");
                                String str1 = str[0];
                                String str2 = str[1];
                                String str3 = str[2];

                                Time.add(str2 + "/" + str1 + "/" + str3 + " at " + timeFrom);
                            }

                        }

                        progressDialog.dismiss();
                        HomeEventsSetAdapter(social_token);
                        reverse();
                    } else if (response.body().getStatus().equals("400")) {
                        noevents.setVisibility(View.VISIBLE);
                        see_related.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    } else if (response.body().getStatus().equals("401")) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        editor.commit();

                        Intent intent = new Intent(getActivity(), Join_us.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }

                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<NormalGetEvent> call, Throwable t) {
                progressDialog.dismiss();
                if (NoInternet.isOnline(getContext()) == false) {
                    progressDialog.dismiss();

                    NoInternet.dialog(getContext());
                } else {
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void shuffleEvents() {
        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        social_token = "Bearer " + sharedPreferences.getString("Socailtoken", "");

        progressDialog.show();
        Call<NormalGetEvent> call = WebAPI.getInstance().getApi().normalGetEvent(social_token, lat, lng);
        call.enqueue(new Callback<NormalGetEvent>() {
            @Override
            public void onResponse(Call<NormalGetEvent> call, Response<NormalGetEvent> response) {

                if (response.body() != null) {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("200")) {

                        NormalGetEvent data = response.body();
                        List<NormalGetEvent.Datum> datumArrayList = data.getData();

                        clear();
                        for (NormalGetEvent.Datum normalGetEvent : datumArrayList) {

                            Cost.add(normalGetEvent.getPrice().toString());
                            Title.add(normalGetEvent.getTitle());
                            cate.add(normalGetEvent.getCategory());
                            Address.add(normalGetEvent.getAddressline1());
                            //Time.add(normalGetEvent.getTimeFrom());
                            cate_id.add(normalGetEvent.getCategoryId().toString());
                            Image.add(normalGetEvent.getFile());
                            date.add(normalGetEvent.getDate());

                            String time_t = normalGetEvent.getTimeFrom().toString();

                            try {
                                final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                final Date dateObj = sdf.parse(time_t);
                                time_t = new SimpleDateFormat("hh:mm aa").format(dateObj);

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

                                if (formattedDate.matches(normalGetEvent.getDate())) {
                                    Time.add("Today at " + timeFrom);
                                } else if (getFormattedDate.matches(normalGetEvent.getDate())) {
                                    Time.add("Tomorrow at " + timeFrom);
                                } else {

                                    String dateformat = normalGetEvent.getDate();
                                    /*to change server date formate*/
                                    String s1 = dateformat;
                                    String[] str = s1.split("/");
                                    String str1 = str[0];
                                    String str2 = str[1];
                                    String str3 = str[2];

                                    Time.add(str2 + "/" + str1 + "/" + str3 + " at " + timeFrom);
                                }

                                //   Toast.makeText(getContext(), "14141 "+datum.getDate(), Toast.LENGTH_SHORT).show();


                            } catch (final ParseException e) {
                                e.printStackTrace();
                            }

                            event_id.add(normalGetEvent.getEventId().toString());
                            fav_id.add(normalGetEvent.getFavourite().toString());
                            event_host_username.add(normalGetEvent.getUsername().toString());

                        }
                        progressDialog.dismiss();
                        HomeEventsSetAdapter(social_token);
                    } else if (response.body().getStatus().equals("401")) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        editor.commit();

                        Intent intent = new Intent(getActivity(), Join_us.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<NormalGetEvent> call, Throwable t) {
                progressDialog.dismiss();
                if (NoInternet.isOnline(getContext()) == false) {
                    progressDialog.dismiss();

                    NoInternet.dialog(getContext());
                } else {
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void reverse() {
        Collections.reverse(Image);
        Collections.reverse(Title);
        Collections.reverse(Cost);
        Collections.reverse(Time);
        Collections.reverse(Address);
        Collections.reverse(cate);
        Collections.reverse(cate_id);
        Collections.reverse(date);
        Collections.reverse(fav_id);
        Collections.reverse(event_host_username);
        Collections.reverse(event_id);
    }


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            CurrentLocation();
            Log.d("+++++++++++++", "++ app is working current location ++ ");
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        CurrentLocation();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void CurrentLocation() {
        final FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "Location Not Found...", Toast.LENGTH_SHORT).show();
            return;
        } else {

            Task<Location> locationTask = client.getLastLocation();
            if (locationTask != null) {
                locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        task.addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location == null) {
                                    Toast.makeText(getContext(), "Unable to get Location", Toast.LENGTH_SHORT).show();
//                                    progressDialog.show();
                                } else {
                                    lat = String.valueOf(location.getLatitude());
                                    lng = String.valueOf(location.getLongitude());
                                    getAllEvent(lat, lng);
                                }
                            }
                        });

                        task.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Location Not Found...,Enter Location Manually...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION:
                if (resultCode == Activity.RESULT_OK) {
                    CurrentLocation();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No permission Granted", Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }

    }

}