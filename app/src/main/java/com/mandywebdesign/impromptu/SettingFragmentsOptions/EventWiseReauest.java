package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Adapters.EventWiseRequestAdapter;
import com.mandywebdesign.impromptu.Adapters.StripeTransferIF;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RefundEventRequest;
import com.mandywebdesign.impromptu.Utils.Util;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventWiseReauest extends AppCompatActivity {

    ImageButton back_on_eventrequest;
    RecyclerView eventrewuetsrecycler;
    EventWiseRequestAdapter adapter;
    ArrayList<RefundEventRequest.Datum> refundEvents = new ArrayList<>();
    String socailtoken, BToken;
    SharedPreferences sharedPreferences;
    Dialog dialog;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_wise_reauest);

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        socailtoken = sharedPreferences.getString("Socailtoken", "");
        BToken = sharedPreferences.getString("Usertoken", "");
        dialog = ProgressBarClass.showProgressDialog(this);
        dialog.show();

        back_on_eventrequest = findViewById(R.id.back_on_eventrequest);
        eventrewuetsrecycler = findViewById(R.id.eventrewuetsrecycler);

        if (!BToken.equals(""))
        {
            setData(BToken);
        }else {
            setData(socailtoken);
        }

        back_on_eventrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setData(String s) {
        Call<RefundEventRequest> refundEventRequestCall = WebAPI.getInstance().getApi().eventRefundRequests("Bearer "+s);
        refundEventRequestCall.enqueue(new Callback<RefundEventRequest>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<RefundEventRequest> call, Response<RefundEventRequest> response) {
                refundEvents.clear();
                dialog.dismiss();
                if (response.isSuccessful())
                {
                    if (response.body().getStatus().equals("200"))
                    {
                        for (int i=0;i<response.body().getData().size();i++)
                        {
                            refundEvents.add(response.body().getData().get(i));
                        }

                        LinearLayoutManager linearLayout = new LinearLayoutManager(EventWiseReauest.this);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        eventrewuetsrecycler.setLayoutManager(linearLayout);
                        adapter = new EventWiseRequestAdapter(EventWiseReauest.this,refundEvents);
                        eventrewuetsrecycler.setAdapter(adapter);
                        adapter.setStripeTransferIF(new StripeTransferIF() {
                            @Override
                            public void stripeTransfer(String eventid) {

                            }
                        });


                    }
                }else {
                    Intent intent = new Intent(EventWiseReauest.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RefundEventRequest> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EventWiseReauest.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
