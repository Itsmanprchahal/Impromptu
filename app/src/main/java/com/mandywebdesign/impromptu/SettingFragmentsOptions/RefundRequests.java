package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.mandywebdesign.impromptu.Adapters.RefundAdapter;
import com.mandywebdesign.impromptu.R;

public class RefundRequests extends AppCompatActivity {

    RecyclerView refund_recycler;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_requests);

        back = findViewById(R.id.back_refund);
        refund_recycler = findViewById(R.id.refund_recycler);
        RefundAdapter refundAdapter = new RefundAdapter(RefundRequests.this);
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
