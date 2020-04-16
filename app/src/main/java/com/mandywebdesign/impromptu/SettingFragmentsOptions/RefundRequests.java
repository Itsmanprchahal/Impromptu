package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.mandywebdesign.impromptu.Adapters.RefundAdapter;
import com.mandywebdesign.impromptu.R;

public class RefundRequests extends AppCompatActivity {

    RecyclerView refund_recycler;
    ImageButton back;
    String socailtoken;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_requests);

        back = findViewById(R.id.back_refund);
        refund_recycler = findViewById(R.id.refund_recycler);
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        socailtoken =  sharedPreferences.getString("Socailtoken", "");

        RefundAdapter refundAdapter = new RefundAdapter(RefundRequests.this,socailtoken);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(RefundRequests.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        refund_recycler.setLayoutManager(layoutManager);
        refund_recycler.setNestedScrollingEnabled(false);
        refund_recycler.setAdapter(refundAdapter);

        listerners();

    }

    private void listerners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
