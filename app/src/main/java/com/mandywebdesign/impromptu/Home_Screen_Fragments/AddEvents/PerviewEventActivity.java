package com.mandywebdesign.impromptu.Home_Screen_Fragments.AddEvents;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Adapters.PerviewImageAdpater;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessUserProfile;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroAddEvent;
import com.mandywebdesign.impromptu.Retrofit.RetroUsernameiMage;
import com.mandywebdesign.impromptu.Retrofit.UpdateDraft;
import com.mandywebdesign.impromptu.Utils.Util;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.Join_us;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerviewEventActivity extends AppCompatActivity {

    private static int CurrentPage = 0;
    Dialog paymentpopup;
    int REQUEST_CODE = 141;
    private ProgressBar progressBar;
    ViewPager viewPager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    PagerAdapter pagerAdapter;
    CircleIndicator indicator;
    ConstraintLayout invitefriendslayout;
    ImageView close, back, perview_message;
    ReadMoreTextView perview_description;
    TextView perview_categry,perview_time ,perview_location, value_text, readmore, perview_save_draft, perviewTitle, event_price, perview_link1, perview_link2, perview_link3;
    Toolbar toolbar;
    TextView perview_date, perview_organiser_name;
    Button perview_publish;
    RoundedImageView host_image;
    SharedPreferences.Editor editor;
    SharedPreferences preferences, sharedPreferences1, profileupdatedPref;
    Dialog progressDialog;
    String publish = "publish";
    String draft = "draft";
    String B_token = "", S_Token = "",edit;
    FragmentManager manager;
    static String Tic_Price;
    String id, title, desc, cate, address1, address2, date, To_date, FromTime, Username, frommilles, tomilles, sex, freeevent, attendeesNo, link1, link2, link3, postcode, city, ticketType, numbersTickets;
    String userToken = "", Socai_user, formattedDate, getFormattedDate, timeto, timeFrom, timeTo, editvalue, username;
    ArrayList<MultipartBody.Part> parts = new ArrayList<>();
    EditText payment_cardnumber,payment_holdername,payment_expirydate,payment_csv;
    Button payment_paybt;
    String type,getTic_Price,getNumbersTickets;
    boolean isPublish = false;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perview_event);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        manager = getSupportFragmentManager();

        init();

        sharedPreferences1 = getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        profileupdatedPref = getSharedPreferences("profileupdated", Context.MODE_PRIVATE);
        preferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);

        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();
        Intent intent = getIntent();
        title = intent.getStringExtra("eventTitle");
        desc = intent.getStringExtra("eventDesc");
        cate = intent.getStringExtra("eventCate");
        edit = intent.getStringExtra("editevent");
        address1 = intent.getStringExtra("address1");
        frommilles = intent.getStringExtra("fromtimeinmilles");
        tomilles = intent.getStringExtra("totimeinmilles");
        To_date = intent.getStringExtra("To_Date");
        editvalue = intent.getStringExtra("value");
        city = intent.getStringExtra("city");
        Tic_Price = intent.getStringExtra("Price");
        ticketType = intent.getStringExtra("ticketType");
        numbersTickets = intent.getStringExtra("numbersTickets");
        freeevent = intent.getStringExtra("freeevent");
        id = preferences.getString("id", "");
        if (link1!=null)
        {
            link1 = intent.getStringExtra("link1");
        }else {
            link1 = "";
        }

        if (link2!=null)
        {
            link2 = intent.getStringExtra("link2");
        }else {
            link2 = "";
        }

        if (link3!=null)
        {
            link3 = intent.getStringExtra("link1");
        }else {
            link3 = "";
        }
        if (!intent.getStringExtra("type").equalsIgnoreCase("") || !intent.getStringExtra("type").equals(""))
        {
            type = intent.getStringExtra("type");
            getTic_Price = intent.getStringExtra("ticketprice");
            getNumbersTickets = intent.getStringExtra("numbertickets");
            event_price.setText("Paid");
        }else
        {
            if (Tic_Price.equals("0")) {
                event_price.setText("Free");
            }else if (Tic_Price.equals("Paid"))
            {
                event_price.setText("Paid");
            }else {
                event_price.setText("£ " + Tic_Price);
            }
        }

        Log.d("TimeCheck", frommilles + "  " + tomilles);

        String s = address1;
        Log.e("add2", s);

        String[] arrayString = s.split(" NearBy ");

        String add1 = arrayString[0];


        Log.e("add1", add1);

        address2 = intent.getStringExtra("address2");
        date = intent.getStringExtra("SelectedDate");
        FromTime = intent.getStringExtra("FromTime");
        sex = intent.getStringExtra("gender");
        attendeesNo = intent.getStringExtra("attendeesNo");
        if (intent.getStringExtra("postcode").contains(" "))
        {
            postcode = intent.getStringExtra("postcode").replace(" ","");
        }else {
            postcode = intent.getStringExtra("postcode");
        }



        Username = preferences.getString("Username", "");

        B_token = preferences.getString("Usertoken", "");
        S_Token = preferences.getString("Socailtoken", "");

        Log.d("images", "" + Add_Event_Activity.part.toString());
        if (address2.equals("")) {
            address2 = "No Address line";
        }


        if (!B_token.equalsIgnoreCase("")) {

            getUSerData("Bearer " + B_token);
            progressDialog.show();
        } else {
            getUSerData("Bearer " + S_Token);
            progressDialog.show();
        }



        if (editvalue!=null)
        {
            perview_save_draft.setVisibility(View.GONE);
        }

        // ---- Puneet work --- start --//


        perview_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!B_token.equalsIgnoreCase("")) {
                    Log.d("B_token", B_token);
//                    payment(B_token);
                    Add_Event_Activity.image_uris.clear();
                    if (editvalue!=null)
                    {
                        if (edit.equalsIgnoreCase("edit"))
                        {
                            publishdraft("Bearer "+B_token,editvalue);
                        }else if (edit.equalsIgnoreCase("republish"))
                        {


                            PublishEvent("Bearer " + B_token, Add_Event_Activity.count + "", title, desc, cate, Add_Event_Activity.part, address1, address2, postcode, city, date, FromTime, EventDetailsActivity.to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, username, publish, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets);
                        }

                    }else {
                        PublishEvent("Bearer " + B_token, Add_Event_Activity.count + "", title, desc, cate, Add_Event_Activity.part, address1, address2, postcode, city, date, FromTime, EventDetailsActivity.to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, username, publish, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets);
                    }
                } else {

                    Add_Event_Activity.image_uris.clear();
                    progressDialog.show();
                    if (editvalue!=null)
                    {
                        if (edit.equalsIgnoreCase("edit"))
                        {
                            publishdraft("Bearer "+S_Token,editvalue);
                        }else if (edit.equalsIgnoreCase("republish"))
                        {
                            NormalEvent("Bearer " + S_Token, Add_Event_Activity.count + "", title, desc, cate, Add_Event_Activity.part, address1, address2, postcode, city, date, FromTime, EventDetailsActivity.to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, username, publish, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets);
                        }

                    }else {
                        NormalEvent("Bearer " + S_Token, Add_Event_Activity.count + "", title, desc, cate, Add_Event_Activity.part, address1, address2, postcode, city, date, FromTime, EventDetailsActivity.to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, username, publish, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets);
                    }
                }
            }
        });

        perview_save_draft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!B_token.equalsIgnoreCase("")) {
                    Add_Event_Activity.image_uris.clear();
                    progressDialog.show();
                    BusinessDraft("Bearer " + B_token);

                } else {
                    Add_Event_Activity.image_uris.clear();
                    progressDialog.show();
                    NormalDraft("Bearer " + S_Token);
                }
            }
        });

        // ---- Puneet work --- end --//

        id = preferences.getString("id", "");


        progressBar.setProgress(100);
        progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorTheme), PorterDuff.Mode.SRC_ATOP);


        //set Image into ViewPager And alos set dot Indicator
        viewPager.setOffscreenPageLimit(1);
        pagerAdapter = new PerviewImageAdpater(PerviewEventActivity.this, Add_Event_Activity.image_uris);
        viewPager.setAdapter(pagerAdapter);
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

                    int pagecount = Add_Event_Activity.image_uris.size();

                    if (CurrentPage == pagecount) {
                        viewPager.setCurrentItem(pagecount, true);
                    } else
                        CurrentPage++;
                }

            }
        });

//===================================================================================================//
        //change date format and time format
        String start_date = Util.convertTimeStampDate(Long.parseLong(frommilles));
        String end_date = Util.convertTimeStampDate(Long.parseLong(tomilles));

        if (start_date.matches(end_date))
        {
            perview_date.setText(start_date);
        }else {
            perview_date.setText(start_date+ " - " + end_date);
        }

        Log.d("date",start_date+"   "+end_date);


        //get Time to in AM PM
        String time_t = FromTime;
        String time_to = EventDetailsActivity.to_time_milles;

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            final Date dateObj = sdf.parse(time_t);
            final Date date1 = sdf.parse(time_to);
            time_t = new SimpleDateFormat("hh:mm aa").format(dateObj).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM","am").replaceFirst("PM","pm");
            time_to = new SimpleDateFormat("hh:mm aa").format(date1).replaceFirst("a.m.", "am").replaceFirst("p.m.", "pm").replaceFirst("AM","am").replaceFirst("PM","pm");

            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            formattedDate = df.format(c.getTime());
            c.add(Calendar.DATE, 1);

            getFormattedDate = df.format(c.getTime());

            System.out.println("Current time ==> " + c.getTime());
            timeto = time_to;

            if (time_t.startsWith("0") && time_to.startsWith("0")) {
                timeFrom = time_t.substring(1);
                timeTo = time_to.substring(1);
                perview_time.setText( timeFrom + " - " + timeTo);
            } else if (time_t.startsWith("0")) {
                timeFrom = time_t.substring(1);
                if (time_to.startsWith("0")) {
                    timeTo = time_to.substring(1);
                    perview_time.setText( timeFrom + " - " + timeTo);
                } else {
                    timeTo = time_to;
                    perview_time.setText( timeFrom + " - " + timeTo);
                }
            } else if (time_to.startsWith("0")) {
                timeTo = time_to.substring(1);
                if (time_t.startsWith("0")) {
                    timeFrom = time_t.substring(1);
                    perview_time.setText( timeFrom + " - " + timeTo);
                } else {
                    timeFrom = time_t;
                    perview_time.setText( timeFrom + " - " + timeTo);
                }
            } else if (!time_t.startsWith("0") && !time_to.startsWith("0")) {
                timeFrom = time_t;
                timeTo = time_to;
                perview_time.setText( timeFrom + " - " + timeTo);
            }


        } catch (final ParseException e) {
            e.printStackTrace();
        }


//=========================================================================================//

        //perview_date.setText(date + "\n" + FromTime + " - " + EventDetailsActivity.to_time_milles);
        perview_description.setText(desc);
        perview_categry.setText(cate);
        perview_location.setText(add1 + " , " + postcode);
        perviewTitle.setText(title);
        if (!link1.equals("")) {
            SpannableString content = new SpannableString(link1);
            content.setSpan(new UnderlineSpan(), 0, link1.length(), 0);
            perview_link1.setVisibility(View.VISIBLE);
            perview_link1.setText(content);
        }

        if (!link2.equals("")) {
            SpannableString content = new SpannableString(link2);
            content.setSpan(new UnderlineSpan(), 0, link2.length(), 0);
            perview_link2.setVisibility(View.VISIBLE);
            perview_link2.setText(content);
        }
        if (!link3.equals("")) {
            SpannableString content = new SpannableString(link3);
            content.setSpan(new UnderlineSpan(), 0, link3.length(), 0);
            perview_link3.setVisibility(View.VISIBLE);
            perview_link3.setText(content);
        }

        value_text.setText("( " + sex + " )");

        listeners();

    }

    private void payment(String b_token) {
        paymentpopup = new Dialog(this);
        Window window = paymentpopup.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        paymentpopup.setContentView(R.layout.paymentdialog);
        paymentpopup.setCancelable(true);
        paymentpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        paymentpopup.show() ;


        payment_cardnumber = paymentpopup.findViewById(R.id.pay_card_number);
        payment_holdername = paymentpopup.findViewById(R.id.pay_card_name);
        payment_expirydate = paymentpopup.findViewById(R.id.pay_expiry_date);
        payment_csv = paymentpopup.findViewById(R.id.pay_csv);
        payment_paybt = paymentpopup.findViewById(R.id.payment_paybt);

        payment_paybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Event_Activity.image_uris.clear();
                HashMap<String,String> hashMap = new HashMap<>();
                //token, Add_Event_Activity.count + "", title, desc, cate, Add_Event_Activity.part, address1, address2,
                // postcode, city, date, FromTime, EventDetailsActivity.to_time_milles, sex, attendeesNo,
                // freeevent, ticketType, Tic_Price, numbersTickets, username, publish, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets
                hashMap.put("cat_id",Add_Event_Activity.count + "");
                hashMap.put("title",title);
                hashMap.put("description",desc);
                hashMap.put("category",cate);
                hashMap.put("addressline1",address1);
                hashMap.put("addressline2",address2);
                hashMap.put("postcode",postcode);
                hashMap.put("city",city);
                hashMap.put("date",date);
                hashMap.put("time_from",FromTime);
                hashMap.put("time_to",EventDetailsActivity.to_time_milles);
                hashMap.put("attendees_gender",sex);
                hashMap.put("attendees_no",attendeesNo);
                hashMap.put("free_event",freeevent);
                hashMap.put("ticket_type",ticketType);
                hashMap.put("price",Tic_Price);
                hashMap.put("no_of_tickets",numbersTickets);
                hashMap.put("b_event_hostname",username);
                hashMap.put("status",publish);
                hashMap.put("link1",link1);
                hashMap.put("link2",link2);
                hashMap.put("link3",link3);
                hashMap.put("start_datetime",frommilles);
                hashMap.put("end_datetime",tomilles);
                hashMap.put("type",type);
                hashMap.put("value",getTic_Price);
                hashMap.put("numberoftickets",getNumbersTickets);

                Log.d("++++++","++ hashmap size create event ++"+hashMap.size());
                progressDialog.show();
                if (editvalue!=null)
                {
                    if (edit.equalsIgnoreCase("edit"))
                    {
                        publishdraft("Bearer "+B_token,editvalue);
                    }else if (edit.equalsIgnoreCase("republish"))
                    {
                        PublishEvent("Bearer " + S_Token, Add_Event_Activity.count + "", title, desc, cate, Add_Event_Activity.part, address1, address2, postcode, city, date, FromTime, EventDetailsActivity.to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, username, publish, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets);
                    }

                }else {
                    PublishEvent("Bearer " + S_Token, Add_Event_Activity.count + "", title, desc, cate, Add_Event_Activity.part, address1, address2, postcode, city, date, FromTime, EventDetailsActivity.to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, username, publish, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets);
                }
            }
        });
    }


    private void getUSerData(String s) {
        Call<RetroUsernameiMage> call = WebAPI.getInstance().getApi().userNameImage(s);
        call.enqueue(new Callback<RetroUsernameiMage>() {
            @Override
            public void onResponse(Call<RetroUsernameiMage> call, Response<RetroUsernameiMage> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    {
                        if (response.body().getStatus().equals("200")) {
                            Glide.with(PerviewEventActivity.this).load(response.body().getData().getUserImg()).into(host_image);
                            username = response.body().getData().getUserName();
                           /* String[] name = username.split(" ");
                            String Fname = name[0];
                            String Lname = name[1];
                            perview_organiser_name.setText(Fname + " " + Lname.subSequence(0, 1));
*/
                            //================================
                            if (username != null) {
                                String[] name = username.split(" ");
                                if (name.length == 1) {
                                    String Fname = name[0];
                                    perview_organiser_name.setText(Fname + " ");
                                } else {
                                    String Fname = name[0];
                                    String Lname = name[1];
                                    perview_organiser_name.setText(Fname + " " + Lname.subSequence(0, 1));
                                }
                            } else {
                                perview_organiser_name.setText(username);
                            }

                            //=========================
                        }
                    }

                } else {
                    Intent intent = new Intent(PerviewEventActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RetroUsernameiMage> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(PerviewEventActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listeners() {


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Event_Activity.image_uris.clear();
                Intent intent = new Intent(PerviewEventActivity.this, Home_Screen.class);
                startActivity(intent);
                finish();
            }
        });

        invitefriendslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void init() {

        perview_time = findViewById(R.id.perview_time);
        event_price = findViewById(R.id.event_price);
        progressBar = findViewById(R.id.perview_progress_bar);
        viewPager = findViewById(R.id.perview_viewpager);
        //indicator = (CircleIndicator) findViewById(R.id.perview_indicator);
        toolbar = findViewById(R.id.perview_toolbar);
        dotsLayout = findViewById(R.id.perview_indicator);
        perview_date = findViewById(R.id.perview_date);
        perview_description = findViewById(R.id.perview_description);
        perview_categry = findViewById(R.id.perview_categry);
        perview_location = findViewById(R.id.perview_location);
        value_text = findViewById(R.id.value_text);
        readmore = findViewById(R.id.readmore);
        perview_publish = findViewById(R.id.perview_publish);
        perview_organiser_name = findViewById(R.id.perview_organiser_name);
        perview_save_draft = findViewById(R.id.perview_save_draft);
        back = findViewById(R.id.back_eventpublish);
        close = findViewById(R.id.event_close_puublis);
        perview_message = findViewById(R.id.perview_message);
        host_image = findViewById(R.id.perview_user_picture);
        perviewTitle = findViewById(R.id.perview_tittle);
        setSupportActionBar(toolbar);
        perview_message.setVisibility(View.GONE);
        perview_link1 = findViewById(R.id.perview_link1);
        perview_link2 = findViewById(R.id.perview_link2);
        perview_link3 = findViewById(R.id.perview_link3);
        invitefriendslayout = findViewById(R.id.invitefriendslayout);
    }

    public void PublishEvent(final String token,String count,String title,String desc,String cate,ArrayList<MultipartBody.Part> part,String address1,String address2,String postcode,String city,String date,String FromTime,String to_time_milles,String sex,String attendeesNo,String freeevent,String ticketType,String Tic_Price,String numbersTickets,String username,String publish,String link1,String link2,String link3,String frommilles,String tomilles,String type,String getTic_Price,String getNumbersTickets) {
        System.gc();
        progressDialog.show();
        Call<RetroAddEvent> call = WebAPI.getInstance().getApi().addEvent(token, count + "", title, desc, cate, part, address1, address2, postcode, city, date, FromTime, to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, username, publish, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets);
        //Call<RetroAddEvent> call = WebAPI.getInstance().getApi().addEventNew(token,hashMap,part);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!isPublish)
//                {
//                    perview_publish.performClick();
//                    dialog.show();
//                    //final Call<RetroAddEvent> call = WebAPI.getInstance().getApi().addEvent(token, Add_Event_Activity.count + "", title, desc, cate, Add_Event_Activity.part, address1, address2, postcode, city, date, FromTime, EventDetailsActivity.to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, username, publish, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets);
//                }else {
//                    isPublish = false;
//                   progressDialog.show();
//                }
//            }
//        }, 60000);
        call.enqueue(new Callback<RetroAddEvent>() {
            @Override
            public void onResponse(Call<RetroAddEvent> call, final Response<RetroAddEvent> response) {
                progressDialog.dismiss();

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (response.isSuccessful())
//                        {
//                            isPublish = true;
//                        }else {
//                            progressDialog.dismiss();
//                            perview_publish.performClick();
//                        }
//                    }
//                }, 60000);

                if (response.body() != null) {

                    if (response.body().getStatus().equals("200")) {
                        Toast.makeText(PerviewEventActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        Home_Screen.framgposition =1;
                        Log.d("++++++++", "++++response ++" + response);
                        Intent intent = new Intent(PerviewEventActivity.this, Home_Screen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("isFrom","NormaleventCreation");
                        startActivity(intent);
                        finish();

                    } else if (response.body().getStatus().equals("401")) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();

                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.clear();
                        editor1.commit();
                        Toast.makeText(PerviewEventActivity.this, "Business Logout", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor2 = profileupdatedPref.edit();
                        editor2.clear();
                        editor2.commit();

                        progressDialog.show();

                        Intent intent = new Intent(PerviewEventActivity.this, Join_us.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PerviewEventActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PerviewEventActivity.this, NoInternetScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

            }

            @Override
            public void onFailure(Call<RetroAddEvent> call, Throwable t) {
                Log.d("profileresponse1", "" + t);
                progressDialog.dismiss();
                Toast.makeText(PerviewEventActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void NormalEvent(final String token,String count,String title,String desc,String cate,ArrayList<MultipartBody.Part> part,String address1,String address2,String postcode,String city,String date,String FromTime,String to_time_milles,String sex,String attendeesNo,String freeevent,String ticketType,String Tic_Price,String numbersTickets,String username,String publish,String link1,String link2,String link3,String frommilles,String tomilles,String type,String getTic_Price,String getNumbersTickets) {
        System.gc();
        progressDialog.show();


        Log.d("profileresponse", "" + token);
        String no_of_tic = numbersTickets;
        Log.e("Ticket_val", no_of_tic);

        final Call<RetroAddEvent> call = WebAPI.getInstance().getApi().addEvent(token, count, title, desc, cate, part, address1, address2, postcode, city, date, FromTime, to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, username, publish, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets);
        Log.e("Ticket_val", call + " ");
        call.enqueue(new Callback<RetroAddEvent>() {
            @Override
            public void onResponse(Call<RetroAddEvent> call, final Response<RetroAddEvent> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {
                        Home_Screen.framgposition =1;
                        Toast.makeText(PerviewEventActivity.this, "Event created ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        isPublish = true;
                        Intent intent = new Intent(PerviewEventActivity.this, Home_Screen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("isFrom","NormaleventCreation");
                        //Home_Screen.newCount=1;
                        startActivity(intent);
                        //finish();
                    } else if (response.body().getStatus().equals("401")) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        isPublish = true;
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.clear();
                        editor1.commit();
                        Toast.makeText(PerviewEventActivity.this, "Business Logout", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor2 = profileupdatedPref.edit();
                        editor2.clear();
                        editor2.commit();

                        progressDialog.show();

                        Intent intent = new Intent(PerviewEventActivity.this, Join_us.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PerviewEventActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        isPublish = true;
                        Intent intent = new Intent(PerviewEventActivity.this, NoInternetScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

            }

            @Override
            public void onFailure(Call<RetroAddEvent> call, Throwable t) {
                Log.d("profileresponse1", "" + t);
                progressDialog.dismiss();
                Toast.makeText(PerviewEventActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                isPublish = true;
            }
        });
    }

    public void BusinessDraft(String token) {
        System.gc();
        progressDialog.show();

        Log.d("draftresponse", "" + Add_Event_Activity.part.get(0));
        Log.d("draftresponse", "" + userToken);

        Call<RetroAddEvent> call = WebAPI.getInstance().getApi().addEvent(token, Add_Event_Activity.count + "", title, desc, cate, Add_Event_Activity.part, address1, address2, postcode, city, date, FromTime, EventDetailsActivity.to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, BusinessUserProfile.userName, draft, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets);
        call.enqueue(new Callback<RetroAddEvent>() {
            @Override
            public void onResponse(Call<RetroAddEvent> call, Response<RetroAddEvent> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {
                        Toast.makeText(PerviewEventActivity.this, "Event Saved In drafts ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        Intent intent = new Intent(PerviewEventActivity.this, Home_Screen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("refresh", "1");
                        startActivity(intent);
                        finish();
                    } else if (response.body().getStatus().equals("401")) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();

                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.clear();
                        editor1.commit();
                        Toast.makeText(PerviewEventActivity.this, "Business Logout", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor2 = profileupdatedPref.edit();
                        editor2.clear();
                        editor2.commit();

                        progressDialog.show();

                        Intent intent = new Intent(PerviewEventActivity.this, Join_us.class);
                        startActivity(intent);
                        finish();

                    }
                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(PerviewEventActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RetroAddEvent> call, Throwable t) {
                Log.d("draftresponse1", "" + t);
                progressDialog.dismiss();
                Toast.makeText(PerviewEventActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void NormalDraft(String token) {
        System.gc();
        progressDialog.show();

        Log.d("profileresponse", "" + Add_Event_Activity.part.get(0));
        Log.d("profileresponse", "" + token);

        Call<RetroAddEvent> call = WebAPI.getInstance().getApi().addEvent(token, Add_Event_Activity.count + "", title, desc, cate, Add_Event_Activity.part, address1, address2, postcode, city, date, FromTime, EventDetailsActivity.to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, username, draft, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!isPublish)
//                {
//                    perview_save_draft.performClick();
//                    dialog.show();
//                    //final Call<RetroAddEvent> call = WebAPI.getInstance().getApi().addEvent(token, Add_Event_Activity.count + "", title, desc, cate, Add_Event_Activity.part, address1, address2, postcode, city, date, FromTime, EventDetailsActivity.to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, username, publish, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets);
//                }else {
//                    isPublish = false;
//                    progressDialog.show();
//                }
//            }
//        }, 60000);
        call.enqueue(new Callback<RetroAddEvent>() {
            @Override
            public void onResponse(Call<RetroAddEvent> call, Response<RetroAddEvent> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equals("200")) {
                        Toast.makeText(PerviewEventActivity.this, "Event Saved In drafts ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        Intent intent = new Intent(PerviewEventActivity.this, Home_Screen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("refresh", "1");
                        startActivity(intent);
                        finish();
                    } else if (response.body().getStatus().equals("401")) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();

                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.clear();
                        editor1.commit();
                        Toast.makeText(PerviewEventActivity.this, "Business Logout", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor2 = profileupdatedPref.edit();
                        editor2.clear();
                        editor2.commit();

                        progressDialog.show();

                        Intent intent = new Intent(PerviewEventActivity.this, Join_us.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(PerviewEventActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RetroAddEvent> call, Throwable t) {
                Log.d("profileresponse1", "" + t);
                progressDialog.dismiss();
                Toast.makeText(PerviewEventActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //update event with update values
    public void publishdraft(String token, String eventId) {
        Log.d("jkjkjkj", "" + Add_Event_Activity.part);

        Call<UpdateDraft> call = WebAPI.getInstance().getApi().updateDraft(token, eventId, Add_Event_Activity.count + "", title, desc, cate, Add_Event_Activity.part, address1, address2, postcode, city, date, FromTime, EventDetailsActivity.to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, username, publish, frommilles, tomilles,type,getTic_Price,getNumbersTickets);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!isPublish)
//                {
//                    perview_save_draft.performClick();
//                    dialog.show();
//                    //final Call<RetroAddEvent> call = WebAPI.getInstance().getApi().addEvent(token, Add_Event_Activity.count + "", title, desc, cate, Add_Event_Activity.part, address1, address2, postcode, city, date, FromTime, EventDetailsActivity.to_time_milles, sex, attendeesNo, freeevent, ticketType, Tic_Price, numbersTickets, username, publish, link1, link2, link3, frommilles, tomilles,type,getTic_Price,getNumbersTickets);
//                }else {
//                    isPublish = false;
//                }
//            }
//        }, 60000);
        call.enqueue(new Callback<UpdateDraft>() {
            @Override
            public void onResponse(Call<UpdateDraft> call, Response<UpdateDraft> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    Toast.makeText(PerviewEventActivity.this, "Draft Published", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(PerviewEventActivity.this, Home_Screen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("refresh", "1");
                    startActivity(intent);
                    finish();
                    Log.d("ERRORMESSAGE", "" + response.body().getMessage());
                } else {
                    Intent intent = new Intent(PerviewEventActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<UpdateDraft> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PerviewEventActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("PUBLISHERROR", t.getMessage());
            }
        });
    }

    //add dots at bottom
    private void addBottomDots(int currentPage) {

        dots = new TextView[Add_Event_Activity.image_uris.size()];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(this);
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


