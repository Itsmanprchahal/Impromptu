package com.mandywebdesign.impromptu.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.R;

public class Add_Adverts extends AppCompatActivity {

    Button addPerview;
    ImageView close;
    TextView duration;
    ProgressBar progressBar;
    Spinner spinner;
    String week;
    double total;
    String weeks[] ={"1","2","3","4","5","6","7","8","9","10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__adverts);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        init();
        listerners();

        progressBar.setProgress(50);
        progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorTheme), PorterDuff.Mode.SRC_ATOP);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, weeks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
         adapter.notifyDataSetChanged();

    }

    private void listerners() {

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_Adverts.this,Home_Screen.class);
                startActivity(intent);
                finish();
            }
        });

        addPerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_Adverts.this,PerviewAdverts.class);
                intent.putExtra("total",total);
                startActivity(intent);

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 week = parent.getSelectedItem().toString();
                 int numberOfWeeks = Integer.valueOf(week);
                 double price = 1.99;

                 total = numberOfWeeks * price;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void init() {

        addPerview = findViewById(R.id.addPerview);
        close = findViewById(R.id.closeAdvert);
        progressBar = findViewById(R.id.advertProgressBar);
        spinner = findViewById(R.id.priceSpinner);
        duration = findViewById(R.id.duration);
    }
}
