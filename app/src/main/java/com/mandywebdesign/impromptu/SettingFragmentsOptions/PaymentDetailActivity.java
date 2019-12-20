package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.mandywebdesign.impromptu.R;

public class PaymentDetailActivity extends AppCompatActivity {

    ImageButton back_on_paymentdetail_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        back_on_paymentdetail_activity = findViewById(R.id.back_on_paymentdetail_activity);

        listerners();
    }

    private void listerners() {
        back_on_paymentdetail_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
