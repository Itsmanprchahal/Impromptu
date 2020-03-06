package com.mandywebdesign.impromptu.BusinessRegisterLogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessAdapter.LiveEventAdapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Models.AllEventsPojo;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroUploadProfilePojo;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BussinessProfileActivity3 extends AppCompatActivity {

    View view;
    Button mBack;
    FragmentManager manager;
    ArrayList<AllEventsPojo> pojoList = new ArrayList<>();
    RecyclerView mLiveEventsThumbs, mPastEventsThumbs;
    TextView LiveEventtv, PaseventTv, BName, address1, address2, aboutus;
    RoundedImageView userImage;
    ImageView fb_url_bt, twitter_url_bt, instagram_url_bt, web_url_bt, editProfile;
    String facebook, twitter, instagram, web, Bname, Saddress1, Saddress2, image, SaboutUs, SCity, SPostCode, id, userToken;
    SharedPreferences preferences, preferences1, preferences2, profileUpdatedPref;
    SharedPreferences.Editor editor;
    Button publishprofile_bt;
    Dialog progressDialog;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_profile3);

        progressDialog = ProgressBarClass.showProgressDialog(BussinessProfileActivity3.this);
        progressDialog.dismiss();

        init();
        listeners();

        preferences = getSharedPreferences("BusinessProfile2", Context.MODE_PRIVATE);
        preferences1 = getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        preferences2 = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        profileUpdatedPref = getSharedPreferences("profileupdated", Context.MODE_PRIVATE);
        editor = profileUpdatedPref.edit();

        Bname = preferences1.getString("name", "");
        image = preferences1.getString("image", "");
        Saddress1 = preferences1.getString("address1", "");
        Saddress2 = preferences1.getString("address2", "");
        SaboutUs = preferences1.getString("about", "");
        SCity = preferences1.getString("city", "");
        SPostCode = preferences1.getString("postcode", "");
        web = preferences.getString("weburl", "");
        facebook = preferences.getString("facebookurl", "");
        instagram = preferences.getString("instagramurl", "");
        twitter = preferences.getString("twitterurl", "");
        id = preferences2.getString("id", "");
        userToken = "Bearer " + preferences2.getString("Usertoken", "");
        Log.d("id1234", userToken + "\n" + id);

        if (facebook.equals(""))
        {
         fb_url_bt.setVisibility(View.GONE);
        }

        if (instagram.equals(""))
        {
            instagram_url_bt.setVisibility(View.GONE);
        }

        if (twitter.equals(""))
        {
            twitter_url_bt.setVisibility(View.GONE);
        }

        BName.setText(Bname);
        userImage.setImageBitmap(decodeBase64(image));
        address1.setText(Saddress1);
        address2.setText(Saddress2);
        aboutus.setText(SaboutUs);

        LiveEventAdapter();
        PastEventAdapter();
        String pojosize = String.valueOf(pojoList.size());

        LiveEventtv.setText("(" + pojosize + ")");
        PaseventTv.setText("(" + pojosize + ")");
    }


    private void init() {

        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        mLiveEventsThumbs = findViewById(R.id.business_publish_live_events_recylerview);
        mPastEventsThumbs = findViewById(R.id.business_publish__pastevents_recylerview);
        LiveEventtv = findViewById(R.id.live_event_texttotal);
        PaseventTv = findViewById(R.id.past_events_tv_totall);
        fb_url_bt = findViewById(R.id.fb_url_bt);
        twitter_url_bt = findViewById(R.id.twiter_url_bt);
        instagram_url_bt = findViewById(R.id.insta_url_bt);
        web_url_bt = findViewById(R.id.web_url_bt);
        BName = findViewById(R.id.business__publishprofile_user_Name);
        address1 = findViewById(R.id.business__publishprofile_address1);
        address2 = findViewById(R.id.business__publishprofile_address2);
        aboutus = findViewById(R.id.aboutUs_Desc_TV);
        userImage = findViewById(R.id.business_profilepublish_user_Image);
        publishprofile_bt = findViewById(R.id.publish_profile);
        mBack = findViewById(R.id.business_publish_profile_back_bt);
    }

    private void listeners() {

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BussinessProfileActivity3.this,BussinessProfileActivity2.class);
                intent.putExtra("value","back");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        fb_url_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = getIntent();
                if (intent!=null)
                {
                    facebook = intent.getStringExtra("facebook");
                    if (!Patterns.WEB_URL.matcher(facebook).matches())
                    {
                        Toast.makeText(BussinessProfileActivity3.this, "No valid URL Added", Toast.LENGTH_SHORT).show();
                    }else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebook)));
                    }
                }

            }
        });

        web_url_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intent!=null)
                {
                    web = intent.getStringExtra("web");
                    if (!Patterns.WEB_URL.matcher(web).matches())
                    {
                        Toast.makeText(BussinessProfileActivity3.this, "No valid URL Added", Toast.LENGTH_SHORT).show();
                    }else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(web)));
                    }
                }
            }
        });

        twitter_url_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (intent!=null)
                {
                    twitter = intent.getStringExtra("twitter");
                    if (!Patterns.WEB_URL.matcher(web).matches())
                    {
                        Toast.makeText(BussinessProfileActivity3.this, "No valid URL Added", Toast.LENGTH_SHORT).show();
                    }else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(twitter)));
                    }
                }
            }
        });

        instagram_url_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent!=null)
                {
                    instagram = intent.getStringExtra("insta");
                    if (!Patterns.WEB_URL.matcher(instagram).matches())
                    {
                        Toast.makeText(BussinessProfileActivity3.this, "No valid URL Added", Toast.LENGTH_SHORT).show();
                    }else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(instagram)));
                    }
                }

            }
        });

        publishprofile_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.gc();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Call<RetroUploadProfilePojo> call = WebAPI.getInstance().getApi().PublishProfile(userToken, id, Bname, Saddress1, Saddress2, SPostCode, SCity, SaboutUs, web, facebook, instagram, twitter, BussinessProfileAcitivity1.part);
                call.enqueue(new Callback<RetroUploadProfilePojo>() {
                    @Override
                    public void onResponse(Call<RetroUploadProfilePojo> call, Response<RetroUploadProfilePojo> response) {
                        // Log.d("responsedata", "" + response.body().getMessage());
                        if (response.body() != null)
                        {
                            if (response.body().getStatus().equals("200"))
                            {
                                progressDialog.dismiss();
                                Log.d("response", ""
                                        + response.body().getData().getName() + "\n"
                                        + response.body().getData().getAvatar() + "\n"
                                        + response.body().getData().getAddress1() + "\n"
                                        + response.body().getData().getAddress2() + "\n"
                                        + response.body().getData().getPostcode() + "\n"
                                        + response.body().getData().getCity() + "\n"
                                        + response.body().getData().getAboutOrganisation() + "\n"
                                        + response.body().getData().getWebUrl() + "\n"
                                        + response.body().getData().getFacebookUrl() + "\n"
                                        + response.body().getData().getInstagramUrl() + "\n"
                                        + response.body().getData().getTwitterUrl());
                                SharedPreferences sharedPreferences=getSharedPreferences("12345",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("1234","1");
                                editor.apply();
                                Toast.makeText(BussinessProfileActivity3.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(BussinessProfileActivity3.this,Home_Screen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        }else {
                            Intent intent = new Intent(BussinessProfileActivity3.this, NoInternetScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<RetroUploadProfilePojo> call, Throwable t) {
                        if (t instanceof SocketTimeoutException) {
                            // Toast.makeText(getContext(), "Socket Time out. Please try again.", Toast.LENGTH_SHORT).show();
                            NoInternetdialog();
                        }else {
                            Toast.makeText(BussinessProfileActivity3.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.d("response1", "" + t);
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }


    public void LiveEventAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(BussinessProfileActivity3.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLiveEventsThumbs.setLayoutManager(layoutManager);
        mLiveEventsThumbs.setNestedScrollingEnabled(false);

        LiveEventAdapter adapter = new LiveEventAdapter(BussinessProfileActivity3.this, pojoList);
        // mLiveEventsThumbs.setAdapter(adapter);
    }

    public void PastEventAdapter() {
        final LinearLayoutManager layoutManager1 = new LinearLayoutManager(BussinessProfileActivity3.this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mPastEventsThumbs.setLayoutManager(layoutManager1);
        mPastEventsThumbs.setNestedScrollingEnabled(false);
        LiveEventAdapter adapter = new LiveEventAdapter(BussinessProfileActivity3.this, pojoList);
        // mPastEventsThumbs.setAdapter(adapter);

    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }


    private void NoInternetdialog() {

        final Dialog dialog = new Dialog(BussinessProfileActivity3.this);
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
