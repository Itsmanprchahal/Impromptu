package com.mandywebdesign.impromptu.BusinessRegisterLogin;

import android.content.Intent;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mandywebdesign.impromptu.Adapters.RegisterSliderAdapter;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.ui.Join_us;

public class RegisterSlideActivity extends AppCompatActivity {

    ConstraintLayout layout;
    Button mBack, mRegister;
    ViewPager viewPager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    int currentPage,mBackPressCount;
    RegisterSliderAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_slide);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        init();

        adapter = new RegisterSliderAdapter(RegisterSlideActivity.this);
        viewPager.setAdapter(adapter);
        addBottomDots(0);
        viewPager.addOnPageChangeListener(viewListener);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterSlideActivity.this, Join_us.class);
                startActivity(intent);
                finish();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterSlideActivity.this,BusinessCharityRegister.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() ==0){
            if (mBackPressCount++ > 0) {
                // if you pressed back for the second time this line will terminate the activity
                super.onBackPressed();
            }
        }else
            super.onBackPressed();
    }

    private void init() {

        layout = findViewById(R.id.register_conststraint);
        mBack = findViewById(R.id.back_bussiness_bt);
        mRegister = findViewById(R.id.Register_bussiness_bt);
        dotsLayout = findViewById(R.id.registerlayoutDots);
        viewPager = findViewById(R.id.register_viewpager);

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
            if (i == 0) {

                mRegister.setVisibility(View.INVISIBLE);
                mBack.setVisibility(View.VISIBLE);
            }

            if (i == 1) {
                mBack.setVisibility(View.INVISIBLE);
                mRegister.setVisibility(View.VISIBLE);
            }

            if (i==2)
            {
                mBack.setVisibility(View.INVISIBLE);
                mRegister.setText("Register");
                mRegister.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

            if (i == ViewPager.SCROLL_STATE_IDLE) {
                int curr = viewPager.getCurrentItem();
                int lastReal = viewPager.getAdapter().getCount() - 1;

                if (curr < lastReal) {
                    currentPage = 0;
                } else if (curr == lastReal) {
                    currentPage++;

                    if (currentPage > 1) {
                        startActivity(new Intent(getApplicationContext(), BusinessCharityRegister.class));
                        finish();
                    }
                }
            }
        }
    };

}
