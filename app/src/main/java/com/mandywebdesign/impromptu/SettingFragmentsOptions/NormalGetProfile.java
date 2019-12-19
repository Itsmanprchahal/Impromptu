package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Adapters.Business_History_adapter;
import com.mandywebdesign.impromptu.Adapters.NormalUSerSetQues_answer;
import com.mandywebdesign.impromptu.Adapters.NormalUserAttendingEvents;
import com.mandywebdesign.impromptu.Adapters.NormalUserLiveEvents;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessAdapter.UsersLiveEventsAdapter;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessAdapter.UsersPastBookedEventsAdapter;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessAdapter.UsersPastEventsAdapter;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessUserPRofileActivity;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.Normal_past_booked;
import com.mandywebdesign.impromptu.Retrofit.RetroHistoryEvents;
import com.mandywebdesign.impromptu.Retrofit.RetroLiveEvents;
import com.mandywebdesign.impromptu.Retrofit.UsersBookedPastEvent;
import com.mandywebdesign.impromptu.Retrofit.UsersLiveEvent;
import com.mandywebdesign.impromptu.Retrofit.UsersPastEvent;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NormalGetProfile extends AppCompatActivity {

    TextView user_profile_age, username, status;
    RoundedImageView userImage;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView questionRecycler, hostRecycler, eventsAttendingRecycler;
    Toolbar toolbar;
    TextView totalpoints;
    ImageView back;
    TextView normal_user_gender;
    String userToken, BToken;
    ImageView editprofile;
    Dialog progressDialog;
    FragmentManager manager;
    TextView totlaEvents, pastEvents, user_profile_Event, user_profile_Event_attend;
    public static String s_username, s_image, getS_username = "", getProfileStatus = "", getNormalUserImage, getUserImage, getgender;
    public static ArrayList<String> images = new ArrayList<>();
    public static ArrayList<String> eventTitle = new ArrayList<>();
    public static ArrayList<String> liveevent_id = new ArrayList<>();
    public static ArrayList<String> attendingimage = new ArrayList<>();
    public static ArrayList<String> attentingTietle = new ArrayList<>();
    public static ArrayList<String> attentingevent_id = new ArrayList<>();
    public static ArrayList<String> Questions = new ArrayList<>();
    public static ArrayList<String> Answer = new ArrayList<>();
    public static ArrayList<String> QA_id = new ArrayList<>();
    static String prfileAge;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_get_profile);

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("Socailtoken", "");
        BToken = sharedPreferences.getString("Usertoken", "");
        s_username = sharedPreferences.getString("Social_username", "");
        s_image = sharedPreferences.getString("Social_image", "");


        progressDialog = ProgressBarClass.showProgressDialog(this);
        Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);

        progressDialog.show();
        init();
        listeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        intent = getIntent();

        if (intent != null) {
            String userid = intent.getStringExtra("user_id");

            if (userid != null) {
                if (!userToken.equals("")) {
                    getProfile(userToken, userid);
                    getUsersLiveEvents(userid);
                    getUsersattendingEvents(userid);
                    editprofile.setVisibility(View.GONE);
                    user_profile_Event.setVisibility(View.VISIBLE);
                    pastEvents.setVisibility(View.VISIBLE);
                    user_profile_Event_attend.setVisibility(View.VISIBLE);
                    hostRecycler.setVisibility(View.VISIBLE);
                    eventsAttendingRecycler.setVisibility(View.VISIBLE);
                    totlaEvents.setVisibility(View.VISIBLE);
                } else {
                    getProfile(BToken, userid);
                    getUsersLiveEvents(userid);
                    getUsersattendingEvents(userid);
                    editprofile.setVisibility(View.GONE);
                    user_profile_Event.setVisibility(View.VISIBLE);
                    pastEvents.setVisibility(View.VISIBLE);
                    user_profile_Event_attend.setVisibility(View.VISIBLE);
                    hostRecycler.setVisibility(View.VISIBLE);
                    eventsAttendingRecycler.setVisibility(View.VISIBLE);
                    totlaEvents.setVisibility(View.VISIBLE);
                }



            } else {
                if (!userToken.equals("")) {
                    getProfile(userToken, userid);
                    getLiveEvents(userToken);
                    getattendingEvents(userToken);
//                    getHistoryEvents(userToken);
                } else {
                    getProfile(BToken, userid);
                    getLiveEvents(userToken);
//                    getHistoryEvents(userToken);
                    getattendingEvents(userToken);
                }
                editprofile.setVisibility(View.VISIBLE);
                user_profile_Event.setVisibility(View.VISIBLE);
                pastEvents.setVisibility(View.VISIBLE);
                user_profile_Event_attend.setVisibility(View.VISIBLE);
                hostRecycler.setVisibility(View.VISIBLE);
                eventsAttendingRecycler.setVisibility(View.VISIBLE);
                totlaEvents.setVisibility(View.VISIBLE);
            }
        } else {
            editprofile.setVisibility(View.VISIBLE);
            user_profile_Event.setVisibility(View.VISIBLE);
            pastEvents.setVisibility(View.VISIBLE);
            user_profile_Event_attend.setVisibility(View.VISIBLE);
            hostRecycler.setVisibility(View.VISIBLE);
            eventsAttendingRecycler.setVisibility(View.VISIBLE);
            totlaEvents.setVisibility(View.VISIBLE);
        }
    }

    private void getHistoryEvents(final String userToken) {
        Call<RetroHistoryEvents> call = WebAPI.getInstance().getApi().history("Bearer "+userToken,"application/json");
        call.enqueue(new Callback<RetroHistoryEvents>() {
            @Override
            public void onResponse(Call<RetroHistoryEvents> call, Response<RetroHistoryEvents> response) {
                RetroHistoryEvents historyEvents = response.body();
                List<RetroHistoryEvents.Datum> datumList = historyEvents.getData();

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("200")) {
                        attentingTietle.clear();
                        attendingimage.clear();
                        attentingevent_id.clear();

                        for (RetroHistoryEvents.Datum datum : datumList) {

                            Log.d("cates", "" + datum.getCategory());

                            attendingimage.add(datum.getFile());
                            attentingTietle.add(datum.getTitle().toString());
                            attentingevent_id.add(String.valueOf(datum.getEventId()));

                            Collections.reverse(attendingimage);
                            Collections.reverse(attentingTietle);
                            Collections.reverse(attentingevent_id);
                            pastEvents.setText("( " + attentingTietle.size() + " )");

                            LinearLayoutManager layoutManager = new LinearLayoutManager(NormalGetProfile.this, LinearLayoutManager.HORIZONTAL, false);
                            eventsAttendingRecycler.setLayoutManager(layoutManager);

                            NormalUserAttendingEvents adapter = new NormalUserAttendingEvents(NormalGetProfile.this);
                            eventsAttendingRecycler.setAdapter(adapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RetroHistoryEvents> call, Throwable t) {

            }
        });
    }

    private void getUsersattendingEvents(String userid) {
        Call<UsersBookedPastEvent> call2 = WebAPI.getInstance().getApi().userbookedPast_event(userid);
        call2.enqueue(new Callback<UsersBookedPastEvent>() {
            @Override
            public void onResponse(Call<UsersBookedPastEvent> call, Response<UsersBookedPastEvent> response) {
                attentingTietle.clear();
                attendingimage.clear();
                attentingevent_id.clear();
                if (response.body() != null) {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("200")) {

                        UsersBookedPastEvent data = response.body();
                        List<UsersBookedPastEvent.Datum> datumArrayList = data.getData();



                        for (UsersBookedPastEvent.Datum datum : datumArrayList) {

                            Log.d("cates", "" + datum.getCategory());
                            attendingimage.add(datum.getFile().toString());
                            attentingTietle.add(datum.getTitle().toString());
                            attentingevent_id.add(String.valueOf(datum.getEventId()));
                            pastEvents.setText("( " + attentingTietle.size() + " )");


                            LinearLayoutManager layoutManager = new LinearLayoutManager(NormalGetProfile.this, LinearLayoutManager.HORIZONTAL, false);
                            eventsAttendingRecycler.setLayoutManager(layoutManager);

                            UsersPastBookedEventsAdapter adapter = new UsersPastBookedEventsAdapter(NormalGetProfile.this, datumArrayList);
                            eventsAttendingRecycler.setAdapter(adapter);
                        }
                    } else if (response.body().getStatus().equals("400")) {
                        pastEvents.setText("( " + attentingTietle.size() + " )");
                    }
                    progressDialog.dismiss();
                } else {
                    Intent intent = new Intent(NormalGetProfile.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UsersBookedPastEvent> call, Throwable t) {
                if (isOnline() == false) {
                    progressDialog.dismiss();
                    NoInternetdialog();
                } else if (isOnline() == true) {
                    progressDialog.dismiss();
                    // NoInternetdialog();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(NormalGetProfile.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getLiveEvents(String userToken) {

        Call<RetroLiveEvents> call = WebAPI.getInstance().getApi().liveEvents("Bearer " + userToken, "application/json");
        call.enqueue(new Callback<RetroLiveEvents>() {
            @Override
            public void onResponse(Call<RetroLiveEvents> call, Response<RetroLiveEvents> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {
                        RetroLiveEvents retroLiveEvents = response.body();
                        List<RetroLiveEvents.Datum> datumList = retroLiveEvents.getData();
                        images.clear();
                        eventTitle.clear();
                        liveevent_id.clear();
                        for (RetroLiveEvents.Datum datum : datumList) {
                            eventTitle.add(datum.getTitle());
                            images.add(datum.getFile());
                            liveevent_id.add(String.valueOf(datum.getEventId()));

                            Collections.reverse(eventTitle);
                            Collections.reverse(images);
                            Collections.reverse(liveevent_id);

                            totlaEvents.setText("( " + String.valueOf(images.size()) + " )");

                            LinearLayoutManager layoutManager = new LinearLayoutManager(NormalGetProfile.this, LinearLayoutManager.HORIZONTAL, false);
                            hostRecycler.setLayoutManager(layoutManager);

                            NormalUserLiveEvents adapter = new NormalUserLiveEvents(NormalGetProfile.this);
                            hostRecycler.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }
                } else {
                    Intent intent = new Intent(NormalGetProfile.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<RetroLiveEvents> call, Throwable t) {

            }
        });

    }

    private void getUsersLiveEvents(String userid) {
        //=======================get live events===========================

        Call<UsersLiveEvent> call1 = WebAPI.getInstance().getApi().userliveevents(userid);
        call1.enqueue(new Callback<UsersLiveEvent>() {
            @Override
            public void onResponse(Call<UsersLiveEvent> call, Response<UsersLiveEvent> response) {

                if (response.body() != null) {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("200")) {
                        UsersLiveEvent retroLiveEvents = response.body();
                        List<UsersLiveEvent.Datum> datumList = retroLiveEvents.getData();
                        images.clear();
                        eventTitle.clear();
                        liveevent_id.clear();

                        for (UsersLiveEvent.Datum datum : datumList) {
                            eventTitle.add(datum.getTitle());
                            images.add(datum.getFile());
                            liveevent_id.add(String.valueOf(datum.getEventId()));
                            totlaEvents.setText("( " + String.valueOf(images.size()) + " )");

                            LinearLayoutManager layoutManager = new LinearLayoutManager(NormalGetProfile.this, LinearLayoutManager.HORIZONTAL, false);
                            hostRecycler.setLayoutManager(layoutManager);

                            NormalUserLiveEvents adapter = new NormalUserLiveEvents(NormalGetProfile.this);
                            hostRecycler.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }
                } else {
                    Intent intent = new Intent(NormalGetProfile.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UsersLiveEvent> call, Throwable t) {
                if (isOnline() == false) {
                    progressDialog.dismiss();
                    NoInternetdialog();
                } else if (isOnline() == true) {
                    progressDialog.dismiss();
                    //NoInternetdialog();
                }
            }
        });
    }

    private void getattendingEvents(final String userToken) {
        Call<Normal_past_booked> call = WebAPI.getInstance().getApi().past_booked("Bearer " + userToken, "application/json");
        call.enqueue(new Callback<Normal_past_booked>() {
            @Override
            public void onResponse(Call<Normal_past_booked> call, Response<Normal_past_booked> response) {

                Log.d("+++++++++", "++ response ++" + userToken);
                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {

                        Normal_past_booked data = response.body();
                        List<Normal_past_booked.Datum> datumArrayList = data.getData();

                        attentingTietle.clear();
                        attendingimage.clear();
                        attentingevent_id.clear();

                        for (Normal_past_booked.Datum datum : datumArrayList) {

                            Log.d("cates", "" + datum.getCategory());

                            attendingimage.add(datum.getFile().get(0));
                            attentingTietle.add(datum.getTitle().toString());
                            attentingevent_id.add(String.valueOf(datum.getEventId()));

                            Collections.reverse(attendingimage);
                            Collections.reverse(attentingTietle);
                            Collections.reverse(attentingevent_id);
                            pastEvents.setText("( " + attentingTietle.size() + " )");

                            LinearLayoutManager layoutManager = new LinearLayoutManager(NormalGetProfile.this, LinearLayoutManager.HORIZONTAL, false);
                            eventsAttendingRecycler.setLayoutManager(layoutManager);

                            NormalUserAttendingEvents adapter = new NormalUserAttendingEvents(NormalGetProfile.this);
                            eventsAttendingRecycler.setAdapter(adapter);
                        }
                    } else if (response.body().getStatus().equals("400")) {
                        pastEvents.setText("( " + attentingTietle.size() + " )");
                    }
                    progressDialog.dismiss();
                } else {
                    Intent intent = new Intent(NormalGetProfile.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<Normal_past_booked> call, Throwable t) {
                if (NoInternet.isOnline(NormalGetProfile.this) == false) {
                    progressDialog.dismiss();

                    NoInternet.dialog(NormalGetProfile.this);
                }
            }
        });

    }

    private void getProfile(String userToken, String user_id) {
        Call<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> call = WebAPI.getInstance().getApi().normalGetPRofile("Bearer " + userToken, "application/json", user_id);
        call.enqueue(new Callback<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile>() {
            @Override
            public void onResponse(Call<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> call, Response<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> response) {

                if (response.body() != null) {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("200")) {
                        prfileAge = response.body().getData().get(0).getAge();
                        if (prfileAge != null) {
                            Log.d("normal", "" + response.body().getStatus() + "\n" + response.body().getData().get(0).getUsername());

                            editor = sharedPreferences.edit();
                            editor.putString("Username", response.body().getData().get(0).getUsername());
                            getS_username = response.body().getData().get(0).getUsername();
                            getProfileStatus = response.body().getData().get(0).getStatus().toString();
                            getUserImage = response.body().getData().get(0).getImage();
                            getNormalUserImage = response.body().getData().get(0).getImage();
                            getgender = response.body().getData().get(0).getGender();
                            editor.putString("UserImage", getNormalUserImage);
                            Log.d("image", getNormalUserImage);


                            if (getS_username != null) {
                                String[] name = getS_username.split(" ");
                                if (name.length == 1) {
                                    String Fname = name[0];
                                    username.setText(Fname + " ");
                                } else {
                                    String Fname = name[0];
                                    String Lname = name[1];
                                    username.setText(Fname + " " + Lname.subSequence(0, 1));
                                }
                            } else {
                                username.setText(getS_username);
                            }

                            normal_user_gender.setText(response.body().getData().get(0).getGender());
                            user_profile_age.setText(", " + response.body().getData().get(0).getAge() + "yo");

                            status.setText(getProfileStatus);
                            if (response.body().getData().get(0).getRating_points() != null) {
                                totalpoints.setText(response.body().getData().get(0).getRating_points());
                            }

                            Glide.with(NormalGetProfile.this).load(getNormalUserImage).into(userImage);
                            com.mandywebdesign.impromptu.Retrofit.NormalGetProfile normalGetProfile = response.body();
                            List<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile.Question> datum = normalGetProfile.getData().get(0).getQuestion();

                            Questions.clear();
                            Answer.clear();
                            QA_id.clear();

                            for (com.mandywebdesign.impromptu.Retrofit.NormalGetProfile.Question question : datum) {
                                Questions.add(question.getQuestion());
                                Answer.add(question.getAnswer());
                                QA_id.add(question.getQuestionId().toString());

                                LinearLayoutManager layoutManager = new LinearLayoutManager(NormalGetProfile.this, LinearLayoutManager.VERTICAL, false);
                                questionRecycler.setLayoutManager(layoutManager);


                                NormalUSerSetQues_answer adapter = new NormalUSerSetQues_answer(NormalGetProfile.this, Questions, Answer);
                                questionRecycler.setAdapter(adapter);
                            }
                            Log.d("ques", "" + Questions + "\n" + Answer);
                        } else {
                            username.setText(s_username);
                            Glide.with(NormalGetProfile.this).load(s_image).into(userImage);
                        }
                    }

                } else {
                    Intent intent = new Intent(NormalGetProfile.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> call, Throwable t) {
                if (NoInternet.isOnline(NormalGetProfile.this) == false) {
                    progressDialog.dismiss();

                    NoInternet.dialog(NormalGetProfile.this);
                }
            }
        });
    }

    private void listeners() {
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prfileAge != null) {
                    Intent intent = new Intent(NormalGetProfile.this, NormalPublishProfile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("normal_edit", "1");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(NormalGetProfile.this, NormalPublishProfile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("normal_edit", "0");
                    startActivity(intent);
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        questionRecycler = (RecyclerView) findViewById(R.id.user_profile_question_recycle);
        username = (TextView) findViewById(R.id.user_profile_username);
        userImage = (RoundedImageView) findViewById(R.id.user_profile_userimage);
        editprofile = (ImageView) findViewById(R.id.user_profile_edit_toolbar);
        user_profile_age = (TextView) findViewById(R.id.user_profile_age);
        status = (TextView) findViewById(R.id.user_profile_staus);
        back = (ImageView) findViewById(R.id.back_user_profile);
        hostRecycler = (RecyclerView) findViewById(R.id.user_profile_events_recycler);
        totlaEvents = (TextView) findViewById(R.id.normal_user_total_live_events);
        eventsAttendingRecycler = (RecyclerView) findViewById(R.id.user_profile_eventAttend_recycler);
        pastEvents = findViewById(R.id.normal_user_total_past_events);
        normal_user_gender = findViewById(R.id.normal_user_gender);
        totalpoints = findViewById(R.id.totalpoints);
        user_profile_Event = findViewById(R.id.user_profile_Event);
        user_profile_Event_attend = findViewById(R.id.user_profile_Event_attend);

    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            //Toast.makeText(BusinessCharityRegister.this, "No Internet connection!,Unable to Register", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void NoInternetdialog() {

        final Dialog dialog = new Dialog(NormalGetProfile.this);
        dialog.setContentView(R.layout.nointernetdialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button done = dialog.findViewById(R.id.done_bt_on_no_net_dilog);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
//                System.exit(0);
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });

        dialog.show();
    }
}
