package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroDelete;
import com.mandywebdesign.impromptu.ui.Join_us;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpOptionsActivity extends AppCompatActivity {
  SharedPreferences sharedPreferences, sharedPreferences1, socialpref;
  SharedPreferences.Editor editor;
  String user, token, socialtoken,type;
  Dialog progressDialog;
  TextView setting_help_option,deleteAccount,FAQ,help_help_flag;
  ImageView back_on_help;
  Intent intent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_help_options);

    progressDialog = ProgressBarClass.showProgressDialog(this);
    sharedPreferences1 = getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
    sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
    user = sharedPreferences.getString("Usertoken", "");
    token = "Bearer " + sharedPreferences.getString("Usertoken", "");
    socialtoken = "Bearer " + sharedPreferences.getString("Socailtoken", "");

    intent = getIntent();
    type = intent.getStringExtra("usertype");

    setting_help_option = (TextView) findViewById(R.id.help_help_guide);
    deleteAccount = findViewById(R.id.setting_deleteAccount_option);
    back_on_help = findViewById(R.id.back_on_help);
    FAQ = findViewById(R.id.setting_FAQ_option);
    help_help_flag = findViewById(R.id.help_help_flag);


    listeners();
  }

  private void listeners() {
    setting_help_option.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(HelpOptionsActivity.this, HelpActivity.class);
        startActivity(intent);
      }
    });

    deleteAccount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!socialtoken.equals("Bearer "))
        {
          ConfirmationDialog(socialtoken);
        }else {
          ConfirmationDialog(token);
        }
      }
    });

    FAQ.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//                    manager.beginTransaction().replace(R.id.home_frame_layout, new FAQ()).addToBackStack(null).commit();
        Intent intent = new Intent(HelpOptionsActivity.this, FAQs.class);
        startActivity(intent);
      }
    });

    back_on_help.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });


    if (type.equalsIgnoreCase("1"))
    {
      help_help_flag.setVisibility(View.GONE);
    }else
    {
      help_help_flag.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(HelpOptionsActivity.this,FlagInappropriate.class);
          startActivity(intent);
        }
      });
    }
  }

  public void ConfirmationDialog(final String usrToken) {
    final Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.confirmationdialog);
    dialog.setCanceledOnTouchOutside(true);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    Button yes = dialog.findViewById(R.id.yesdialog);
    Button no = dialog.findViewById(R.id.nodialog);
    yes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (isOnline() == true) {

          progressDialog.show();

          Call<RetroDelete> call = WebAPI.getInstance().getApi().delete(usrToken,"application/json");
          call.enqueue(new Callback<RetroDelete>() {
            @Override
            public void onResponse(Call<RetroDelete> call, Response<RetroDelete> response) {
              if (response.body() != null) {
                progressDialog.dismiss();
                dialog.dismiss();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Toast.makeText(HelpOptionsActivity.this, "Your account removed succussfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HelpOptionsActivity.this, Join_us.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

              } else {
                progressDialog.dismiss();
                Toast.makeText(HelpOptionsActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
              }
            }

            @Override
            public void onFailure(Call<RetroDelete> call, Throwable t) {

            }
          });
        } else {
          NoInternetdialog();
          progressDialog.dismiss();
        }
      }
    });

    no.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });

    dialog.show();
  }

  //check internet is online or not
  public boolean isOnline() {
    ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

    if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {

      return false;
    }

    return true;
  }


  private void NoInternetdialog() {

    final Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.nointernetdialog);
    dialog.setCanceledOnTouchOutside(true);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    Button done = dialog.findViewById(R.id.done_bt_on_no_net_dilog);
    done.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//                finish();
//                System.exit(0);
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
      }
    });

    dialog.show();
  }

}
