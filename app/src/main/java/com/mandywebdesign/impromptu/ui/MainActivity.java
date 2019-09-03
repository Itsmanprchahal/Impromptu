package com.mandywebdesign.impromptu.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mandywebdesign.impromptu.Adapters.SliderAdapter;
import com.mandywebdesign.impromptu.R;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    RelativeLayout layout;
    Button mSkip;
    ViewPager viewPager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    SliderAdapter sliderAdapter;
    int currentPage, mBackPressCount;
    private static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        init();
        checkFBUserLoginOrNot();
        checkPermissions();

        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        addBottomDots(0);
        viewPager.addOnPageChangeListener(viewListener);


        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Join_us.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            if (mBackPressCount++ > 0) {
                // if you pressed back for the second time this line will terminate the activity
                super.onBackPressed();
            }
        } else
            super.onBackPressed();
    }

    //find ids here
    private void init() {
        mSkip = (Button) findViewById(R.id.mainActivity_skip);
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        layout = (RelativeLayout) findViewById(R.id.mainActivity_layout);
    }


    //add dots at bottom
    private void addBottomDots(int currentPage) {
        dots = new TextView[3];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(50);
            dots[i].setTextColor(getResources().getColor(R.color.colortextwhite));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(getResources().getColor(R.color.colorTheme));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addBottomDots(i);

        }

        @Override
        public void onPageScrollStateChanged(int i) {

            if (currentPage == 0) {
                mSkip.setText("Skip");
            }

            if (currentPage == 1) {
                mSkip.setText("Skip");
            }

            if (i == ViewPager.SCROLL_STATE_IDLE) {
                int curr = viewPager.getCurrentItem();
                int lastReal = viewPager.getAdapter().getCount() - 1;

                if (curr < lastReal) {
                    currentPage = 0;
                } else if (curr == lastReal) {
                    currentPage++;

                    if (currentPage == 2) {
                        startActivity(new Intent(getApplicationContext(), Join_us.class));
                        finish();
                    }

                    if (currentPage == 1) {
                        mSkip.setText("Start");
                    }
                }
            }
        }
    };

    public void checkFBUserLoginOrNot() {
        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

        if (!loggedOut) {
            Toast.makeText(this, "User allready login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Home_Screen.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            Toast.makeText(this, "User allready login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Home_Screen.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(MainActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        } else {
        }
        return true;
    }
}