package com.mandywebdesign.impromptu.Home_Screen_Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mandywebdesign.impromptu.MyEventsFragments.Hosting;
import com.mandywebdesign.impromptu.MyEventsFragments.Favourite;
import com.mandywebdesign.impromptu.MyEventsFragments.Attending;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Utils.Constants;
import com.mandywebdesign.impromptu.ui.Home_Screen;

/**
 * A simple {@link Fragment} subclass.
 */
public class Events extends Fragment {

    private Button attending, hosting, favourites, upComing;
    FragmentManager manager;
    SharedPreferences sharedPreferences, itemPositionPref;
    String BToken, S_Token, itemPosition, eventType="", favType;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event, container, false);

        sharedPreferences = getContext().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");
        itemPositionPref = getContext().getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        itemPosition = itemPositionPref.getString(Constants.itemPosition, String.valueOf(0));
        manager = getFragmentManager();
        initlization();

        Bundle bundle = getArguments();

        if (bundle != null) {
            eventType = bundle.getString("eventType");
            favType = bundle.getString("other_events");
            if (eventType.equals("live") || eventType.equals("history") || eventType.equals("draft")) {
                hostingData();
            } else if (eventType.equals("upcoming") || eventType.equals("past")||eventType.equals("")) {
                attendingData();
            } else if (eventType.equals("fav")) {
                favouritesData();
            }
        }

        else {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.myEvent_framelayout,new Attending());
            transaction.commit();

            attending.setBackgroundColor(getResources().getColor(R.color.colorTheme));
            attending.setTextColor(getResources().getColor(R.color.colortextwhite));

            hosting.setBackgroundColor(getResources().getColor(R.color.colorTrans));
            hosting.setTextColor(getResources().getColor(R.color.colorTheme));

            favourites.setBackgroundColor(getResources().getColor(R.color.colorTrans));
            favourites.setTextColor(getResources().getColor(R.color.colorTheme));

        }

        attending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendingData();
                clearItemPosition();
            }
        });


        hosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hostingData();
                clearItemPosition();
            }
        });


        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                favouritesData();
                clearItemPosition();
            }
        });


        return view;
    }

    private void favouritesData() {

        favourites.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        favourites.setTextColor(getResources().getColor(R.color.colortextwhite));

        hosting.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        hosting.setTextColor(getResources().getColor(R.color.colorTheme));

        attending.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        attending.setTextColor(getResources().getColor(R.color.colorTheme));

        Bundle bundle = new Bundle();
        bundle.putString("eventType", eventType);
        bundle.putString("other_events", favType);
//
        Favourite favourite = new Favourite();
        favourite.setArguments(bundle);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.myEvent_framelayout, favourite);
        transaction.commit();
    }

    private void attendingData() {

        attending.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        attending.setTextColor(getResources().getColor(R.color.colortextwhite));

        hosting.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        hosting.setTextColor(getResources().getColor(R.color.colorTheme));

        favourites.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        favourites.setTextColor(getResources().getColor(R.color.colorTheme));

        Bundle bundle = new Bundle();
        bundle.putString("eventType", eventType);
//        Toast.makeText(getContext(), "Value_is"+eventType, Toast.LENGTH_SHORT).show();
        Attending attending = new Attending();
        attending.setArguments(bundle);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.myEvent_framelayout, attending);
        transaction.commit();

    }

    private void hostingData() {
        hosting.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        hosting.setTextColor(getResources().getColor(R.color.colortextwhite));

        attending.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        attending.setTextColor(getResources().getColor(R.color.colorTheme));

        favourites.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        favourites.setTextColor(getResources().getColor(R.color.colorTheme));

        Bundle bundle = new Bundle();
        bundle.putString("eventType", eventType);

        Hosting hosting = new Hosting();
        hosting.setArguments(bundle);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.myEvent_framelayout, hosting);
        transaction.commit();

    }


    public void clearItemPosition() {
        SharedPreferences.Editor editor = itemPositionPref.edit();
        editor.clear();
        editor.commit();
    }

    private void initlization() {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        attending = (Button) view.findViewById(R.id.myEvent_attending_btn);
        hosting = (Button) view.findViewById(R.id.myEvent_hosting_btn);
        favourites = (Button) view.findViewById(R.id.myEvent_favorite_btn);
        upComing = (Button) view.findViewById(R.id.myEvent_upcoming_btn);

    }
}
