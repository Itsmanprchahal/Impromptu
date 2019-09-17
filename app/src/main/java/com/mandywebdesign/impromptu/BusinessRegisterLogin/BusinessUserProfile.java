package com.mandywebdesign.impromptu.BusinessRegisterLogin;


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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessUserProfile extends Fragment implements View.OnClickListener {

    public static final int MULTIPLE_PERMISSIONS = 10;

    public RecyclerView recyclerView, pasteventsrecyles;
    ImageView editProfile;
    TextView UserName, Address, Address2, totalliveevents, pastevnets;
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
    public static ArrayList<String> profilePastEvents = new ArrayList<>();
    public static ArrayList<String> images = new ArrayList<>();
    public static ArrayList<String> pastImages = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_business_user_profile, container, false);



        progressDialog = ProgressBarClass.showProgressDialog(getContext());
        progressDialog.dismiss();
        Drawable drawable = new ProgressBar(getContext()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        manager = getFragmentManager();
        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        sharedPreferences1 = getActivity().getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        profileupdatedPref = getContext().getSharedPreferences("profileupdated", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("Usertoken", "");
        user = "Bearer " + sharedPreferences.getString("Usertoken", "");

        init();
        getUserPRofileData();
        listerners();


        return view;
    }

    private void listerners() {
        webUrl.setOnClickListener(this);
        facebookUrl.setOnClickListener(this);
        InstagramUrl.setOnClickListener(this);
        TwitterUrl.setOnClickListener(this);
        editProfile.setOnClickListener(this);
    }

    private void init() {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        editProfile = (ImageView) view.findViewById(R.id._UserPRofile_edit_busines_publish_profile);
        UserName = (TextView) view.findViewById(R.id.UserProfile_business__publishprofile_user_Name);
        Address = (TextView) view.findViewById(R.id.UserProfile_business__publishprofile_address1);
        Address2 = (TextView) view.findViewById(R.id.UserProfile_business__publishprofile_address2);
        AboutUs = (ReadMoreTextView) view.findViewById(R.id.userProfile_aboutUs_Desc_TV);
        webUrl = (ImageButton) view.findViewById(R.id.UserPRofile_web_url_bt);
        facebookUrl = (ImageButton) view.findViewById(R.id.userProfile_fb_url_bt);
        InstagramUrl = (ImageButton) view.findViewById(R.id.UserProfile_insta_url_bt);
        TwitterUrl = (ImageButton) view.findViewById(R.id.UserProfile_twiter_url_bt);
        UserImage = (RoundedImageView) view.findViewById(R.id.UserProfile_business_profilepublish_user_Image);
        totalliveevents = (TextView) view.findViewById(R.id.UserProfile_live_event_texttotal);
        recyclerView = (RecyclerView) view.findViewById(R.id.UserProfile_business_publish_live_events_recylerview);
        pastevnets = (TextView) view.findViewById(R.id.UserProfile_past_events_tv_totall);
        pasteventsrecyles = (RecyclerView) view.findViewById(R.id.UserProfile_business_publish__pastevents_recylerview);
    }

    private void getUserPRofileData() {
        progressDialog.show();
        Call<RetroGetProfile> call = WebAPI.getInstance().getApi().getProfile(user, accept);
        call.enqueue(new Callback<RetroGetProfile>() {
            @Override
            public void onResponse(Call<RetroGetProfile> call, Response<RetroGetProfile> response) {

                if (response.body() != null)
                {
//                    progressDialog.dismiss();
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

                            UserName.setText(userName);
                            Address.setText(address1);
                            Address2.setText(address2);
                            AboutUs.setText(desc);
                            Glide.with(BusinessUserProfile.this).load(avatar).into(UserImage);
                        }
                    } else if (response.body().getStatus().equals("401")) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.clear();
                        editor1.commit();
                        Toast.makeText(getContext(), "Login another device", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor2 = profileupdatedPref.edit();
                        editor2.clear();
                        editor2.commit();

                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        Intent intent = new Intent(getActivity(), Join_us.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                }else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
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

        Call<RetroLiveEvents> call1 = WebAPI.getInstance().getApi().liveEvents(user, "application/json");
        call1.enqueue(new Callback<RetroLiveEvents>() {
            @Override
            public void onResponse(Call<RetroLiveEvents> call, Response<RetroLiveEvents> response) {

                if (response.body() != null)
                {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("200")) {
                        RetroLiveEvents data = response.body();

                        List<RetroLiveEvents.Datum> datumArrayList = data.getData();
                        profileliveevents.clear();
                        images.clear();
                        for (RetroLiveEvents.Datum datum : datumArrayList) {
                            profileliveevents.add(datum.getCategory());
                            images.add(datum.getFile().toString());
                            Collections.reverse(images);
                            Collections.reverse(profileliveevents);
                            String total = String.valueOf(profileliveevents.size());
                            totalliveevents.setText("(" + String.valueOf(profileliveevents.size()) + ")");
                            progressDialog.dismiss();
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(layoutManager);

                        BusinessuserProfileLiveEvents adapter = new BusinessuserProfileLiveEvents(getContext());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        reverseLive();
                    }
                }else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RetroLiveEvents> call, Throwable t) {
                if (isOnline() == false) {
                    progressDialog.dismiss();
                    NoInternetdialog();
                } else if (isOnline() == true) {
                    progressDialog.dismiss();
                    //NoInternetdialog();
                }
            }
        });

        //getPastEvents

        Call<RetroHistoryEvents> call2 = WebAPI.getInstance().getApi().history(user,"application/json");
        call2.enqueue(new Callback<RetroHistoryEvents>() {
            @Override
            public void onResponse(Call<RetroHistoryEvents> call, Response<RetroHistoryEvents> response) {

                if (response.body()!=null)
                {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("200")) {
                        RetroHistoryEvents data = response.body();

                        List<RetroHistoryEvents.Datum> datumArrayList = data.getData();
                        profilePastEvents.clear();
                        pastImages.clear();
                        for (RetroHistoryEvents.Datum datum : datumArrayList) {
                            profilePastEvents.add(datum.getCategory());
                            Collections.reverse(profilePastEvents);
                            pastImages.add(datum.getFile());
                            Collections.reverse(profilePastEvents);
                            Collections.reverse(pastImages);

                            String total = String.valueOf(profilePastEvents.size());
                            pastevnets.setText("(" + String.valueOf(profilePastEvents.size()) + ")");
                        }


                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        pasteventsrecyles.setLayoutManager(layoutManager);

                        BusinessUserPastAdapter adapter = new BusinessUserPastAdapter(getContext());
                        pasteventsrecyles.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RetroHistoryEvents> call, Throwable t) {
                if (isOnline() == false) {
                    progressDialog.dismiss();
                    NoInternetdialog();
                } else if (isOnline() == true) {
                    progressDialog.dismiss();
                    // NoInternetdialog();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                Toast.makeText(getActivity(), "No Web URL added", Toast.LENGTH_SHORT).show();
            }

        }
        if (v == facebookUrl) {

            if (!facebookURL.equals("") && Patterns.WEB_URL.matcher(facebookURL).matches()) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookURL)));
            } else {
                Toast.makeText(getActivity(), "No valid Facebook Page added", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == InstagramUrl) {
            if (!instaGramURL.equals("") && Patterns.WEB_URL.matcher(instaGramURL).matches())
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(instaGramURL)));
            }else {
                Toast.makeText(getActivity(), "No valid Instagram Page added", Toast.LENGTH_SHORT).show();
            }
        }
        if (v == TwitterUrl) {
            if (!TwitteURL.equals("") && Patterns.WEB_URL.matcher(TwitteURL).matches())
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(TwitteURL)));
            }else {
                Toast.makeText(getActivity(), "No valid Twitter Page aaded", Toast.LENGTH_SHORT).show();
            }

        }

        if (v == editProfile) {

            Intent intent = new Intent(getContext(),BussinessProfileAcitivity1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("value","1");
            startActivity(intent);
        }
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            //Toast.makeText(BusinessCharityRegister.this, "No Internet connection!,Unable to Register", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void NoInternetdialog() {

        final Dialog dialog = new Dialog(getContext());
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
        getUserPRofileData();
    }
}