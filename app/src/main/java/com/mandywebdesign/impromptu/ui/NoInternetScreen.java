package com.mandywebdesign.impromptu.ui;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mandywebdesign.impromptu.R;

public class NoInternetScreen extends AppCompatActivity {

    Button nointernetBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_screen);

        init();
        listerners();
    }

    private void listerners() {
        nointernetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoInternetScreen.this,Home_Screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void init() {

        nointernetBt = (Button)findViewById(R.id.notinternetretry);
    }
}
