package com.mandywebdesign.impromptu.Filter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AddEvents.EventDetailsActivity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Home;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.NormalEventPrice;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    ImageButton close;
    ImageView locationPin;
    View view;
    FragmentManager manager;
    TextView cityname, endcost;
    AutoCompleteTextView searchLoc;
    AutoCompleteAdapter adapter;
    PlacesClient placesClient;
    public static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 121;
    MapView mapView;
    GoogleMap Gmap;
    RadioButton male, female, all;
    RadioGroup radioGroup;
    String lat = "0.0";
    static String eventMax_Price, formattedDate, todayENdtime, gender, itemPosition;
    static String value;
    String lng = "0.0";
    LatLng latLng;
    Dialog progressDialog;
    String city = "";
    String social_token;
    Button today, tomorrow, setlocation, filter_bt_filter;
    ArrayList<LatLng> loc = new ArrayList<>();
    SeekBar seekBar;
    TextView filterPrice;
    String latt, lnng, getGender;
    SharedPreferences sharedPreferences;
    int step = 1;
    int min = 0;
    Intent intent;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        manager = getSupportFragmentManager();
        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();

        autocompleteAddress();
        init();
        intent = getIntent();

        Calendar c = Calendar.getInstance();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = null;
        try {
            date = (Date) formatter.parse(formatter.format(c.getTime()));
            Log.d("TodayDate", String.valueOf(date.getTime()));
            formattedDate = String.valueOf(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //------------today enddatetime=---------------
        DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            String endtime = formatter1.format(c.getTime()) + " 23:59";
            Date date1 = formatter.parse(endtime);
            Log.d("TodayDATE", "" + date1.getTime());
            todayENdtime = String.valueOf(date1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        social_token = "Bearer " + sharedPreferences.getString("Socailtoken", "");
        getGender = sharedPreferences.getString("profilegender", "");
        getEventPrice();

        value = eventMax_Price;
        gender = "all";

        if (getGender.equals("Male")) {
            female.setVisibility(View.GONE);
        } else if (getGender.equals("Female")) {
            male.setVisibility(View.GONE);
        }


        Places.initialize(this, getResources().getString(R.string.googleclientId));

        listerner();

        //for today button
        today.setBackground(getResources().getDrawable(R.drawable.button_color_theme));
        today.setTextColor(getResources().getColor(R.color.colortextwhite));

        tomorrow.setBackground(getResources().getDrawable(R.drawable.linearback));
        tomorrow.setTextColor(getResources().getColor(R.color.colorTheme));

        seekBar.getProgressDrawable().setColorFilter(R.color.colorTheme, PorterDuff.Mode.MULTIPLY);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = String.valueOf(min + (progress * step));
                //Toast.makeText(FilterActivity.this, ""+value, Toast.LENGTH_SHORT).show();
                filterPrice.setText("- £" + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.filter_male_only:
                        gender = "male";
                        break;

                    case R.id.filter_radio_female_only:
                        gender = "female";
                        break;

                    case R.id.filter_radio_all:
                        gender = "all";
                        break;
                }
            }
        });

        statusCheck();
//        checkPermissions();


        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) FilterActivity.this);
        MapsInitializer.initialize(this);


    }

    private void autocompleteAddress() {
        String apiKey = getString(R.string.googleclientId);
        if (apiKey.isEmpty()) {

            return;
        }

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = Places.createClient(this);
    }

    private void getEventPrice() {
        progressDialog.show();
        Call<NormalEventPrice> call = WebAPI.getInstance().getApi().eventPrice();
        call.enqueue(new Callback<NormalEventPrice>() {
            @Override
            public void onResponse(Call<NormalEventPrice> call, Response<NormalEventPrice> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {

                        eventMax_Price = response.body().getData().getPrice();
                        endcost.setText("£ " + "" + "50");
                        seekBar.setMax(50);
                    }

                }
                /*else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }*/

            }

            @Override
            public void onFailure(Call<NormalEventPrice> call, Throwable t) {
                if (NoInternet.isOnline(FilterActivity.this) == false) {
                    NoInternet.dialog(FilterActivity.this);
                    progressDialog.dismiss();
                }
            }
        });
    }


    private void init() {
        close = findViewById(R.id.close_filter);

        searchLoc = findViewById(R.id.search_loc);
        searchLoc.setThreshold(1);
        searchLoc.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        searchLoc.setAdapter(adapter);

        mapView = findViewById(R.id.mapview);
        today = findViewById(R.id.filter_today);
        tomorrow = findViewById(R.id.filter_tomorrow);
        seekBar = findViewById(R.id.costseekbar);
        cityname = findViewById(R.id.loc);
        setlocation = findViewById(R.id.setLocatiion);
        filterPrice = findViewById(R.id.filterprice);
        endcost = findViewById(R.id.endcost);
        filter_bt_filter = findViewById(R.id.filter_bt_filter);
        male = findViewById(R.id.filter_male_only);
        female = findViewById(R.id.filter_radio_female_only);
        all = findViewById(R.id.filter_radio_all);
        radioGroup = findViewById(R.id.radiogroup_filter);
        locationPin = findViewById(R.id.locationpin);

    }

    private void listerner() {

        close.setOnClickListener(this);
        today.setOnClickListener(this);
        tomorrow.setOnClickListener(this);
        filter_bt_filter.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == close) {

            onBackPressed();
        }
       /* if (v == searchLoc) {
            if (isNetworkIsConnected()) {

                Intent intent = new Intent(FilterActivity.this, CustomPlacePicker.class);
                intent.putExtra("is_from","filter");
                startActivity(intent);
                finish();
//                FireSearchIntent();
//                showPlacePicker();
//                locationPicker();
            } else {
                Toast.makeText(FilterActivity.this, "Check Internet Connection...", Toast.LENGTH_SHORT).show();

            }*/

//        }

        if (v == filter_bt_filter) {

            if (male.isChecked() || female.isChecked() || all.isChecked()) {
                progressDialog.show();
                final String loc = city;
                lat = latt;
                lng = lnng;
                String value1;

                if (value.equals("50")) {
                    value1 = "0-" + eventMax_Price;
                } else {
                    value1 = "0-" + value;
                }

                progressDialog.dismiss();

                Intent intent = new Intent(FilterActivity.this, FilteredActivity.class);
                intent.putExtra("loc", loc);
                intent.putExtra("gender", gender);
                intent.putExtra("date", formattedDate);
                intent.putExtra("price", value1);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("edate", todayENdtime);
                startActivity(intent);
            } else {

                Toast.makeText(FilterActivity.this, "Select atleast one option from gender", Toast.LENGTH_SHORT).show();
            }


            Log.d("filter", "" + loc + " \n" + formattedDate + "\n " + "0-" + value + " \n" + gender + " \n" + social_token);
        }

        if (v == today) {


            today.setBackground(getResources().getDrawable(R.drawable.button_color_theme));
            today.setTextColor(getResources().getColor(R.color.colortextwhite));

            tomorrow.setBackground(getResources().getDrawable(R.drawable.linearback));
            tomorrow.setTextColor(getResources().getColor(R.color.colorTheme));


//            frommilles = String.valueOf(date.getTime());

            Calendar c = Calendar.getInstance();

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

            Date date = null;
            try {
                date = (Date) formatter.parse(formatter.format(c.getTime()));
                Log.d("TodayDate", String.valueOf(date.getTime()));
                formattedDate = String.valueOf(date.getTime());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            //---------------******************---------------------

            try {
                String endtime = formatter1.format(c.getTime()) + " 23:59";
                Date date1 = formatter.parse(endtime);
                Log.d("TodayDATE", "" + date1.getTime());
                todayENdtime = String.valueOf(date1.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else if (v == tomorrow) {

            tomorrow.setBackground(getResources().getDrawable(R.drawable.button_color_theme));
            tomorrow.setTextColor(getResources().getColor(R.color.colortextwhite));

            today.setBackground(getResources().getDrawable(R.drawable.linearback));
            today.setTextColor(getResources().getColor(R.color.colorTheme));

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 1);

            System.out.println("Current time ==> " + c.getTime());

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = (Date) formatter.parse(formatter.format(c.getTime()));
                Log.d("Tommorow", String.valueOf(date.getTime()));
                formattedDate = String.valueOf(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat formatter0 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            try {
                String endtime = formatter1.format(c.getTime()) + " 23:59";
                Date date1 = formatter0.parse(endtime);
                Log.d("TommorowEnd", "" + date1.getTime());
                todayENdtime = String.valueOf(date1.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public boolean isNetworkIsConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            progressDialog.dismiss();
        } else {

            CurrentLocation();

        }

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(FilterActivity.this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

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
        final FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(FilterActivity.this);
        if (ActivityCompat.checkSelfPermission(FilterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(FilterActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
                                // location(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

                                if (location == null) {
                                    Toast.makeText(FilterActivity.this, "Unable to get Location", Toast.LENGTH_SHORT).show();
                                } else {
                                    lat = String.valueOf(location.getLatitude());
                                    lng = String.valueOf(location.getLongitude());
                                }

                                Log.d("Location", "" + lat + "" + lng);
                                Geocoder geocoder = new Geocoder(FilterActivity.this, Locale.getDefault());
                                try {
                                    List<Address> address = (List<Address>) geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);

                                    if (address != null) {
                                        city = address.get(0).getAdminArea();
                                        cityname.setText(address.get(0).getLocality());
                                    } else {
                                        cityname.setText("Address not found");
                                    }

                                    if (mapView != null) {
                                        mapView.onCreate(null);
                                        mapView.onResume();
                                        mapView.getMapAsync((OnMapReadyCallback) FilterActivity.this);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        task.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FilterActivity.this, "Location Not Found...,Enter Location Manually...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        }
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Gmap = googleMap;
        Gmap.getUiSettings().setMyLocationButtonEnabled(true);
        Gmap.getUiSettings().setZoomControlsEnabled(true);

        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).zoom(15).bearing(0).tilt(40).build();
        Gmap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if (ActivityCompat.checkSelfPermission(FilterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FilterActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Gmap.setMyLocationEnabled(true);
        getCityName(lat, lng);

        if (lat != null && lng != null) {
            Gmap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng midLatLng = Gmap.getCameraPosition().target;

                    latt = String.valueOf(midLatLng.latitude);
                    lnng = String.valueOf(midLatLng.longitude);

                    Log.d("cameralatlong", "" + latt + "    " + lnng);
                    getCityName(latt, lnng);
                }
            });


            setlocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Geocoder geocoder = new Geocoder(FilterActivity.this, Locale.getDefault());
                    try {
                        List<Address> address = (List<Address>) geocoder.getFromLocation(Double.valueOf(latt), Double.valueOf(lnng), 1);
                        city = address.get(0).getLocality();
                        if (address != null) {
                            city = address.get(0).getAddressLine(0);
                            cityname.setText(address.get(0).getAddressLine(0));
                        } else {
                            cityname.setText("Address not found");
                        }

                        lat = latt;
                        lng = lnng;


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(FilterActivity.this, "New Location Set " + city, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getCityName(String lat, String lng) {
        Geocoder geocoder = new Geocoder(FilterActivity.this, Locale.getDefault());
        List<Address> address = null;
        try {
            address = (List<Address>) geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
            if (address != null) {

                if (address.size() == 0) {
                    cityname.setText("Address not found");
                } else {
                    city = address.get(0).getLocality();
                    cityname.setText(address.get(0).getAddressLine(0));
                }


            } else {
                cityname.setText("Address not found");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(FilterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(FilterActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(FilterActivity.this, "No permission Granted", Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }

    }


    private void location(String lat, String lng) {


        Geocoder geocoder = new Geocoder(FilterActivity.this, Locale.getDefault());
        try {
            List<Address> address = (List<Address>) geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(lng), 1);
            city = address.get(0).getLocality();

            if (address != null) {
                city = address.get(0).getAddressLine(0);
                cityname.setText(city);
            } else {
                cityname.setText("Address not found");
            }


            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(FilterActivity.this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
                            Geocoder geocoder = new Geocoder(FilterActivity.this);
                            try {
                                List<Address> addresses = geocoder.getFromLocationName(loc, 10);
                                List<LatLng> latLngs = new ArrayList<LatLng>(addresses.size());

                                Log.d("latLngs", String.valueOf(latLngs));


                                for (Address a : addresses) {
                                    if (a.hasLatitude() && a.hasLongitude()) {
                                        latLngs.add(new LatLng(a.getLatitude(), a.getLongitude()));

                                        String lat1 = String.valueOf(a.getLatitude());
                                        String lng1 = String.valueOf(a.getLongitude());


                                        lat = lat1;
                                        lng = lng1;
                                        getCityName(lat1, lng1);
                                        location(lat1, lng1);

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
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
