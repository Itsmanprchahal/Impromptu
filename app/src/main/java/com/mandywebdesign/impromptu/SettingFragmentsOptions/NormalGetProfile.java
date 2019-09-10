package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Adapters.NormalUSerSetQues_answer;
import com.mandywebdesign.impromptu.Adapters.NormalUserAttendingEvents;
import com.mandywebdesign.impromptu.Adapters.NormalUserLiveEvents;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.Normal_past_booked;
import com.mandywebdesign.impromptu.Retrofit.RetroLiveEvents;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NormalGetProfile extends AppCompatActivity {

    public static TextView user_profile_age, username, status;
    RoundedImageView userImage;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView questionRecycler, hostRecycler,eventsAttendingRecycler;
    Toolbar toolbar;
    TextView totalpoints;
    ImageView back;
    TextView normal_user_gender;
    String userToken;
    ImageView editprofile;
    Dialog progressDialog;
    FragmentManager manager;
    TextView totlaEvents,pastEvents;
    public static String s_username, s_image,getS_username="",getProfileStatus="",getNormalUserImage,getUserImage,getgender;
    public static ArrayList<String> images = new ArrayList<>();
    public static ArrayList<String> eventTitle = new ArrayList<>();
    public static ArrayList<String> attendingimage = new ArrayList<>();
    public static ArrayList<String> attentingTietle= new ArrayList<>();
    public static ArrayList<String> Questions = new ArrayList<>();
    public static ArrayList<String> Answer = new ArrayList<>();
    public static ArrayList<String> QA_id = new ArrayList<>();
    static String prfileAge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_get_profile);

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("Socailtoken", "");
        s_username = sharedPreferences.getString("Social_username", "");
        s_image = sharedPreferences.getString("Social_image", "");


        progressDialog = ProgressBarClass.showProgressDialog(this);
        Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);

        progressDialog.show();
        init();
        listeners();


        getProfile(userToken);
        getLiveEvents(userToken);
        getattendingEvents(userToken);

    }

    private void getLiveEvents(String userToken) {

        Call<RetroLiveEvents> call = WebAPI.getInstance().getApi().liveEvents("Bearer " + userToken, "application/json");
        call.enqueue(new Callback<RetroLiveEvents>() {
            @Override
            public void onResponse(Call<RetroLiveEvents> call, Response<RetroLiveEvents> response) {

                if (response.body()!=null)
                {
                    if (response.body().getStatus().equals("200")) {
                        RetroLiveEvents retroLiveEvents = response.body();
                        List<RetroLiveEvents.Datum> datumList = retroLiveEvents.getData();
                        images.clear();
                        eventTitle.clear();

                        for (RetroLiveEvents.Datum datum : datumList) {
                            eventTitle.add(datum.getCategory());
                            images.add(datum.getFile());
                            totlaEvents.setText("( " + String.valueOf(images.size()) + " )");

                            LinearLayoutManager layoutManager = new LinearLayoutManager(NormalGetProfile.this, LinearLayoutManager.HORIZONTAL, false);
                            hostRecycler.setLayoutManager(layoutManager);

                            NormalUserLiveEvents adapter = new NormalUserLiveEvents(NormalGetProfile.this);
                            hostRecycler.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }else {
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

    private void getattendingEvents(final String userToken)
    {
        Call<Normal_past_booked> call = WebAPI.getInstance().getApi().past_booked("Bearer "+userToken, "application/json");
        call.enqueue(new Callback<Normal_past_booked>() {
            @Override
            public void onResponse(Call<Normal_past_booked> call, Response<Normal_past_booked> response) {

                Log.d("+++++++++", "++ response ++" + userToken);
                if (response.body()!=null)
                {
                    if (response.body().getStatus().equals("200")) {

                        Normal_past_booked data = response.body();
                        List<Normal_past_booked.Datum> datumArrayList = data.getData();

                        attentingTietle.clear();
                        attendingimage.clear();

                        for (Normal_past_booked.Datum datum : datumArrayList) {

                            Log.d("cates", "" + datum.getCategory());

                            attendingimage.add(datum.getFile().get(0));
                            attentingTietle.add(datum.getTitle().toString());
                            pastEvents.setText("( "+attentingTietle.size()+" )");


                            LinearLayoutManager layoutManager = new LinearLayoutManager(NormalGetProfile.this, LinearLayoutManager.HORIZONTAL, false);
                            eventsAttendingRecycler.setLayoutManager(layoutManager);


                            NormalUserAttendingEvents adapter = new NormalUserAttendingEvents(NormalGetProfile.this);
                            eventsAttendingRecycler.setAdapter(adapter);
                        }
                    } else if (response.body().getStatus().equals("400")) {
                        pastEvents.setText("( "+attentingTietle.size()+" )");
                    }
                    progressDialog.dismiss();
                }else {
                    Intent intent = new Intent(NormalGetProfile.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<Normal_past_booked> call, Throwable t) {
                if (NoInternet.isOnline(NormalGetProfile.this)==false)
                {
                    progressDialog.dismiss();

                    NoInternet.dialog(NormalGetProfile.this);
                }
            }
        });

    }

    private void getProfile(String userToken) {
        Call<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> call = WebAPI.getInstance().getApi().normalGetPRofile("Bearer " + userToken, "application/json");
        call.enqueue(new Callback<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile>() {
            @Override
            public void onResponse(Call<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> call, Response<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> response) {

                if (response.body()!=null)
                {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("200"))
                    {
                        prfileAge = response.body().getData().get(0).getAge();
                        if (prfileAge !=null) {
                            Log.d("normal", "" + response.body().getStatus() + "\n" + response.body().getData().get(0).getUsername());

                            editor = sharedPreferences.edit();
                            editor.putString("Username", response.body().getData().get(0).getUsername());
                            getS_username = response.body().getData().get(0).getUsername();
                            getProfileStatus = response.body().getData().get(0).getStatus().toString();
                            getUserImage = response.body().getData().get(0).getImage();
                            getNormalUserImage =response.body().getData().get(0).getImage();
                            getgender = response.body().getData().get(0).getGender();
                            editor.putString("UserImage",getNormalUserImage);
                            username.setText(getS_username);
                            normal_user_gender.setText(response.body().getData().get(0).getGender());
                            user_profile_age.setText( "("+response.body().getData().get(0).getAge()+")");
                            status.setText(getProfileStatus);
                            if (response.body().getData().get(0).getRating_points()!=null)
                            {
                                totalpoints.setText(response.body().getData().get(0).getRating_points());
                            }

                            Glide.with(NormalGetProfile.this).load(response.body().getData().get(0).getImage().toString()).into(userImage);
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

                }else {
                    Intent intent = new Intent(NormalGetProfile.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<com.mandywebdesign.impromptu.Retrofit.NormalGetProfile> call, Throwable t) {
                if (NoInternet.isOnline(NormalGetProfile.this)==false)
                {
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

                if (prfileAge !=null) {
                    Intent intent = new Intent(NormalGetProfile.this,NormalPublishProfile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("normal_edit","1");
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(NormalGetProfile.this,NormalPublishProfile.class);
                    intent.putExtra("normal_edit","0");
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
        back =(ImageView)findViewById(R.id.back_user_profile);
        hostRecycler = (RecyclerView) findViewById(R.id.user_profile_events_recycler);
        totlaEvents = (TextView) findViewById(R.id.normal_user_total_live_events);
        eventsAttendingRecycler=(RecyclerView)findViewById(R.id.user_profile_eventAttend_recycler);
        pastEvents =findViewById(R.id.normal_user_total_past_events);
        normal_user_gender = findViewById(R.id.normal_user_gender);
        totalpoints = findViewById(R.id.totalpoints);

    }
}
