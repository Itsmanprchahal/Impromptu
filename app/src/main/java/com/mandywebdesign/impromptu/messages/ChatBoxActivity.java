package com.mandywebdesign.impromptu.messages;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessAdapter.ChatBox_Adapter;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEventDetailAcitvity;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Home;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroChat;
import com.mandywebdesign.impromptu.Retrofit.RetroGetMessages;
import com.mandywebdesign.impromptu.ui.BookEventActivity;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.MainActivity;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.util.ArrayList;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBoxActivity extends AppCompatActivity {

    LinearLayout revealeffect;
    NotificationManager notificationManager;
    View view;
    TextView chat_title;
    ImageView back;
    ImageButton add_smuiley;
    SwipeRefreshLayout swipeRefreshLayout;
    RoundedImageView event_image;
    FragmentManager manager;
    EmojiconEditText typemess;
    ImageView sendmessg;
    String seen_status;
    Dialog progressDialog;
    RecyclerView recyclerView;
    String eventID, titl, image, BToken, S_Token, userId, hostUserID,event_status;
    SharedPreferences sharedPreferences;
    ArrayList<RetroGetMessages.Datum> arrayList = new ArrayList<>();
    Intent intent;
    EmojIconActions emojIcon;
    boolean hidden = true;

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
            event_status = intent.getStringExtra("event_status");

            if (titl.length() > 13) {
                chat_title.setText(titl.substring(0, 12) + "...");
            } else {
                chat_title.setText(titl);
            }
            
            Glide.with(this).load(image).into(event_image);
            if (!BToken.equals("")) {
                getChat("Bearer " + BToken, eventID, seen_status);

                Log.d("chatadata", "" + BToken + " " + eventID);
            } else if (!S_Token.equals("")) {
                Log.d("chatadata1", "" + S_Token + " " + eventID);
                getChat("Bearer " + S_Token, eventID, seen_status);
            }
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
                    } else if (!S_Token.equals("")) {
                        SendMesg("Bearer " + S_Token, eventID, titl, message);
                        typemess.setText("");
                    }
                }
            }
        });



        event_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!BToken.equals(""))
                {
                    Intent intent = new Intent(ChatBoxActivity.this, BusinessEventDetailAcitvity.class);
                    intent.putExtra("event_id",eventID);
                    intent.putExtra("lat", Home.lat);
                    intent.putExtra("lng",Home.lng);
                    intent.putExtra("eventType", "");
                    startActivity(intent);
                }else if (!S_Token.equals(""))
                {
                    Intent intent = new Intent(ChatBoxActivity.this, BookEventActivity.class);
                    intent.putExtra("event_id",eventID);
                    intent.putExtra("lat", Home.lat);
                    intent.putExtra("lng",Home.lng);
                    intent.putExtra("eventType",event_status);
                    startActivity(intent);
                }

            }
        });

        chat_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatBoxActivity.this, BookEventActivity.class);
                intent.putExtra("event_id",eventID);
                intent.putExtra("lat",Home.lat);
                intent.putExtra("lng",Home.lng);
                intent.putExtra("eventType",event_status);
//                intent.putExtra("user_ID",)
                startActivity(intent);
            }
        });
    }


    public void SendMesg(final String token, final String eventID, final String titl, String message) {
        progressDialog.show();
        Call<RetroChat> call = WebAPI.getInstance().getApi().chat(token, eventID, titl, message, userId);
        call.enqueue(new Callback<RetroChat>() {
            @Override
            public void onResponse(Call<RetroChat> call, Response<RetroChat> response) {
                if (response.body() != null) {


//                    Toast.makeText(ChatBoxActivity.this, ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                        getChat( token, eventID, seen_status);


                } else {
                    Intent intent = new Intent(ChatBoxActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RetroChat> call, Throwable t) {
                if (NoInternet.isOnline(ChatBoxActivity.this) == false) {

                    NoInternet.dialog(ChatBoxActivity.this);
                } else {
                    Toast.makeText(ChatBoxActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        revealeffect = findViewById(R.id.revealeffect);
        chat_title = findViewById(R.id.chat_title);
        back = findViewById(R.id.chat_back);
        event_image = findViewById(R.id.chat_iamge);
        add_smuiley = findViewById(R.id.add_smiley);
        typemess = findViewById(R.id.type_messeage);
        sendmessg = findViewById(R.id.send_mesg);
        view = findViewById(R.id.root_view);
        recyclerView = findViewById(R.id.chats_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);

        emojIcon = new EmojIconActions(this, view, typemess, add_smuiley);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("smiley", "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("smiley", "Keyboard closed");
            }
        });
    }

    public void getChat(String token, String eventID, String seenStatus) {
        progressDialog.show();
        Call<RetroGetMessages> chatCall = WebAPI.getInstance().getApi().getMessages(token, eventID, seen_status);

        chatCall.enqueue(new Callback<RetroGetMessages>() {
            @Override
            public void onResponse(Call<RetroGetMessages> call, Response<RetroGetMessages> response) {
                arrayList.clear();
                if (response.body() != null) {

                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("200")) {

                        arrayList.clear();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            RetroGetMessages.Datum datum = response.body().getData().get(i);
                            arrayList.add(datum);
//                        Collections.reverse(arrayList);
                            setAdapter(arrayList);
                        }

                    } else {
                    }
                } else {
                    Intent intent = new Intent(ChatBoxActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RetroGetMessages> call, Throwable t) {
                progressDialog.dismiss();
                if (NoInternet.isOnline(ChatBoxActivity.this) == false) {
                    NoInternet.dialog(ChatBoxActivity.this);
                } else {
                    Toast.makeText(ChatBoxActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("HERE",t.getMessage());
                }
            }
        });
    }

    private void setAdapter(ArrayList<RetroGetMessages.Datum> arrayList) {
        LinearLayoutManager linearLayout = new LinearLayoutManager(ChatBoxActivity.this);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.scrollToPosition(arrayList.size() - 1);
        ChatBox_Adapter adapter = new ChatBox_Adapter(ChatBoxActivity.this, userId, arrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /*###################Code to Close the keyboard when your touch anywhere############*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    private void makeEffect(final LinearLayout layout,int cx,int cy){

        int radius = Math.max(layout.getWidth(), layout.getHeight());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            SupportAnimator animator =
                    ViewAnimationUtils.createCircularReveal(layout, cx, cy, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(800);

            SupportAnimator animator_reverse = animator.reverse();

            if (hidden) {
                layout.setVisibility(View.VISIBLE);
                animator.start();
                hidden = false;
            } else {
                animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        layout.setVisibility(View.INVISIBLE);
                        hidden = true;

                    }

                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationRepeat() {

                    }
                });
                animator_reverse.start();

            }
        } else {
            if (hidden) {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(layout, cx, cy, 0, radius);
                layout.setVisibility(View.VISIBLE);
                anim.start();
                hidden = false;

            } else {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(layout, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layout.setVisibility(View.INVISIBLE);
                        hidden = true;
                    }
                });
                anim.start();

            }
        }
    }
}
