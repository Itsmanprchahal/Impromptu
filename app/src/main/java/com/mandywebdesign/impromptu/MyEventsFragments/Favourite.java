package com.mandywebdesign.impromptu.MyEventsFragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mandywebdesign.impromptu.Home_Screen_Fragments.favouriteTab.EventsFrag;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.favouriteTab.Hosts;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class Favourite extends Fragment {


    Button Events, Hosts;
    FragmentManager manager;
    View view;
    String eventType,favType;
    SharedPreferences sharedPreferences,itemPositionPref;
    SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favourite, container, false);

        manager = getFragmentManager();

        initlization();
        listeners();
        setPostion();

        Bundle bundle = getArguments();
        if (bundle!=null){
            eventType = bundle.getString("eventType");
            favType = bundle.getString("other_events");

            if (favType==(""))
            {
                hostData();
                manager.beginTransaction().replace(R.id.myEvent_favourite_framelayout,new Hosts()).commit();
                setPostion();
            }else {
                eventData();
                manager.beginTransaction().replace(R.id.myEvent_favourite_framelayout,new EventsFrag()).commit();
                setPostion();
            }
        }else {
            manager.beginTransaction().replace(R.id.myEvent_favourite_framelayout, new EventsFrag()).commit();
            setPostion();
        }
        return view;
    }

    private void setPostion() {
        itemPositionPref = getContext().getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        editor = itemPositionPref.edit();
        editor.putString(Constants.itemPosition,"0");
        editor.commit();
    }

    private void listeners() {
        Events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventData();
                manager.beginTransaction().replace(R.id.myEvent_favourite_framelayout, new EventsFrag()).commit();
                setPostion();


            }
        });
        Hosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hostData();
                manager.beginTransaction().replace(R.id.myEvent_favourite_framelayout, new Hosts()).commit();

                setPostion();
            }
        });
    }

    private void hostData() {
        Hosts.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        Hosts.setTextColor(getResources().getColor(R.color.colortextwhite));

        Events.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        Events.setTextColor(getResources().getColor(R.color.colorTheme));
    }

    private void eventData() {
        Events.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        Events.setTextColor(getResources().getColor(R.color.colortextwhite));

        Hosts.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        Hosts.setTextColor(getResources().getColor(R.color.colorTheme));

    }


    private void initlization() {
        Events = view.findViewById(R.id.myEvent_events_btn);
        Hosts = view.findViewById(R.id.myEvent_hosting_btn);

        Events.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        Events.setTextColor(getResources().getColor(R.color.colortextwhite));

        Hosts.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        Hosts.setTextColor(getResources().getColor(R.color.colorTheme));


    }

}
