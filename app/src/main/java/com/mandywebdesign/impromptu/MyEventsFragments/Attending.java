package com.mandywebdesign.impromptu.MyEventsFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Home_Screen_Fragments.AttendingTab.Past;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AttendingTab.Upcoming;
import com.mandywebdesign.impromptu.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Attending extends Fragment {


    Button Upcoming,Past;
    FragmentManager manager;
    View view;
    String eventType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_attending, container, false);
        manager = getFragmentManager();

        init();

        Bundle bundle = getArguments();

        if(bundle!=null)
        {
            eventType = bundle.getString("eventType");
            if (eventType==("upcoming"))
            {
                upcomingColor();
               manager.beginTransaction().replace(R.id.attending_events_frame_layout,new Upcoming()).commit();
            }else if (eventType==("past"))
            {
                pastColor();
                manager.beginTransaction().replace(R.id.attending_events_frame_layout,new Past()).commit();
            }else{
                upcomingColor();
                manager.beginTransaction().replace(R.id.attending_events_frame_layout,new Upcoming()).commit();
            }
        }else {
            upcomingColor();
            manager.beginTransaction().replace(R.id.attending_events_frame_layout,new Upcoming()).commit();
        }
        listeners();

        return view;
    }

    private void init() {
        Upcoming = (Button)view.findViewById(R.id.myEvent_upcoming_btn);
        Past=(Button)view.findViewById(R.id.myEvent_past_btn);


        Upcoming.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        Upcoming.setTextColor(getResources().getColor(R.color.colortextwhite));

        Past.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        Past.setTextColor(getResources().getColor(R.color.colorTheme));
    }

    private void listeners() {

        Upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upcomingColor();
                manager.beginTransaction().replace(R.id.attending_events_frame_layout,new Upcoming()).commit();
            }
        });

        Past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pastColor();
                manager.beginTransaction().replace(R.id.attending_events_frame_layout,new Past()).commit();

            }
        });

    }

    private void pastColor() {
        Past.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        Past.setTextColor(getResources().getColor(R.color.colortextwhite));

        Upcoming.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        Upcoming.setTextColor(getResources().getColor(R.color.colorTheme));
    }

    private void upcomingColor() {

        Upcoming.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        Upcoming.setTextColor(getResources().getColor(R.color.colortextwhite));

        Past.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        Past.setTextColor(getResources().getColor(R.color.colorTheme));
    }


}
