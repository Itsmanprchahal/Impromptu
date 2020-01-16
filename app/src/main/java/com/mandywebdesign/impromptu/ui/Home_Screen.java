package com.mandywebdesign.impromptu.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.cardview.widget.CardView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BussinessProfileAcitivity1;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessUserProfile;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Home;
import com.mandywebdesign.impromptu.Retrofit.NormalGetProfile;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.NormalPublishProfile;
import com.mandywebdesign.impromptu.firebasenotification.MyFirebaseMessagingService;
import com.mandywebdesign.impromptu.messages.Messages;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Setting;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Events;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AddEvents.Add_Event_Activity;
import com.mandywebdesign.impromptu.MyEventsFragments.Hosting;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroGetProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_Screen extends AppCompatActivity {

    FragmentManager manager;
    boolean loggedOut;
    CardView view;
    GoogleSignInAccount account;
    public static BottomNavigationView bottomNavigationView;
    SharedPreferences profileupdatedPref1, sharedPreferences, sharedPreferences1;
    SharedPreferences.Editor editor;
    String userToken, refresh;
    Intent intent;
    Dialog progressDialog;
    String accept = "application/json";
    public static String BprofileStatus, data;
    public static int countt = 0, newCount = 0;
    String refreshvalue, checkgender, socailtoken, counter;
    public static String count = "0";
    public static int framgposition, lastposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__screen);

        newCount = 0;
        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();
        Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);

        progressDialog.show();


        sharedPreferences1 = getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = "Bearer " + sharedPreferences.getString("Usertoken", "");
        socailtoken = "Bearer " + sharedPreferences.getString("Socailtoken", "");

        profileupdatedPref1 = getSharedPreferences("profileupdated", Context.MODE_PRIVATE);
        refreshvalue = sharedPreferences.getString("tab1", "");

        manager = getSupportFragmentManager();

        account = GoogleSignIn.getLastSignedInAccount(this);

        getProfile(socailtoken);

        //facebook check
        loggedOut = AccessToken.getCurrentAccessToken() == null;

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                    }
                });


        init();
        listeners();

        intent = getIntent();


        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        final View iconView = menuView.getChildAt(2).findViewById(com.google.android.material.R.id.icon);
        final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
        layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
        iconView.setLayoutParams(layoutParams);

        setHomeScreen();
        setMesgIcon();
    }


    private void getProfile(String socailtoken) {
        progressDialog.show();
        Call<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> call = WebAPI.getInstance().getApi().normalGetPRofile(socailtoken, "application/json", "");
        call.enqueue(new Callback<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile>() {
            @Override
            public void onResponse(Call<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> call, Response<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> response) {
                if (response.body() != null) {
                    progressDialog.dismiss();
                    editor = sharedPreferences.edit();
                    editor.putString("profilegender", response.body().getData().get(0).getGender());
                    editor.apply();

                }
            }

            @Override
            public void onFailure(Call<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> call, Throwable t) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkgender = sharedPreferences.getString("profilegender", "");
    }

    private void setHomeScreen() {

        if (!loggedOut || account != null) {
            //getProfile(socailtoken);
            //updateProfiledialog();
            Intent intent1 = getIntent();
            String value = intent1.getStringExtra("bookevent");
            String s = intent1.getStringExtra("isFrom");

            if (value == null && s == null) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_frame_layout, new Home());
                transaction.commit();
                setMesgIcon();


            } else if (value == null && s.equals("NormaleventCreation")) {

                Bundle bundle = new Bundle();
                bundle.putString("eventType", "live");
                Events events = new Events();
                events.setArguments(bundle);
                FragmentTransaction transaction1 = manager.beginTransaction();
                transaction1.replace(R.id.home_frame_layout, events);
                transaction1.commit();
                setMesgIcon();

            } else {
                Bundle bundle = new Bundle();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_frame_layout, new Events());
                transaction.commit();
                setMesgIcon();
            }


            Menu menu = bottomNavigationView.getMenu();
            menu.findItem(R.id.hometab).setIcon(R.drawable.iconhome);

            progressDialog.dismiss();


        }  else {
                Call<RetroGetProfile> call = WebAPI.getInstance().getApi().getProfile(userToken, accept, "");
                call.enqueue(new Callback<RetroGetProfile>() {
                    @Override
                    public void onResponse(Call<RetroGetProfile> call, Response<RetroGetProfile> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("200")) {
                                BprofileStatus = response.body().getData().get(0).getProfileNumber().toString();
                                if (response.body().getData().get(0).getProfileNumber().equals(1)) {

                                    FragmentTransaction transaction = manager.beginTransaction();
                                    transaction.replace(R.id.home_frame_layout, new BusinessUserProfile());
                                    transaction.commit();

                                    Menu menu = bottomNavigationView.getMenu();
                                    menu.findItem(R.id.hometab).setIcon(R.drawable.iconprofile);

                                } else {
                                    Intent intent = new Intent(Home_Screen.this, BussinessProfileAcitivity1.class);
                                    intent.putExtra("value", "0");
                                    startActivity(intent);
                                    finish();

                                }
                            } else if (response.body().getStatus().equals("401")) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();

                                Intent intent = new Intent(Home_Screen.this, Join_us.class);
                                startActivity(intent);
                                finish();
                            }

                            progressDialog.dismiss();

                        } else {
                            Intent intent = new Intent(Home_Screen.this, NoInternetScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<RetroGetProfile> call, Throwable t) {
                        if (NoInternet.isOnline(Home_Screen.this) == false) {
                            progressDialog.dismiss();
                            NoInternet.dialog(Home_Screen.this);
                        }
                    }
                });

            }


    }

    public void setMesgIcon() {
        if (MyFirebaseMessagingService.counter != null) {
            counter = MyFirebaseMessagingService.counter.toString();

            if (!counter.equals("0") && count.equals("0")) {
                Menu menu = bottomNavigationView.getMenu();
                menu.findItem(R.id.messagetab).setIcon(R.drawable.mesgnotify);

            } else if (!counter.equals("0") && count.equals("1")) {
                Menu menu = bottomNavigationView.getMenu();
                menu.findItem(R.id.messagetab).setIcon(R.drawable.messageinactive);
            }

        }

    }

    public void updateProfiledialog() {
        final Dialog dialog = new Dialog(Home_Screen.this);
        dialog.setContentView(R.layout.welcomedialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Button continue_bt = dialog.findViewById(R.id.continue_bt);
        continue_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Home_Screen.this, NormalPublishProfile.class);
                intent.putExtra("normal_edit", "0");
                intent.putExtra("backongendercheck", "0");
                startActivity(intent);
            }
        });
    }

    private void init() {
        Intent intent = getIntent();
        refresh = intent.getStringExtra("refresh");
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setSelectedItemId(R.id.hometab);
    }


    private void listeners() {

        if (account != null || !loggedOut) {

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.hometab:

                            editor = sharedPreferences.edit();
                            editor.putString("tab1", "1");
                            editor.apply();

                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.home_frame_layout, new Home());
                            transaction.commit();
                            framgposition = 0;
                            if (framgposition == 0) {
                                Menu menu = bottomNavigationView.getMenu();
                                menu.findItem(R.id.wallettab).setIcon(R.drawable.walletinactive);
                                menu.findItem(R.id.profiletab).setIcon(R.drawable.useractive);
                                menu.findItem(R.id.hometab).setIcon(R.drawable.eventactive);
                                menu.findItem(R.id.messagetab).setIcon(R.drawable.messageinactive);
                            }
                            setMesgIcon();
                            return true;

                        case R.id.wallettab:

                            editor = sharedPreferences.edit();
                            editor.putString("tab1", "2");
                            editor.apply();
                            FragmentTransaction transaction1 = manager.beginTransaction();
                            transaction1.replace(R.id.home_frame_layout, new Events());
                            transaction1.commit();
                            framgposition = 1;
                            lastposition = 1;
                            if (framgposition == 1) {
                                Menu menu = bottomNavigationView.getMenu();
                                menu.findItem(R.id.wallettab).setIcon(R.drawable.walletactive);
                                menu.findItem(R.id.profiletab).setIcon(R.drawable.useractive);
                                menu.findItem(R.id.hometab).setIcon(R.drawable.eventinactive);
                                menu.findItem(R.id.messagetab).setIcon(R.drawable.messageinactive);
                            }
                            setMesgIcon();
                            return true;

                        case R.id.myeventstab:
                            checkgender = sharedPreferences.getString("profilegender", "");
                            if (checkgender.equals("")) {
                                updateProfiledialog();
                            } else {
                                Intent intent = new Intent(Home_Screen.this, Add_Event_Activity.class);
                                startActivity(intent);
                            }
                            setMesgIcon();
                            return true;

                        case R.id.messagetab:

                            editor = sharedPreferences.edit();
                            editor.putString("tab1", "3");
                            editor.apply();
                            FragmentTransaction transaction2 = manager.beginTransaction();
                            transaction2.replace(R.id.home_frame_layout, new Messages());
                            transaction2.commit();

                            count = "1";
                            framgposition = 3;
                            lastposition = 3;
                            if (framgposition == 3) {
                                Menu menu = bottomNavigationView.getMenu();
                                menu.findItem(R.id.wallettab).setIcon(R.drawable.walletinactive);
                                menu.findItem(R.id.profiletab).setIcon(R.drawable.useractive);
                                menu.findItem(R.id.hometab).setIcon(R.drawable.eventinactive);
                                menu.findItem(R.id.messagetab).setIcon(R.drawable.messageactive);
                            }
                            return true;

                        case R.id.profiletab:

                            editor = sharedPreferences.edit();
                            editor.putString("tab1", "4");
                            editor.apply();
                            FragmentTransaction transaction3 = manager.beginTransaction();
                            transaction3.replace(R.id.home_frame_layout, new Setting());
                            transaction3.commit();
                            setMesgIcon();
                            framgposition = 4;
                            lastposition = 4;
                            if (framgposition == 4) {
                                Menu menu = bottomNavigationView.getMenu();
                                menu.findItem(R.id.wallettab).setIcon(R.drawable.walletinactive);
                                menu.findItem(R.id.profiletab).setIcon(R.drawable.userinactive);
                                menu.findItem(R.id.hometab).setIcon(R.drawable.eventinactive);
                                menu.findItem(R.id.messagetab).setIcon(R.drawable.messageinactive);
                            }
                            return true;
                    }

                    return false;
                }
            });
        } else {
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {

                        case R.id.hometab:

                            FragmentTransaction transaction0 = manager.beginTransaction();
                            transaction0.replace(R.id.home_frame_layout, new BusinessUserProfile());
                            transaction0.commit();
                            setMesgIcon();
                            framgposition = 0;
                            framgposition = 3;
                            lastposition = 3;
                            if (framgposition == 3) {
                                Menu menu = bottomNavigationView.getMenu();
                                menu.findItem(R.id.wallettab).setIcon(R.drawable.walletinactive);
                                menu.findItem(R.id.profiletab).setIcon(R.drawable.useractive);
                                menu.findItem(R.id.hometab).setIcon(R.drawable.profileactive);
                                menu.findItem(R.id.messagetab).setIcon(R.drawable.messageinactive);
                            }
                            return true;

                        case R.id.wallettab:

                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.home_frame_layout, new Hosting());
                            transaction.commit();
                            framgposition = 1;
                            lastposition = 1;
                            if (framgposition == 1) {
                                Menu menu = bottomNavigationView.getMenu();
                                menu.findItem(R.id.wallettab).setIcon(R.drawable.walletactive);
                                menu.findItem(R.id.profiletab).setIcon(R.drawable.useractive);
                                menu.findItem(R.id.hometab).setIcon(R.drawable.profileinactive);
                                menu.findItem(R.id.messagetab).setIcon(R.drawable.messageinactive);
                            }
                            setMesgIcon();
                            return true;

                        case R.id.myeventstab:
                            SharedPreferences sharedPreferences = getSharedPreferences("12345", Context.MODE_PRIVATE);
                            String abc = sharedPreferences.getString("1234", "");
                            Log.d("1234567", abc);


                            if (!abc.equalsIgnoreCase("")) {
//                                setDialogOptions();
                                Intent intent = new Intent(Home_Screen.this, Add_Event_Activity.class);
                                startActivity(intent);

                            } else if (BusinessUserProfile.userName != null) {
                                Intent intent = new Intent(Home_Screen.this, Add_Event_Activity.class);
                                startActivity(intent);
//                                setDialogOptions();
                            } else {
                                Toast.makeText(Home_Screen.this, "Publish Profile first to create event", Toast.LENGTH_SHORT).show();
                            }

                            setMesgIcon();
                            return true;


                        case R.id.messagetab:
                            FragmentTransaction transaction1 = manager.beginTransaction();
                            transaction1.replace(R.id.home_frame_layout, new Messages());
                            transaction1.commit();
                            count = "1";
                            framgposition = 3;
                            lastposition = 3;
                            if (framgposition == 3) {
                                Menu menu = bottomNavigationView.getMenu();
                                menu.findItem(R.id.wallettab).setIcon(R.drawable.walletinactive);
                                menu.findItem(R.id.profiletab).setIcon(R.drawable.useractive);
                                menu.findItem(R.id.hometab).setIcon(R.drawable.profileinactive);
                                menu.findItem(R.id.messagetab).setIcon(R.drawable.messageactive);
                            }
                            return true;

                        case R.id.profiletab:
                            FragmentTransaction transaction2 = manager.beginTransaction();
                            transaction2.replace(R.id.home_frame_layout, new Setting());
                            transaction2.commit();
                            setMesgIcon();
                            framgposition = 4;
                            lastposition = 4;
                            if (framgposition == 4) {
                                Menu menu = bottomNavigationView.getMenu();
                                menu.findItem(R.id.wallettab).setIcon(R.drawable.walletinactive);
                                menu.findItem(R.id.profiletab).setIcon(R.drawable.userinactive);
                                menu.findItem(R.id.hometab).setIcon(R.drawable.profileinactive);
                                menu.findItem(R.id.messagetab).setIcon(R.drawable.messageinactive);
                            }
                            return true;
                    }
                    return false;
                }
            });


        }

    }

    @Override
    public void onBackPressed() {
        if (framgposition != 0) {
            if (account != null || !loggedOut) {
                FragmentTransaction transaction2 = manager.beginTransaction();
                transaction2.replace(R.id.home_frame_layout, new Home());
                transaction2.commit();
                framgposition = 0;
            } else {
                FragmentTransaction transaction2 = manager.beginTransaction();
                transaction2.replace(R.id.home_frame_layout, new BusinessUserProfile());
                transaction2.commit();
                framgposition = 0;
            }

            if (account != null || !loggedOut) {
                if (lastposition == 4) {
                    Menu menu = bottomNavigationView.getMenu();
                    menu.findItem(R.id.hometab).setIcon(R.drawable.eventactive);
                    menu.findItem(R.id.profiletab).setIcon(R.drawable.useractive);
                } else if (lastposition == 1) {
                    Menu menu = bottomNavigationView.getMenu();
                    menu.findItem(R.id.hometab).setIcon(R.drawable.eventactive);
                    menu.findItem(R.id.wallettab).setIcon(R.drawable.walletinactive);
                } else if (framgposition == 0) {
                    Menu menu = bottomNavigationView.getMenu();
                    menu.findItem(R.id.hometab).setIcon(R.drawable.eventactive);
                    menu.findItem(R.id.wallettab).setIcon(R.drawable.walletinactive);
                    menu.findItem(R.id.messagetab).setIcon(R.drawable.messageinactive);
                    menu.findItem(R.id.profiletab).setIcon(R.drawable.useractive);
                }
            } else {
                if (lastposition == 4) {
                    Menu menu = bottomNavigationView.getMenu();
                    menu.findItem(R.id.hometab).setIcon(R.drawable.profileactive);
                    menu.findItem(R.id.profiletab).setIcon(R.drawable.useractive);
                } else if (lastposition == 1) {
                    Menu menu = bottomNavigationView.getMenu();
                    menu.findItem(R.id.hometab).setIcon(R.drawable.profileactive);
                    menu.findItem(R.id.wallettab).setIcon(R.drawable.walletinactive);
                } else if (framgposition == 0) {
                    Menu menu = bottomNavigationView.getMenu();
                    menu.findItem(R.id.hometab).setIcon(R.drawable.profileactive);
                    menu.findItem(R.id.wallettab).setIcon(R.drawable.walletinactive);
                    menu.findItem(R.id.messagetab).setIcon(R.drawable.messageinactive);
                    menu.findItem(R.id.profiletab).setIcon(R.drawable.useractive);
                }
            }

        } else {

            if (account != null || !loggedOut) {
                Menu menu = bottomNavigationView.getMenu();
                menu.findItem(R.id.hometab).setIcon(R.drawable.eventinactive);
            } else {
                Menu menu = bottomNavigationView.getMenu();
                menu.findItem(R.id.hometab).setIcon(R.drawable.profileactive);
            }

            super.onBackPressed();
        }
    }


    private void dialog() {
        final Dialog dialog = new Dialog(this);
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
                Intent intent = new Intent(Home_Screen.this, NormalPublishProfile.class);
                intent.putExtra("normal_edit", "0");
                intent.putExtra("backongendercheck", "0");
                startActivity(intent);
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

    public void setDialogOptions() {

        final Dialog dialog = new Dialog(Home_Screen.this);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        dialog.setContentView(R.layout.eventadvertdialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        LinearLayout addEvent = dialog.findViewById(R.id.addeventlayout);
        LinearLayout addAdvert = dialog.findViewById(R.id.addadvertlayout);
        dialog.show();

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, Add_Event_Activity.class);
                startActivity(intent);
            }
        });

        addAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this, Add_Adverts.class);
                startActivity(intent);
            }
        });
    }
}


