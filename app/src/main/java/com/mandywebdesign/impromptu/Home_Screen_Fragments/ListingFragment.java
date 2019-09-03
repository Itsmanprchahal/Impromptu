package com.mandywebdesign.impromptu.Home_Screen_Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.mandywebdesign.impromptu.MyEventsFragments.Hosting;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.ui.Add_Adverts;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListingFragment extends Fragment {


    Toolbar toolbar;
  Button myListing_events,mtListing_adverts;
  View view;
  FragmentManager manager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_listing, container, false);

        manager = getFragmentManager();

        init();
        listerners();

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mylistingFrame,new Hosting());
        transaction.commit();

        return view;
    }

    private void listerners() {

        myListing_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventColor();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.mylistingFrame,new Hosting());
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        mtListing_adverts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // advertsColor();

                Intent intent = new Intent(getContext(), Add_Adverts.class);
                startActivity(intent);
            }
        });
    }

    private void advertsColor() {
        myListing_events.setBackgroundColor(getResources().getColor(R.color.colortextwhite));
        myListing_events.setTextColor(getResources().getColor(R.color.colorTheme));

        mtListing_adverts.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        mtListing_adverts.setTextColor(getResources().getColor(R.color.colortextwhite));
    }

    private void eventColor() {

        myListing_events.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        myListing_events.setTextColor(getResources().getColor(R.color.colortextwhite));

        mtListing_adverts.setBackgroundColor(getResources().getColor(R.color.colortextwhite));
        mtListing_adverts.setTextColor(getResources().getColor(R.color.colorTheme));

    }

    private void init() {

        myListing_events = view.findViewById(R.id.myListing_events);
        mtListing_adverts = view.findViewById(R.id.mtListing_adverts);

        myListing_events.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        myListing_events.setTextColor(getResources().getColor(R.color.colortextwhite));

        mtListing_adverts.setBackgroundColor(getResources().getColor(R.color.colortextwhite));
        mtListing_adverts.setTextColor(getResources().getColor(R.color.colorTheme));
    }

}
