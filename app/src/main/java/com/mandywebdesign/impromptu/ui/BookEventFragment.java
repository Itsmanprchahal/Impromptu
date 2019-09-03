package com.mandywebdesign.impromptu.ui;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Adapters.B_EventDetailImageAdapter;
import com.mandywebdesign.impromptu.Adapters.Booked_users;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AddEvents.Add_Event_Activity;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.See_all;
import com.mandywebdesign.impromptu.Retrofit.BookFreeEvents;
import com.mandywebdesign.impromptu.Retrofit.Rating;
import com.mandywebdesign.impromptu.Retrofit.RemainingTickets;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.Normal_user_profile;
import com.mandywebdesign.impromptu.Utils.Util;
import com.mandywebdesign.impromptu.messages.ChatBox;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.Booked_User;
import com.mandywebdesign.impromptu.Retrofit.EventMessageClick;
import com.mandywebdesign.impromptu.Retrofit.NormalRetroFav;
import com.mandywebdesign.impromptu.Retrofit.NormalRetrodeleteFav;
import com.mandywebdesign.impromptu.Retrofit.RetroGetEventData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookEventFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private LinearLayout dotsLayout;
    private TextView[] dots;
    ImageView book_message;
    LinearLayout organiser_layout;
    RelativeLayout messagelayout, invite_layouit;
    FragmentManager fragmentManager;
    Button mBookEvent;
    TextView organiserName,book_time,book_categry, peoplegoing, seeAll, remainingTicketTV, invitefriends, dialogtickttype,link1,link2,link3;
    ViewPager viewPager;
    RecyclerView users;
    ReadMoreTextView descri;
    RoundedImageView host_pic;
    PagerAdapter pagerAdapter;
    ProgressDialog progressDialog;
    TextView ticketPrice, book_location, book_date, totalPrice, event_title, eventprice;
    Spinner spinner;
    Button dialogButoon;
    public static CheckBox addtoFavCheck_box;
    View view;
    String checkgender,booklink1,booklink2,booklink3;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    Bundle bundle;
    ArrayList<String> userImage = new ArrayList<>();
    static String tot;
    static String InviteFriend;
    static String total_ticket;
    static String loginUserId;
    static String hostUserID;
    static String remaini_tickets;
    static String formattedDate;
    static String getFormattedDate;
    static String timeFrom;
    static String timeTo;
    static String value, S_token, fav_id, hostname, payvalue;
    public static ArrayList<String> image = new ArrayList<>();
    public static String id, cate, host_image, date, decs, postcode, ticktType, ticktprice, timefrom, hostimage, timeto, title, location, location2, city, gender, andendeenumber, numberoftickts, freeEvent, username;
    int CurrentPage = 0;
    AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_book_event, container, false);
        builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Coming soon...");

        fragmentManager = getActivity().getSupportFragmentManager();
        sharedPreferences = getContext().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        S_token = sharedPreferences.getString("Socailtoken", "");
        loginUserId = sharedPreferences.getString("userID", "");
        checkgender = sharedPreferences.getString("profilegender", "");

        progressDialog = new ProgressDialog(getActivity());
        Drawable drawable = new ProgressBar(getContext()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(drawable);

        Toolbar toolbar = view.findViewById(R.id.boobk_event_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });


        init();
        listeners();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        users.setLayoutManager(layoutManager);
        users.setNestedScrollingEnabled(false);


        bundle = getArguments();
        if (bundle != null) {
            value = bundle.getString("event_id");
            payvalue = bundle.getString("back_pay");

            //  fav_id = bundle.getString("fav_id");
            hostname = bundle.getString("hostname");
            hostimage = bundle.getString("hostImage");
            if (hostname!=null)
            {
                String[] name = hostname.split(" ");
                if (name.length==1)
                {
                    String Fname = name[0];
                    organiserName.setText(Fname+" ");
                }else {
                    String Fname = name[0];
                    String Lname = name[1];
                    organiserName.setText(Fname+" "+Lname.subSequence(0,1));
                }




            }else {
                organiserName.setText(hostname);
            }
            getEventData(value);

            addFav(value);


            getUsers(S_token, value);

            book_message.setVisibility(View.VISIBLE);
        }
        getRaminingEvents(S_token, value);

        return view;
    }

    //check remainig tickets
    private void getRaminingEvents(String s_token, String id) {

        Call<RemainingTickets> call = WebAPI.getInstance().getApi().remainingTickets("Bearer " + s_token, id);
        call.enqueue(new Callback<RemainingTickets>() {
            @Override
            public void onResponse(Call<RemainingTickets> call, Response<RemainingTickets> response) {

                if (response.body()!=null) {
                    remaini_tickets = String.valueOf(response.body().getData());
                    remainingTicketTV.setText("Tickets Remaining " + remaini_tickets);
                } else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RemainingTickets> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {

        book_time = view.findViewById(R.id.book_time);
        book_categry = view.findViewById(R.id.book_categry);
        eventprice = view.findViewById(R.id.event_price);
        dotsLayout = view.findViewById(R.id.book_indicator);
        viewPager = (ViewPager) view.findViewById(R.id.book_viewpager);
        addtoFavCheck_box = (CheckBox) view.findViewById(R.id.book_event_favourite);
        mBookEvent = (Button) view.findViewById(R.id.book_event_book_bt);
        descri = view.findViewById(R.id.book_description);
        book_location = view.findViewById(R.id.book_location);
        book_date = view.findViewById(R.id.book_date);
        event_title = view.findViewById(R.id.book_tittle);
        host_pic = view.findViewById(R.id.book_user_picture);
        organiserName = view.findViewById(R.id.book_organiser_name);
        peoplegoing = view.findViewById(R.id.book_attendess_people);
        users = view.findViewById(R.id.book_recyclerView);
        seeAll = view.findViewById(R.id.book_seeAll);
        book_message = view.findViewById(R.id.book_message);
        organiser_layout = view.findViewById(R.id.organiser_layout);
        messagelayout = view.findViewById(R.id.messagelayout);
        invite_layouit = view.findViewById(R.id.invite_layouit);
        remainingTicketTV = view.findViewById(R.id.remainingTicketTV);
        invitefriends = view.findViewById(R.id.invitefriends);
        link1 = view.findViewById(R.id.book_link1);
        link2 = view.findViewById(R.id.book_link2);
        link3 = view.findViewById(R.id.book_link3);
    }

    private void listeners() {

        invite_layouit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        builder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBookEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkgender.equals("")) {
                    dialogUpdate();
                } else {
                    dialog();
                }
            }
        });
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("value", id);

                See_all see_all = new See_all();
                see_all.setArguments(bundle);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.home_frame_layout, see_all);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        book_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("event_title", title);
                bundle.putString("event_image", host_image);
                bundle.putString("eventID", value);
                bundle.putString("event_host_user", hostUserID);

                final ChatBox chatBox = new ChatBox();
                chatBox.setArguments(bundle);

                Call<EventMessageClick> call = WebAPI.getInstance().getApi().eventMEsgClick("Bearer " + S_token, id);
                call.enqueue(new Callback<EventMessageClick>() {
                    @Override
                    public void onResponse(Call<EventMessageClick> call, Response<EventMessageClick> response) {
                        if (response.body()!=null) {
                            if (response.body().getStatus().equals("200"))
                            {
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.home_frame_layout, chatBox);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }

                        } else {
                            Intent intent = new Intent(getContext(), NoInternetScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<EventMessageClick> call, Throwable t) {
                        Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(booklink1);
                if (uri.getPath().contains("https://"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }else {
                    Uri uri1 = Uri.parse("https://"+uri);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri1);
                    startActivity(intent);
                }
            }
        });

        link2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(booklink2);

                if (uri.getPath().contains("https://"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }else {
                    Uri uri1 = Uri.parse("https://"+uri);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri1);
                    startActivity(intent);
                }

            }
        });

        link3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(booklink3);
                if (uri.getPath().contains("https://"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }else {
                    Uri uri1 = Uri.parse("https://"+uri);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri1);
                    startActivity(intent);
                }
            }
        });
    }

    public void dialog() {
        String ticketNum[] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_book_ticket);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        FindId(dialog);

        ticketPrice.setText(ticktprice);
        dialogtickttype.setText(ticktType);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, ticketNum);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        if (total_ticket != null)
        {
            int ticket = Integer.parseInt(total_ticket);
//            spinner.setSelection(ticket);
        }


        if (ticktprice.equals("0")) {

            dialogButoon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Float TotalTIcket = Float.valueOf(((total_ticket)));

                    Float RemainingTIckets = Float.valueOf((remaini_tickets));
                    if (TotalTIcket > RemainingTIckets) {
                        Toast.makeText(getContext(), "Not Available", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.show();
                        Call<BookFreeEvents> call = WebAPI.getInstance().getApi().freebookevent("Bearer " + S_token, value, total_ticket);
                        call.enqueue(new Callback<BookFreeEvents>() {
                            @Override
                            public void onResponse(Call<BookFreeEvents> call, Response<BookFreeEvents> response) {

                                if (response.body()!=null) {
                                    progressDialog.dismiss();
//                                    ratingDialog();
                                    dialog.dismiss();

                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("eventID", value);
                                    //  bundle1.putString("userId",userId);
                                    bundle1.putString("paid", "Paid");
                                    editor.putString("eventImage", BookEventFragment.image.get(0));
                                    editor.apply();

                                    ConfirmationFragment confirmationFragment = new ConfirmationFragment();
                                    confirmationFragment.setArguments(bundle1);

                                    FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                                    transaction2.replace(R.id.home_frame_layout, confirmationFragment);
                                    transaction2.addToBackStack(null);
                                    transaction2.commit();

                                } else {
                                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<BookFreeEvents> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Book Error=> " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

        } else {
            dialogButoon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putString("total_price", tot);
                    bundle.putString("event_id", value);
                    bundle.putString("total_tickets", total_ticket);
                    bundle.putString("ticket_Price", ticktprice);
                    PayFragment payFragment = new PayFragment();
                    payFragment.setArguments(bundle);

                    FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                    transaction2.replace(R.id.home_frame_layout, payFragment);
                    transaction2.addToBackStack(null);
                    transaction2.commit();


                    dialog.dismiss();
                }
            });

        }

        dialog.show();
    }


    private void FindId(Dialog dialog) {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        ticketPrice = dialog.findViewById(R.id.dailog_ticket_price);
        totalPrice = dialog.findViewById(R.id.dailog_total_price);
        spinner = dialog.findViewById(R.id.dailog_spinner);
        dialogButoon = dialog.findViewById(R.id.dailog_button);
        dialogtickttype = dialog.findViewById(R.id.dailog_ticket_type);
    }

    private void dialogUpdate() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.updateprofilepopoup);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button okay, cancel;
        okay = dialog.findViewById(R.id.checkokaydialog);
        cancel = dialog.findViewById(R.id.checkcanceldialog);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("normal_edit", "0");
                Normal_user_profile user_profile = new Normal_user_profile();
                user_profile.setArguments(bundle);

                FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                transaction2.replace(R.id.home_frame_layout, user_profile);
                transaction2.commit();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String text = parent.getItemAtPosition(position).toString();


        Float a = Float.valueOf((text));
        total_ticket = String.valueOf(a);

        String tickt = ticketPrice.getText().toString();
        Float b = Float.valueOf((tickt));

        Float total = a * b;

        tot = String.valueOf(total);

        totalPrice.setText(tot);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void getUsers(String s_token, String value) {

        Call<Booked_User> call = WebAPI.getInstance().getApi().booked_users("Bearer " + s_token, value);
        call.enqueue(new Callback<Booked_User>() {
            @Override
            public void onResponse(Call<Booked_User> call, Response<Booked_User> response) {

                if (response.body()!=null) {

                    Booked_User booked_users = response.body();
                    List<Booked_User.Datum> bookedUsersList = booked_users.getData();
                    userImage.clear();

                    if (response.body().getStatus().equals("200")) {
                        for (int i = 0; i < bookedUsersList.size(); i++) {
                            userImage.add(response.body().getData().get(i).getFile());
                            peoplegoing.setText(bookedUsersList.size() + " People are going");
                            Log.d("userImage", "" + response.body().getData().get(i).toString());

                        }
                    } else {
                        peoplegoing.setText("0 People are going");
                        seeAll.setClickable(false);
                        seeAll.setVisibility(View.GONE);
                        users.setVisibility(View.GONE);
                    }

                    Booked_users adapter = new Booked_users(getContext(), userImage);
                    users.setAdapter(adapter);
                } else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Booked_User> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getEventData(final String value) {
        progressDialog.setMessage("Please wait until we fetch your events");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Call<RetroGetEventData> call = WebAPI.getInstance().getApi().getEvents("Bearer " + S_token, "application/json", value);
        call.enqueue(new Callback<RetroGetEventData>() {
            @Override
            public void onResponse(Call<RetroGetEventData> call, Response<RetroGetEventData> response) {

                if (response.body()!=null) {
                    if (response.body().getStatus().equals("200")) {
                        RetroGetEventData getEventData = response.body();
                        Log.d("123456", "" + getEventData.getData().size());
                        List<RetroGetEventData.Datum> datumList = getEventData.getData();
                        Log.d("events", "" + response.code() + " " + datumList.size());
                        for (RetroGetEventData.Datum datum : datumList) {
                            id = datum.getEventId().toString();
                            cate = datum.getCategory();
                            decs = datum.getDescription();
                            location = datum.getAddressline1();
                            location2 = datum.getAddressline2();
                            postcode = datum.getPostcode();
                            city = datum.getCity();
                            gender = datum.getAttendeesGender();
                            hostUserID = datum.getUserid().toString();// host id
                            andendeenumber = datum.getAttendeesNo();
                            freeEvent = datum.getFreeEvent();
                            ticktType = datum.getTicketType();
                            ticktprice = datum.getPrice();
                            numberoftickts = datum.getNoOfTickets();
                            username = datum.getBEventHostname();
                            InviteFriend = datum.getAttendeesGender();
                            booklink1 = datum.getLink1();
                            booklink2 = datum.getLink2();
                            booklink3=datum.getLink3();


                            String dateformat = datum.getDate();
                            /*to change server date formate*/
                            String s1 = dateformat;
                            String[] str = s1.split("/");
                            String str1 = str[0];
                            String str2 = str[1];
                            String str3 = str[2];

                            book_categry.setText(cate);
                            title = datum.getTitle();
                            timefrom = datum.getTimeFrom();
                            timeto = datum.getTimeTo();
                            host_image = datum.getHostImage();
                            image = (ArrayList<String>) datum.getFile().get(0).getImg();
                            Collections.reverse(image);

                            if (ticktprice.equals("0"))
                            {
                                eventprice.setText("Free");
                            }else {
                                eventprice.setText("£ "+ticktprice);
                            }
                            if (!datum.getLink1().isEmpty())
                            {
                                SpannableString content = new SpannableString(datum.getLink1().toString());
                                content.setSpan(new UnderlineSpan(), 0, datum.getLink1().length(), 0);
                                link1.setText(content);
                                link1.setVisibility(View.VISIBLE);
                            }
                            if (!datum.getLink2().isEmpty())
                            {
                                SpannableString content = new SpannableString(datum.getLink2());
                                content.setSpan(new UnderlineSpan(), 0, datum.getLink2().length(), 0);
                                link2.setText(content);
                                link2.setVisibility(View.VISIBLE);
                            }
                            if (!datum.getLink3().isEmpty())
                            {
                                SpannableString content = new SpannableString(datum.getLink3());
                                content.setSpan(new UnderlineSpan(), 0, datum.getLink3().length(), 0);
                                link3.setText(content);
                                link3.setVisibility(View.VISIBLE);
                            }

                            fav_id = datum.getFavourite().toString();
                            Glide.with(getContext()).load(host_image).into(host_pic);

                            Collections.reverse(image);
                            //get Time to in AM PM

                            String time_t = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.","am").replaceFirst("p.m.","pm").replaceFirst("AM","am").replaceFirst("PM","pm");
                            String time_to = Util.convertTimeStampToTime(Long.parseLong(datum.getEventEndDt())).replaceFirst("a.m.","am").replaceFirst("p.m.","pm").replaceFirst("AM","am").replaceFirst("PM","pm");
                            String end_date = Util.convertTimeStampDate(Long.parseLong(datum.getEventEndDt()));

                                if (time_t.startsWith("0") && time_to.startsWith("0")) {
                                    timeFrom = time_t.substring(1);
                                    timeTo = time_to.substring(1);
                                    book_time.setText(timeFrom + " - " + timeTo);
                                    book_date.setText(str2 + "/" + str1 + "/" + str3 +" - "+ end_date);
                                } else if (time_t.startsWith("0")) {
                                    timeFrom = time_t.substring(1);
                                    if (time_to.startsWith("0")) {
                                        timeTo = time_to.substring(1);
                                        book_time.setText(timeFrom + " - " + timeTo);
                                        book_date.setText(str2 + "/" + str1 + "/" + str3 +" - "+ end_date);
                                    } else {
                                        timeTo = time_to.substring(0);
                                        book_time.setText(timeFrom + " - " + timeTo);
                                        book_date.setText(str2 + "/" + str1 + "/" + str3 +" - "+ end_date);
                                    }
                                } else if (time_to.startsWith("0")) {
                                    timeTo = time_to.substring(1);
                                    if (time_t.startsWith("0")) {
                                        timeFrom = time_t.substring(1);
                                        book_time.setText(timeFrom + " - " + timeTo);
                                        book_date.setText(str2 + "/" + str1 + "/" + str3 +" - "+ end_date);
                                    } else {
                                        timeFrom = time_t.substring(0);
                                        book_time.setText(timeFrom + " - " + timeTo);
                                        book_date.setText(str2 + "/" + str1 + "/" + str3 +" - "+ end_date);
                                    }
                                } else if (!time_t.startsWith("0") && !time_to.startsWith("0")) {
                                    timeFrom = time_t.substring(0);
                                    timeTo = time_to.substring(0);
                                    book_time.setText(timeFrom + " - " + timeTo);
                                    book_date.setText(str2 + "/" + str1 + "/" + str3 +" - "+ end_date);
                                }



                            Log.d("hostImage", "" + host_image);

                            //Check Event Fav Status
                            if (fav_id.equals("1")) {
                                addtoFavCheck_box.setChecked(true);
                            } else if (fav_id.equals("0")) {
                                addtoFavCheck_box.setChecked(false);
                            }
                            viewPager.setOffscreenPageLimit(1);
                            pagerAdapter = new B_EventDetailImageAdapter(getContext(), image);
                            viewPager.setAdapter(pagerAdapter);
                            Log.d("image", "" + image);

                            addBottomDots(0);
                            //indicator.setViewPager(viewPager);
                            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


                                @Override
                                public void onPageScrolled(int i, float v, int i1) {
                                    CurrentPage = i;
                                }

                                @Override
                                public void onPageSelected(int i) {
                                    addBottomDots(i);
                                }

                                @Override
                                public void onPageScrollStateChanged(int i) {

                                    if (i == ViewPager.SCROLL_STATE_IDLE) {

                                        int pagecount = image.size();

                                        if (CurrentPage == pagecount) {
                                            viewPager.setCurrentItem(pagecount, true);
                                        } else
                                            CurrentPage++;
                                    }

                                }
                            });

                            descri.setText(decs);
                            book_location.setText(location);
                            event_title.setText(title);
                            invitefriends.setText("Invite Friends " + "( " + InviteFriend + " )");



                            /*id_code*/

                            if (loginUserId.equals(hostUserID)) {
                                book_message.setVisibility(View.INVISIBLE);
                                organiser_layout.setVisibility(View.GONE);
                                messagelayout.setVisibility(View.GONE);
                                mBookEvent.setVisibility(View.GONE);
                            } else if (hostUserID == (null)) {
                                book_message.setVisibility(View.INVISIBLE);
                            } else {
                                book_message.setVisibility(View.VISIBLE);
                            }
                            Log.d("Check_User", "" + loginUserId + "  " + hostUserID);
                        }
                    } else if (response.body().getStatus().equals("400")) {
                        Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RetroGetEventData> call, Throwable t) {
                Log.d("events1", "" + t.getMessage() + " " + value);
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public void addFav(final String value) {

        addtoFavCheck_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if (!fav_id.contains("1")) {
                        Call<NormalRetroFav> call = WebAPI.getInstance().getApi().fav("Bearer " + S_token, "application/json", value);
                        call.enqueue(new Callback<NormalRetroFav>() {
                            @Override
                            public void onResponse(Call<NormalRetroFav> call, Response<NormalRetroFav> response) {
                                if (response.body()!=null)
                                {

                                }
                                //Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<NormalRetroFav> call, Throwable t) {

                            }
                        });
                    }

                } else {
                    Call<NormalRetrodeleteFav> call = WebAPI.getInstance().getApi().deletefav("Bearer " + S_token, "application/json", value);
                    call.enqueue(new Callback<NormalRetrodeleteFav>() {
                        @Override
                        public void onResponse(Call<NormalRetrodeleteFav> call, Response<NormalRetrodeleteFav> response) {
                            if (response.body()!=null)
                            {

                            }
                            //  Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<NormalRetrodeleteFav> call, Throwable t) {

                        }
                    });
                }
            }
        });

    }


    public void ratingDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_rating_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        final RatingBar ratingBar = dialog.findViewById(R.id.rating_bar);
        final EditText feedback = dialog.findViewById(R.id.feedback);
        Button dialogratingshare_button = dialog.findViewById(R.id.dialogratingshare_button);
        dialog.show();

        dialogratingshare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rating = String.valueOf(ratingBar.getRating());
                String feedbck = feedback.getText().toString();
                if (rating.equals("") | feedbck.equals("")) {
                    Toast.makeText(getContext(), "Add Rating  and reviews", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    Call<Rating> call = WebAPI.getInstance().getApi().rating("Bearer " + S_token, value, rating, feedbck);
                    call.enqueue(new Callback<Rating>() {
                        @Override
                        public void onResponse(Call<Rating> call, Response<Rating> response) {
                            if (response.body()!=null) {
                                dialog.dismiss();
                                progressDialog.dismiss();
                                // Toast.makeText(getContext(), "Rating => " + response.body().getData().getRating(), Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<Rating> call, Throwable t) {
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    //add dots at bottom
    private void addBottomDots(int currentPage) {

        dots = new TextView[image.size()];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(50);
            dots[i].setTextColor(getResources().getColor(R.color.colortextwhite));
            dotsLayout.addView(dots[i]);

        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(getResources().getColor(R.color.colorTheme));
        }

    }
}

