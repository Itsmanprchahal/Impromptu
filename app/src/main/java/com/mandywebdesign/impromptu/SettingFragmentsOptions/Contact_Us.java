package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Models.GetAdminEmail;
import com.mandywebdesign.impromptu.Models.SendContactUSmesg;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Contact_Us extends AppCompatActivity {

    String BToken,S_Token,messg;
    SharedPreferences sharedPreferences;
    EditText contactus_emialet,contactus_messageet;
    Button contactus_submitbt;
    Dialog dialog;
    ImageButton contactus_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__us);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        dialog = ProgressBarClass.showProgressDialog(this);
        dialog.dismiss();

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");

        init();

        if (!BToken.equalsIgnoreCase("")) {

            usermail(BToken);
        } else if (!S_Token.equalsIgnoreCase("")) {
            usermail(S_Token);
        }

        listernerrs();
    }

    private void listernerrs() {
        contactus_submitbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                messg = contactus_messageet.getText().toString();
                if (!TextUtils.isEmpty(messg))
                {
                    if (!BToken.equalsIgnoreCase("")) {

                        sendmesg("Bearer "+BToken,messg);
                    } else if (!S_Token.equalsIgnoreCase("")) {
                        sendmesg("Bearer "+S_Token,messg);
                    }
                }else {
                    contactus_messageet.setError("");
                }

            }
        });

        contactus_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void sendmesg(String s_token,String messg) {
        dialog.show();
        Call<SendContactUSmesg> contactUSmesgCall = WebAPI.getInstance().getApi().sendcontactmesg(s_token,messg);
        contactUSmesgCall.enqueue(new Callback<SendContactUSmesg>() {
            @Override
            public void onResponse(Call<SendContactUSmesg> call, Response<SendContactUSmesg> response) {
                dialog.dismiss();
                if (response.body()!=null)
                {

                    if (response.body().getStatus().equals("200"))
                    {
                        Toast.makeText(Contact_Us.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        contactus_messageet.setText("");
                    }else {

                    }

                }
            }

            @Override
            public void onFailure(Call<SendContactUSmesg> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Contact_Us.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {
        contactus_emialet = findViewById(R.id.contactus_emialet);
        contactus_messageet = findViewById(R.id.contactus_messageet);
        contactus_submitbt= findViewById(R.id.contactus_submitbt);
        contactus_back = findViewById(R.id.contactus_back);
    }

    private void usermail(String s_token) {

        Call<GetAdminEmail> call = WebAPI.getInstance().getApi().getAdminemail("Bearer "+s_token);
        call.enqueue(new Callback<GetAdminEmail>() {
            @Override
            public void onResponse(Call<GetAdminEmail> call, Response<GetAdminEmail> response) {
                if (response.body()!=null) {
                    if (response.body().getStatus().equals("200")) {
                        contactus_emialet.setText(response.body().getData().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAdminEmail> call, Throwable t) {

            }
        });
    }
}
