package com.mandywebdesign.impromptu.MyEventsFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Home_Screen_Fragments.favouriteTab.EventsFrag;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.favouriteTab.Hosts;
import com.mandywebdesign.impromptu.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Favourite extends Fragment {


    Button Events, Hosts;
    FragmentManager manager;
    View view;
    String eventType,favType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favourite, container, false);

        manager = getFragmentManager();

        initlization();
        listeners();

        Bundle bundle = getArguments();
        if (bundle!=null){
            eventType = bundle.getString("eventType");
            favType = bundle.getString("other_events");

            if (favType==(""))
            {
                hostData();
                manager.beginTransaction().replace(R.id.myEvent_favourite_framelayout,new Hosts()).commit();
            }else {
                eventData();
                manager.beginTransaction().replace(R.id.myEvent_favourite_framelayout,new EventsFrag()).commit();
            }
        }else {
            manager.beginTransaction().replace(R.id.myEvent_favourite_framelayout, new EventsFrag()).commit();
        }
        return view;
    }

    private void listeners() {
        Events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventData();
                manager.beginTransaction().replace(R.id.myEvent_favourite_framelayout, new EventsFrag()).commit();


            }
        });
        Hosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hostData();
                manager.beginTransaction().replace(R.id.myEvent_favourite_framelayout, new Hosts()).commit();


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
        Events = (Button) view.findViewById(R.id.myEvent_events_btn);
        Hosts = (Button) view.findViewById(R.id.myEvent_hosting_btn);

        Events.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        Events.setTextColor(getResources().getColor(R.color.colortextwhite));

        Hosts.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        Hosts.setTextColor(getResources().getColor(R.color.colorTheme));


    }

}
