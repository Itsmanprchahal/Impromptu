package com.mandywebdesign.impromptu.BusinessRegisterLogin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Adapters.B_EventDetailImageAdapter;
import com.mandywebdesign.impromptu.Adapters.Booked_users;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.History;
import com.mandywebdesign.impromptu.Retrofit.BookFreeEvents;
import com.mandywebdesign.impromptu.messages.ChatBoxActivity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AddEvents.Add_Event_Activity;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.Booked_User;
import com.mandywebdesign.impromptu.Retrofit.EventMessageClick;
import com.mandywebdesign.impromptu.Retrofit.NormalRetroFav;
import com.mandywebdesign.impromptu.Retrofit.NormalRetrodeleteFav;
import com.mandywebdesign.impromptu.Retrofit.RemainingTickets;
import com.mandywebdesign.impromptu.Retrofit.RetroGetEventData;
import com.mandywebdesign.impromptu.Retrofit.RetroPostDraft;
import com.mandywebdesign.impromptu.Retrofit.TotalTickets;
import com.mandywebdesign.impromptu.Utils.Util;
import com.mandywebdesign.impromptu.ui.BookEventActivity;
import com.mandywebdesign.impromptu.ui.ConfirmationActivity;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.Join_us;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.PayActivity;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessEventDetailAcitvity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    ImageButton backon_b_eventdetail;
    View view;
    RecyclerView users;
    TextView category, event_price, datetime, loc, BusinessEvent_detailsFragment_book_time, BusinessEvent_detailsFragment_book_time2, ticketPrice, ticketPrice1, ticketPrice2, dtotalPrice, dticketPrice, numberofTickets, numberofTickets1, numberofTickets2, totalPrice, totalPrice1, totalPrice2, freetext, event_title, see_all,
            BusinessEvent_detailsFragment_book_link1, BusinessEvent_detailsFragment_book_link2, BusinessEvent_detailsFragment_book_link3;
    public TextView peoplecoming, revenue;
    ReadMoreTextView descri;
    FragmentManager manager;
    Button checkInGuest, publish, BusinessEvent_detailsFragment_book_button;
    GoogleSignInAccount account;
    LinearLayout linearLayout, priceLayput, priceLayput1, priceLayput2;
    boolean loggedOut;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences, sharedPreferences1, profileupdatedPref;
    String user, fav_id;
    RoundedImageView bannerImage;
    public static String value, event_type, otherEvnts, formattedDate, getFormattedDate, bookstatus;
    Dialog progressDialog;
    ViewPager viewPager;
    ImageView editevent;
    PagerAdapter pagerAdapter;
    public static ArrayList<String> userImage = new ArrayList<>();
    public static ArrayList<String> total_tickets = new ArrayList<>();
    public static ArrayList<String> user_id = new ArrayList<>();
    public static ArrayList<String> userName = new ArrayList<>();
    String BToken, S_Token, attendess, link1, link2, link3;
    public static ArrayList<String> image = new ArrayList<>();
    static String id, cate, ticktprice, event_status, ticketprice1, ticketprice2, ticketprice3, hostImage, hostUserID, tickets_booked_by_user, decs, postcode, ticktType, timefrom, timeto, title, location, location2, city, gender, andendeenumber, bookedtickets, numberoftickts, freeEvent, username, timeFrom, timeTo;
    int CurrentPage = 0;
    CheckBox eventdetail_favbt;
    TextView ticketview;
    Button seemessagesforthisevent;
    Intent intent;
    String from, userTYpe, getTickets_booked_by_user, bookedticket;
    static String tickettypeposition, spinnerposition, total_ticket, tot, remaini_tickets, getSpinnerposition = "1";
    Spinner spinner, ticketype_spinner;
    Button dialogButoon;
    TextView dialogtickttype;
    String tickettypedialog, tickettypespinnerposintion;
    ArrayList<String> tickettypes = new ArrayList<>();
    ArrayList<String> ticketprice = new ArrayList<>();
    String currentUser;
    TextView tickettype1, tickettype2, tickettype3,eventid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_event_detail_acitvity);

        account = GoogleSignIn.getLastSignedInAccount(this);
        loggedOut = AccessToken.getCurrentAccessToken() == null;
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        user = "Bearer " + sharedPreferences.getString("Usertoken", "");
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");
        sharedPreferences1 = getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        profileupdatedPref = getSharedPreferences("profileupdated", Context.MODE_PRIVATE);

        currentUser = sharedPreferences.getString("userID", "").toString();

        progressDialog = ProgressBarClass.showProgressDialog(this);
        Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        intent = getIntent();
        if (intent != null) {
            from = intent.getStringExtra("from");
        }

        init();
        listerners();

        checkEventtype();
        gotomessagebox(value);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        users.setLayoutManager(layoutManager);
        users.setNestedScrollingEnabled(false);


    }

    private void gotomessagebox(final String id) {
        seemessagesforthisevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!S_Token.equals("")) {
                    OpenEventChat(S_Token, id);
                } else {
                    OpenEventChat(BToken, id);
                }
            }
        });
    }

    private void checkEventtype() {

        Intent intent = getIntent();
        if (intent != null) {

            value = intent.getStringExtra("event_id");
            event_type = intent.getStringExtra("eventType");
            otherEvnts = intent.getStringExtra("other_events");

            if (event_type.equals("draft")) {

                linearLayout.setVisibility(View.GONE);
                publish.setVisibility(View.VISIBLE);
                editevent.setImageDrawable(getResources().getDrawable(R.drawable.edit));
                eventdetail_favbt.setVisibility(View.GONE);
                checkInGuest.setVisibility(View.GONE);

                if (!BToken.equalsIgnoreCase("")) {
                    getEventdata(BToken, value);
                    getUsers(BToken, value);

                } else if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
                }

                publish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.show();

                        if (!BToken.equalsIgnoreCase("")) {
                            postDraft("Bearer " + BToken);
                        } else if (!S_Token.equalsIgnoreCase("")) {
                            postDraft("Bearer " + S_Token);
                        }
                    }
                });

            } else if (event_type.equals("live")) {

                if (!BToken.equalsIgnoreCase("")) {
                    getEventdata(BToken, value);
                    getUsers(BToken, value);
                    getRaminingEvents(BToken, value);
                } else if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
                    addFav(value);
                    eventdetail_favbt.setVisibility(View.GONE);
                    getRaminingEvents(S_Token, value);
                }
            } else if (event_type.equals("history")) {

                if (!BToken.equalsIgnoreCase("")) {
                    getEventdata(BToken, value);
                    getUsers(BToken, value);
                    peoplecoming.setVisibility(View.GONE);
                    checkInGuest.setText("Relist");

                } else if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
                    peoplecoming.setVisibility(View.GONE);
                    checkInGuest.setText("Relist");

                }
            } else if (event_type.equals("fav")) {

                if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
                    getRaminingEvents(S_Token, value);
                    checkInGuest.setVisibility(View.GONE);
                    if (!otherEvnts.equalsIgnoreCase("")) {
                        linearLayout.setVisibility(View.GONE);
                    }


                }
            } else if (event_type.equals("upcoming")) {
                if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
                    linearLayout.setVisibility(View.GONE);
                    checkInGuest.setVisibility(View.GONE);
                }
            } else if (event_type.equals("past")) {
                peoplecoming.setVisibility(View.GONE);
                if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
                    linearLayout.setVisibility(View.GONE);
                    checkInGuest.setVisibility(View.GONE);
                } else {
                    getEventdata(BToken, value);
                    getUsers(BToken, value);
                    linearLayout.setVisibility(View.GONE);
                    checkInGuest.setVisibility(View.GONE);
                }
            } else if (event_type.equals("CheckGuest")) {
                if (!BToken.equalsIgnoreCase("")) {
                    getEventdata(BToken, value);
                    getUsers(BToken, value);
                } else if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
                    addFav(value);
                }
            } else if (event_type.equals("")) {
                if (!BToken.equalsIgnoreCase("")) {
                    getEventdata(BToken, value);
                    getUsers(BToken, value);
                    getRaminingEvents(BToken, value);
                } else if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
                    addFav(value);
                    eventdetail_favbt.setVisibility(View.GONE);
                    getRaminingEvents(S_Token, value);
                }
            }

            editevent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (event_type.equals("draft")) {
                        Intent intent = new Intent(BusinessEventDetailAcitvity.this, Add_Event_Activity.class);
                        intent.putExtra("editevent", "edit");
                        intent.putExtra("value", value);
                        startActivity(intent);
                    } else {
                        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                .setLink(Uri.parse("https://www.impromptusocial.com/" + "event_id/" + value))
                                .setDomainUriPrefix("impromptusocial.page.link")
                                // Open links with this app on Android  amitpandey12.page.link
                                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build()).setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder().setDescription("I am hosting this Impromptu event, fancy coming?").build())
                                // Open links with com.example.ios on iOS
                                .setIosParameters(new DynamicLink.IosParameters.Builder("impromptusocial.page.link").build()).setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder().setDescription("I am hosting this Impromptu event, fancy coming?").build())
                                .buildDynamicLink();

                        Uri dynamicLinkUri = dynamicLink.getUri();

                        Log.d("hello123", "1" + dynamicLink.getUri());


                        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink().setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder().setDescription("Hey! Would you like to join me at this Impromptu event?").build())
                                .setLongLink(Uri.parse("https://" + dynamicLink.getUri().toString()))
                                .buildShortDynamicLink()
                                .addOnCompleteListener(BusinessEventDetailAcitvity.this, new OnCompleteListener<ShortDynamicLink>() {
                                    @Override
                                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                        if (task.isSuccessful()) {
                                            // Short link created
                                            Uri shortLink = task.getResult().getShortLink();
                                            Uri flowchartLink = task.getResult().getPreviewLink();
                                            Intent share = new Intent(Intent.ACTION_SEND);
                                            share.setType("text/plain");
                                            share.putExtra(Intent.EXTRA_TEXT, "I am hosting this Impromptu event, fancy coming?" + "\n" + "\n" + shortLink.toString());
                                            startActivity(Intent.createChooser(share, "Share Event"));
                                        } else {

                                        }
                                    }
                                });
                    }

                }
            });
        }
    }

    private void postDraft(String s_token) {

        Call<RetroPostDraft> call = WebAPI.getInstance().getApi().postDraft(s_token, "application/json", value);
        call.enqueue(new Callback<RetroPostDraft>() {
            @Override
            public void onResponse(Call<RetroPostDraft> call, Response<RetroPostDraft> response) {

                if (response.body() != null) {
                    Intent intent = new Intent(BusinessEventDetailAcitvity.this, Home_Screen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(BusinessEventDetailAcitvity.this, "Published", Toast.LENGTH_SHORT).show();
                } else {

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RetroPostDraft> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    private void getTotalTickets(String token, String value) {
        Call<TotalTickets> call = WebAPI.getInstance().getApi().totalEvents("Bearer " + token, value);
        call.enqueue(new Callback<TotalTickets>() {
            @Override
            public void onResponse(Call<TotalTickets> call, Response<TotalTickets> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {
                        attendess = response.body().getData().get(0).getTotalTickets();
//                        numberofTickets.setText(attendess);
                        if (response.body().getData().get(0).getPrice() != null) {
                            ticktprice = response.body().getData().get(0).getPrice();
                        } else {
                            ticktprice = "0";
                        }

                        Float TicketPrice = Float.valueOf((ticktprice));
                        Float TotalAttendess = Float.valueOf((attendess));
                        numberofTickets.setText(response.body().getData().get(0).getTotalTickets());
                        Float totalprice = TicketPrice * TotalAttendess;

                        totalPrice.setText("£ " + totalprice);

                    } else if (response.body().getStatus().equals("400")) {
                        numberofTickets.setText("0");
                        attendess = String.valueOf(0);
                        totalPrice.setText("£0");
                    }

                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(BusinessEventDetailAcitvity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<TotalTickets> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private void getUsers(String s_token, String value) {

        Call<Booked_User> call = WebAPI.getInstance().getApi().booked_users("Bearer " + s_token, value);
        call.enqueue(new Callback<Booked_User>() {
            @Override
            public void onResponse(Call<Booked_User> call, Response<Booked_User> response) {

                if (response.body() != null) {
                    Booked_User booked_users = response.body();
                    List<Booked_User.Datum> bookedUsersList = booked_users.getData();
                    userImage.clear();
                    user_id.clear();
                    total_tickets.clear();

                    if (response.body().getStatus().equals("200")) {
                        for (int i = 0; i < bookedUsersList.size(); i++) {
                            userImage.add(response.body().getData().get(i).getFile());
                            user_id.add(response.body().getData().get(i).getUserid().toString());
                            userName.add(response.body().getData().get(i).getUsername());
                            total_tickets.add(response.body().getData().get(i).getTotalTickets().toString());

                            Log.d("userImage", "" + response.body().getData().get(i).getFile());

                        }

                        if (event_status != null) {
                            if (event_status.equals("past")) {
                                peoplecoming.setVisibility(View.GONE);

                            } else {
                                peoplecoming.setVisibility(View.VISIBLE);
                            }
                        }


                        if (event_type.equals("past") || event_type.equals("history")) {

                        } else {
                            if (bookedUsersList.size() == 1) {
                                peoplecoming.setText("1 Person is coming");
                            } else if (bookedUsersList.size() >= 2) {
                                peoplecoming.setText(bookedUsersList.size() + " people are coming");

                            } else {
                                peoplecoming.setText("No one booked this event yet");
                            }
                        }
                        Booked_users adapter = new Booked_users(BusinessEventDetailAcitvity.this, userImage, user_id, total_tickets);
                        users.setAdapter(adapter);

                    } else if (response.body().getStatus().equals("400")) {

                        peoplecoming.setText("0 people coming");


                        users.setVisibility(View.GONE);
                        see_all.setVisibility(View.VISIBLE);
                        see_all.setClickable(false);
                    }
                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(BusinessEventDetailAcitvity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Booked_User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(BusinessEventDetailAcitvity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getEventdata(final String token, final String value) {
        Call<RetroGetEventData> call = WebAPI.getInstance().getApi().getEvents("Bearer " + token, "application/json", value);
        call.enqueue(new Callback<RetroGetEventData>() {
            @Override
            public void onResponse(Call<RetroGetEventData> call, Response<RetroGetEventData> response) {

                if (response.body() != null) {
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
                            andendeenumber = datum.getAttendeesNo();
                            freeEvent = datum.getFreeEvent();
                            tickets_booked_by_user = datum.getTickets_booked_by_user();
                            ticktType = datum.getTicketType();
                            bookstatus = datum.getEventBook();
                            getTickets_booked_by_user = datum.getTickets_booked_by_user();
                            event_status = datum.getEvent_status();

                            //Todo: get tickets types
                            for (int i = 0; i < datum.getTicketsType().size(); i++) {
                                tickettypes.add(datum.getTicketsType().get(i).getTicketType());
                                ticketprice.add(datum.getTicketsType().get(i).getValue());
                            }

                            if (datum.getPrice() != null) {
                                getTotalTickets(token, value);
                                ticktprice = datum.getPrice();
                                numberofTickets.setText(datum.getTickets_booked_by_user());
                                ticketPrice.setText("£ " + ticktprice);
                            }


                            if (datum.getTicketsType().size() == 1) {
                                ticktprice = datum.getTicketsType().get(0).getValue();
                                Float TicketPrice = Float.valueOf((ticktprice));
                                Float TotalAttendess = Float.valueOf((datum.getTicketsType().get(0).getBooked_tickets().toString()));

                                Float totalprice = TicketPrice * TotalAttendess;
                                numberofTickets.setText(datum.getTicketsType().get(0).getBooked_tickets().toString());
                                totalPrice.setText("£ " + totalprice);
                                ticketPrice.setText("£ " + ticktprice);
                                tickettype1.setText(datum.getTicketsType().get(0).getTicketType());
                                priceLayput1.setVisibility(View.GONE);

                            }else
                            if (datum.getTicketsType().size() == 2) {
                                ticktprice = datum.getTicketsType().get(0).getValue();
                                ticketprice1 = datum.getTicketsType().get(1).getValue();
                                priceLayput1.setVisibility(View.VISIBLE);
                                Float TicketPrice = Float.valueOf((ticktprice));
                                Float TotalAttendess = Float.valueOf((datum.getTicketsType().get(0).getBooked_tickets().toString()));

                                Float totalprice = TicketPrice * TotalAttendess;

                                totalPrice.setText("£ " + totalprice);
                                ticketPrice.setText("£ "+ticktprice);
                                ticketPrice1.setText("£ "+ticketprice1);
                                tickettype1.setText(datum.getTicketsType().get(0).getTicketType());
                                //-----------------------++++++++++++++++++++++++----------------------
                                numberofTickets.setText(datum.getTicketsType().get(0).getBooked_tickets().toString());
                                numberofTickets1.setText(datum.getTicketsType().get(1).getBooked_tickets().toString());
                                Float TicketPrice1 = Float.valueOf((ticketprice1));
                                Float TotalAttendess1 = Float.valueOf((datum.getTicketsType().get(1).getBooked_tickets().toString()));

                                Float totalprice1 = TicketPrice1 * TotalAttendess1;

                                totalPrice1.setText("£ " + totalprice1);
                                tickettype2.setText(datum.getTicketsType().get(1).getTicketType());

                            }else
                            if (datum.getTicketsType().size() == 3) {
                                ticktprice = datum.getTicketsType().get(0).getValue();
                                priceLayput2.setVisibility(View.VISIBLE);
                                priceLayput1.setVisibility(View.VISIBLE);
                                numberofTickets.setText(datum.getTicketsType().get(0).getBooked_tickets().toString());
                                numberofTickets1.setText(datum.getTicketsType().get(1).getBooked_tickets().toString());
                                numberofTickets2.setText(datum.getTicketsType().get(2).getBooked_tickets().toString());
                                ticketprice1 = datum.getTicketsType().get(1).getValue();
                                ticketprice2 = datum.getTicketsType().get(2).getValue();
                                ticketPrice.setText("£ " + ticktprice);
                                ticketPrice1.setText("£ " + ticketprice1);
                                ticketPrice2.setText("£ " + ticketprice2);


                                Float TicketPrice = Float.valueOf((ticktprice));
                                Float TotalAttendess = Float.valueOf((datum.getTicketsType().get(0).getBooked_tickets().toString()));

                                Float totalprice = TicketPrice * TotalAttendess;

                                totalPrice.setText("£ " + totalprice);
                                tickettype1.setText(datum.getTicketsType().get(0).getTicketType());
                                //-----------------------++++++++++++++++++++++++----------------------

                                Float TicketPrice1 = Float.valueOf((ticketprice1));
                                Float TotalAttendess1 = Float.valueOf((datum.getTicketsType().get(1).getBooked_tickets().toString()));

                                Float totalprice1 = TicketPrice1 * TotalAttendess1;

                                totalPrice1.setText("£ " + totalprice1);
                                tickettype2.setText(datum.getTicketsType().get(1).getTicketType());

                                //----------------------------++++++++++++++++++--------------------------

                                Float TicketPrice2 = Float.valueOf((ticketprice2));
                                Float TotalAttendess2 = Float.valueOf((datum.getTicketsType().get(2).getBooked_tickets().toString()));

                                Float totalprice2 = TicketPrice2 * TotalAttendess2;

                                totalPrice2.setText("£ " + totalprice2);
                                tickettype3.setText(datum.getTicketsType().get(2).getTicketType());
                            }


                            eventid.setText("EID " + datum.getEventId().toString());
                            if (ticktprice.equals("0")) {
                                event_price.setText("Free");
                                priceLayput.setVisibility(View.GONE);
                                priceLayput2.setVisibility(View.GONE);
                                priceLayput1.setVisibility(View.GONE);
                                tickettypedialog = event_price.getText().toString();
                            } else if (!ticktprice.equals("0") && !ticktprice.equals("Paid")) {
                                event_price.setText("£ " + ticktprice);
                                tickettypedialog = event_price.getText().toString();
                            } else {
                                event_price.setText(ticktprice);
                                tickettypedialog = event_price.getText().toString();
                            }

                            bookedtickets = String.valueOf(datum.getTotalEventBookings());

                            numberoftickts = datum.getNoOfTickets();
                            username = datum.getBEventHostname();
                            link1 = datum.getLink1();
                            link2 = datum.getLink2();
                            link3 = datum.getLink3();


                            title = datum.getTitle();
                            hostImage = datum.getHostImage();
                            hostUserID = datum.getUserid().toString();
                            timefrom = datum.getTimeFrom();
                            timeto = datum.getTimeTo();
                            image = (ArrayList<String>) datum.getFile().get(0).getImg();
                            fav_id = datum.getFavourite().toString();

                            //get Time to in AM PM
                            String time_t = Util.convertTimeStampToTime1(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM", "am").replaceFirst("PM", "pm");
                            String time_to = Util.convertTimeStampToTime1(Long.parseLong(datum.getEventEndDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM", "am").replaceFirst("PM", "pm");
                            String start_date = Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt()));
                            String end_date = Util.convertTimeStampDate(Long.parseLong(datum.getEventEndDt()));
                            if (start_date.matches(end_date)) {
                                datetime.setText(start_date);
                            } else {
                                datetime.setText(start_date + " - " + end_date);
                            }


                            String startTime = removeLeadingZeroes(time_t);
                            if (startTime.contains(":00")) {
                                startTime = removeLeadingZeroes(time_t).replace(":00", "");
                                BusinessEvent_detailsFragment_book_time.setText(startTime);
                            } else {
                                BusinessEvent_detailsFragment_book_time.setText(removeLeadingZeroes(time_t));
                            }

                            String endTime = removeLeadingZeroes(time_to);
                            if (endTime.contains(":00")) {
                                endTime = removeLeadingZeroes(time_to).replace(":00", "");
                                BusinessEvent_detailsFragment_book_time2.setText(endTime);
                            } else {
                                BusinessEvent_detailsFragment_book_time2.setText(removeLeadingZeroes(endTime));
                            }

                            if (currentUser.equals(hostUserID)) {
                                eventdetail_favbt.setVisibility(View.GONE);
                            }


                            String imageurl = image.get(0);
                            Glide.with(BusinessEventDetailAcitvity.this).load(imageurl).into(bannerImage);

                            //Check Event Fav Status
                            if (fav_id.equals("1")) {
                                eventdetail_favbt.setChecked(true);
                            } else if (fav_id.equals("0")) {
                                eventdetail_favbt.setChecked(false);
                            }
                            viewPager.setOffscreenPageLimit(1);
                            pagerAdapter = new B_EventDetailImageAdapter(BusinessEventDetailAcitvity.this, image);
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

                                        int pagecount = Add_Event_Activity.image_uris.size();

                                        if (CurrentPage == pagecount) {
                                            viewPager.setCurrentItem(pagecount, true);
                                        } else
                                            CurrentPage++;
                                    }

                                }
                            });

                            if (ticktprice.equals("0")) {
                                event_price.setText("Free");
                                priceLayput.setVisibility(View.GONE);
                                revenue.setVisibility(View.GONE);
                            } else if (ticktprice.equals("Paid")) {
                                //event_price.setText(ticktprice);
                            }


                            category.setText(cate);
                            descri.setText(decs);
                            event_title.setText(title);
                            if (!link1.isEmpty()) {
                                SpannableString content = new SpannableString(link1);
                                content.setSpan(new UnderlineSpan(), 0, link1.length(), 0);
                                BusinessEvent_detailsFragment_book_link1.setText(content);
                                BusinessEvent_detailsFragment_book_link1.setVisibility(View.VISIBLE);
                            }
                            if (!link2.isEmpty()) {
                                SpannableString content = new SpannableString(link2);
                                content.setSpan(new UnderlineSpan(), 0, link2.length(), 0);
                                BusinessEvent_detailsFragment_book_link2.setText(content);
                                BusinessEvent_detailsFragment_book_link2.setVisibility(View.VISIBLE);
                            }
                            if (!link3.isEmpty()) {
                                SpannableString content = new SpannableString(link3);
                                content.setSpan(new UnderlineSpan(), 0, link3.length(), 0);
                                BusinessEvent_detailsFragment_book_link3.setText(content);
                                BusinessEvent_detailsFragment_book_link3.setVisibility(View.VISIBLE);
                            }

                            loc.setText(location + " , " + postcode);


                            if (event_type.equals("fav")) {
                                if (getTickets_booked_by_user.equals("0") || getTickets_booked_by_user.equals("1")) {
                                    BusinessEvent_detailsFragment_book_button.setVisibility(View.VISIBLE);
                                    BusinessEvent_detailsFragment_book_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog(value);
                                        }
                                    });
                                }
                            }

                        }
                    } else if (response.body().getStatus().equals("401")) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.clear();
                        editor1.commit();
                        Toast.makeText(BusinessEventDetailAcitvity.this, "Business Logout", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor2 = profileupdatedPref.edit();
                        editor2.clear();
                        editor2.commit();

                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        Intent intent = new Intent(BusinessEventDetailAcitvity.this, Join_us.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(BusinessEventDetailAcitvity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RetroGetEventData> call, Throwable t) {
                Log.d("events1", "" + t.getMessage() + " " + value);
                if (NoInternet.isOnline(BusinessEventDetailAcitvity.this) == false) {
                    progressDialog.dismiss();
                    NoInternet.dialog(BusinessEventDetailAcitvity.this);
                }
                progressDialog.dismiss();
            }
        });
    }

    String removeLeadingZeroes(String s) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() > 0 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }

        return sb.toString();
    }

    public void dialog(final String value) {
        String[] ticketNum = new String[]{"1", "2"};

        final Dialog dialog = new Dialog(BusinessEventDetailAcitvity.this);
        dialog.setContentView(R.layout.custom_dialog_book_ticket);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        FindId(dialog);

        if (tickettypedialog.equals("Paid")) {
            dticketPrice.setText("0");
            ticketype_spinner.setVisibility(View.VISIBLE);
            dialogtickttype.setVisibility(View.GONE);
            //Todo: ticket type spinner
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BusinessEventDetailAcitvity.this, android.R.layout.simple_spinner_item, tickettypes);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ticketype_spinner.setAdapter(arrayAdapter);
            ticketype_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tickettypespinnerposintion = parent.getItemAtPosition(position).toString();
                    tickettypeposition = String.valueOf(position);

                    ticktprice = ticketprice.get(Integer.parseInt(tickettypeposition));

                    Float a = Float.valueOf((getSpinnerposition));
                    total_ticket = String.valueOf(a);

                    String tickt = ticktprice;
                    Float b = Float.valueOf((tickt));

                    Float total = a * b;

                    tot = String.valueOf(total);

                    dticketPrice.setText(ticktprice);
                    dtotalPrice.setText(tot);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        } else {
            dticketPrice.setText(ticktprice);
            tickettypespinnerposintion = "";
            ticketype_spinner.setVisibility(View.GONE);
            dialogtickttype.setVisibility(View.VISIBLE);

        }

        dialogtickttype.setText(ticktType);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BusinessEventDetailAcitvity.this,
                android.R.layout.simple_spinner_item, ticketNum);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        if (spinnerposition != null) {
            spinner.setSelection(Integer.parseInt(spinnerposition) - 1);
        }


        if (ticktprice.equals("0")) {

            dialogButoon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Float TotalTIcket = Float.valueOf(((total_ticket)));

                    Float RemainingTIckets = Float.valueOf((remaini_tickets));
                    String ticket = tickets_booked_by_user + ".0";
                    String total_ticket = String.valueOf(TotalTIcket);

                    if (TotalTIcket > RemainingTIckets) {
                        Toast.makeText(BusinessEventDetailAcitvity.this, "Not Available", Toast.LENGTH_SHORT).show();
                    } else {

                        if (ticket.equals(total_ticket)) {
                            freebookevent(dialog);
                        } else if (ticket.equals("0.0")) {
                            freebookevent(dialog);
                        } else {
                            Toast.makeText(BusinessEventDetailAcitvity.this, "You can buy one more ticket only", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });

        } else {
            dialogButoon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ticket = tickets_booked_by_user + ".0";

                    if (total_ticket.equals(ticket)) {
                        Intent intent = new Intent(BusinessEventDetailAcitvity.this, PayActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("total_price", tot);
                        intent.putExtra("event_id", value);
                        intent.putExtra("event_Title", title);
                        intent.putExtra("total_tickets", total_ticket);
                        intent.putExtra("ticket_Price", ticktprice);
                        intent.putExtra("imagesend", "BUA");
                        intent.putExtra("tickettype", tickettypespinnerposintion);
                        startActivity(intent);
                        dialog.dismiss();
                    } else if (ticket.equals("0.0")) {
                        Intent intent = new Intent(BusinessEventDetailAcitvity.this, PayActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("total_price", tot);
                        intent.putExtra("event_id", value);
                        intent.putExtra("event_Title", title);
                        intent.putExtra("total_tickets", total_ticket);
                        intent.putExtra("imagesend", "BUA");
                        intent.putExtra("ticket_Price", ticktprice);
                        intent.putExtra("tickettype", tickettypespinnerposintion);
                        startActivity(intent);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(BusinessEventDetailAcitvity.this, "You can buy one more ticket only", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }

        dialog.show();
    }


    private void freebookevent(final Dialog dialog) {
        progressDialog.show();
        Call<BookFreeEvents> call = WebAPI.getInstance().getApi().freebookevent("Bearer " + S_Token, value, total_ticket);
        call.enqueue(new Callback<BookFreeEvents>() {
            @Override
            public void onResponse(Call<BookFreeEvents> call, Response<BookFreeEvents> response) {

                if (response.body() != null) {
                    progressDialog.dismiss();
                    dialog.dismiss();

                    Intent intent = new Intent(BusinessEventDetailAcitvity.this, ConfirmationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("eventID", value);
                    intent.putExtra("paid", value);
                    editor.putString("eventImage", image.get(0));
                    editor.apply();
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(BusinessEventDetailAcitvity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<BookFreeEvents> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(BusinessEventDetailAcitvity.this, "Book Error=> " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void FindId(Dialog dialog) {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        dticketPrice = dialog.findViewById(R.id.dailog_ticket_price);
        dtotalPrice = dialog.findViewById(R.id.dailog_total_price);
        spinner = dialog.findViewById(R.id.dailog_spinner);
        ticketype_spinner = dialog.findViewById(R.id.ticketype_spinner);
        dialogButoon = dialog.findViewById(R.id.dailog_button);
        dialogtickttype = dialog.findViewById(R.id.dailog_ticket_type);
    }

    private void listerners() {

        backon_b_eventdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        checkInGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (event_type.equals("live")) {
                    Intent intent = new Intent(BusinessEventDetailAcitvity.this, CheckGuestActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("value", value);
                    intent.putExtra("eventType", event_type);
                    startActivity(intent);
                } else if (event_type.equals("history")) {

                    Intent intent = new Intent(BusinessEventDetailAcitvity.this, Add_Event_Activity.class);
                    intent.putExtra("editevent", "republish");
                    intent.putExtra("value", value);
                    startActivity(intent);
                }

            }
        });

        see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BusinessEventDetailAcitvity.this, SeeAll_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("value", id);
                startActivity(intent);

            }
        });

        BusinessEvent_detailsFragment_book_link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(link1);
                if (uri.getPath().contains("https://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Uri uri1 = Uri.parse("https://" + uri);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri1);
                    startActivity(intent);
                }

            }
        });

        BusinessEvent_detailsFragment_book_link2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(link2);
                if (uri.getPath().contains("https://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Uri uri1 = Uri.parse("https://" + uri);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri1);
                    startActivity(intent);
                }
            }
        });

        BusinessEvent_detailsFragment_book_link3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(link3);
                if (uri.getPath().contains("https://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Uri uri1 = Uri.parse("https://" + uri);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri1);
                    startActivity(intent);
                }
            }
        });


    }

    private void OpenEventChat(String s, String id) {

        final Intent intent = new Intent(BusinessEventDetailAcitvity.this, ChatBoxActivity.class);
        intent.putExtra("event_title", title);
        intent.putExtra("event_image", image.get(0));
        intent.putExtra("eventID", id);
        intent.putExtra("event_host_user", hostUserID);


        Call<EventMessageClick> call = WebAPI.getInstance().getApi().eventMEsgClick("Bearer " + s, id);
        call.enqueue(new Callback<EventMessageClick>() {
            @Override
            public void onResponse(Call<EventMessageClick> call, Response<EventMessageClick> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {

                        startActivity(intent);
                    }

                } else {
                    Intent intent = new Intent(BusinessEventDetailAcitvity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<EventMessageClick> call, Throwable t) {
                Toast.makeText(BusinessEventDetailAcitvity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        BusinessEvent_detailsFragment_book_time2 = findViewById(R.id.BusinessEvent_detailsFragment_book_time2);
        BusinessEvent_detailsFragment_book_button = findViewById(R.id.BusinessEvent_detailsFragment_book_button);
        ticketview = findViewById(R.id.ticketview);
        tickettype1 = findViewById(R.id.tickettype1);
        tickettype2 = findViewById(R.id.tickettype2);
        tickettype3 = findViewById(R.id.tickettype3);
        revenue = findViewById(R.id.revenue);
        eventid = findViewById(R.id.eventid);
        event_price = findViewById(R.id.event_price);
        editevent = findViewById(R.id.editevent);
        eventdetail_favbt = findViewById(R.id.eventdetail_favbt);
        dotsLayout = findViewById(R.id.BusinessEvent_detailsFragment_book_indicator);
        checkInGuest = findViewById(R.id.BusinessEvent_detailsFragment_book_event_book_bt);
        category = findViewById(R.id.BusinessEvent_detailsFragment_book_categry);
        descri = findViewById(R.id.BusinessEvent_detailsFragment_book_description);
        datetime = findViewById(R.id.BusinessEvent_detailsFragment_book_date);
        loc = findViewById(R.id.BusinessEvent_detailsFragment_book_location);
        viewPager = findViewById(R.id.BusinessEvent_detailsFragment_book_viewpager);
        BusinessEvent_detailsFragment_book_time = findViewById(R.id.BusinessEvent_detailsFragment_book_time);
        publish = findViewById(R.id.publish_on_eventsdetails);
        linearLayout = findViewById(R.id.bulletin_layout);
        peoplecoming = findViewById(R.id.BusinessEvent_detailsFragment_book_attendess_people);
        users = findViewById(R.id.BusinessEvent_detailsFragment_book_recyclerView);
        ticketPrice = findViewById(R.id.BusinessEvent_detailsFragment_book_price);
        ticketPrice1 = findViewById(R.id.BusinessEvent_detailsFragment_book_price1);
        ticketPrice2 = findViewById(R.id.BusinessEvent_detailsFragment_book_price2);
        numberofTickets = findViewById(R.id.BusinessEvent_detailsFragment_book_ticket_number);
        totalPrice = findViewById(R.id.BusinessEvent_detailsFragment_book_total_price);
        totalPrice1 = findViewById(R.id.BusinessEvent_detailsFragment_book_total_price1);
        totalPrice2 = findViewById(R.id.BusinessEvent_detailsFragment_book_total_price2);
        priceLayput = findViewById(R.id.pricelayout);
        event_title = findViewById(R.id.BusinessEvent_detailsFragment_book_tittle);
        freetext = findViewById(R.id.freeText);
        see_all = findViewById(R.id.BusinessEvent_detailsFragment_book_seeAll);
        bannerImage = findViewById(R.id.BusinessEvent_detailsFragment_book_bulletin);
        BusinessEvent_detailsFragment_book_link1 = findViewById(R.id.BusinessEvent_detailsFragment_book_link1);
        BusinessEvent_detailsFragment_book_link2 = findViewById(R.id.BusinessEvent_detailsFragment_book_link2);
        BusinessEvent_detailsFragment_book_link3 = findViewById(R.id.BusinessEvent_detailsFragment_book_link3);
        seemessagesforthisevent = findViewById(R.id.seemessagesforthisevent);
        backon_b_eventdetail = findViewById(R.id.backon_b_eventdetail);
        priceLayput1 = findViewById(R.id.pricelayout1);
        priceLayput2 = findViewById(R.id.pricelayout2);
        numberofTickets1 = findViewById(R.id.BusinessEvent_detailsFragment_book_ticket_number1);
        numberofTickets2 = findViewById(R.id.BusinessEvent_detailsFragment_book_ticket_number2);
    }

    //add dots at bottom
    private void addBottomDots(int currentPage) {

        dots = new TextView[image.size()];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(BusinessEventDetailAcitvity.this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(50);
            dots[i].setTextColor(getResources().getColor(R.color.colortextwhite));
            dotsLayout.addView(dots[i]);

        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(getResources().getColor(R.color.colorTheme));
        }

    }

    public void addFav(final String value) {

        eventdetail_favbt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if (!fav_id.contains("1")) {
                        Call<NormalRetroFav> call = WebAPI.getInstance().getApi().fav("Bearer " + S_Token, "application/json", value);
                        call.enqueue(new Callback<NormalRetroFav>() {
                            @Override
                            public void onResponse(Call<NormalRetroFav> call, Response<NormalRetroFav> response) {
                                if (response.body() != null) {

                                }
                                Toast.makeText(BusinessEventDetailAcitvity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<NormalRetroFav> call, Throwable t) {

                            }
                        });
                    }

                } else {
                    Call<NormalRetrodeleteFav> call = WebAPI.getInstance().getApi().deletefav("Bearer " + S_Token, "application/json", value);
                    call.enqueue(new Callback<NormalRetrodeleteFav>() {
                        @Override
                        public void onResponse(Call<NormalRetrodeleteFav> call, Response<NormalRetrodeleteFav> response) {
                            if (response.body() != null) {

                            }
                            Toast.makeText(BusinessEventDetailAcitvity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<NormalRetrodeleteFav> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    //check remainig tickets
    private void getRaminingEvents(String s_token, String id) {

        Call<RemainingTickets> call = WebAPI.getInstance().getApi().remainingTickets("Bearer " + s_token, id);
        call.enqueue(new Callback<RemainingTickets>() {
            @Override
            public void onResponse(Call<RemainingTickets> call, Response<RemainingTickets> response) {

                if (response.body() != null) {
                    String totalticket = String.valueOf(response.body().getData().getTotal());
                    String bookedtciket = String.valueOf(response.body().getData().getBooked());
                    remaini_tickets = String.valueOf(response.body().getData().getRemaining());
                    if (bookedtciket.equals("null")) {
                        bookedtciket = "0";
                        ticketview.setText(" " + "(" + "0" + "/" + totalticket + ")");
                    } else {
                        ticketview.setText(" " + "(" + bookedtciket + "/" + totalticket + ")");
                    }


                } else {
                    Intent intent = new Intent(BusinessEventDetailAcitvity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RemainingTickets> call, Throwable t) {
                Toast.makeText(BusinessEventDetailAcitvity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerposition = parent.getItemAtPosition(position).toString();

        getSpinnerposition = spinnerposition;


        Float a = Float.valueOf((spinnerposition));
        total_ticket = String.valueOf(a);

        String tickt = dticketPrice.getText().toString();
        Float b = Float.valueOf((tickt));

        Float total = a * b;

        tot = String.valueOf(total);

        dtotalPrice.setText(tot);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
