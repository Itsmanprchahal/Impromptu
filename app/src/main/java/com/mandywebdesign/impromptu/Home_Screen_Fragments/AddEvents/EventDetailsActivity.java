package com.mandywebdesign.impromptu.Home_Screen_Fragments.AddEvents;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Models.RetroPostcode;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroGetEventData;
import com.mandywebdesign.impromptu.Utils.Util;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    Calendar calendar;
    EditText event_lcation_address1, event_lcation_address2, event_postcode, event_city, event_attendees_no, mDate, event_details_date_etto, eventTime_from, eventTime_to;
    RadioButton event_radiobutton_all, event_radiobutton_female, event_radiobutton_male;
    TextView addTicket;
    CheckBox freeevent_checkbox;
    Button okayDialog;
    ProgressDialog progressDialog;
    ImageView close;
    String value_checkout, value_checkIn;
    String prceET, ticketET, tickettype, myFormat, to_date;
    String getPrceET, getTicketET, getTickettype;
    EditText price_et, tickettype_et;
    TextView numbersTicketET;
    DatePickerDialog.OnDateSetListener dateSetListener, dateSetListener1;
    String eventTitle, eventDesc, eventCate, edit, value, userToken, BToken, S_Token, getGender, link1, link2, link3;
    public static String to_time_milles;
    String frommilles;
    String tomilles;
    int year1;
    int month1;
    int date1;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        progressDialog = ProgressBarClass.showProgressDialog(EventDetailsActivity.this, "Please wait...");
        progressDialog.dismiss();

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = "Bearer " + sharedPreferences.getString("Usertoken", "");
        getGender = sharedPreferences.getString("profilegender", "");
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");

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
            } else if (!S_Token.equalsIgnoreCase("")) {
                editEVnet("Bearer " + S_Token, value);
            }
        }


        listeners();

    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.event_progress_bar);
        perviewButoon = (Button) findViewById(R.id.event_preview);
        toolbar = (Toolbar) findViewById(R.id.event_toolbar);
        mDate = (EditText) findViewById(R.id.event_details_date_et);
        event_details_date_etto = findViewById(R.id.event_details_date_etto);
        eventTime_from = (EditText) findViewById(R.id.event_from);
        eventTime_to = (EditText) findViewById(R.id.event_to);
        addTicket = (TextView) findViewById(R.id.event_tickettype);
        event_lcation_address1 = (EditText) findViewById(R.id.event_lcation_address1);
        event_lcation_address2 = (EditText) findViewById(R.id.event_lcation_address2);
        event_postcode = (EditText) findViewById(R.id.event_postcode);
        event_city = (EditText) findViewById(R.id.event_city);
        event_radiobutton_all = (RadioButton) findViewById(R.id.event_radiobutton_all);
        event_radiobutton_female = (RadioButton) findViewById(R.id.event_radiobutton_female);
        event_radiobutton_male = (RadioButton) findViewById(R.id.event_radiobutton_male);
        event_attendees_no = (EditText) findViewById(R.id.event_attendees_no);
        freeevent_checkbox = (CheckBox) findViewById(R.id.freeevent_checkbox);
        close = (ImageView) findViewById(R.id.event_close);
        setSupportActionBar(toolbar);

        event_postcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Call<RetroPostcode> call = WebAPI.getInstance().getApi().checkpostcode(event_postcode.getText().toString());
                call.enqueue(new Callback<RetroPostcode>() {
                    @Override
                    public void onResponse(Call<RetroPostcode> call, Response<RetroPostcode> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("200")) {

                                event_postcode.setError(null);
                            } else if (response.body().getStatus().equals("400")) {
                                event_postcode.setError("Invalid Postcode");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RetroPostcode> call, Throwable t) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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


        perviewButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EventDetailsActivity.this, PerviewEventActivity.class);
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

                    String givenDateString = mDate.getText().toString() + " " + eventTime_from.getText().toString();
                    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    Date date = null;
                    try {
                        date = (Date) formatter.parse(givenDateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    frommilles = String.valueOf(date.getTime());


                    String givenDateString1 = event_details_date_etto.getText().toString() + " " + eventTime_to.getText().toString();
                    DateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    Date date1 = null;
                    try {
                        date1 = (Date) formatter1.parse(givenDateString1);
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
                    intent.putExtra("editevent", "edit");
                    intent.putExtra("link1", link1);
                    intent.putExtra("link2", link2);
                    intent.putExtra("link3", link3);
                    intent.putExtra("fromtimeinmilles", frommilles);
                    intent.putExtra("totimeinmilles", tomilles);
                    intent.putExtra("to_Date", to_date);
                    startActivity(intent);
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
              calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myFormat = "MM/dd/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);

                mDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };


        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year1);
                calendar.set(Calendar.MONTH, month1);
                calendar.set(Calendar.DAY_OF_MONTH, date1);
                myFormat = "MM/dd/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);

                event_details_date_etto.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };

        eventTime_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();

                final int hour = calendar.get(Calendar.AM_PM);
                int minute = calendar.get(Calendar.MINUTE);

                final TimePickerDialog timePickerDialog = new TimePickerDialog(EventDetailsActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        if (hourOfDay < 10 && minute < 10) {
                            eventTime_from.setText("0" + hourOfDay + ":" + "0" + minute);
                        } else if (hourOfDay < 10) {
                            eventTime_from.setText("0" + hourOfDay + ":" + minute);
                        } else if (minute < 10) {
                            eventTime_from.setText(hourOfDay + ":" + "0" + minute);
                        } else {
                            eventTime_from.setText(hourOfDay + ":" + minute);
                        }
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        eventTime_to.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(final View v) {
                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.AM_PM);
                int minute = calendar.get(Calendar.MINUTE);

                final TimePickerDialog timePickerDialog = new TimePickerDialog(EventDetailsActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        //convert time to milliseconds
                        final Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);

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

                        Log.d("time", "" + to_time_milles);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        freeevent_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (freeevent_checkbox.isChecked()) {
                    addTicket.setVisibility(View.INVISIBLE);
                } else {
                    addTicket.setVisibility(View.VISIBLE);
                }
            }
        });

        addTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
                price_et.setText(prceET);
                tickettype_et.setText(tickettype);
                numbersTicketET.setText(event_attendees_no.getText().toString());
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

    }



    /*###################Code to Close the keyboard when your touch anywhere############*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
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

    public void dialog() {
        final Dialog dialog = new Dialog(this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setCancelable(false);
        dialog.setContentView(R.layout.ticket_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        numbersTicketET = (TextView) dialog.findViewById(R.id.numbers_of_tickets_et);
        price_et = (EditText) dialog.findViewById(R.id.price_et_ticketdialog);
        okayDialog = (Button) dialog.findViewById(R.id.okaydialog);
        tickettype_et = (EditText) dialog.findViewById(R.id.tickettypename_et);
        dialog.show();


        okayDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ticketET = numbersTicketET.getText().toString();
                prceET = price_et.getText().toString();
                tickettype = tickettype_et.getText().toString();
                getPrceET = prceET;
                getTicketET = ticketET;
                getTickettype = tickettype;

                if (!TextUtils.isEmpty(prceET) && !TextUtils.isEmpty(ticketET) && !TextUtils.isEmpty(tickettype)) {
                    dialog.cancel();
                    Log.d("type", ticketET + "" + prceET + "" + tickettype);
                    addTicket.setText("edit or delete ticket type");

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
                    mDate.setText(response.body().getData().get(0).getDate());
                    eventTime_from.setText(response.body().getData().get(0).getTimeFrom());
                    eventTime_to.setText(response.body().getData().get(0).getTimeTo());
                    event_attendees_no.setText(response.body().getData().get(0).getAttendeesNo());
                    tickettype = response.body().getData().get(0).getTicketType();
                    prceET = response.body().getData().get(0).getPrice();
                    to_date = Util.convertTimeStampDate(Long.parseLong(response.body().getData().get(0).getEventStartDt()));
                    event_details_date_etto.setText(to_date);
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
                    if (getPrceET.equals("0")) {
                        freeevent_checkbox.setChecked(true);
                    }
                    if (response.body().getData().get(0).getFreeEvent().equals("1")) {
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
}