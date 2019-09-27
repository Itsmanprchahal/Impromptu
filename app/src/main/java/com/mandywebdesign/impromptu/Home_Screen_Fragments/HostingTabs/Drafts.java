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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Adapters.Business_DraftsEventAdapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroDraftsEvents;
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
public class Drafts extends Fragment implements DiscreteScrollView.OnItemChangedListener {

    DiscreteScrollView recyclerView;
    InfiniteScrollAdapter infiniteAdapter;
    FragmentManager fragmentManager;
    SharedPreferences sharedPreferences, itemPositionPref, sharedPreferences1, profileupdatedPref;
    TextView noEvents;
    View view;
    Business_DraftsEventAdapter adapter;
    String user, BToken, S_Token, itemPosition, formattedDate, getFormattedDate, timeFrom;
    Dialog progressDialog;
    public static ArrayList<String> name1 = new ArrayList<>();
    public static ArrayList<String> title = new ArrayList<>();
    public static ArrayList<String> prices = new ArrayList<>();
    public static ArrayList<String> addres = new ArrayList<>();
    public static ArrayList<String> eventTIme = new ArrayList<>();
    public static ArrayList<String> categois = new ArrayList<>();
    public static ArrayList<String> draftsimages = new ArrayList<>();
    public static ArrayList<String> event_id = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_drafts, container, false);
        fragmentManager = getFragmentManager();
        progressDialog = ProgressBarClass.showProgressDialog(getContext());
        progressDialog.dismiss();

        sharedPreferences = getContext().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        itemPositionPref = getContext().getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        sharedPreferences1 = getActivity().getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        profileupdatedPref = getContext().getSharedPreferences("profileupdated", Context.MODE_PRIVATE);
        user = "Bearer " + sharedPreferences.getString("Usertoken", "");
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");
        itemPosition = itemPositionPref.getString(Constants.itemPosition, String.valueOf(0));

        init();

        if (!BToken.equalsIgnoreCase("")) {

            progressDialog.show();
            Drafts(BToken);
        } else if (!S_Token.equalsIgnoreCase("")) {
            Drafts(S_Token);
        } else {
            progressDialog.dismiss();
        }
        recyclerView = (DiscreteScrollView) view.findViewById(R.id.business_drafts__recyclerview);
        recyclerView.setOrientation(DSVOrientation.HORIZONTAL);
        recyclerView.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new Business_DraftsEventAdapter(getContext(), fragmentManager));
        recyclerView.setAdapter(infiniteAdapter);
        recyclerView.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        recyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)

                .build());


        return view;
    }


    private void Drafts(String bToken) {
        name1.clear();
        title.clear();
        prices.clear();
        eventTIme.clear();
        addres.clear();
        categois.clear();
        draftsimages.clear();
        event_id.clear();
        progressDialog.show();
        Call<RetroDraftsEvents> call = WebAPI.getInstance().getApi().drafts("Bearer " + bToken, "application/json");
        call.enqueue(new Callback<RetroDraftsEvents>() {
            @Override
            public void onResponse(Call<RetroDraftsEvents> call, Response<RetroDraftsEvents> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {
                        RetroDraftsEvents data = response.body();
                        List<RetroDraftsEvents.Datum> datumArrayList = data.getData();

                        for (RetroDraftsEvents.Datum datum : datumArrayList) {

                            name1.add(datum.getBEventHostname());
                            title.add(datum.getTitle());
                            draftsimages.add(datum.getFile().toString());
                            prices.add(datum.getPrice());
                            addres.add(datum.getAddressline1());

                            String time_t = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM", "am").replaceFirst("PM", "pm");


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

                            if (formattedDate.matches(Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt())))) {
                                eventTIme.add("Today at " + timeFrom);
                            } else if (getFormattedDate.matches(Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt())))) {
                                eventTIme.add("Tomorrow at " + timeFrom);
                            } else {
                                String date = Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt()));
                                /*to change server date formate*/
                                String s1 = date;
                                String[] str = s1.split("/");
                                String str1 = str[0];
                                String str2 = str[1];
                                String str3 = str[2];
                                eventTIme.add(str2 + "/" + str1 + "/" + str3 + " at " + timeFrom);
                            }

                            categois.add(datum.getCategory());
                            event_id.add(datum.getEventId().toString());

                            adapter = new Business_DraftsEventAdapter(getContext(), fragmentManager);
                            recyclerView.setAdapter(adapter);
                            recyclerView.getLayoutManager().scrollToPosition(Integer.parseInt(itemPosition));
                            SharedPreferences.Editor editor = itemPositionPref.edit();
                            editor.clear();
                            editor.commit();
                        }

                    } else if (response.body().getStatus().equals("400")) {
                        noEvents.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        progressDialog.dismiss();
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
            public void onFailure(Call<RetroDraftsEvents> call, Throwable t) {
                if (NoInternet.isOnline(getContext()) == false) {
                    progressDialog.dismiss();

                    NoInternet.dialog(getContext());
                } else {
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void init() {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        // recyclerView = view.findViewById(R.id.business_drafts__recyclerview);
        noEvents = view.findViewById(R.id.noevents_drafts);
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
        Collections.reverse(event_id);
        Collections.reverse(eventTIme);
        Collections.reverse(draftsimages);
    }
}
