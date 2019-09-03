package com.mandywebdesign.impromptu.BusinessRegisterLogin;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Adapters.B_EventDetailImageAdapter;
import com.mandywebdesign.impromptu.Adapters.Booked_users;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AddEvents.Add_Event_Activity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Events;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Home;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.HostingTabs.Drafts;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.MyEventsFragments.Hosting;
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
import com.mandywebdesign.impromptu.messages.ChatBox;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.Join_us;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;

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
public class BusinessEvent_detailsFragment extends Fragment {

    private LinearLayout dotsLayout;
    private TextView[] dots;
    View view;
    RecyclerView users;
    TextView category, event_price, datetime, loc, BusinessEvent_detailsFragment_book_time, ticketPrice, numberofTickets, totalPrice, freetext, event_title, see_all,
            BusinessEvent_detailsFragment_book_link1, BusinessEvent_detailsFragment_book_link2, BusinessEvent_detailsFragment_book_link3;
    public TextView peoplecoming;
    ReadMoreTextView descri;
    FragmentManager manager;
    Button checkInGuest, publish, edit;
    GoogleSignInAccount account;
    LinearLayout linearLayout, priceLayput;
    boolean loggedOut;
    SharedPreferences sharedPreferences, sharedPreferences1, profileupdatedPref;
    String user, fav_id;
    RoundedImageView bannerImage;
    Bundle bundle;
    public static String value, event_type, otherEvnts, formattedDate, getFormattedDate;
    ProgressDialog progressDialog;
    ViewPager viewPager;
    ImageView editevent;
    PagerAdapter pagerAdapter;
    public static ArrayList<String> userImage = new ArrayList<>();
    public static ArrayList<String> userName = new ArrayList<>();
    String BToken, S_Token, attendess, ticktprice, link1, link2, link3;
    ArrayList<String> image = new ArrayList<>();
    static String id, cate, hostImage, hostUserID, decs, postcode, ticktType, timefrom, timeto, title, location, location2, city, gender, andendeenumber, numberoftickts, freeEvent, username, timeFrom, timeTo;
    int CurrentPage = 0;
    CheckBox eventdetail_favbt;
    TextView seemessagesforthisevent;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_business_event_details, container, false);
        manager = getActivity().getSupportFragmentManager();

        account = GoogleSignIn.getLastSignedInAccount(getContext());
        loggedOut = AccessToken.getCurrentAccessToken() == null;
        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        user = "Bearer " + sharedPreferences.getString("Usertoken", "");
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");
        sharedPreferences1 = getActivity().getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        profileupdatedPref = getContext().getSharedPreferences("profileupdated", Context.MODE_PRIVATE);


        progressDialog = new ProgressDialog(getActivity());
        Drawable drawable = new ProgressBar(getContext()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(drawable);

        progressDialog.setMessage("Please wait until we fetch your events");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        init();
        listerners();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        users.setLayoutManager(layoutManager);
        users.setNestedScrollingEnabled(false);

        Toolbar toolbar = view.findViewById(R.id.BusinessEvent_detailsFragment_book_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().onBackPressed();


                if (!BToken.equals("")) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("eventType", event_type);

                    Hosting attending = new Hosting();
                    attending.setArguments(bundle1);


                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.home_frame_layout, attending);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    Home_Screen.countt = 0;
                } else if (!S_Token.equalsIgnoreCase("")) {

                    Bundle bundle = new Bundle();
                    bundle.putString("eventType", event_type);
                    bundle.putString("other_events", otherEvnts);

                    Events events = new Events();
                    events.setArguments(bundle);


                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.home_frame_layout, events);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    Home_Screen.countt = 0;


                }
            }
        });

        bundle = getArguments();


        if (bundle != null) {

            value = bundle.getString("event_id");
            event_type = bundle.getString("eventType");
            otherEvnts = bundle.getString("other_events");

            if (event_type.equals("draft")) {

                linearLayout.setVisibility(View.GONE);
                publish.setVisibility(View.VISIBLE);
                editevent.setImageDrawable(getResources().getDrawable(R.drawable.edit));
                eventdetail_favbt.setVisibility(View.GONE);

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
                } else if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
                    addFav(value);
                }
            } else if (event_type.equals("history")) {
                if (!BToken.equalsIgnoreCase("")) {
                    getEventdata(BToken, value);
                    getUsers(BToken, value);
                } else if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);

                }
            } else if (event_type.equals("fav")) {
                if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
                    if (!otherEvnts.equalsIgnoreCase("")) {
                        linearLayout.setVisibility(View.GONE);
                    }

                }
            } else if (event_type.equals("upcoming")) {
                if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
                    linearLayout.setVisibility(View.GONE);
                }
            } else if (event_type.equals("past")) {
                if (!S_Token.equalsIgnoreCase("")) {
                    getEventdata(S_Token, value);
                    getUsers(S_Token, value);
                    linearLayout.setVisibility(View.GONE);
                }
            }

            editevent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Add_Event_Activity.class);
                    intent.putExtra("editevent", "edit");
                    intent.putExtra("value", value);
//                        editor.putString("editimages",image.);
                    startActivity(intent);
                }
            });
        }

        return view;
    }

    private void postDraft(String s_token) {

        Call<RetroPostDraft> call = WebAPI.getInstance().getApi().postDraft(s_token, "application/json", value);
        call.enqueue(new Callback<RetroPostDraft>() {
            @Override
            public void onResponse(Call<RetroPostDraft> call, Response<RetroPostDraft> response) {

                if (response.body() != null) {
                    Intent intent = new Intent(getContext(), Home_Screen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                    Toast.makeText(getContext(), "Published", Toast.LENGTH_SHORT).show();
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

    private void getRaminingEvents(String s_token, String id) {

        Call<RemainingTickets> call = WebAPI.getInstance().getApi().remainingTickets("Bearer " + s_token, id);
        call.enqueue(new Callback<RemainingTickets>() {
            @Override
            public void onResponse(Call<RemainingTickets> call, Response<RemainingTickets> response) {

                String tickets = String.valueOf(response.body().getData());
                //Toast.makeText(getContext(), ""+tickets, Toast.LENGTH_SHORT).show();
                //   remainingTicketTV.setText("Tickets Remaining "+tickets);
            }

            @Override
            public void onFailure(Call<RemainingTickets> call, Throwable t) {

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
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
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

                    if (response.body().getStatus().equals("200")) {
                        for (int i = 0; i < bookedUsersList.size(); i++) {
                            userImage.add(response.body().getData().get(i).getFile());
                            userName.add(response.body().getData().get(i).getUsername());

                            Log.d("userImage", "" + response.body().getData().get(i).getFile());

                        }
                        peoplecoming.setText(bookedUsersList.size() + " People are coming");

                    } else {
                        peoplecoming.setText("0 People coming");
                        users.setVisibility(View.GONE);
                        see_all.setVisibility(View.GONE);
                    }


                    Booked_users adapter = new Booked_users(getContext(), userImage);
                    users.setAdapter(adapter);
                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Booked_User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                            ticktprice = datum.getPrice();
                            numberoftickts = datum.getNoOfTickets();
                            username = datum.getBEventHostname();
                            link1 = datum.getLink1();
                            link2 = datum.getLink2();
                            link3 = datum.getLink3();

                            String dateformat = datum.getDate();
                            /*to change server date formate*/
                            String s1 = dateformat;
                            String[] str = s1.split("/");
                            String str1 = str[0];
                            String str2 = str[1];
                            String str3 = str[2];

                            title = datum.getTitle();
                            hostImage = datum.getHostImage();
                            hostUserID = datum.getUserid().toString();
                            timefrom = datum.getTimeFrom();
                            timeto = datum.getTimeTo();
                            image = (ArrayList<String>) datum.getFile().get(0).getImg();
                            fav_id = datum.getFavourite().toString();

                            //get Time to in AM PM
                            String time_t = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM","am").replaceFirst("PM","pm");
                            String time_to = Util.convertTimeStampToTime(Long.parseLong(datum.getEventEndDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM","am").replaceFirst("PM","pm");
                            String end_date = Util.convertTimeStampDate(Long.parseLong(datum.getEventEndDt()));


                            if (time_t.startsWith("0") && time_to.startsWith("0")) {
                                timeFrom = time_t.substring(1);
                                timeTo = time_to.substring(1);
                                BusinessEvent_detailsFragment_book_time.setText(timeFrom + " - " + timeTo);
                                datetime.setText(str2 + "/" + str1 + "/" + str3 + " - " + end_date );
                            } else if (time_t.startsWith("0")) {
                                timeFrom = time_t.substring(1);
                                if (time_to.startsWith("0")) {
                                    timeTo = time_to.substring(1);
                                    BusinessEvent_detailsFragment_book_time.setText(timeFrom + " - " + timeTo);
                                    datetime.setText(str2 + "/" + str1 + "/" + str3 + " - " + end_date );
                                } else {
                                    timeTo = time_to.substring(0);
                                    BusinessEvent_detailsFragment_book_time.setText(timeFrom + " - " + timeTo);
                                    datetime.setText(str2 + "/" + str1 + "/" + str3 + " - " + end_date );
                                }
                            } else if (time_to.startsWith("0")) {
                                timeTo = time_to.substring(1);
                                if (time_t.startsWith("0")) {
                                    timeFrom = time_t.substring(1);
                                    BusinessEvent_detailsFragment_book_time.setText(timeFrom + " - " + timeTo);
                                    datetime.setText(str2 + "/" + str1 + "/" + str3 + " - " + end_date );
                                } else {
                                    timeFrom = time_t.substring(0);
                                    BusinessEvent_detailsFragment_book_time.setText(timeFrom + " - " + timeTo);
                                    datetime.setText(str2 + "/" + str1 + "/" + str3 + " - " + end_date );
                                }
                            } else if (!time_t.startsWith("0") && !time_to.startsWith("0")) {
                                timeFrom = time_t.substring(0);
                                timeTo = time_to.substring(0);
                                BusinessEvent_detailsFragment_book_time.setText(timeFrom + " - " + timeTo);
                                datetime.setText(str2 + "/" + str1 + "/" + str3 + " - " + end_date );
                            }


                            String imageurl = image.get(0).toString();
                            Glide.with(getContext()).load(imageurl).into(bannerImage);
//                        Picasso.get().load(imageurl).resize(110,110).into(bannerImage);

                            //Check Event Fav Status
                            if (fav_id.equals("1")) {
                                eventdetail_favbt.setChecked(true);
                            } else if (fav_id.equals("0")) {
                                eventdetail_favbt.setChecked(false);
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
                            } else {
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
                        Toast.makeText(getContext(), "Business Logout", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor2 = profileupdatedPref.edit();
                        editor2.clear();
                        editor2.commit();

                        progressDialog.setMessage("login in another device");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        Intent intent = new Intent(getActivity(), Join_us.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RetroGetEventData> call, Throwable t) {
                Log.d("events1", "" + t.getMessage() + " " + value);
                if (NoInternet.isOnline(getContext()) == false) {
                    progressDialog.dismiss();
                    NoInternet.dialog(getContext());
                }
                progressDialog.dismiss();
            }
        });
    }


    private void listerners() {

        checkInGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("value", value);
                bundle.putString("eventType", event_type);

                CheckGuestFragment checkGuestFragment = new CheckGuestFragment();
                checkGuestFragment.setArguments(bundle);

                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_frame_layout, checkGuestFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("value", id);

                See_all see_all = new See_all();
                see_all.setArguments(bundle);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_frame_layout, see_all);
                transaction.addToBackStack(null);
                transaction.commit();
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

    private void OpenEventChat(String s, String id) {

        Bundle bundle = new Bundle();
        bundle.putString("event_title", title);
        bundle.putString("event_image", hostImage);
        bundle.putString("eventID", value);
        bundle.putString("event_host_user", hostUserID);

        final ChatBox chatBox = new ChatBox();
        chatBox.setArguments(bundle);

        Call<EventMessageClick> call = WebAPI.getInstance().getApi().eventMEsgClick("Bearer " + s, id);
        call.enqueue(new Callback<EventMessageClick>() {
            @Override
            public void onResponse(Call<EventMessageClick> call, Response<EventMessageClick> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {
                        FragmentTransaction transaction = manager.beginTransaction();
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

    private void init() {

        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        event_price = view.findViewById(R.id.event_price);
        editevent = view.findViewById(R.id.editevent);
        eventdetail_favbt = view.findViewById(R.id.eventdetail_favbt);
        dotsLayout = (LinearLayout) view.findViewById(R.id.BusinessEvent_detailsFragment_book_indicator);
        checkInGuest = (Button) view.findViewById(R.id.BusinessEvent_detailsFragment_book_event_book_bt);
        category = (TextView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_categry);
        descri = (ReadMoreTextView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_description);
        datetime = (TextView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_date);
        loc = (TextView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_location);
        viewPager = (ViewPager) view.findViewById(R.id.BusinessEvent_detailsFragment_book_viewpager);
        BusinessEvent_detailsFragment_book_time = view.findViewById(R.id.BusinessEvent_detailsFragment_book_time);
        publish = (Button) view.findViewById(R.id.publish_on_eventsdetails);
        linearLayout = (LinearLayout) view.findViewById(R.id.bulletin_layout);
        peoplecoming = (TextView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_attendess_people);
        users = (RecyclerView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_recyclerView);
        ticketPrice = (TextView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_price);
        numberofTickets = (TextView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_ticket_number);
        totalPrice = (TextView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_total_price);
        priceLayput = (LinearLayout) view.findViewById(R.id.pricelayout);
        event_title = (TextView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_tittle);
        freetext = (TextView) view.findViewById(R.id.freeText);
        see_all = (TextView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_seeAll);
        bannerImage = (RoundedImageView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_bulletin);
        BusinessEvent_detailsFragment_book_link1 = (TextView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_link1);
        BusinessEvent_detailsFragment_book_link2 = (TextView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_link2);
        BusinessEvent_detailsFragment_book_link3 = (TextView) view.findViewById(R.id.BusinessEvent_detailsFragment_book_link3);
        seemessagesforthisevent = view.findViewById(R.id.seemessagesforthisevent);
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
                                Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<NormalRetrodeleteFav> call, Throwable t) {

                        }
                    });
                }
            }
        });

    }
}
