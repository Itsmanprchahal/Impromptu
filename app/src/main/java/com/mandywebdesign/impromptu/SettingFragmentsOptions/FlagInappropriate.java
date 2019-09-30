package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.InappropriateBehaviour;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlagInappropriate extends AppCompatActivity {

    ImageView back_on_help;
    Button inappropriate_send;
    SharedPreferences sharedPreferences;
    String user,social_token;
    Dialog progressDialog;
    EditText event_id,complaint_et;
    String getEventID;
    RadioButton hostRB,AttendeeRB,OtherRB;
    RadioGroup radiogroup;
    String usertype,decs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_inappropriate);

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        user =  sharedPreferences.getString("Usertoken", "");
        social_token =  sharedPreferences.getString("Socailtoken", "");

        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();


        back_on_help = findViewById(R.id.back_on_help);
        inappropriate_send = findViewById(R.id.inappropriate_send);
        event_id = findViewById(R.id.event_id);
        hostRB = findViewById(R.id.hostRB);
        AttendeeRB = findViewById(R.id.AttendeeRB);
        OtherRB = findViewById(R.id.OtherRB);
        complaint_et = findViewById(R.id.complaint_et);
        radiogroup = findViewById(R.id.radiogroup);


        listeners();
    }

    private void listeners() {
        back_on_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.hostRB:
                        usertype = "host";
                        break;

                    case R.id.AttendeeRB:
                        usertype = "attendee";
                        break;

                    case R.id.OtherRB:
                        usertype = "other";
                        break;
                }
            }
        });

        inappropriate_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEventID = event_id.getText().toString();
                decs = complaint_et.getText().toString();

                if (TextUtils.isEmpty(event_id.getText().toString()))
                {
                   event_id.setError("error");
                }else if (TextUtils.isEmpty(complaint_et.getText().toString()))
                {
                    complaint_et.setError("error");
                }else if (hostRB.isChecked() || AttendeeRB.isChecked() || OtherRB.isChecked())
                {
                     if (!social_token.equalsIgnoreCase(""))
                    {
                        progressDialog.show();
                        inappro(social_token,getEventID,usertype,decs);
                    }
                }else {
                    Toast.makeText(FlagInappropriate.this, "Select atleast one option from inappropriate find", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void inappro(String user, final String eventid, String usertype, final String decs) {

        Call<InappropriateBehaviour> call = WebAPI.getInstance().getApi().inappropriate_behaviour("Bearer "+user,eventid,usertype,decs);
        call.enqueue(new Callback<InappropriateBehaviour>() {
            @Override
            public void onResponse(Call<InappropriateBehaviour> call, Response<InappropriateBehaviour> response) {
                if (response.isSuccessful())
                {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("200"))
                    {
                        complaint_et.setText("");
                        event_id.setText("");
                        hostRB.setChecked(false);
                        AttendeeRB.setChecked(false);
                        OtherRB.setChecked(false);
                        progressDialog.dismiss();
                        Toast.makeText(FlagInappropriate.this, "Successfully Sent", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<InappropriateBehaviour> call, Throwable t) {

            }
        });
    }
}
