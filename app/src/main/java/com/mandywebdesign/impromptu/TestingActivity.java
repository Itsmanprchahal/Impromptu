package com.mandywebdesign.impromptu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_screen);

            Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();

        String refreshh = getIntent().getStringExtra("refresh");
        if (refreshh!=null && refreshh.length()!=0)
            Log.d("++++++++","++ refresh ++"+refreshh);
    }
}
