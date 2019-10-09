package com.mandywebdesign.impromptu.BusinessRegisterLogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Adapters.BusinessUserPastAdapter;
import com.mandywebdesign.impromptu.Adapters.BusinessuserProfileLiveEvents;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroGetProfile;
import com.mandywebdesign.impromptu.Retrofit.RetroHistoryEvents;
import com.mandywebdesign.impromptu.Retrofit.RetroLiveEvents;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.Join_us;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessUserPRofileActivity extends AppCompatActivity implements View.OnClickListener {

    public RecyclerView recyclerView, pasteventsrecyles;
    ImageView editProfile,bacck_obviewbusnessprofile;
    TextView UserName, Address, Address2, totalliveevents, pastevnets,UserProfile_past_events_tv,UserProfie_live_event_text,UserProfile_past_events_tv_totall;
    ReadMoreTextView AboutUs;
    ImageButton webUrl, facebookUrl, InstagramUrl, TwitterUrl;
    RoundedImageView UserImage;
    View view;
    SharedPreferences sharedPreferences, sharedPreferences1, profileupdatedPref;
    SharedPreferences.Editor editor;
    public static String user, userName, address1, address2, desc, webURL, facebookURL, instaGramURL, TwitteURL, avatar, postcode, city, charity_number;
    FragmentManager manager;
    public static String accept = "application/json";
    Dialog progressDialog;
    String usertoken, token;
    public static ArrayList<String> profileliveevents = new ArrayList<>();
    public static ArrayList<String> profileliveevents_id = new ArrayList<>();
    public static ArrayList<String> profilePastEvents = new ArrayList<>();
    public static ArrayList<String> profilePastEvents_id = new ArrayList<>();
    public static ArrayList<String> images = new ArrayList<>();
    public static ArrayList<String> pastImages = new ArrayList<>();
    Intent intent;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_user_profile);

        progressDialog = ProgressBarClass.showProgressDialog(BusinessUserPRofileActivity.this);
        progressDialog.dismiss();
        Drawable drawable = new ProgressBar(BusinessUserPRofileActivity.this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(BusinessUserPRofileActivity.this, R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        profileupdatedPref = getSharedPreferences("profileupdated", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("Usertoken", "");
        user ="Bearer " + sharedPreferences.getString("Socailtoken", "");
        intent = getIntent();
         userid = intent.getStringExtra("user_id");
        Toast.makeText(this, ""+userid, Toast.LENGTH_SHORT).show();
        init();
        getUserPRofileData(userid);
        listerners();

    }

    private void listerners() {
        webUrl.setOnClickListener(this);
        facebookUrl.setOnClickListener(this);
        InstagramUrl.setOnClickListener(this);
        TwitterUrl.setOnClickListener(this);
        editProfile.setOnClickListener(this);
        bacck_obviewbusnessprofile.setOnClickListener(this);
    }

    private void init() {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        editProfile = (ImageView) findViewById(R.id._UserPRofile_edit_busines_publish_profile);
        UserName = (TextView) findViewById(R.id.UserProfile_business__publishprofile_user_Name);
        Address = (TextView) findViewById(R.id.UserProfile_business__publishprofile_address1);
        Address2 = (TextView) findViewById(R.id.UserProfile_business__publishprofile_address2);
        AboutUs = (ReadMoreTextView) findViewById(R.id.userProfile_aboutUs_Desc_TV);
        webUrl = (ImageButton) findViewById(R.id.UserPRofile_web_url_bt);
        facebookUrl = (ImageButton) findViewById(R.id.userProfile_fb_url_bt);
        InstagramUrl = (ImageButton) findViewById(R.id.UserProfile_insta_url_bt);
        TwitterUrl = (ImageButton) findViewById(R.id.UserProfile_twiter_url_bt);
        UserImage = (RoundedImageView) findViewById(R.id.UserProfile_business_profilepublish_user_Image);
        totalliveevents = (TextView) findViewById(R.id.UserProfile_live_event_texttotal);
        recyclerView = (RecyclerView) findViewById(R.id.UserProfile_business_publish_live_events_recylerview);
        pastevnets = (TextView) findViewById(R.id.UserProfile_past_events_tv_totall);
        UserProfile_past_events_tv = findViewById(R.id.UserProfile_past_events_tv);
        pasteventsrecyles = (RecyclerView) findViewById(R.id.UserProfile_business_publish__pastevents_recylerview);
        UserProfie_live_event_text = findViewById(R.id.UserProfie_live_event_text);
        bacck_obviewbusnessprofile = findViewById(R.id.bacck_obviewbusnessprofile);
        UserProfie_live_event_text.setVisibility(View.GONE);
        totalliveevents.setVisibility(View.GONE);
        pastevnets.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        pasteventsrecyles.setVisibility(View.GONE);
        UserProfile_past_events_tv.setVisibility(View.GONE);
    }

    private void getUserPRofileData(String userid) {
        progressDialog.show();
        Call<RetroGetProfile> call = WebAPI.getInstance().getApi().getProfile(user, accept,userid);
        call.enqueue(new Callback<RetroGetProfile>() {
            @Override
            public void onResponse(Call<RetroGetProfile> call, Response<RetroGetProfile> response) {

                if (response.body() != null)
                {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("200")) {
                        if (!response.body().getData().get(0).getName().equals("") || !response.body().getData().get(0).getAddress1().equals("") ||
                                !response.body().getData().get(0).getAddress2().equals("") || response.body().getData().get(0).getPostcode().equals("") ||
                                !response.body().getData().get(0).getCity().equals("") || !response.body().getData().get(0).getAboutOrganisation().equals("")) {
                            userName = response.body().getData().get(0).getName();
                            address1 = response.body().getData().get(0).getAddress1().toString();
                            address2 = response.body().getData().get(0).getAddress2().toString();
                            desc = response.body().getData().get(0).getAboutOrganisation().toString();
                            avatar = response.body().getData().get(0).getAvatar();
                            postcode = response.body().getData().get(0).getPostcode().toString();
                            city = response.body().getData().get(0).getCity().toString();
                            webURL = response.body().getData().get(0).getWebUrl().toString();

                            if (response.body().getData().get(0).getFacebookUrl()==null)
                            {
                                facebookURL = "";
                            }else {
                                facebookURL = response.body().getData().get(0).getFacebookUrl().toString();
                            }

                            if (response.body().getData().get(0).getInstagramUrl()==null)
                            {
                                instaGramURL ="";
                            }else {

                                instaGramURL = response.body().getData().get(0).getInstagramUrl().toString();
                            }

                            if (response.body().getData().get(0).getTwitterUrl()==null)
                            {
                                TwitteURL = "";
                            }else
                            {
                                TwitteURL = response.body().getData().get(0).getTwitterUrl().toString();
                            }

                            charity_number = response.body().getData().get(0).getCharityNumber().toString();
                            // Toast.makeText(getContext(), ""+charity_number, Toast.LENGTH_SHORT).show();

                            if (userName != null) {
                                String[] name = userName.split(" ");
                                if (name.length == 1) {
                                    String Fname = name[0];
                                    UserName.setText(Fname + " ");
                                } else {
                                    String Fname = name[0];
                                    String Lname = name[1];
                                    UserName.setText(Fname + " " + Lname.subSequence(0, 1));
                                }


                            } else {
                                UserName.setText(userName);
                            }

//                            UserName.setText(userName);
                            Address.setText(address1);
                            Address2.setText(address2);
                            AboutUs.setText(desc);
                            Glide.with(BusinessUserPRofileActivity.this).load(avatar).into(UserImage);
                        }
                    }
//                    else if (response.body().getStatus().equals("401")) {
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.clear();
//                        editor.commit();
//
//                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
//                        editor1.clear();
//                        editor1.commit();
//                        Toast.makeText(BusinessUserPRofileActivity.this, "Login another device", Toast.LENGTH_SHORT).show();
//
//                        SharedPreferences.Editor editor2 = profileupdatedPref.edit();
//                        editor2.clear();
//                        editor2.commit();
//
//                        progressDialog.setCanceledOnTouchOutside(false);
//                        progressDialog.show();
//
//                        Intent intent = new Intent(BusinessUserPRofileActivity.this, Join_us.class);
//                        startActivity(intent);
//                        finish();
//                    }
                }else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(BusinessUserPRofileActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RetroGetProfile> call, Throwable t) {
                if (isOnline() == false) {
                    progressDialog.dismiss();
                    NoInternetdialog();
                } else if (isOnline() == true) {
                    progressDialog.dismiss();
                    NoInternetdialog();
                }

                Log.d("userprofile", "" + t);
            }
        });


//=======================get live events===========================

//        Call<RetroLiveEvents> call1 = WebAPI.getInstance().getApi().liveEvents("Bearer "+token, "application/json");
//        call1.enqueue(new Callback<RetroLiveEvents>() {
//            @Override
//            public void onResponse(Call<RetroLiveEvents> call, Response<RetroLiveEvents> response) {
//
//                if (response.body() != null)
//                {
//                    progressDialog.dismiss();
//                    if (response.body().getStatus().equals("200")) {
//                        RetroLiveEvents data = response.body();
//
//                        List<RetroLiveEvents.Datum> datumArrayList = data.getData();
//                        profileliveevents.clear();
//                        images.clear();
//                        for (RetroLiveEvents.Datum datum : datumArrayList) {
//                            profileliveevents.add(datum.getCategory());
//                            profileliveevents_id.add(String.valueOf(datum.getEventId()));
//                            images.add(datum.getFile().toString());
//                            Collections.reverse(images);
//                            Collections.reverse(profileliveevents);
//                            String total = String.valueOf(profileliveevents.size());
//                            totalliveevents.setText("(" + String.valueOf(profileliveevents.size()) + ")");
//                            progressDialog.dismiss();
//                        }
//
//                        LinearLayoutManager layoutManager = new LinearLayoutManager(BusinessUserPRofileActivity.this, LinearLayoutManager.HORIZONTAL, false);
//                        recyclerView.setLayoutManager(layoutManager);
//
//                        BusinessuserProfileLiveEvents adapter = new BusinessuserProfileLiveEvents(BusinessUserPRofileActivity.this);
//                        recyclerView.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//                        reverseLive();
//                    }
//                }else {
//                    Intent intent = new Intent(BusinessUserPRofileActivity.this, NoInternetScreen.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<RetroLiveEvents> call, Throwable t) {
//                if (isOnline() == false) {
//                    progressDialog.dismiss();
//                    NoInternetdialog();
//                } else if (isOnline() == true) {
//                    progressDialog.dismiss();
//                    //NoInternetdialog();
//                }
//            }
//        });

        //getPastEvents

//        Call<RetroHistoryEvents> call2 = WebAPI.getInstance().getApi().history("Bearer "+token,"application/json");
//        call2.enqueue(new Callback<RetroHistoryEvents>() {
//            @Override
//            public void onResponse(Call<RetroHistoryEvents> call, Response<RetroHistoryEvents> response) {
//
//                if (response.body()!=null)
//                {
//                    progressDialog.dismiss();
//                    if (response.body().getStatus().equals("200")) {
//                        RetroHistoryEvents data = response.body();
//
//                        List<RetroHistoryEvents.Datum> datumArrayList = data.getData();
//                        profilePastEvents.clear();
//                        pastImages.clear();
//                        for (RetroHistoryEvents.Datum datum : datumArrayList) {
//                            profilePastEvents.add(datum.getCategory());
//                            Collections.reverse(profilePastEvents);
//                            pastImages.add(datum.getFile());
//                            Collections.reverse(profilePastEvents);
//                            Collections.reverse(pastImages);
//                            profilePastEvents_id.add(String.valueOf(datum.getEventId()));
//                            Collections.reverse(profilePastEvents_id);
//
//                            String total = String.valueOf(profilePastEvents.size());
//                            pastevnets.setText("(" + String.valueOf(profilePastEvents.size()) + ")");
//                        }
//
//
//                        LinearLayoutManager layoutManager = new LinearLayoutManager(BusinessUserPRofileActivity.this, LinearLayoutManager.HORIZONTAL, false);
//                        pasteventsrecyles.setLayoutManager(layoutManager);
//
//                        BusinessUserPastAdapter adapter = new BusinessUserPastAdapter(BusinessUserPRofileActivity.this);
//                        pasteventsrecyles.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//                    }
//                }else {
//                    Intent intent = new Intent(BusinessUserPRofileActivity.this, NoInternetScreen.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<RetroHistoryEvents> call, Throwable t) {
//                if (isOnline() == false) {
//                    progressDialog.dismiss();
//                    NoInternetdialog();
//                } else if (isOnline() == true) {
//                    progressDialog.dismiss();
//                    // NoInternetdialog();
//                }else {
//                    progressDialog.dismiss();
//                    Toast.makeText(BusinessUserPRofileActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void reverseLive() {
        Collections.reverse(profileliveevents);
        Collections.reverse(images);
    }



    @Override
    public void onClick(View v) {

        if (v == webUrl) {

            if (!webURL.equals("") && Patterns.WEB_URL.matcher(webURL).matches()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(webURL));
                startActivity(intent);
            } else {
                Toast.makeText(BusinessUserPRofileActivity.this, "No Web URL added", Toast.LENGTH_SHORT).show();
            }

        }
        if (v == facebookUrl) {

            if (!facebookURL.equals("") && Patterns.WEB_URL.matcher(facebookURL).matches()) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookURL)));
            } else {
                Toast.makeText(BusinessUserPRofileActivity.this, "No valid Facebook Page added", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == InstagramUrl) {
            if (!instaGramURL.equals("") && Patterns.WEB_URL.matcher(instaGramURL).matches())
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(instaGramURL)));
            }else {
                Toast.makeText(BusinessUserPRofileActivity.this, "No valid Instagram Page added", Toast.LENGTH_SHORT).show();
            }
        }
        if (v == TwitterUrl) {
            if (!TwitteURL.equals("") && Patterns.WEB_URL.matcher(TwitteURL).matches())
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(TwitteURL)));
            }else {
                Toast.makeText(BusinessUserPRofileActivity.this, "No valid Twitter Page aaded", Toast.LENGTH_SHORT).show();
            }

        }

        if (v == editProfile) {

            Intent intent = new Intent(BusinessUserPRofileActivity.this,BussinessProfileAcitivity1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("value","1");
            startActivity(intent);
        }

        if (v==bacck_obviewbusnessprofile)
        {
            onBackPressed();
        }
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

        final Dialog dialog = new Dialog(BusinessUserPRofileActivity.this);
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

    @Override
    public void onResume() {
        super.onResume();
        getUserPRofileData(userid);
    }
}
