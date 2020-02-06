package com.mandywebdesign.impromptu.BusinessRegisterLogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Adapters.CheckGuestAdapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Models.CheckInGuest;
import com.mandywebdesign.impromptu.Models.TotalCheckIn;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckGuestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public static TextView QRData;
    View view;
    CheckGuestAdapter adapter;
    ImageView scanner;
    ImageButton close;
    String BToken, S_Token;
    ArrayList<CheckInGuest.Datum> checkinguset = new ArrayList<>();
    SharedPreferences sharedPreferences;
    TextView checkInguset;
    static   String id,eventType;
    public static ArrayList<String> userID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_guest);
        init();
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");

        final Intent intent = getIntent();
        if (intent!=null)
        {
            id = intent.getStringExtra("value");
            if (intent.getStringExtra("eventType")!=null)
            {
                eventType = intent.getStringExtra("eventType");
                if (eventType.equalsIgnoreCase("history"))
                {
                    scanner.setVisibility(View.GONE);
                }
            }
        }


        if (!BToken.equalsIgnoreCase("")) {

            getBookedUSers(BToken);
            getTotalCheckin(BToken, id);

        } else if (!S_Token.equalsIgnoreCase("")) {
            getBookedUSers(S_Token);
            getTotalCheckin(S_Token, id);
        }


        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CheckGuestActivity.this,QrScanActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent1.putExtra("value",id);
                startActivity(intent1);
                finish();
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    private void getBookedUSers(String Token) {
        Call<CheckInGuest> checkInGuestCall = WebAPI.getInstance().getApi().checkinguest("Bearer " + Token, BusinessEventDetailAcitvity.value, "", "");
        checkInGuestCall.enqueue(new Callback<CheckInGuest>() {
            @Override
            public void onResponse(Call<CheckInGuest> call, Response<CheckInGuest> response) {

                    checkinguset.clear();
                if (response.body()!=null) {

                    if (response.body().getStatus().equals("200")) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            checkinguset.add(response.body().getData().get(i));
                            userID.add(String.valueOf(response.body().getData().get(i).getUserid()));
                            setDat(checkinguset);
                        }
                    }
                    Log.d("Hello1234", "1" + response.message());
                } else
                {
                    Intent intent = new Intent(CheckGuestActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<CheckInGuest> call, Throwable t) {
                Toast.makeText(CheckGuestActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalCheckin(String s, String eventID) {

        Call<TotalCheckIn> call = WebAPI.getInstance().getApi().totleTickets("Bearer " +s, eventID);
        call.enqueue(new Callback<TotalCheckIn>() {
            @Override
            public void onResponse(Call<TotalCheckIn> call, Response<TotalCheckIn> response) {
                if (response.body()!=null)
                {
                    checkInguset.setText(response.body().getData().getCheckinTotal() + "/" + response.body().getData().getTotal() + " Checked in");
                }else {
                    Intent intent = new Intent(CheckGuestActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<TotalCheckIn> call, Throwable t) {
            }
        });
    }

    private void setDat(ArrayList<CheckInGuest.Datum> checkinguset) {

        adapter = new CheckGuestAdapter(this, checkinguset);
        LinearLayoutManager linearLayoune = new LinearLayoutManager(CheckGuestActivity.this);
        linearLayoune.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoune);
        recyclerView.setAdapter(adapter);
    }

    private void init() {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.check_guest_recycler);
        scanner = findViewById(R.id.check_guest_qrcode);
        close = findViewById(R.id.check_guest_close);
        QRData = findViewById(R.id.QRData);
        checkInguset = findViewById(R.id.check_guest_text);

    }

}
