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

import com.mandywebdesign.impromptu.Home_Screen_Fragments.Setting;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroPrivancy;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivancyActivity extends AppCompatActivity {

    TextView privacy;
    View view;
    SharedPreferences sharedPreferences;
    String user,social_token;
    Dialog progressDialog;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privancy);
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        user = sharedPreferences.getString("Usertoken", "");
        social_token = sharedPreferences.getString("Socailtoken", "");

        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();

        init();
        listerners();

        if (!user.equalsIgnoreCase(""))
        {
            normalprivancy(user);
        }else
        {
            normalprivancy(social_token);
        }
    }

    private void normalprivancy(String social_token) {
        progressDialog.show();
        Call<RetroPrivancy> call = WebAPI.getInstance().getApi().privacy("Bearer "+social_token,"application/json");
        call.enqueue(new Callback<RetroPrivancy>() {
            @Override
            public void onResponse(Call<RetroPrivancy> call, Response<RetroPrivancy> response) {

                if (response.body()!=null)
                {
                    if (response.body().getStatus().equals("200")) {
                        String privacydata = response.body().getData().get(0).getContent();
                        // Toast.makeText(getContext(), ""+helpdata, Toast.LENGTH_SHORT).show();
                        privacy.setText(privacydata);
                        progressDialog.dismiss();
                    }
                }else {
                    Intent intent = new Intent(PrivancyActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<RetroPrivancy> call, Throwable t) {
                 progressDialog.dismiss();

                    NoInternet.dialog(PrivancyActivity.this);

            }
        });
    }

    private void listerners() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             onBackPressed();
            }
        });
    }

    private void init() {
        privacy = findViewById(R.id.privacy);
        back = findViewById(R.id.back_on_privacy);

    }
}
