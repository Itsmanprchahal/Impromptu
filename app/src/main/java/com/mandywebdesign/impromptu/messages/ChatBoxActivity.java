package com.mandywebdesign.impromptu.messages;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBoxActivity extends AppCompatActivity {

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
    Dialog progressDialog;
    RecyclerView recyclerView;
    String eventID, titl, image, BToken, S_Token, userId, hostUserID;
    SharedPreferences sharedPreferences;
    ArrayList<RetroGetMessages.Datum> arrayList = new ArrayList<>();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        progressDialog = ProgressBarClass.showProgressDialog(this);

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");
        userId = sharedPreferences.getString("userID", "");
        intent = getIntent();


        init();
        listerners();

        if (intent != null) {
            titl = intent.getStringExtra("event_title");
            image = intent.getStringExtra("event_image");
            eventID = intent.getStringExtra("eventID");
            hostUserID = intent.getStringExtra("event_host_user");
            seen_status = intent.getStringExtra("seen_status");



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

    }
    private void listerners() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
               finish();
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
                    Intent intent = new Intent(ChatBoxActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RetroChat> call, Throwable t) {
                if (NoInternet.isOnline(ChatBoxActivity.this) == false) {

                    NoInternet.dialog(ChatBoxActivity.this);
                }else {
                    Toast.makeText(ChatBoxActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {

        back = findViewById(R.id.chat_back);
        event_image = findViewById(R.id.chat_iamge);
        title = findViewById(R.id.chat_title);
        // add_smuiley = view.findViewById(R.id.add_smiley);
        typemess = findViewById(R.id.type_messeage);
        sendmessg = findViewById(R.id.send_mesg);
        recyclerView = findViewById(R.id.chats_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
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
                    Intent intent = new Intent(ChatBoxActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RetroGetMessages> call, Throwable t) {
                if (NoInternet.isOnline(ChatBoxActivity.this) == false) {
                    NoInternet.dialog(ChatBoxActivity.this);
                }else {
                    Toast.makeText(ChatBoxActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAdapter(ArrayList<RetroGetMessages.Datum> arrayList) {
        LinearLayoutManager linearLayout = new LinearLayoutManager(ChatBoxActivity.this);
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.scrollToPosition(arrayList.size() - 1);
        ChatBox_Adapter adapter = new ChatBox_Adapter(ChatBoxActivity.this, userId, arrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
