package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandywebdesign.impromptu.R;

public class TandCOptions extends AppCompatActivity {

    TextView setting_termsandconditions_option, setting_privancyStatement_option;
    ImageView back_on_help;
    SharedPreferences sharedPreferences, sharedPreferences1, socialpref;
    SharedPreferences.Editor editor;
    String user, token, socialtoken;
    Dialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tand_coptions);


        sharedPreferences1 = getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        user = sharedPreferences.getString("Usertoken", "");
        token = "Bearer " + sharedPreferences.getString("Usertoken", "");
        init();
        listerners();
    }

    private void listerners() {
        setting_privancyStatement_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TandCOptions.this, PrivancyActivity.class);
                startActivity(intent);

            }
        });

        setting_termsandconditions_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TandCOptions.this, TermsAndConditionsActivityy.class);
                startActivity(intent);
            }
        });

        back_on_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        back_on_help = findViewById(R.id.back_on_help);
        setting_termsandconditions_option = findViewById(R.id.setting_termsandconditions_option);
        setting_privancyStatement_option = findViewById(R.id.setting_privancyStatement_option);
    }
}
