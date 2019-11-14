package com.mandywebdesign.impromptu.BusinessRegisterLogin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.Join_us;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessEventDetailAcitvity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    ImageButton backon_b_eventdetail;
    View view;
    RecyclerView users;
    TextView category, event_price, datetime, loc, BusinessEvent_detailsFragment_book_time, ticketPrice, numberofTickets, totalPrice, freetext, event_title, see_all,
            BusinessEvent_detailsFragment_book_link1, BusinessEvent_detailsFragment_book_link2, BusinessEvent_detailsFragment_book_link3;
    public TextView peoplecoming, revenue;
    ReadMoreTextView descri;
    FragmentManager manager;
    Button checkInGuest, publish, edit;
    GoogleSignInAccount account;
    LinearLayout linearLayout, priceLayput;
    boolean loggedOut;
    SharedPreferences sharedPreferences, sharedPreferences1, profileupdatedPref;
    String user, fav_id;
    RoundedImageView bannerImage;
    public static String value, event_type, otherEvnts, formattedDate, getFormattedDate;
    Dialog progressDialog;
    ViewPager viewPager;
    ImageView editevent;
    PagerAdapter pagerAdapter;
    public static ArrayList<String> userImage = new ArrayList<>();
    public static ArrayList<String> user_id = new ArrayList<>();
    public static ArrayList<String> userName = new ArrayList<>();
    String BToken, S_Token, attendess, ticktprice, link1, link2, link3;
    ArrayList<String> image = new ArrayList<>();
    static String id, cate, hostImage, hostUserID, decs, postcode, ticktType, timefrom, timeto, title, location, location2, city, gender, andendeenumber, bookedtickets, numberoftickts, freeEvent, username, timeFrom, timeTo;
    int CurrentPage = 0;
    CheckBox eventdetail_favbt;
    TextView ticketview;
    Button seemessagesforthisevent;
    Intent intent;
    String from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_event_detail_acitvity);

        account = GoogleSignIn.getLastSignedInAccount(this);
        loggedOut = AccessToken.getCurrentAccessToken() == null;
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        user = "Bearer " + sharedPreferences.getString("Usertoken", "");
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");
        sharedPreferences1 = getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        profileupdatedPref = getSharedPreferences("profileupdated", Context.MODE_PRIVATE);


        progressDialog = ProgressBarClass.showProgressDialog(this);
        Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        intent = getIntent();
        if (intent !=null)
        {
            from = intent.getStringExtra("from");
        }

        init();
        listerners();

        checkEventtype();
        gotomessagebox(id);

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
                } else if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
                    checkInGuest.setText("Relist");

                }
            } else if (event_type.equals("fav")) {

                if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
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

                if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
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
            } else {
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
                        String message = "https://play.google.com/store";
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, message);
                        startActivity(Intent.createChooser(share, "Testing Impromptu"));
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
                        attendess = response.body().getData().get(0).getTotalTickets().toString();
                        numberofTickets.setText(attendess);
                        ticktprice = response.body().getData().get(0).getAmount();

                        Float TicketPrice = Float.valueOf((ticktprice));
                        Float TotalAttendess = Float.valueOf((attendess));

                        Float totalprice = TicketPrice * TotalAttendess;
                        totalPrice.setText("£ " + String.valueOf(totalprice));

                    } else if (response.body().getStatus().equals("400")) {
                        numberofTickets.setText("0");
                        attendess = String.valueOf(0);
                        totalPrice.setText("£ 0");
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

                    if (response.body().getStatus().equals("200")) {
                        for (int i = 0; i < bookedUsersList.size(); i++) {
                            userImage.add(response.body().getData().get(i).getFile());
                            user_id.add(response.body().getData().get(i).getUserid().toString());
                            userName.add(response.body().getData().get(i).getUsername());

                            Log.d("userImage", "" + response.body().getData().get(i).getFile());

                        }
                        if (event_type.equals("past")) {
                            if (bookedUsersList.size() == 1) {
                                peoplecoming.setText("1 person attended");
                            } else if (bookedUsersList.size() >= 2) {
                                peoplecoming.setText(bookedUsersList.size() + " person attended");

                            } else {
                                peoplecoming.setText("No one booked this event yet");
                            }
                        } else {
                            if (bookedUsersList.size() == 1) {
                                peoplecoming.setText("1 Person is coming");
                            } else if (bookedUsersList.size() >= 2) {
                                peoplecoming.setText(bookedUsersList.size() + " People are coming");

                            } else {
                                peoplecoming.setText("No one booked this event yet");
                            }
                        }
                        Booked_users adapter = new Booked_users(BusinessEventDetailAcitvity.this, userImage,user_id);
                        users.setAdapter(adapter);

                    } else if (response.body().getStatus().equals("400")) {
                        peoplecoming.setText("0 People coming");
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
                            ticktType = datum.getTicketType();
                            if (datum.getPrice()!=null)
                            {
                                ticktprice = datum.getPrice();
                            }else {
                                ticktprice="Paid";
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
                            String time_t = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM", "am").replaceFirst("PM", "pm");
                            String time_to = Util.convertTimeStampToTime(Long.parseLong(datum.getEventEndDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM", "am").replaceFirst("PM", "pm");
                            String start_date = Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt()));
                            String end_date = Util.convertTimeStampDate(Long.parseLong(datum.getEventEndDt()));
                            if (start_date.matches(end_date)) {
                                datetime.setText(start_date);
                            } else {
                                datetime.setText(start_date + " - " + end_date);
                            }


                            if (time_t.startsWith("0") && time_to.startsWith("0")) {
                                timeFrom = time_t.substring(1);
                                timeTo = time_to.substring(1);
                                BusinessEvent_detailsFragment_book_time.setText(timeFrom + " - " + timeTo);
                            } else if (time_t.startsWith("0")) {
                                timeFrom = time_t.substring(1);
                                if (time_to.startsWith("0")) {
                                    timeTo = time_to.substring(1);
                                    BusinessEvent_detailsFragment_book_time.setText(timeFrom + " - " + timeTo);
                                } else {
                                    timeTo = time_to.substring(0);
                                    BusinessEvent_detailsFragment_book_time.setText(timeFrom + " - " + timeTo);
                                }
                            } else if (time_to.startsWith("0")) {
                                timeTo = time_to.substring(1);
                                if (time_t.startsWith("0")) {
                                    timeFrom = time_t.substring(1);
                                    BusinessEvent_detailsFragment_book_time.setText(timeFrom + " - " + timeTo);
                                } else {
                                    timeFrom = time_t.substring(0);
                                    BusinessEvent_detailsFragment_book_time.setText(timeFrom + " - " + timeTo);
                                }
                            } else if (!time_t.startsWith("0") && !time_to.startsWith("0")) {
                                timeFrom = time_t.substring(0);
                                timeTo = time_to.substring(0);
                                BusinessEvent_detailsFragment_book_time.setText(timeFrom + " - " + timeTo);
                            }


                            String imageurl = image.get(0).toString();
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
                                }else if (ticktprice.equals("Paid"))
                                {
                                    event_price.setText(ticktprice);

                                }else {
                                    event_price.setText("£ " + ticktprice);
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
                            if (ticktprice.equals("")) {
                                ticketPrice.setText("£ 0");
                                totalPrice.setText("£ 0");


                            } else {
                                ticketPrice.setText("£ " + ticktprice);
                            }
                            loc.setText(location + " , " + postcode);

                            getTotalTickets(token, value);

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


    private void listerners() {

        backon_b_eventdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                Intent intent = new Intent(BusinessEventDetailAcitvity.this, Home_Screen.class);
//                intent.putExtra("eventType", event_type);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent);
                onBackPressed();
            }
        });

        checkInGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (event_type.equals("live"))
               {
                   Intent intent = new Intent(BusinessEventDetailAcitvity.this, CheckGuestActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   intent.putExtra("value", value);
                   intent.putExtra("eventType", event_type);
                   startActivity(intent);
               }else if (event_type.equals("history")){

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

        ticketview = findViewById(R.id.ticketview);
        revenue = findViewById(R.id.revenue);
        event_price = findViewById(R.id.event_price);
        editevent = findViewById(R.id.editevent);
        eventdetail_favbt = findViewById(R.id.eventdetail_favbt);
        dotsLayout = (LinearLayout) findViewById(R.id.BusinessEvent_detailsFragment_book_indicator);
        checkInGuest = (Button) findViewById(R.id.BusinessEvent_detailsFragment_book_event_book_bt);
        category = (TextView) findViewById(R.id.BusinessEvent_detailsFragment_book_categry);
        descri = (ReadMoreTextView) findViewById(R.id.BusinessEvent_detailsFragment_book_description);
        datetime = (TextView) findViewById(R.id.BusinessEvent_detailsFragment_book_date);
        loc = (TextView) findViewById(R.id.BusinessEvent_detailsFragment_book_location);
        viewPager = (ViewPager) findViewById(R.id.BusinessEvent_detailsFragment_book_viewpager);
        BusinessEvent_detailsFragment_book_time = findViewById(R.id.BusinessEvent_detailsFragment_book_time);
        publish = (Button) findViewById(R.id.publish_on_eventsdetails);
        linearLayout = (LinearLayout) findViewById(R.id.bulletin_layout);
        peoplecoming = (TextView) findViewById(R.id.BusinessEvent_detailsFragment_book_attendess_people);
        users = (RecyclerView) findViewById(R.id.BusinessEvent_detailsFragment_book_recyclerView);
        ticketPrice = (TextView) findViewById(R.id.BusinessEvent_detailsFragment_book_price);
        numberofTickets = (TextView) findViewById(R.id.BusinessEvent_detailsFragment_book_ticket_number);
        totalPrice = (TextView) findViewById(R.id.BusinessEvent_detailsFragment_book_total_price);
        priceLayput = (LinearLayout) findViewById(R.id.pricelayout);
        event_title = (TextView) findViewById(R.id.BusinessEvent_detailsFragment_book_tittle);
        freetext = (TextView) findViewById(R.id.freeText);
        see_all = (TextView) findViewById(R.id.BusinessEvent_detailsFragment_book_seeAll);
        bannerImage = (RoundedImageView) findViewById(R.id.BusinessEvent_detailsFragment_book_bulletin);
        BusinessEvent_detailsFragment_book_link1 = (TextView) findViewById(R.id.BusinessEvent_detailsFragment_book_link1);
        BusinessEvent_detailsFragment_book_link2 = (TextView) findViewById(R.id.BusinessEvent_detailsFragment_book_link2);
        BusinessEvent_detailsFragment_book_link3 = (TextView) findViewById(R.id.BusinessEvent_detailsFragment_book_link3);
        seemessagesforthisevent = findViewById(R.id.seemessagesforthisevent);
        backon_b_eventdetail = findViewById(R.id.backon_b_eventdetail);
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
}
