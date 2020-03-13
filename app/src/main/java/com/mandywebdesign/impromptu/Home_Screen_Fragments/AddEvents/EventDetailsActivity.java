package com.mandywebdesign.impromptu.Home_Screen_Fragments.AddEvents;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.mandywebdesign.impromptu.Filter.AutoCompleteAdapter;
import com.mandywebdesign.impromptu.Filter.CustomPlacePicker;
import com.mandywebdesign.impromptu.Filter.FilterActivity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Events;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Interfaces.WebAPI1;
import com.mandywebdesign.impromptu.Models.RetroPostcode;
import com.mandywebdesign.impromptu.Models.TicketTypeModel;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.PostCode;
import com.mandywebdesign.impromptu.Retrofit.RetroGetEventData;
import com.mandywebdesign.impromptu.Utils.Util;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
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
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button perviewButoon;
    Toolbar toolbar;
    SimpleDateFormat dateFormat;
    Calendar calendar, calendar3;
    AutoCompleteTextView event_lcation_address1, event_lcation_address2;
    EditText event_postcode, event_city, event_attendees_no, mDate, event_details_date_etto, eventTime_from, eventTime_to;
    RadioButton event_radiobutton_all, event_radiobutton_female, event_radiobutton_male;
    TextView addTicket;
    CheckBox freeevent_checkbox;
    Button okayDialog;
    Dialog progressDialog;
    ImageView close;
    String prceET, ticketET, tickettype, myFormat, to_date;
    String getPrceET, getTicketET, getTickettype;
    EditText price_et, tickettype_et;
    TextView numbersTicketET;
    DatePickerDialog.OnDateSetListener dateSetListener, dateSetListener1;
    String eventTitle, eventDesc, eventCate, edit, value, userToken, BToken, S_Token, getGender, link1 = "", link2 = "", link3 = "";
    public static String to_time_milles;
    String frommilles;
    String tomilles;
    int year1, hour1, minute1, second1, year2, month2, day2, hour2, minute2, second2;
    int month1;
    int date1 = 0, totalTicket;
    SharedPreferences sharedPreferences;
    Dialog dialog1;
    AutoCompleteAdapter adapter, autoCompleteAdapter;
    PlacesClient placesClient;
    long newChangeForDate = 0;

    EditText edt_tiketType;
    EditText edt_price;
    EditText edt_numbersOfTicket;
    TextView type1, type2, type3;
    Button btn_done;
    static TicketTypeModel ticketTypeModel;
    static ArrayList<TicketTypeModel> arryList;
    static int pendingTicker;
    ArrayList<String> tickettyp = new ArrayList<>();
    ArrayList<String> ticketprice = new ArrayList<>();
    ArrayList<String> ticketnumber = new ArrayList<>();

    String type;
    String values;
    String numbertickets;
    long startdatemilli;
    long enddatemilli;
    int totalticketsnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = "Bearer " + sharedPreferences.getString("Usertoken", "");
        getGender = sharedPreferences.getString("profilegender", "");
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");

        autocompleteAddress();
        dateFormat = new SimpleDateFormat("MM-dd-yy", Locale.US);
        init();
        Toolbar toolbar = findViewById(R.id.event_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        Intent intent = getIntent();
        eventTitle = intent.getStringExtra("eventTitle");
        eventDesc = intent.getStringExtra("eventDesc");
        eventCate = intent.getStringExtra("eventCate");
        edit = intent.getStringExtra("editevent");
        value = intent.getStringExtra("value");
        link1 = intent.getStringExtra("link1");
        link2 = intent.getStringExtra("link2");
        link3 = intent.getStringExtra("link3");

        if (getGender.equals("Male")) {
            event_radiobutton_female.setVisibility(View.GONE);
        } else if (getGender.equals("Female")) {
            event_radiobutton_male.setVisibility(View.GONE);
        }

        progressBar.setProgress(67);
        progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorTheme), PorterDuff.Mode.SRC_ATOP);

        calendar = Calendar.getInstance();

        if (value != null) {
            if (!BToken.equalsIgnoreCase("")) {
                editEVnet("Bearer " + BToken, value);
                type = "";
            } else if (!S_Token.equalsIgnoreCase("")) {
                editEVnet("Bearer " + S_Token, value);

                type = "";
            }
        }


        listeners();

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

    private void init() {

        progressBar = findViewById(R.id.event_progress_bar);
        perviewButoon = findViewById(R.id.event_preview);
        toolbar = findViewById(R.id.event_toolbar);
        mDate = findViewById(R.id.event_details_date_et);
        event_details_date_etto = findViewById(R.id.event_details_date_etto);
        eventTime_from = findViewById(R.id.event_from);
        eventTime_to = findViewById(R.id.event_to);
        addTicket = findViewById(R.id.event_tickettype);
        if (!BToken.equals("")) {
            addTicket.setText("+ Add ticket type");
        } else {
            addTicket.setText("+ Add ticket");
        }

        event_lcation_address1 = findViewById(R.id.event_lcation_address1);
        event_lcation_address1.setThreshold(1);
        event_lcation_address1.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        event_lcation_address1.setAdapter(adapter);

        event_lcation_address2 = findViewById(R.id.event_lcation_address2);
        event_lcation_address2.setThreshold(1);
        event_lcation_address2.setOnItemClickListener(autocompleteClickListener1);
        autoCompleteAdapter = new AutoCompleteAdapter(this, placesClient);
        event_lcation_address2.setAdapter(autoCompleteAdapter);

        event_postcode = findViewById(R.id.event_postcode);
        event_postcode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        event_city = findViewById(R.id.event_city);
        event_radiobutton_all = findViewById(R.id.event_radiobutton_all);
        event_radiobutton_female = findViewById(R.id.event_radiobutton_female);
        event_radiobutton_male = findViewById(R.id.event_radiobutton_male);
        event_attendees_no = findViewById(R.id.event_attendees_no);
        freeevent_checkbox = findViewById(R.id.freeevent_checkbox);
        close = findViewById(R.id.event_close);
        setSupportActionBar(toolbar);

    }


    public void listeners() {


        mDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EventDetailsActivity.this, R.style.DialogTheme
                        , dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


        mDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                event_details_date_etto.setText(mDate.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {

                } else {
                    event_details_date_etto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            DatePickerDialog datePickerDialog = new DatePickerDialog(EventDetailsActivity.this, R.style.DialogTheme
                                    , dateSetListener1, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                            datePickerDialog.show();
                            eventTime_to.setText("");
                        }
                    });
                }
            }
        });

        event_details_date_etto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event_details_date_etto.getText().length() == 0) {

                    Toast.makeText(EventDetailsActivity.this, "Enter start date", Toast.LENGTH_SHORT).show();
                }


            }
        });

        event_lcation_address1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (event_lcation_address1.getText().toString().contains(",")) {
                    String place = s.toString();
                    if (s != null) {
                        String[] name = place.split(",");
                        String Fname = name[0];
                        event_lcation_address1.setText(Fname + " ");

                    }
                }

            }
        });

        event_lcation_address2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (event_lcation_address2.getText().toString().contains(",")) {
                    String place = s.toString();
                    if (s != null) {
                        String[] name = place.split(",");
                        String Fname = name[0];
                        event_lcation_address2.setText(Fname + " ");

                    }
                }

            }
        });

        event_postcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        perviewButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent = new Intent(EventDetailsActivity.this, PerviewEventActivity.class);
                if (event_radiobutton_all.isChecked()) {
                    intent.putExtra("gender", "All");
                } else if (event_radiobutton_male.isChecked()) {
                    intent.putExtra("gender", "Male");
                } else if (event_radiobutton_female.isChecked()) {
                    intent.putExtra("gender", "Female");
                }

                if (freeevent_checkbox.isChecked()) {
                    intent.putExtra("freeevent", "0");
                    intent.putExtra("Price", "0");
                    intent.putExtra("ticketType", "");
                    intent.putExtra("numbersTickets", event_attendees_no.getText().toString());
                } else {
                    intent.putExtra("freeevent", "1");
                    intent.putExtra("Price", getPrceET);
                    intent.putExtra("ticketType", getTickettype);
                    intent.putExtra("numbersTickets", event_attendees_no.getText().toString());
                }

                if (TextUtils.isEmpty(event_lcation_address1.getText().toString())
                        && TextUtils.isEmpty(event_postcode.getText().toString()) && TextUtils.isEmpty(event_city.getText().toString())
                        && TextUtils.isEmpty(mDate.getText().toString()) && TextUtils.isEmpty(eventTime_from.getText().toString())
                        && TextUtils.isEmpty(eventTime_to.getText().toString())) {

                    event_lcation_address1.setError("Add address line 1");
                    event_postcode.setError("Add postcode");
                    event_city.setError("Add city");
                    mDate.setError("Set Date");
                    eventTime_from.setError("Set From Time");
                    eventTime_to.setError("Set To Time");

                } else if (TextUtils.isEmpty(event_lcation_address1.getText().toString())) {
                    event_lcation_address1.setError("Add address line 1");
                } else if (TextUtils.isEmpty(event_postcode.getText().toString()) || event_postcode.getError() != null) {
                    event_postcode.setError("Add Valid postcode");
                } else if (TextUtils.isEmpty(event_city.getText().toString())) {
                    event_city.setError("Add city");
                } else if (TextUtils.isEmpty(mDate.getText().toString())) {
                    mDate.setError("Enter Start Date");
                } else if (TextUtils.isEmpty(event_details_date_etto.getText().toString())) {
                    event_details_date_etto.setError("Enter End date");
                } else if (TextUtils.isEmpty(eventTime_from.getText().toString())) {
                    eventTime_from.setError("Set From Time");
                } else if (TextUtils.isEmpty(eventTime_to.getText().toString())) {
                    eventTime_to.setError("Set To Time");
                } else if (TextUtils.isEmpty(event_attendees_no.getText().toString())) {
                    event_attendees_no.setError("Set Attendees no.");
                } else if (!freeevent_checkbox.isChecked() && addTicket.getText().equals("+Add ticket type")) {
                    Toast.makeText(EventDetailsActivity.this, "Select event type", Toast.LENGTH_SHORT).show();
                } else if (event_radiobutton_male.isChecked() | event_radiobutton_female.isChecked() | event_radiobutton_all.isChecked()) {
                    String postcode;
                    if (event_postcode.getText().toString().contains(" ")) {
                        postcode = event_postcode.getText().toString().replace(" ", "");
                    } else {
                        postcode = event_postcode.getText().toString();
                    }

                    progressDialog.show();
                    progressDialog.setTitle("Checking Poscode...");
                    Call<PostCode> call = WebAPI1.getInstance().getApi().postcode(postcode);
                    call.enqueue(new Callback<PostCode>() {
                        @Override
                        public void onResponse(Call<PostCode> call, Response<PostCode> response) {
                            progressDialog.dismiss();
                            if (response.body() != null) {
                                if (response.code() == 200) {

                                    event_postcode.setError(null);

                                    String givenDateString = mDate.getText().toString() + " " + eventTime_from.getText().toString();
                                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                    Date date = null;
                                    try {
                                        date = formatter.parse(givenDateString);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    frommilles = String.valueOf(date.getTime());

                                    Log.d("frommilles", "" + frommilles);


                                    String givenDateString1 = event_details_date_etto.getText().toString() + " " + eventTime_to.getText().toString();
                                    DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                    Date date1 = null;
                                    try {
                                        date1 = formatter1.parse(givenDateString1);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    tomilles = String.valueOf(date1.getTime());

                                    intent.putExtra("eventTitle", eventTitle);
                                    intent.putExtra("eventDesc", eventDesc);
                                    intent.putExtra("eventCate", eventCate);
                                    intent.putExtra("address1", event_lcation_address1.getText().toString());
                                    intent.putExtra("address2", event_lcation_address2.getText().toString());
                                    intent.putExtra("postcode", event_postcode.getText().toString());
                                    intent.putExtra("city", event_city.getText().toString());
                                    intent.putExtra("SelectedDate", mDate.getText().toString());
                                    intent.putExtra("To_date", event_details_date_etto.getText().toString());
                                    intent.putExtra("FromTime", eventTime_from.getText().toString());
                                    intent.putExtra("ToTime", eventTime_to.getText().toString());
                                    intent.putExtra("attendeesNo", event_attendees_no.getText().toString());
                                    intent.putExtra("value", value);
                                    intent.putExtra("editevent", edit);
                                    intent.putExtra("link1", link1);
                                    intent.putExtra("link2", link2);
                                    intent.putExtra("link3", link3);
                                    intent.putExtra("fromtimeinmilles", frommilles);
                                    intent.putExtra("totimeinmilles", tomilles);
                                    intent.putExtra("type", type);
                                    intent.putExtra("ticketprice", values);
                                    intent.putExtra("numbertickets", numbertickets);
                                    to_date = event_details_date_etto.getText().toString();
                                    intent.putExtra("to_Date", to_date);
                                    startActivity(intent);

                                } else if (response.code() == 404) {
                                    event_postcode.setError("Invalid Postcode");
                                    event_postcode.setText("");
                                }
                            } else if (response.code() == 404) {
                                event_postcode.setError("Invalid Postcode");
                                event_postcode.setText("");
                            }
                        }

                        @Override
                        public void onFailure(Call<PostCode> call, Throwable t) {
                        progressDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(EventDetailsActivity.this, "Select atleast one option from gender", Toast.LENGTH_SHORT).show();
                }

            }
        });


        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                year1 = year;
                month1 = month;
                date1 = dayOfMonth;
                year2 = year1;
                month2 = month1;
                day2 = date1;
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                newChangeForDate = calendar.getTimeInMillis();
                myFormat = "dd/MM/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.UK);

                mDate.setText(simpleDateFormat.format(calendar.getTime()));
                eventTime_from.setText("");
                eventTime_to.setText("");

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = formatter.parse(formatter.format(calendar.getTime()));
                    Log.d("StartDate", String.valueOf(date.getTime()));
                    startdatemilli = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        };


        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year1);
                calendar.set(Calendar.MONTH, month1);
                calendar.set(Calendar.DAY_OF_MONTH, date1);
                //calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                if (calendar3 == null) {
                    calendar3 = Calendar.getInstance();
                }
                calendar3.set(Calendar.YEAR, year);
                calendar3.set(Calendar.MONTH, month);
                calendar3.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //calendar3.setTimeZone(TimeZone.getTimeZone("GMT"));
                myFormat = "dd/MM/yyyy";


                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = formatter.parse(formatter.format(calendar3.getTime()));
                    Log.d("StartDate", String.valueOf(date.getTime()));
                    year2 = year;
                    month2 = month;
                    day2 = dayOfMonth;
                    enddatemilli = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("DATETEST", startdatemilli + "   " + enddatemilli);


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.UK);

                if (startdatemilli <= enddatemilli) {
                    event_details_date_etto.setText(simpleDateFormat.format(calendar3.getTime()));
                } else {
                    eventTime_to.setText("");
                    event_details_date_etto.setText("");
                    Toast.makeText(getApplicationContext(), "Invalid Date", Toast.LENGTH_SHORT).show();
                }

            }
        };


        eventTime_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calendar = Calendar.getInstance();

                Calendar normal = Calendar.getInstance();
                //normal.setTimeZone(TimeZone.getTimeZone("GMT"));
                final int hour = normal.get(Calendar.HOUR_OF_DAY);
                int minute = normal.get(Calendar.MINUTE);

//                if (calendar!=null) {
//                    //calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
//                    calendar.set(Calendar.HOUR_OF_DAY, hour);
//                    calendar.set(Calendar.MINUTE, minute);
//                }

                if (TextUtils.isEmpty(mDate.getText().toString())) {
                    Toast.makeText(EventDetailsActivity.this, "Enter Start date", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(event_details_date_etto.getText().toString())) {
                    Toast.makeText(EventDetailsActivity.this, "Enter End date", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mDate.getText().toString()) && TextUtils.isEmpty(event_details_date_etto.getText().toString())) {
                    Toast.makeText(EventDetailsActivity.this, "Enter Event date", Toast.LENGTH_SHORT).show();
                } else {
                    final TimePickerDialog timePickerDialog = new TimePickerDialog(EventDetailsActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);

                            Calendar startCalenderTime = Calendar.getInstance();
                            //startCalenderTime.setTimeZone(TimeZone.getTimeZone("GMT"));

                            if (date1 != 0) {
//                               //if (date1 != calendar.get(Calendar.DAY_OF_MONTH)) {
                                if (calendar.getTimeInMillis() >= startCalenderTime.getTimeInMillis() - 30000) {
                                    //if (newChangeForDate != calendar.getTimeInMillis() ||newChangeForDate != calendar.getTimeInMillis()) {
                                    if (hourOfDay < 10 && minute < 10) {
                                        eventTime_from.setText("0" + hourOfDay + ":" + "0" + minute);
                                    } else if (hourOfDay < 10) {
                                        eventTime_from.setText("0" + hourOfDay + ":" + minute);
                                    } else if (minute < 10) {
                                        eventTime_from.setText(hourOfDay + ":" + "0" + minute);
                                    } else {
                                        eventTime_from.setText(hourOfDay + ":" + minute);
                                    }

                                    String myTime = eventTime_from.getText().toString();
                                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                                    Date d = null;
                                    try {
                                        d = df.parse(myTime);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    //=========================================================================================

                                    if (mDate.getText().toString().equals(event_details_date_etto.getText().toString())) {
                                        Calendar cal = Calendar.getInstance();
                                        //cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        cal.setTime(d);

                                        if (!eventTime_from.getText().toString().contains("23:")) {
                                            cal.add(Calendar.MINUTE, 60);
                                            String newTime = df.format(cal.getTime());
                                            eventTime_to.setText(newTime);
                                            hour1 = hourOfDay;
                                            minute1 = minute;
                                            hour2 = hourOfDay;
                                            minute2 = minute;

                                            to_time_milles = eventTime_to.getText().toString();
                                        } else {
                                            cal.add(Calendar.MINUTE, 0);
                                            String newTime = df.format(cal.getTime());
                                            eventTime_to.setText("23:59");
                                            hour1 = hourOfDay;
                                            minute1 = minute;
                                            hour2 = hourOfDay;
                                            minute2 = minute;

                                            to_time_milles = eventTime_to.getText().toString();
                                        }

                                    } else {
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(d);
                                        cal.add(Calendar.MINUTE, 60);
                                        String newTime = df.format(cal.getTime());
                                        eventTime_to.setText(newTime);
                                        hour1 = hourOfDay;
                                        minute1 = minute;
                                        hour2 = hourOfDay;
                                        minute2 = minute;

                                        to_time_milles = eventTime_to.getText().toString();
                                    }

                                    //=============================================================================


                                /*    Calendar cal = Calendar.getInstance();
                                    //cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    cal.setTime(d);
                                    cal.add(Calendar.MINUTE, 60);
                                    String newTime = df.format(cal.getTime());
                                    eventTime_to.setText(newTime);
                                    hour1 = hourOfDay;
                                    minute1 = minute;
                                    hour2 = hourOfDay;
                                    minute2 = minute;

                                    to_time_milles = eventTime_to.getText().toString();*/
                                }
                                else {
                                    // calendar.set(Calendar.HOUR,hourOfDay);
                                    // calendar.set(Calendar.MINUTE,minute);
                                    /*Calendar datetime = Calendar.getInstance();
                                    Calendar c = Calendar.getInstance();
                                    //c.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    datetime.set(Calendar.MINUTE, minute);
                                    //if (datetime.getTimeInMillis() + 1 >= c.getTimeInMillis()) {
                                    if (calendar.getTimeInMillis() >= c.getTimeInMillis()) {
                                        //it's after current
                                        if (hourOfDay < 10 && minute < 10) {
                                            eventTime_from.setText("0" + hourOfDay + ":" + "0" + minute);
                                        } else if (hourOfDay < 10) {
                                            eventTime_from.setText("0" + hourOfDay + ":" + minute);
                                        } else if (minute < 10) {
                                            eventTime_from.setText(hourOfDay + ":" + "0" + minute);
                                        } else {
                                            eventTime_from.setText(hourOfDay + ":" + minute);
                                        }
//                                    eventTime_from.setText(hourOfDay + ":" + minute);
                                        String myTime = eventTime_from.getText().toString();
                                        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                                        Date d = null;
                                        try {
                                            d = df.parse(myTime);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if (mDate.getText().toString().equals(event_details_date_etto.getText().toString())) {
                                            Calendar cal = Calendar.getInstance();
                                            //cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                                            cal.setTime(d);

                                            if (!eventTime_from.getText().toString().contains("23:")) {
                                                cal.add(Calendar.MINUTE, 60);
                                                String newTime = df.format(cal.getTime());
                                                eventTime_to.setText(newTime);
                                                hour1 = hourOfDay;
                                                minute1 = minute;
                                                hour2 = hourOfDay;
                                                minute2 = minute;

                                                to_time_milles = eventTime_to.getText().toString();
                                            } else {
                                                cal.add(Calendar.MINUTE, 0);
                                                String newTime = df.format(cal.getTime());
                                                eventTime_to.setText("23:59");
                                                hour1 = hourOfDay;
                                                minute1 = minute;
                                                hour2 = hourOfDay;
                                                minute2 = minute;

                                                to_time_milles = eventTime_to.getText().toString();
                                            }

                                        } else {
                                            Calendar cal = Calendar.getInstance();
                                            cal.setTime(d);
                                            cal.add(Calendar.MINUTE, 60);
                                            String newTime = df.format(cal.getTime());
                                            eventTime_to.setText(newTime);
                                            hour1 = hourOfDay;
                                            minute1 = minute;
                                            hour2 = hourOfDay;
                                            minute2 = minute;

                                            to_time_milles = eventTime_to.getText().toString();
                                        }
                                    } else {*/
                                        //it's before current'
                                        eventTime_to.setText("");
                                        eventTime_from.setText("");
                                        Toast.makeText(getApplicationContext(), "Invalid Time", Toast.LENGTH_SHORT).show();
//                                    }
                                }
                            } else {
                                if (hourOfDay < 10 && minute < 10) {
                                    eventTime_from.setText("0" + hourOfDay + ":" + "0" + minute);
                                } else if (hourOfDay < 10) {
                                    eventTime_from.setText("0" + hourOfDay + ":" + minute);
                                } else if (minute < 10) {
                                    eventTime_from.setText(hourOfDay + ":" + "0" + minute);
                                } else {
                                    eventTime_from.setText(hourOfDay + ":" + minute);
                                }

                                String myTime = eventTime_from.getText().toString();
                                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                                Date d = null;
                                try {
                                    d = df.parse(myTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(d);
                                cal.add(Calendar.MINUTE, 60);
                                String newTime = df.format(cal.getTime());
                                eventTime_to.setText(newTime);
                                hour1 = hourOfDay;
                                minute1 = minute;
                                hour2 = hourOfDay;
                                minute2 = minute;

                                to_time_milles = eventTime_to.getText().toString();
                            }

                        }
                    }, hour, minute, true);
                    timePickerDialog.show();

                    //calendar.add(Calendar.HOUR, 1);
                }

            }
        });


        eventTime_to.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(final View v) {
                if (TextUtils.isEmpty(mDate.getText().toString())) {
                    Toast.makeText(EventDetailsActivity.this, "Enter Start date", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(event_details_date_etto.getText().toString())) {
                    Toast.makeText(EventDetailsActivity.this, "Enter End dat", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(eventTime_from.getText().toString())) {
                    Toast.makeText(EventDetailsActivity.this, "Enter Start time", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(mDate.getText().toString()) && TextUtils.isEmpty(eventTime_from.getText().toString()) && TextUtils.isEmpty(event_details_date_etto.getText().toString())) {

                    Toast.makeText(EventDetailsActivity.this, "Enter Event date", Toast.LENGTH_SHORT).show();
                } else {
                    //calendar = Calendar.getInstance();
                    //calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Calendar normal = Calendar.getInstance();
                    //normal.setTimeZone(TimeZone.getTimeZone("GMT"));
                    int hour = normal.get(Calendar.HOUR_OF_DAY);
                    int minute = normal.get(Calendar.MINUTE);

                    final TimePickerDialog timePickerDialog = new TimePickerDialog(EventDetailsActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.set(Calendar.MINUTE, minute1);
                            calendar1.set(Calendar.HOUR_OF_DAY, hour1);
                            calendar1.set(Calendar.MONTH, month1);
                            calendar1.set(Calendar.DAY_OF_MONTH, date1);
                            calendar1.set(Calendar.YEAR, year1);
                            //calendar1.setTimeZone(TimeZone.getTimeZone("GMT"));

                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.set(Calendar.MINUTE, minute);
                            calendar2.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar2.set(Calendar.MONTH, month2);
                            calendar2.set(Calendar.DAY_OF_MONTH, day2);
                            calendar2.set(Calendar.YEAR, year2);
                            //calendar2.setTimeZone(TimeZone.getTimeZone("GMT"));

                            Log.d("+++++++++", "++ calender1 ++" + date1 + month1 + year1 + hour1 + minute1);
                            Log.d("+++++++++", "++ calender1 ++" + day2 + month2 + year2 + hourOfDay + minute);

                            if (calendar1.getTimeInMillis() > calendar2.getTimeInMillis()) {
                                Toast.makeText(EventDetailsActivity.this, "Invalid selection", Toast.LENGTH_SHORT).show();
                                eventTime_to.setText("");
                                return;
                            }

                            if (mDate.getText().toString().equals(event_details_date_etto.getText().toString())) {
                                if (date1 != 0) {
                                    if (date1 != calendar.get(Calendar.DAY_OF_MONTH)) {
                                        if (hourOfDay < 10 && minute < 10) {
                                            eventTime_to.setText("0" + hourOfDay + ":" + "0" + minute);
                                        } else if (hourOfDay < 10) {
                                            eventTime_to.setText("0" + hourOfDay + ":" + minute);
                                        } else if (minute < 10) {
                                            eventTime_to.setText(hourOfDay + ":" + "0" + minute);
                                        } else {
                                            eventTime_to.setText(hourOfDay + ":" + minute);

                                        }
                                        to_time_milles = eventTime_to.getText().toString();
                                    } else {
                                        Calendar datetime = Calendar.getInstance();
                                        Calendar c = Calendar.getInstance();
                                        //c.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        datetime.set(Calendar.MINUTE, minute);
                                        //if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                                        if (calendar3 == null) {
                                            calendar3 = Calendar.getInstance();
                                            calendar3.set(Calendar.YEAR,year2);

                                        }
                                        if (calendar1.getTimeInMillis() < calendar2.getTimeInMillis()) {
                                            //it's after current
                                            int hour = hourOfDay % 24;
                                            if (hourOfDay < 10 && minute < 10) {
                                                eventTime_to.setText("0" + hourOfDay + ":" + "0" + minute);
                                            } else if (hourOfDay < 10) {
                                                eventTime_to.setText("0" + hourOfDay + ":" + minute);
                                            } else if (minute < 10) {
                                                eventTime_to.setText(hourOfDay + ":" + "0" + minute);
                                            } else {
                                                eventTime_to.setText(hourOfDay + ":" + minute);

                                            }
                                            to_time_milles = eventTime_to.getText().toString();
                                        } else {
                                            //it's before current'
                                            eventTime_to.setText("");
                                            Toast.makeText(EventDetailsActivity.this, "Invalid Time", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    if (hourOfDay < 10 && minute < 10) {
                                        eventTime_to.setText("0" + hourOfDay + ":" + "0" + minute);
                                    } else if (hourOfDay < 10) {
                                        eventTime_to.setText("0" + hourOfDay + ":" + minute);
                                    } else if (minute < 10) {
                                        eventTime_to.setText(hourOfDay + ":" + "0" + minute);
                                    } else {
                                        eventTime_to.setText(hourOfDay + ":" + minute);

                                    }
                                    to_time_milles = eventTime_to.getText().toString();

                                }
                            } else {
                                if (hourOfDay < 10 && minute < 10) {
                                    eventTime_to.setText("0" + hourOfDay + ":" + "0" + minute);
                                } else if (hourOfDay < 10) {
                                    eventTime_to.setText("0" + hourOfDay + ":" + minute);
                                } else if (minute < 10) {
                                    eventTime_to.setText(hourOfDay + ":" + "0" + minute);
                                } else {
                                    eventTime_to.setText(hourOfDay + ":" + minute);

                                }
                                to_time_milles = eventTime_to.getText().toString();
                            }

                        }
                    }, hour, minute, true);
                    timePickerDialog.show();
                }

            }
        });

        freeevent_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (freeevent_checkbox.isChecked()) {
                    addTicket.setVisibility(View.INVISIBLE);
                    type = "";
                    values = "";
                    numbertickets = "";
                } else {
                    addTicket.setVisibility(View.VISIBLE);

                }

            }
        });

        addTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ticketprice.clear();
                ticketnumber.clear();
                tickettyp.clear();
                if (!BToken.equalsIgnoreCase("")) {
                    if (!TextUtils.isEmpty(event_attendees_no.getText().toString())) {
                        tikettypedialog();
                    } else {
                        event_attendees_no.setError("Enter attendees");
                    }

                } else {
                    dialog();
                    price_et.setText(prceET);
                    tickettype_et.setText(tickettype);
                    numbersTicketET.setText(event_attendees_no.getText().toString());

                    type = "";
                    values = "";
                    numbertickets = "";
                }

            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Event_Activity.image_uris.clear();
                Intent intent = new Intent(EventDetailsActivity.this, Home_Screen.class);
                startActivity(intent);
                finish();
            }
        });

//        event_lcation_address1

    }



    /*###################Code to Close the keyboard when your touch anywhere############*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int[] scrcoords = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    public void tikettypedialog() {
        dialog1 = new Dialog(this);
        dialog1.setContentView(R.layout.tickettypedialog);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.show();
        arryList = new ArrayList<>();
        ImageButton close_ticket ;
        close_ticket = dialog1.findViewById(R.id.close_ticket);
        edt_tiketType = dialog1.findViewById(R.id.edt_tiketType);
        edt_price = dialog1.findViewById(R.id.edt_price);
        edt_numbersOfTicket = dialog1.findViewById(R.id.edt_numbersOfTicket);
        type1 = dialog1.findViewById(R.id.type1);
        type2 = dialog1.findViewById(R.id.type2);
        type3 = dialog1.findViewById(R.id.type3);
        btn_done = dialog1.findViewById(R.id.btn_done);
        ticketET = event_attendees_no.getText().toString();
        totalticketsnumber = Integer.parseInt(event_attendees_no.getText().toString());
        totalTicket = Integer.parseInt(ticketET);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ToDo Validation
                validationOnDialogfrom();

            }
        });

        close_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                addTicket.setText("+ Add ticket type");
            }
        });


    }

    private void validationOnDialogfrom() {
        if (edt_tiketType.getText().toString().trim().isEmpty() && edt_price.getText().toString().trim().isEmpty() && edt_numbersOfTicket.getText().toString().trim().isEmpty()) {
            edt_tiketType.setError("Enter ticket type");
            edt_price.setError("Enter ticket price");
            edt_numbersOfTicket.setError("Enter number of ticket");
        } else if (edt_tiketType.getText().toString().trim().isEmpty()) {
            edt_tiketType.setError("Enter ticket type");
        } else if (edt_price.getText().toString().trim().isEmpty()) {
            edt_price.setError("Enter ticket price");
        } else if (edt_numbersOfTicket.getText().toString().trim().isEmpty()) {
            edt_numbersOfTicket.setError("Enter number of ticket");
        } else {
            int numofTicker1 = Integer.parseInt(edt_numbersOfTicket.getText().toString());
            if (Float.valueOf(edt_price.getText().toString())<1)
            {
                edt_price.setError("Invalid Price");
            }else {
                if (totalTicket >= numofTicker1) {
                    String ticket_Type = edt_tiketType.getText().toString();
                    String ticket_Price = edt_price.getText().toString();
                    String number_of_Type = edt_numbersOfTicket.getText().toString();
                    ticketTypeModel = new TicketTypeModel(ticket_Type, Float.valueOf(ticket_Price), number_of_Type);

                    arryList.add(ticketTypeModel);

                    for (int i = 0; i < arryList.size(); i++) {
                        Log.d("type", arryList.get(i) + "  " + arryList.size());
                        tickettyp.add(arryList.get(i).getTikcettype());
                        ticketprice.add(String.valueOf(arryList.get(i).getPrice()));
                        ticketnumber.add(String.valueOf(arryList.get(i).getNumberofticket()));

                        if (arryList.size() == 1) {
                            type1.setVisibility(View.VISIBLE);
                            type1.setText(arryList.get(0).getTikcettype() + " £ " + arryList.get(0).getPrice() + " (" + arryList.get(0).getNumberofticket() + ") ");

                            type = arryList.get(0).getTikcettype();
                            values = String.valueOf(arryList.get(0).getPrice());
                            numbertickets = String.valueOf(arryList.get(0).getNumberofticket());

                            addTicket.setText("edit or delete ticket type");
                            edt_tiketType.setText("");
                            edt_price.setText("");
                            edt_numbersOfTicket.setText("");

                        } else if (arryList.size() == 2) {
                            type1.setVisibility(View.VISIBLE);
                            type1.setText(arryList.get(0).getTikcettype() + " £ " + arryList.get(0).getPrice() + " (" + arryList.get(0).getNumberofticket() + ") ");

                            type2.setVisibility(View.VISIBLE);
                            type2.setText(arryList.get(1).getTikcettype() + " £ " + arryList.get(1).getPrice() + " (" + arryList.get(1).getNumberofticket() + ") ");

                            type = arryList.get(0).getTikcettype() + "," + arryList.get(1).getTikcettype();
                            values = arryList.get(0).getPrice() + "," + arryList.get(1).getPrice();
                            numbertickets = arryList.get(0).getNumberofticket() + "," + arryList.get(1).getNumberofticket();

                            edt_tiketType.setText("");
                            edt_price.setText("");
                            edt_numbersOfTicket.setText("");
                            addTicket.setText("edit or delete ticket type");
                        } else {
                            if (totalTicket > numofTicker1) {
//                                pendingTicker = totalTicket + numofTicker1;
                                totalTicket = pendingTicker;

                                if (totalTicket == 0) {
                                    dialog1.dismiss();
                                }
                                edt_numbersOfTicket.setError(totalTicket + " remaining tickets");
                            } else if (totalTicket < numofTicker1) {
//                                pendingTicker = totalTicket + numofTicker1;
                                totalTicket = pendingTicker;

                                if (totalTicket == 0) {
                                    dialog1.dismiss();
                                }
                                edt_numbersOfTicket.setError("limit exceed. " + totalTicket + " are remaining tickets.");
                            } else {


                                if (arryList.size() == 3) {
                                    type1.setVisibility(View.VISIBLE);
                                    type1.setText(arryList.get(0).getTikcettype() + " £ " + arryList.get(0).getPrice() + " (" + arryList.get(0).getNumberofticket() + ") ");

                                    type2.setVisibility(View.VISIBLE);
                                    type2.setText(arryList.get(1).getTikcettype() + " £ " + arryList.get(1).getPrice() + " (" + arryList.get(1).getNumberofticket() + ") ");

                                    type3.setVisibility(View.VISIBLE);
                                    type3.setText(arryList.get(2).getTikcettype() + " £ " + arryList.get(2).getPrice() + " (" + arryList.get(2).getNumberofticket() + ") ");


                                    edt_tiketType.setText("");
                                    edt_price.setText("");
                                    edt_numbersOfTicket.setText("");
                                    getPrceET = "";
                                    getTickettype = "";

                                    Log.d("type1", arryList.get(0).getTikcettype() + " " + arryList.get(1).getTikcettype() + " " + arryList.get(2).getTikcettype());
                                    type = arryList.get(0).getTikcettype() + "," + arryList.get(1).getTikcettype() + "," + arryList.get(2).getTikcettype();
                                    values = arryList.get(0).getPrice() + "," + arryList.get(1).getPrice() + "," + arryList.get(2).getPrice();
                                    numbertickets = arryList.get(0).getNumberofticket() + "," + arryList.get(1).getNumberofticket() + "," + arryList.get(2).getNumberofticket();
                                    addTicket.setText("edit or delete ticket type");
                                    dialog1.dismiss();
                                }
                            }
                        }

                    }
                    if (edt_numbersOfTicket.getError()==null)
                    {
                        pendingTicker = totalTicket - numofTicker1;
                        totalTicket = pendingTicker;
                    }

                    if (totalTicket == 0) {
                        dialog1.dismiss();
                    }


                } else if (totalTicket < numofTicker1) {
                    edt_numbersOfTicket.setError("limit exceed. " + totalTicket + " are remaining tickets.");
                } else {
                    dialog1.dismiss();
                }
            }

        }
    }

    public void dialog() {
        final Dialog dialog = new Dialog(this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setCancelable(false);
        dialog.setContentView(R.layout.ticket_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tickettext = dialog.findViewById(R.id.tickettext);
        numbersTicketET = dialog.findViewById(R.id.numbers_of_tickets_et);
        tickettype_et = dialog.findViewById(R.id.tickettypename_et);
        price_et = dialog.findViewById(R.id.price_et_ticketdialog);
        okayDialog = dialog.findViewById(R.id.okaydialog);
        tickettext.setVisibility(View.GONE);
        tickettype_et.setVisibility(View.GONE);
        dialog.show();


        okayDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ticketET = numbersTicketET.getText().toString();
                prceET = price_et.getText().toString();
                tickettype = "Standard";
                getPrceET = prceET;
                getTicketET = ticketET;
                getTickettype = tickettype;


                    if (!TextUtils.isEmpty(prceET) && !TextUtils.isEmpty(ticketET) && !TextUtils.isEmpty(tickettype)) {
                        if (Float.valueOf(prceET)<1)
                        {
                            price_et.setError("Invalid Price");
                        }else {
                            dialog.cancel();
                            Log.d("type", ticketET + "" + prceET + "" + tickettype);
                            addTicket.setText("edit or delete ticket type");
                        }


                    } else {
                        numbersTicketET.setError("Enter numbers of tickets");
                        price_et.setError("Enter ticket price");
                        tickettype_et.setError("Enter ticket type");
                    }


            }
        });
    }

    public void editEVnet(String userToken, String eventId) {

        Call<RetroGetEventData> call = WebAPI.getInstance().getApi().getEvents(userToken, "application/json", eventId);
        call.enqueue(new Callback<RetroGetEventData>() {
            @Override
            public void onResponse(Call<RetroGetEventData> call, Response<RetroGetEventData> response) {

                if (response.isSuccessful()) {

                    event_lcation_address1.setText(response.body().getData().get(0).getAddressline1());
                    event_lcation_address2.setText(response.body().getData().get(0).getAddressline2());
                    event_postcode.setText(response.body().getData().get(0).getPostcode());
                    event_city.setText(response.body().getData().get(0).getCity());


                    Calendar c = Calendar.getInstance();

                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                    mDate.setText(df.format(c.getTime()));


                    eventTime_from.setText(response.body().getData().get(0).getTimeFrom());
                    eventTime_to.setText(response.body().getData().get(0).getTimeTo());
                    event_attendees_no.setText(response.body().getData().get(0).getAttendeesNo());
                    tickettype = response.body().getData().get(0).getTicketType();
                    prceET = response.body().getData().get(0).getPrice();
                    to_date = Util.convertTimeStampDate(Long.parseLong(response.body().getData().get(0).getEventEndDt()));


//                    if (edit.equalsIgnoreCase("republish")) {
                    Calendar c1 = Calendar.getInstance();

                    SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

                    event_details_date_etto.setText(df1.format(c1.getTime()));
//                    } else {
//                        event_details_date_etto.setText(to_date);
//                    }

                    getPrceET = prceET;
                    getTicketET = ticketET;
                    getTickettype = tickettype;

                    to_time_milles = eventTime_to.getText().toString();
                    String gender = response.body().getData().get(0).getAttendeesGender();
                    if (gender.equals("All")) {
                        event_radiobutton_all.setChecked(true);
                    } else if (gender.equals("Female")) {
                        event_radiobutton_female.setChecked(true);
                    } else if (gender.equals("Male")) {
                        event_radiobutton_male.setChecked(true);
                    }

                    if (getPrceET != null) {
                        if (getPrceET.equals("0")) {
                            freeevent_checkbox.setChecked(true);
                        }

                    }

                    if (response.body().getData().get(0).getFreeEvent().equals("1") && response.body().getData().get(0).getTicketsType().size() > 0) {
                        addTicket.setText("+Add ticket type");
                    } else if (response.body().getData().get(0).getFreeEvent().equals("1")) {
                        addTicket.setText("edit or delete ticket type");
                    }
                } else {
                    Intent intent = new Intent(EventDetailsActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RetroGetEventData> call, Throwable t) {

                Toast.makeText(EventDetailsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                            Geocoder geocoder = new Geocoder(EventDetailsActivity.this);
                            try {
                                List<Address> addresses = geocoder.getFromLocationName(loc, 10);
                                List<LatLng> latLngs = new ArrayList<LatLng>(addresses.size());

                                Log.d("latLngs", String.valueOf(latLngs));


                                for (Address a : addresses) {
                                    if (a.hasLatitude() && a.hasLongitude()) {
                                        latLngs.add(new LatLng(a.getLatitude(), a.getLongitude()));

                                        String lat1 = String.valueOf(a.getLatitude());
                                        String lng1 = String.valueOf(a.getLongitude());

                                        getCityName(lat1, lng1);

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

    private void getCityName(String lat, String lng) {
        Geocoder geocoder = new Geocoder(EventDetailsActivity.this, Locale.getDefault());
        List<Address> address = null;
        try {
            address = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
            if (address != null) {

                if (address.get(0).getPostalCode() != null) {
                    event_postcode.setText(address.get(0).getPostalCode());
                    event_postcode.setError(null);
                } else {
                    event_postcode.setText("");
                }


            } else {
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AdapterView.OnItemClickListener autocompleteClickListener1 = new AdapterView.OnItemClickListener() {
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

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ADDRESS);

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
                            Geocoder geocoder = new Geocoder(EventDetailsActivity.this);
                            try {
                                List<Address> addresses = geocoder.getFromLocationName(loc, 10);
                                List<LatLng> latLngs = new ArrayList<LatLng>(addresses.size());

                                Log.d("latLngs", String.valueOf(latLngs));


                                for (Address a : addresses) {
                                    if (a.hasLatitude() && a.hasLongitude()) {
                                        latLngs.add(new LatLng(a.getLatitude(), a.getLongitude()));

                                        String lat1 = String.valueOf(a.getLatitude());
                                        String lng1 = String.valueOf(a.getLongitude());

//                                        getCityName(lat1,lng1);
//                                        latt = lat1;
//                                        lnng = lng1;


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
