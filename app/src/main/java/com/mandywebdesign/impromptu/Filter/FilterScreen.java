package com.mandywebdesign.impromptu.Filter;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ConfigurationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Home;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.NormalEventPrice;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterScreen extends Fragment implements View.OnClickListener,
        OnMapReadyCallback {
    int PLACE_PICKER_REQUEST = 1;
    private static final int PLACE_REQUEST_2 = 10;
    ImageButton close;
    ImageView locationPin;
    View view;
    FragmentManager manager;
    TextView searchLoc, cityname, endcost;
    public static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 121;
    MapView mapView;
    GoogleMap Gmap;
    RadioButton male, female, all;
    RadioGroup radioGroup;
    String lat = "51.5074";
    static String eventMax_Price, formattedDate, gender;
    static String value;
    String lng = "0.1278";
    LatLng latLng;
    ProgressDialog progressDialog;
    String city = "";
    String social_token;
    Button today, tomorrow, setlocation, filter_bt_filter;
    ArrayList<LatLng> loc = new ArrayList<>();
    SeekBar seekBar;
    TextView filterPrice;
    String latt, lnng,getGender;
    SharedPreferences sharedPreferences;
    int step = 1;
    int min = 0;

    int AUTOCOMPLETE_REQUEST_CODE = 1;
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_filter_screen, container, false);

        manager = getFragmentManager();

        progressDialog = ProgressBarClass.showProgressDialog(getContext(), "please wait...");
        progressDialog.dismiss();

        init();
        Calendar c = Calendar.getInstance();



        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        formattedDate = df.format(c.getTime());

        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        social_token = "Bearer " + sharedPreferences.getString("Socailtoken", "");
        getGender  = sharedPreferences.getString("profilegender","");
        getEventPrice();

        value = eventMax_Price;
        gender = "all";

        if (getGender.equals("Male"))
        {
            female.setVisibility(View.GONE);
        }else if (getGender.equals("Female"))
        {
            male.setVisibility(View.GONE);
        }


        Places.initialize(getContext(), getContext().getResources().getString(R.string.googleclientId));

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
        mapView.getMapAsync(this);
        MapsInitializer.initialize(this.getActivity());


        return view;
    }


    private void getEventPrice() {
        progressDialog.show();
        Call<NormalEventPrice> call = WebAPI.getInstance().getApi().eventPrice();
        call.enqueue(new Callback<NormalEventPrice>() {
            @Override
            public void onResponse(Call<NormalEventPrice> call, Response<NormalEventPrice> response) {
                progressDialog.dismiss();
                if (response.body()!=null) {
                    if (response.body().getStatus().equals("200"))
                    {

                        eventMax_Price = response.body().getData().getPrice();
                        endcost.setText("£ " + response.body().getData().getPrice());
                        seekBar.setMax(Integer.parseInt((response.body().getData().getPrice())));
                    }

                } else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<NormalEventPrice> call, Throwable t) {
                if (NoInternet.isOnline(getContext()) == false) {
                    NoInternet.dialog(getContext());
                }
            }
        });
    }


    private void init() {
        Home_Screen.bottomNavigationView.setVisibility(View.GONE);
        close = view.findViewById(R.id.close_filter);
        searchLoc = view.findViewById(R.id.search_loc);
        mapView = view.findViewById(R.id.mapview);
        today = view.findViewById(R.id.filter_today);
        tomorrow = view.findViewById(R.id.filter_tomorrow);
        seekBar = view.findViewById(R.id.costseekbar);
        cityname = view.findViewById(R.id.loc);
        setlocation = view.findViewById(R.id.setLocatiion);
        filterPrice = view.findViewById(R.id.filterprice);
        endcost = view.findViewById(R.id.endcost);
        filter_bt_filter = view.findViewById(R.id.filter_bt_filter);
        male = view.findViewById(R.id.filter_male_only);
        female = view.findViewById(R.id.filter_radio_female_only);
        all = view.findViewById(R.id.filter_radio_all);
        radioGroup = view.findViewById(R.id.radiogroup_filter);
        locationPin = view.findViewById(R.id.locationpin);


    }

    private void listerner() {

        close.setOnClickListener(this);
        searchLoc.setOnClickListener(this);
        today.setOnClickListener(this);
        tomorrow.setOnClickListener(this);
        filter_bt_filter.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == close) {

            manager.beginTransaction().replace(R.id.home_frame_layout, new Home()).commit();
        }
        if (v == searchLoc) {
            if (isNetworkIsConnected()) {

                FireSearchIntent();
            } else {
                Toast.makeText(getContext(), "Check Internet Connection...", Toast.LENGTH_SHORT).show();

            }
        }

        if (v == filter_bt_filter) {

            if (male.isChecked() || female.isChecked() || all.isChecked()) {
                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                final String loc = city;
                lat = latt;
                lng = lnng;


                final String value1 = "0-" + value;
                Log.d("+++++++++", "+++ price +++" + value1);
                progressDialog.dismiss();

                Bundle bundle = new Bundle();
                bundle.putString("loc", loc);
                bundle.putString("gender", gender);
                bundle.putString("date", formattedDate);
                bundle.putString("price", value1);
                bundle.putString("lat", lat);
                bundle.putString("lng", lng);

                FilteredScreen filterScreen = new FilteredScreen();
                filterScreen.setArguments(bundle);
                manager.beginTransaction().replace(R.id.home_frame_layout, filterScreen).commit();
            } else {

                Toast.makeText(getContext(), "Select atleast one option from gender", Toast.LENGTH_SHORT).show();
            }


            Log.d("filter", "" + loc + " \n" + formattedDate + "\n " + "0-" + value + " \n" + gender + " \n" + social_token);
        }

        if (v == today) {


            today.setBackground(getResources().getDrawable(R.drawable.button_color_theme));
            today.setTextColor(getResources().getColor(R.color.colortextwhite));

            tomorrow.setBackground(getResources().getDrawable(R.drawable.linearback));
            tomorrow.setTextColor(getResources().getColor(R.color.colorTheme));


            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            formattedDate = df.format(c.getTime());


        } else if (v == tomorrow) {

            tomorrow.setBackground(getResources().getDrawable(R.drawable.button_color_theme));
            tomorrow.setTextColor(getResources().getColor(R.color.colortextwhite));

            today.setBackground(getResources().getDrawable(R.drawable.linearback));
            today.setTextColor(getResources().getColor(R.color.colorTheme));

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 1);

            System.out.println("Current time ==> " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            formattedDate = df.format(c.getTime());

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
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    public void FireSearchIntent() {
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            CurrentLocation();
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
                                // location(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

                                if (location == null) {
                                    Toast.makeText(getContext(), "Unable to get Location", Toast.LENGTH_SHORT).show();
                                } else {
                                    lat = String.valueOf(location.getLatitude());
                                    lng = String.valueOf(location.getLongitude());
                                }

                                Log.d("Location", "" + lat + "" + lng);
                                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                try {
                                    List<Address> address = (List<Address>) geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);

                                    if (address != null) {
                                        city = address.get(0).getAdminArea();
                                        cityname.setText(address.get(0).toString());
//                                        Toast.makeText(getContext(), "141 "+address.get(0), Toast.LENGTH_SHORT).show();
                                    } else {
                                        cityname.setText("Address not found");
                                    }

                                    if (mapView != null) {
                                        mapView.onCreate(null);
                                        mapView.onResume();
                                        mapView.getMapAsync(FilterScreen.this);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
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
    public void onMapReady(final GoogleMap googleMap) {
        Gmap = googleMap;
        Gmap.getUiSettings().setMyLocationButtonEnabled(true);
        Gmap.getUiSettings().setZoomControlsEnabled(true);
//        Gmap.getUiSettings().setScrollGesturesEnabled(false);


        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).zoom(15).bearing(0).tilt(40).build();
        Gmap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).title(city));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    try {
                        List<Address> address = (List<Address>) geocoder.getFromLocation(Double.valueOf(latt), Double.valueOf(lnng), 1);
                        city = address.get(0).getLocality();
                        Toast.makeText(getContext(), ""+address.get(0).getLocale(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "New Location Set " + city, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void getCityName(String lat, String lng) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> address = null;
        try {
            address = (List<Address>) geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
            if (address != null) {

                if (address.size()==0)
                {
                    cityname.setText("Address not found");
                }else {
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String puneet = String.valueOf(place).subSequence(String.valueOf(place).indexOf("name") + 5, String.valueOf(place).length()).toString();
                String[] puni = puneet.split(",");
                Log.d("checkplace", "" + String.valueOf(place));
                Log.d("checkplace", "" + String.valueOf(place).subSequence(String.valueOf(place).indexOf("name") + 5, String.valueOf(place).length()));
                Log.d("checkplace", "" + puni[0]);
                cityname.setText(puni[0]);
                //Toast.makeText(getContext(), ""+cityname.getText().toString(), Toast.LENGTH_SHORT).show();

                String loc = String.valueOf(place);
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    List<Address> addresses = geocoder.getFromLocationName(loc, 5);
                    List<LatLng> latLngs = new ArrayList<LatLng>(addresses.size());

                    if (addresses != null) {
                        city = addresses.get(0).getAddressLine(0);
                        cityname.setText(addresses.get(0).getAddressLine(0).toString());
                    } else {
                        cityname.setText("Address not found");
                    }


                    for (Address a : addresses) {
                        if (a.hasLatitude() && a.hasLongitude()) {
                            latLngs.add(new LatLng(a.getLatitude(), a.getLongitude()));

                            lat = String.valueOf(a.getLatitude());
                            lng = String.valueOf(a.getLongitude());
                            location(lat, lng);
                            Log.d("checkplace", "" + String.valueOf(a.getLatitude()));
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("Helloamit", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    private void location(String lat, String lng) {


        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
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
                mapView.getMapAsync(FilterScreen.this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}