package com.mandywebdesign.impromptu.Filter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AddEvents.EventDetailsActivity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Home;
import com.mandywebdesign.impromptu.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CustomPlacePicker extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteAdapter adapter;
    TextView responseView;
    PlacesClient placesClient;
    ImageButton back;
    Intent intent;
    String ifFrom, city, eventTitle, eventDesc, eventCate, edit, value, link1, link2, link3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_place_picker);


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String apiKey = getString(R.string.googleclientId);
        if (apiKey.isEmpty()) {

            return;
        }

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = Places.createClient(this);
        initAutoCompleteTextView();
    }

    private void initAutoCompleteTextView() {

        autoCompleteTextView = findViewById(R.id.auto);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        autoCompleteTextView.setAdapter(adapter);
    }

    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {

                            String loc = String.valueOf(task.getPlace());
                            Geocoder geocoder = new Geocoder(CustomPlacePicker.this);
                            try {
                                List<Address> addresses = geocoder.getFromLocationName(loc, 10);
                                List<LatLng> latLngs = new ArrayList<LatLng>(addresses.size());

                                Log.d("latLngs", String.valueOf(latLngs));


                                for (Address a : addresses) {
                                    if (a.hasLatitude() && a.hasLongitude()) {
                                        latLngs.add(new LatLng(a.getLatitude(), a.getLongitude()));

                                        String lat = String.valueOf(a.getLatitude());
                                        String lng = String.valueOf(a.getLongitude());


                                        Intent intent = new Intent(CustomPlacePicker.this, FilterActivity.class);
                                        intent.putExtra("lat", lat);
                                        intent.putExtra("lng", lng);
                                        intent.putExtra("from", "picker");
                                        startActivity(intent);
                                        finish();

                                        Log.d("checkplace", "" + String.valueOf(a.getLatitude()));


                                    }

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            responseView.setText(e.getMessage());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
