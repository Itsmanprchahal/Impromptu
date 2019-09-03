package com.mandywebdesign.impromptu.messages;


import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessAdapter.ChatBox_Adapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroChat;
import com.mandywebdesign.impromptu.Retrofit.RetroGetMessages;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatBox extends Fragment {

    SwipeRefreshLayout pullToRefresh;
    NotificationManager notificationManager;
    View view;
    ImageView back, add_smuiley;
    SwipeRefreshLayout swipeRefreshLayout;
    RoundedImageView event_image;
    TextView title;
    FragmentManager manager;
    EditText typemess;
    ImageView sendmessg;
    String seen_status;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    String eventID, titl, image, BToken, S_Token, userId, hostUserID;
    SharedPreferences sharedPreferences;
    ArrayList<RetroGetMessages.Datum> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat_box, container, false);
        manager = getActivity().getSupportFragmentManager();
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        progressDialog = ProgressBarClass.showProgressDialog(getContext(), "please wait...");
        progressDialog.dismiss();

        sharedPreferences = getContext().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");
        userId = sharedPreferences.getString("userID", "");
        Bundle bundle = getArguments();


        init();
        listerners();

        if (bundle != null) {
            titl = bundle.getString("event_title");
            image = bundle.getString("event_image");
            eventID = bundle.getString("eventID");
            hostUserID = bundle.getString("event_host_user");
            seen_status = bundle.getString("seen_status");



            title.setText(titl);
            Glide.with(this).load(image).into(event_image);
        }

        if (!BToken.equals("")) {
            getChat("Bearer " + BToken, eventID, seen_status);

            Log.d("chatadata", "" + BToken + " " + eventID);
        } else if (!S_Token.equals("")) {
            Log.d("chatadata1", "" + S_Token + " " + eventID);
            getChat("Bearer " + S_Token, eventID, seen_status);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                if (!BToken.equals("")) {
                    getChat("Bearer " + BToken, eventID, seen_status);

                    Log.d("chatadata", "" + BToken + " " + eventID);
                } else if (!S_Token.equals("")) {
                    Log.d("chatadata1", "" + S_Token + " " + eventID);
                    getChat("Bearer " + S_Token, eventID, seen_status);
                }
            }
        });

        return view;
    }

    private void listerners() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_frame_layout, new Messages());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        sendmessg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = typemess.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    if (!BToken.equals("")) {
                        SendMesg("Bearer " + BToken, eventID, titl, message);
                        typemess.setText("");
                        getChat("Bearer " + BToken, eventID, seen_status);
                    } else if (!S_Token.equals("")) {
                        SendMesg("Bearer " + S_Token, eventID, titl, message);
                        typemess.setText("");
                        getChat("Bearer " + S_Token, eventID, seen_status);
                    }
                }
            }
        });
    }


    public void SendMesg(String token, final String eventID, final String titl, String message) {
        Call<RetroChat> call = WebAPI.getInstance().getApi().chat(token, eventID, titl, message, userId);
        call.enqueue(new Callback<RetroChat>() {
            @Override
            public void onResponse(Call<RetroChat> call, Response<RetroChat> response) {
                if (response.body()!=null)
                {

                }else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RetroChat> call, Throwable t) {
                if (NoInternet.isOnline(getContext()) == false) {

                    NoInternet.dialog(getContext());
                }else {
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {

        Home_Screen.bottomNavigationView.setVisibility(View.GONE);
        back = view.findViewById(R.id.chat_back);
        event_image = view.findViewById(R.id.chat_iamge);
        title = view.findViewById(R.id.chat_title);
        // add_smuiley = view.findViewById(R.id.add_smiley);
        typemess = view.findViewById(R.id.type_messeage);
        sendmessg = view.findViewById(R.id.send_mesg);
        recyclerView = view.findViewById(R.id.chats_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
    }

    public void getChat(String token, String eventID, String seenStatus) {
        Call<RetroGetMessages> chatCall = WebAPI.getInstance().getApi().getMessages(token, eventID, seen_status);

        chatCall.enqueue(new Callback<RetroGetMessages>() {
            @Override
            public void onResponse(Call<RetroGetMessages> call, Response<RetroGetMessages> response) {
                if (response.body()!=null)
                {
                    arrayList.clear();
                    if (response.body().getStatus().equals("200")) {
                        arrayList.clear();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            RetroGetMessages.Datum datum = response.body().getData().get(i);
                            arrayList.add(datum);
//                        Collections.reverse(arrayList);
                            setAdapter(arrayList);
                        }
                    } else {
                        //Toast.makeText(getContext(), ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RetroGetMessages> call, Throwable t) {
                if (NoInternet.isOnline(getContext()) == false) {
                    NoInternet.dialog(getContext());
                }else {
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAdapter(ArrayList<RetroGetMessages.Datum> arrayList) {
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.scrollToPosition(arrayList.size() - 1);
        ChatBox_Adapter adapter = new ChatBox_Adapter(getContext(), userId, arrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
