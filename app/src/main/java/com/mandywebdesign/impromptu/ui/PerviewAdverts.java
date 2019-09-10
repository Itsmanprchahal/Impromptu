package com.mandywebdesign.impromptu.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mandywebdesign.impromptu.R;

public class PerviewAdverts extends AppCompatActivity {

    ImageView close;
    ProgressBar progressBar;
    CardView cardView;
    String total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perview_adverts);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = findViewById(R.id.advertPerviewtoolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        Intent intent = getIntent();
        total = intent.getStringExtra("total");
        Toast.makeText(this, ""+total, Toast.LENGTH_SHORT).show();

        init();
        listerners();

        progressBar.setProgress(100);
        progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorTheme), PorterDuff.Mode.SRC_ATOP);

    }

    private void listerners() {

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerviewAdverts.this,Home_Screen.class);
                startActivity(intent);
            }
        });
    }

    private void init() {

        close = findViewById(R.id.closeAdvertperview);
        progressBar = findViewById(R.id.advertperviewProgressBar);
        cardView = findViewById(R.id.cardadvers);

    }
}
