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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.Drafts;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.History;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.Live;
import com.mandywebdesign.impromptu.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Hosting extends Fragment {
    private Button liveButton, draftButton, historyButton;
    FragmentManager manager;
    String BToken, S_Token;
    LinearLayout linearLayout;
    SharedPreferences sharedPreferences;
    View view;
    TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hosting, container, false);
        manager = getFragmentManager();

        sharedPreferences = getContext().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");

        initlization();
        listeners();


        if (!BToken.equalsIgnoreCase("")) {
            title.setVisibility(View.VISIBLE);
            attending();
            //  manager.beginTransaction().replace(R.id.hosting_event_frame_layout, new Live()).commit();

        } else {
            title.setVisibility(View.GONE);

            attending();
           // manager.beginTransaction().replace(R.id.hosting_event_frame_layout, new Live()).commit();
        }


        return view;
    }

    private void attending() {
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            String eventType = bundle.getString("eventType");
            if (eventType.equals("live"))
            {
                livebtcolor();
                manager.beginTransaction().replace(R.id.hosting_event_frame_layout, new Live()).commit();
            } else if (eventType.equals("history")) {
                HistorybtColor();
                manager.beginTransaction().replace(R.id.hosting_event_frame_layout, new History()).commit();
            }else if(eventType.equals("draft"))
            {
                draftsbtcolor();
                manager.beginTransaction().replace(R.id.hosting_event_frame_layout, new Drafts()).commit();
            }else {
                manager.beginTransaction().replace(R.id.hosting_event_frame_layout, new Live()).commit();
            }

          //  Toast.makeText(getContext(), ""+eventType, Toast.LENGTH_SHORT).show();
        }else {
              manager.beginTransaction().replace(R.id.hosting_event_frame_layout, new Live()).commit();
        }
    }


    private void initlization() {

        liveButton = (Button) view.findViewById(R.id.myEvent_Live_btn);
        draftButton = (Button) view.findViewById(R.id.myEvent_Draft_btn);
        historyButton = (Button) view.findViewById(R.id.myEvent_History_btn);
        title = (TextView) view.findViewById(R.id.hosting_title);
        linearLayout = (LinearLayout) view.findViewById(R.id.tabs_layout);

        liveButton.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        liveButton.setTextColor(getResources().getColor(R.color.colortextwhite));

        draftButton.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        draftButton.setTextColor(getResources().getColor(R.color.colorTheme));

        historyButton.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        historyButton.setTextColor(getResources().getColor(R.color.colorTheme));

    }

    private void listeners() {


        liveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                livebtcolor();
                manager.beginTransaction().replace(R.id.hosting_event_frame_layout, new Live()).commit();
            }
        });


        draftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draftsbtcolor();

                manager.beginTransaction().replace(R.id.hosting_event_frame_layout, new Drafts()).commit();


            }
        });


        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HistorybtColor();

                manager.beginTransaction().replace(R.id.hosting_event_frame_layout, new History()).commit();

            }
        });
    }

    public void livebtcolor() {
        liveButton.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        liveButton.setTextColor(getResources().getColor(R.color.colortextwhite));

        draftButton.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        draftButton.setTextColor(getResources().getColor(R.color.colorTheme));

        historyButton.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        historyButton.setTextColor(getResources().getColor(R.color.colorTheme));
    }

    public void HistorybtColor() {
        historyButton.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        historyButton.setTextColor(getResources().getColor(R.color.colortextwhite));

        draftButton.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        draftButton.setTextColor(getResources().getColor(R.color.colorTheme));

        liveButton.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        liveButton.setTextColor(getResources().getColor(R.color.colorTheme));
    }

    public void draftsbtcolor() {
        draftButton.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        draftButton.setTextColor(getResources().getColor(R.color.colortextwhite));

        liveButton.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        liveButton.setTextColor(getResources().getColor(R.color.colorTheme));

        historyButton.setBackgroundColor(getResources().getColor(R.color.colorTrans));
        historyButton.setTextColor(getResources().getColor(R.color.colorTheme));
    }

}