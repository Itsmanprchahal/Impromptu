package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Adapters.NormalUSerSetQues_answer;
import com.mandywebdesign.impromptu.Adapters.NormalUserAttendingEvents;
import com.mandywebdesign.impromptu.Adapters.NormalUserLiveEvents;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Setting;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.NormalGetProfile;
import com.mandywebdesign.impromptu.Retrofit.Normal_past_booked;
import com.mandywebdesign.impromptu.Retrofit.RetroLiveEvents;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserProfileFragment extends Fragment {


    public static TextView user_profile_age, username, status;
    RoundedImageView userImage;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView questionRecycler, hostRecycler,eventsAttendingRecycler;
    Toolbar toolbar;
    TextView totalpoints;
    View view;
    ImageView back;
    TextView normal_user_gender;
    String userToken;
    ImageView editprofile;
    ProgressDialog progressDialog;
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        manager = getFragmentManager();
        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = sharedPreferences.getString("Socailtoken", "");
        s_username = sharedPreferences.getString("Social_username", "");
        s_image = sharedPreferences.getString("Social_image", "");



        progressDialog = new ProgressDialog(getContext());
        Drawable drawable = new ProgressBar(getContext()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(drawable);

        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        init();
        listeners();


        getProfile(userToken);
        getLiveEvents(userToken);
        getattendingEvents(userToken);


        return view;
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

                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            hostRecycler.setLayoutManager(layoutManager);

                            NormalUserLiveEvents adapter = new NormalUserLiveEvents(getContext());
                            hostRecycler.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
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


                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            eventsAttendingRecycler.setLayoutManager(layoutManager);


                            NormalUserAttendingEvents adapter = new NormalUserAttendingEvents(getContext());
                            eventsAttendingRecycler.setAdapter(adapter);
                        }
                    } else if (response.body().getStatus().equals("400")) {
                        pastEvents.setText("( "+attentingTietle.size()+" )");
                    }
                    progressDialog.dismiss();
                }else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<Normal_past_booked> call, Throwable t) {
                if (NoInternet.isOnline(getContext())==false)
                {
                    progressDialog.dismiss();

                    NoInternet.dialog(getContext());
                }
            }
        });

    }

    private void getProfile(String userToken) {
        Call<NormalGetProfile> call = WebAPI.getInstance().getApi().normalGetPRofile("Bearer " + userToken, "application/json");
        call.enqueue(new Callback<NormalGetProfile>() {
            @Override
            public void onResponse(Call<NormalGetProfile> call, Response<NormalGetProfile> response) {

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

                            Glide.with(getContext()).load(response.body().getData().get(0).getImage().toString()).into(userImage);
                            NormalGetProfile normalGetProfile = response.body();
                            List<NormalGetProfile.Question> datum = normalGetProfile.getData().get(0).getQuestion();

                            Questions.clear();
                            Answer.clear();
                            QA_id.clear();

                            for (NormalGetProfile.Question question : datum) {
                                Questions.add(question.getQuestion());
                                Answer.add(question.getAnswer());
                                QA_id.add(question.getQuestionId().toString());

                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                questionRecycler.setLayoutManager(layoutManager);


                                NormalUSerSetQues_answer adapter = new NormalUSerSetQues_answer(getContext(), Questions, Answer);
                                questionRecycler.setAdapter(adapter);
                            }
                            Log.d("ques", "" + Questions + "\n" + Answer);
                        } else {
                            username.setText(s_username);
                            Glide.with(getContext()).load(s_image).into(userImage);
                        }
                    }

                }else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<NormalGetProfile> call, Throwable t) {
                if (NoInternet.isOnline(getContext())==false)
                {
                    progressDialog.dismiss();

                    NoInternet.dialog(getContext());
                }
            }
        });
    }

    private void listeners() {
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prfileAge !=null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("normal_edit", "1");
                    Normal_user_profile user_profile = new Normal_user_profile();
                    user_profile.setArguments(bundle);

                    manager.beginTransaction().replace(R.id.home_frame_layout, user_profile).addToBackStack(null).commit();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("normal_edit", "0");
                    Normal_user_profile user_profile = new Normal_user_profile();
                    user_profile.setArguments(bundle);

                    manager.beginTransaction().replace(R.id.home_frame_layout, user_profile).addToBackStack(null).commit();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_frame_layout, new Setting());
                transaction.commit();
            }
        });
    }

    private void init() {
        questionRecycler = (RecyclerView) view.findViewById(R.id.user_profile_question_recycle);
        username = (TextView) view.findViewById(R.id.user_profile_username);
        userImage = (RoundedImageView) view.findViewById(R.id.user_profile_userimage);
        editprofile = (ImageView) view.findViewById(R.id.user_profile_edit_toolbar);
        user_profile_age = (TextView) view.findViewById(R.id.user_profile_age);
        status = (TextView) view.findViewById(R.id.user_profile_staus);
        back =(ImageView)view.findViewById(R.id.back_user_profile);
        hostRecycler = (RecyclerView) view.findViewById(R.id.user_profile_events_recycler);
        totlaEvents = (TextView) view.findViewById(R.id.normal_user_total_live_events);
        eventsAttendingRecycler=(RecyclerView)view.findViewById(R.id.user_profile_eventAttend_recycler);
        pastEvents =view.findViewById(R.id.normal_user_total_past_events);
        normal_user_gender = view.findViewById(R.id.normal_user_gender);
        totalpoints = view.findViewById(R.id.totalpoints);

    }
}
