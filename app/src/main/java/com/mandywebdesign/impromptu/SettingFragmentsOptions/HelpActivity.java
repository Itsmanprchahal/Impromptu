package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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
import com.mandywebdesign.impromptu.Retrofit.RetroHelp;
import com.mandywebdesign.impromptu.ui.Join_us;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpActivity extends AppCompatActivity {

    TextView help;
    View view;
    SharedPreferences sharedPreferences;
    String user,social_token;
    Dialog progressDialog;
    ImageView back;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        user =  sharedPreferences.getString("Usertoken", "");
        social_token =  sharedPreferences.getString("Socailtoken", "");

        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();

        init();
        listeners();

        if (!user.equalsIgnoreCase(""))
        {
//            help(user);
            help.setText("Coming soon");
        }else if (!social_token.equalsIgnoreCase(""))
        {
//            help(social_token);
            help.setText("Coming soon");
        }


    }

    private void help(String user) {
        progressDialog.show();
        Call<RetroHelp> call = WebAPI.getInstance().getApi().help("Bearer "+user,"application/json");
        call.enqueue(new Callback<RetroHelp>() {
            @Override
            public void onResponse(Call<RetroHelp> call, Response<RetroHelp> response) {

                if (response.body() != null)
                {
                    if (response.body().getStatus().equals("200"))
                    {
                        String helpdata = response.body().getData().get(0).getContent();
                        // Toast.makeText(getContext(), ""+helpdata, Toast.LENGTH_SHORT).show();
                        help.setText(helpdata);
                        progressDialog.dismiss();
                    }else if (response.body().getStatus().equals("401"))
                    {
                        //Toast.makeText(getContext(), ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(HelpActivity.this, Join_us.class);
                        startActivity(intent);
                        finish();

                    }
                }else {
                    Intent intent = new Intent(HelpActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RetroHelp> call, Throwable t) {

                    progressDialog.dismiss();

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
    }

    private void init() {

        help = findViewById(R.id.help_text);
        back = findViewById(R.id.back_on_help);
    }
}
