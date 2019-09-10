package com.mandywebdesign.impromptu.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mandywebdesign.impromptu.R;

import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences,socialpref;
    String user,socaildata;
    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


         account = GoogleSignIn.getLastSignedInAccount(this);

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        user = sharedPreferences.getString("Usertoken", "");
        socaildata = sharedPreferences.getString("Socailtoken","");

        slapshcode();
    }

    private void slapshcode() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //check user login or not
                boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

                if (!loggedOut) {
                    Intent intent = new Intent(SplashScreen.this, Home_Screen.class);
                    startActivity(intent);
                    finish();
                } else
                if (account != null) {

                    Intent intent = new Intent(SplashScreen.this, Home_Screen.class);
                    startActivity(intent);
                    finish();

                } else if (!user.equalsIgnoreCase("") || !socaildata.equalsIgnoreCase(""))
                {
                    Intent intent = new Intent(SplashScreen.this, Home_Screen.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }


}
