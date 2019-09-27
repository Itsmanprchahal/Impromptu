package com.mandywebdesign.impromptu.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Adapters.B_EventDetailImageAdapter;
import com.mandywebdesign.impromptu.Adapters.Booked_users;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessUserPRofileActivity;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessUserProfile;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.SeeAll_activity;
import com.mandywebdesign.impromptu.Retrofit.FollowUnfollow;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.NormalGetProfile;
import com.mandywebdesign.impromptu.Utils.Constants;
import com.mandywebdesign.impromptu.messages.ChatBoxActivity;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.BookFreeEvents;
import com.mandywebdesign.impromptu.Retrofit.Booked_User;
import com.mandywebdesign.impromptu.Retrofit.EventMessageClick;
import com.mandywebdesign.impromptu.Retrofit.NormalRetroFav;
import com.mandywebdesign.impromptu.Retrofit.NormalRetrodeleteFav;
import com.mandywebdesign.impromptu.Retrofit.RemainingTickets;
import com.mandywebdesign.impromptu.Retrofit.RetroGetEventData;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.NormalPublishProfile;
import com.mandywebdesign.impromptu.Utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private LinearLayout dotsLayout;
    ImageView sharevent, screenshot;
    private TextView[] dots;
    ImageView book_message, backonbookevent;
    LinearLayout organiser_layout;
    RelativeLayout messagelayout, invite_layouit;
    FragmentManager fragmentManager;
    Button mBookEvent,follow_button;
    TextView organiserName, book_time, book_categry, peoplegoing, seeAll, remainingTicketTV, invitefriends, dialogtickttype, link1, link2, link3;
    ViewPager viewPager;
    RecyclerView users;
    ReadMoreTextView descri;
    RoundedImageView host_pic;
    PagerAdapter pagerAdapter;
    Dialog progressDialog;
    TextView ticketPrice, book_location, book_date, totalPrice, event_title, eventprice;
    Spinner spinner;
    Button dialogButoon;
    public static CheckBox addtoFavCheck_box;
    View view;
    String checkgender, booklink1, booklink2, booklink3;
    SharedPreferences.Editor editor, editorItemPos;
    SharedPreferences sharedPreferences, itemPositionPref;
    Bundle bundle;
    ArrayList<String> userImage = new ArrayList<>();
    static String tot;
    static String InviteFriend;
    static String total_ticket;
    static String loginUserId;
    static String hostUserID;
    static String remaini_tickets;
    static String timeFrom;
    static String timeTo,usertype;
    String itemPos;
    static String value, S_token, fav_id, hostname, payvalue, spinnerposition;
    public static ArrayList<String> image = new ArrayList<>();
    public static String id, cate, host_image, date, decs,follow_status, postcode, ticktType, ticktprice, timefrom, hostimage, timeto, title, location, location2, city, gender, andendeenumber, numberoftickts, freeEvent, username;
    int CurrentPage = 0;
    AlertDialog.Builder builder;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_event);
        builder = new AlertDialog.Builder(BookEventActivity.this);
        builder.setMessage("Coming soon...");

        fragmentManager = getSupportFragmentManager();
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        itemPositionPref = getSharedPreferences("ItemPosition", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editorItemPos = itemPositionPref.edit();
        S_token = sharedPreferences.getString("Socailtoken", "");
        loginUserId = sharedPreferences.getString("userID", "");
        checkgender = sharedPreferences.getString("profilegender", "");
        itemPos = itemPositionPref.getString("itemPosition", "");

        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();
        Drawable drawable = new ProgressBar(BookEventActivity.this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(BookEventActivity.this, R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);


        if (!S_token.equalsIgnoreCase(""))
        {
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            // Get deep link from result (may be null if no link is found)
                            Uri deepLink = null;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
                                String path = deepLink.getPath();
                                Log.d("deeplink1", path.toString());
                                String[] evetdata = path.split("/");
                                String eventID = evetdata[2];
                                Log.d("deeplink", eventID);
                                getEventData(eventID);

                                addFav(eventID);
                                getRaminingEvents(S_token, eventID);
                                getUsers(S_token, eventID);
                                bookevent(eventID);
                                gotoEventMesg(eventID);
                            } else {
                                getEventData(value);
                                addFav(value);
                                getRaminingEvents(S_token, value);
                                getUsers(S_token, value);
                                bookevent(value);
                                gotoEventMesg(value);
                            }

                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("dynamiclink", "getDynamicLink:onFailure", e);
                        }
                    });
        }else {
            Intent intent = new Intent(this,Join_us.class);
            startActivity(intent);
        }



        init();
        listeners();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(BookEventActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        users.setLayoutManager(layoutManager);
        users.setNestedScrollingEnabled(false);

        intent = getIntent();
        if (intent != null) {
            value = intent.getStringExtra("event_id");
            payvalue = intent.getStringExtra("back_pay");

            //  fav_id = bundle.getString("fav_id");
            hostname = intent.getStringExtra("hostname");
            hostimage = intent.getStringExtra("hostImage");
            if (hostname != null) {
                String[] name = hostname.split(" ");
                if (name.length == 1) {
                    String Fname = name[0];
                    organiserName.setText(Fname + " ");
                } else {
                    String Fname = name[0];
                    String Lname = name[1];
                    organiserName.setText(Fname + " " + Lname.subSequence(0, 1));
                }


            } else {
                organiserName.setText(hostname);
            }


            book_message.setVisibility(View.VISIBLE);
        }


    }

    private void gotoEventMesg(final String value) {
        book_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent = new Intent(BookEventActivity.this, ChatBoxActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("event_title", title);
                intent.putExtra("event_image", image.get(0));
                intent.putExtra("eventID", value);
                intent.putExtra("event_host_user", hostUserID);


                Call<EventMessageClick> call = WebAPI.getInstance().getApi().eventMEsgClick("Bearer " + S_token, id);
                call.enqueue(new Callback<EventMessageClick>() {
                    @Override
                    public void onResponse(Call<EventMessageClick> call, Response<EventMessageClick> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("200")) {
                                startActivity(intent);
                            }

                        } else {
                            Intent intent = new Intent(BookEventActivity.this, NoInternetScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<EventMessageClick> call, Throwable t) {
                        Toast.makeText(BookEventActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void bookevent(final String value) {
        mBookEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkgender.equals("")) {
                    dialogUpdate();
                } else {
                    dialog(value);
                }
            }
        });
    }

    //check remainig tickets
    private void getRaminingEvents(String s_token, String id) {

        Call<RemainingTickets> call = WebAPI.getInstance().getApi().remainingTickets("Bearer " + s_token, id);
        call.enqueue(new Callback<RemainingTickets>() {
            @Override
            public void onResponse(Call<RemainingTickets> call, Response<RemainingTickets> response) {

                if (response.body() != null) {
                    remaini_tickets = String.valueOf(response.body().getData().getRemaining());
                    remainingTicketTV.setText("Tickets Remaining " + remaini_tickets);
                } else {
                    Intent intent = new Intent(BookEventActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RemainingTickets> call, Throwable t) {
                Toast.makeText(BookEventActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        follow_button = findViewById(R.id.follow_button);
        screenshot = findViewById(R.id.screenshot);
        backonbookevent = findViewById(R.id.backonbookevent);
        book_time = findViewById(R.id.book_time);
        book_categry = findViewById(R.id.book_categry);
        eventprice = findViewById(R.id.event_price);
        dotsLayout = findViewById(R.id.book_indicator);
        viewPager = (ViewPager) findViewById(R.id.book_viewpager);
        addtoFavCheck_box = (CheckBox) findViewById(R.id.book_event_favourite);
        mBookEvent = (Button) findViewById(R.id.book_event_book_bt);
        descri = findViewById(R.id.book_description);
        book_location = findViewById(R.id.book_location);
        book_date = findViewById(R.id.book_date);
        event_title = findViewById(R.id.book_tittle);
        host_pic = findViewById(R.id.book_user_picture);
        organiserName = findViewById(R.id.book_organiser_name);
        peoplegoing = findViewById(R.id.book_attendess_people);
        users = findViewById(R.id.book_recyclerView);
        seeAll = findViewById(R.id.book_seeAll);
        book_message = findViewById(R.id.book_message);
        organiser_layout = findViewById(R.id.organiser_layout);
        messagelayout = findViewById(R.id.messagelayout);
        invite_layouit = findViewById(R.id.invite_layouit);
        remainingTicketTV = findViewById(R.id.remainingTicketTV);
        invitefriends = findViewById(R.id.invitefriends);
        link1 = findViewById(R.id.book_link1);
        link2 = findViewById(R.id.book_link2);
        link3 = findViewById(R.id.book_link3);
        sharevent = findViewById(R.id.sharevent);
    }

    private void listeners() {

        sharevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "https://play.google.com/store";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(share, "Testing Impromptu"));
            }
        });

        backonbookevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookEventActivity.this, Home_Screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                editorItemPos.putString(Constants.itemPosition, itemPos);
                editorItemPos.apply();
                startActivity(intent);
                finish();
                spinnerposition = "0";
            }
        });

        invite_layouit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("https://www.amit.com/" +"event_id/"+ value))
                        .setDomainUriPrefix("impromptusocial.page.link")
                        // Open links with this app on Android  amitpandey12.page.link
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                        // Open links with com.example.ios on iOS
                        .setIosParameters(new DynamicLink.IosParameters.Builder("impromptusocial.page.link").build())
                        .buildDynamicLink();

                Uri dynamicLinkUri = dynamicLink.getUri();

                Log.d("hello123", "1" + dynamicLink.getUri());


                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLongLink(Uri.parse("https://" + dynamicLink.getUri().toString()))
                        .buildShortDynamicLink()
                        .addOnCompleteListener(BookEventActivity.this, new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    // Short link created
                                    Uri shortLink = task.getResult().getShortLink();
                                    Uri flowchartLink = task.getResult().getPreviewLink();
                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("text/plain");
                                    share.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                                    startActivity(Intent.createChooser(share, "Testing Impromptu"));


                                } else {

                                }
                            }
                        });

            }
        });

        builder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookEventActivity.this, SeeAll_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("value", id);
                startActivity(intent);
            }
        });


        link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(booklink1);
                if (uri.getPath().contains("https://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Uri uri1 = Uri.parse("https://" + uri);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri1);
                    startActivity(intent);
                }
            }
        });

        link2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(booklink2);

                if (uri.getPath().contains("https://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Uri uri1 = Uri.parse("https://" + uri);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri1);
                    startActivity(intent);
                }

            }
        });

        link3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(booklink3);
                if (uri.getPath().contains("https://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Uri uri1 = Uri.parse("https://" + uri);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri1);
                    startActivity(intent);
                }
            }
        });

        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<FollowUnfollow> call = WebAPI.getInstance().getApi().followunfollow("Bearer " + S_token,hostUserID);
                call.enqueue(new Callback<FollowUnfollow>() {
                    @Override
                    public void onResponse(Call<FollowUnfollow> call, Response<FollowUnfollow> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getMessage().equals("Follow successfully."))
                            {
                                follow_button.setText("Unfollow");
                            }else {
                                follow_button.setText("Follow");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FollowUnfollow> call, Throwable t) {

                    }
                });
            }
        });

        host_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (usertype.equals("business"))
                {
                    Intent intent = new Intent(BookEventActivity.this, BusinessUserPRofileActivity.class);
                    intent.putExtra("user_id",hostUserID);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(BookEventActivity.this, NormalGetProfile.class);
                    intent.putExtra("user_id",hostUserID);
                    startActivity(intent);
                }

            }
        });
    }


    public void dialog(final String value) {
        String ticketNum[] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        final Dialog dialog = new Dialog(BookEventActivity.this);
        dialog.setContentView(R.layout.custom_dialog_book_ticket);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        FindId(dialog);

        ticketPrice.setText(ticktprice);
        dialogtickttype.setText(ticktType);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BookEventActivity.this,
                android.R.layout.simple_spinner_item, ticketNum);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        if (spinnerposition != null) {
            spinner.setSelection(Integer.parseInt(spinnerposition) - 1);
        }


        if (ticktprice.equals("0")) {

            dialogButoon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Float TotalTIcket = Float.valueOf(((total_ticket)));

                    Float RemainingTIckets = Float.valueOf((remaini_tickets));
                    if (TotalTIcket > RemainingTIckets) {
                        Toast.makeText(BookEventActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.show();
                        Call<BookFreeEvents> call = WebAPI.getInstance().getApi().freebookevent("Bearer " + S_token, value, total_ticket);
                        call.enqueue(new Callback<BookFreeEvents>() {
                            @Override
                            public void onResponse(Call<BookFreeEvents> call, Response<BookFreeEvents> response) {

                                if (response.body() != null) {
                                    progressDialog.dismiss();
                                    dialog.dismiss();

                                    Intent intent = new Intent(BookEventActivity.this, ConfirmationActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    intent.putExtra("eventID", value);
                                    intent.putExtra("paid", value);
                                    editor.putString("eventImage", BookEventActivity.image.get(0));
                                    editor.apply();
                                    startActivity(intent);

                                } else {
                                    Intent intent = new Intent(BookEventActivity.this, NoInternetScreen.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<BookFreeEvents> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(BookEventActivity.this, "Book Error=> " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

        } else {
            dialogButoon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BookEventActivity.this, PayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("total_price", tot);
                    intent.putExtra("event_id", value);
                    intent.putExtra("event_Title", title);
                    intent.putExtra("total_tickets", total_ticket);
                    intent.putExtra("ticket_Price", ticktprice);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
        }

        dialog.show();
    }


    private void FindId(Dialog dialog) {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        ticketPrice = dialog.findViewById(R.id.dailog_ticket_price);
        totalPrice = dialog.findViewById(R.id.dailog_total_price);
        spinner = dialog.findViewById(R.id.dailog_spinner);
        dialogButoon = dialog.findViewById(R.id.dailog_button);
        dialogtickttype = dialog.findViewById(R.id.dailog_ticket_type);
    }

    private void dialogUpdate() {
        final Dialog dialog = new Dialog(BookEventActivity.this);
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
                Intent intent = new Intent(BookEventActivity.this, NormalPublishProfile.class);
                intent.putExtra("normal_edit", "0");
                startActivity(intent);
                finish();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spinnerposition = parent.getItemAtPosition(position).toString();


        Float a = Float.valueOf((spinnerposition));
        total_ticket = String.valueOf(a);

        String tickt = ticketPrice.getText().toString();
        Float b = Float.valueOf((tickt));

        Float total = a * b;

        tot = String.valueOf(total);

        totalPrice.setText(tot);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void getUsers(String s_token, String value) {

        Call<Booked_User> call = WebAPI.getInstance().getApi().booked_users("Bearer " + s_token, value);
        call.enqueue(new Callback<Booked_User>() {
            @Override
            public void onResponse(Call<Booked_User> call, Response<Booked_User> response) {

                if (response.body() != null) {

                    Booked_User booked_users = response.body();
                    List<Booked_User.Datum> bookedUsersList = booked_users.getData();
                    userImage.clear();

                    if (response.body().getStatus().equals("200")) {
                        for (int i = 0; i < bookedUsersList.size(); i++) {
                            userImage.add(response.body().getData().get(i).getFile());
                            peoplegoing.setText(bookedUsersList.size() + " People are going");
                            Log.d("userImage", "" + response.body().getData().get(i).toString());

                        }
                    } else {
                        peoplegoing.setText("0 People are going");
                        seeAll.setClickable(false);
                        seeAll.setVisibility(View.GONE);
                        users.setVisibility(View.GONE);
                    }

                    Booked_users adapter = new Booked_users(BookEventActivity.this, userImage);
                    users.setAdapter(adapter);
                } else {
                    Intent intent = new Intent(BookEventActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Booked_User> call, Throwable t) {
                Toast.makeText(BookEventActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getEventData(final String value) {
        progressDialog.show();

        Call<RetroGetEventData> call = WebAPI.getInstance().getApi().getEvents("Bearer " + S_token, "application/json", value);
        call.enqueue(new Callback<RetroGetEventData>() {
            @Override
            public void onResponse(Call<RetroGetEventData> call, Response<RetroGetEventData> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {
                        RetroGetEventData getEventData = response.body();
                        Log.d("123456", "" + getEventData.getData().size());
                        List<RetroGetEventData.Datum> datumList = getEventData.getData();
                        Log.d("events", "" + response.code() + " " + datumList.size());
                        for (RetroGetEventData.Datum datum : datumList) {
                            id = datum.getEventId().toString();
                            cate = datum.getCategory();
                            decs = datum.getDescription();
                            location = datum.getAddressline1();
                            location2 = datum.getAddressline2();
                            postcode = datum.getPostcode();
                            city = datum.getCity();
                            gender = datum.getAttendeesGender();
                            hostUserID = datum.getUserid().toString();// host id
                            andendeenumber = datum.getAttendeesNo();
                            freeEvent = datum.getFreeEvent();
                            ticktType = datum.getTicketType();
                            usertype = datum.getUser_type();
                            ticktprice = datum.getPrice();
                            follow_status = datum.getFollow_status();
                            if (follow_status.equals("1"))
                            {
                                follow_button.setText("Unfollow");
                            }

                            numberoftickts = datum.getNoOfTickets();
                            username = datum.getBEventHostname();
                            InviteFriend = datum.getAttendeesGender();
                            booklink1 = datum.getLink1();
                            booklink2 = datum.getLink2();
                            booklink3 = datum.getLink3();

                            book_categry.setText(cate);
                            title = datum.getTitle();
                            timefrom = datum.getTimeFrom();
                            timeto = datum.getTimeTo();
                            host_image = datum.getHostImage();
                            image = (ArrayList<String>) datum.getFile().get(0).getImg();
//                            Collections.reverse(image);

                            if (ticktprice.equals("0")) {
                                eventprice.setText("Free");
                            } else {
                                eventprice.setText("£ " + ticktprice);
                            }
                            if (!datum.getLink1().isEmpty()) {
                                SpannableString content = new SpannableString(datum.getLink1().toString());
                                content.setSpan(new UnderlineSpan(), 0, datum.getLink1().length(), 0);
                                link1.setText(content);
                                link1.setVisibility(View.VISIBLE);
                            }
                            if (!datum.getLink2().isEmpty()) {
                                SpannableString content = new SpannableString(datum.getLink2());
                                content.setSpan(new UnderlineSpan(), 0, datum.getLink2().length(), 0);
                                link2.setText(content);
                                link2.setVisibility(View.VISIBLE);
                            }
                            if (!datum.getLink3().isEmpty()) {
                                SpannableString content = new SpannableString(datum.getLink3());
                                content.setSpan(new UnderlineSpan(), 0, datum.getLink3().length(), 0);
                                link3.setText(content);
                                link3.setVisibility(View.VISIBLE);
                            }

                            fav_id = datum.getFavourite().toString();
                            Glide.with(BookEventActivity.this).load(host_image).into(host_pic);

                            Collections.reverse(image);
                            //get Time to in AM PM

                            String time_t = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM", "am").replaceFirst("PM", "pm");
                            String time_to = Util.convertTimeStampToTime(Long.parseLong(datum.getEventEndDt())).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM", "am").replaceFirst("PM", "pm");
                            String start_date =Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt()));
                            String end_date = Util.convertTimeStampDate(Long.parseLong(datum.getEventEndDt()));

                            if (time_t.startsWith("0") && time_to.startsWith("0")) {
                                timeFrom = time_t.substring(1);
                                timeTo = time_to.substring(1);
                                book_time.setText(timeFrom + " - " + timeTo);
                                book_date.setText(start_date+ " - " + end_date);
                            } else if (time_t.startsWith("0")) {
                                timeFrom = time_t.substring(1);
                                if (time_to.startsWith("0")) {
                                    timeTo = time_to.substring(1);
                                    book_time.setText(timeFrom + " - " + timeTo);
                                    book_date.setText(start_date + " - " + end_date);
                                } else {
                                    timeTo = time_to.substring(0);
                                    book_time.setText(timeFrom + " - " + timeTo);
                                    book_date.setText(start_date + " - " + end_date);
                                }
                            } else if (time_to.startsWith("0")) {
                                timeTo = time_to.substring(1);
                                if (time_t.startsWith("0")) {
                                    timeFrom = time_t.substring(1);
                                    book_time.setText(timeFrom + " - " + timeTo);
                                    book_date.setText(start_date + " - " + end_date);
                                } else {
                                    timeFrom = time_t.substring(0);
                                    book_time.setText(timeFrom + " - " + timeTo);
                                    book_date.setText(start_date + " - " + end_date);
                                }
                            } else if (!time_t.startsWith("0") && !time_to.startsWith("0")) {
                                timeFrom = time_t.substring(0);
                                timeTo = time_to.substring(0);
                                book_time.setText(timeFrom + " - " + timeTo);
                                book_date.setText(start_date + " - " + end_date);
                            }


                            Log.d("hostImage", "" + host_image);

                            //Check Event Fav Status
                            if (fav_id.equals("1")) {
                                addtoFavCheck_box.setChecked(true);
                            } else if (fav_id.equals("0")) {
                                addtoFavCheck_box.setChecked(false);
                            }
                            viewPager.setOffscreenPageLimit(1);
                            pagerAdapter = new B_EventDetailImageAdapter(BookEventActivity.this, image);
                            viewPager.setAdapter(pagerAdapter);
                            Log.d("image", "" + image);

                            addBottomDots(0);
                            //indicator.setViewPager(viewPager);
                            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


                                @Override
                                public void onPageScrolled(int i, float v, int i1) {
                                    CurrentPage = i;
                                }

                                @Override
                                public void onPageSelected(int i) {
                                    addBottomDots(i);
                                }

                                @Override
                                public void onPageScrollStateChanged(int i) {

                                    if (i == ViewPager.SCROLL_STATE_IDLE) {

                                        int pagecount = image.size();

                                        if (CurrentPage == pagecount) {
                                            viewPager.setCurrentItem(pagecount, true);
                                        } else
                                            CurrentPage++;
                                    }

                                }
                            });

                            descri.setText(decs);
                            book_location.setText(location);
                            event_title.setText(title);
                            invitefriends.setText("Invite Friends " + "( " + InviteFriend + " )");



                            /*id_code*/

                            if (loginUserId.equals(hostUserID)) {
                                book_message.setVisibility(View.INVISIBLE);
                                organiser_layout.setVisibility(View.GONE);
                                messagelayout.setVisibility(View.GONE);
                                mBookEvent.setVisibility(View.GONE);
                            } else if (hostUserID == (null)) {
                                book_message.setVisibility(View.INVISIBLE);
                            } else {
                                book_message.setVisibility(View.VISIBLE);
                            }
                            Log.d("Check_User", "" + loginUserId + "  " + hostUserID);
                        }
                    } else if (response.body().getStatus().equals("400")) {
                        Toast.makeText(BookEventActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } else {
                    Intent intent = new Intent(BookEventActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RetroGetEventData> call, Throwable t) {
                Log.d("events1", "" + t.getMessage() + " " + value);
                Toast.makeText(BookEventActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public void addFav(final String value) {

        addtoFavCheck_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if (!fav_id.contains("1")) {
                        Call<NormalRetroFav> call = WebAPI.getInstance().getApi().fav("Bearer " + S_token, "application/json", value);
                        call.enqueue(new Callback<NormalRetroFav>() {
                            @Override
                            public void onResponse(Call<NormalRetroFav> call, Response<NormalRetroFav> response) {
                                if (response.body() != null) {

                                }
                                //Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<NormalRetroFav> call, Throwable t) {

                            }
                        });
                    }

                } else {
                    Call<NormalRetrodeleteFav> call = WebAPI.getInstance().getApi().deletefav("Bearer " + S_token, "application/json", value);
                    call.enqueue(new Callback<NormalRetrodeleteFav>() {
                        @Override
                        public void onResponse(Call<NormalRetrodeleteFav> call, Response<NormalRetrodeleteFav> response) {
                            if (response.body() != null) {

                            }
                            //  Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<NormalRetrodeleteFav> call, Throwable t) {

                        }
                    });
                }
            }
        });

    }


    //add dots at bottom
    private void addBottomDots(int currentPage) {

        dots = new TextView[image.size()];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(BookEventActivity.this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(50);
            dots[i].setTextColor(getResources().getColor(R.color.colortextwhite));
            dotsLayout.addView(dots[i]);

        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(getResources().getColor(R.color.colorTheme));
        }

    }

}
