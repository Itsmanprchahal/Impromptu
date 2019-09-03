package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Adapters.FAQ_Adapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.FAQ;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FAQs extends AppCompatActivity {

    String BToken,S_Token;
    SharedPreferences sharedPreferences;
    RecyclerView faq_recyclerview;
    ArrayList<FAQ.Datum> faqs = new ArrayList<>();
    ProgressDialog dialog;
    ImageButton faq_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        init();
        listerners();
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");
        dialog = ProgressBarClass.showProgressDialog(this,"Please wait...");


        if (!BToken.equalsIgnoreCase("")) {

            getques(BToken);
        } else if (!S_Token.equalsIgnoreCase("")) {
            getques(S_Token);
        }
    }

    private void listerners() {
        faq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getques(String s_token) {

        Call<FAQ> call = WebAPI.getInstance().getApi().faq("Bearer "+s_token);
        call.enqueue(new Callback<FAQ>() {
            @Override
            public void onResponse(Call<FAQ> call, Response<FAQ> response) {
                if (response.body()!=null)
                {

                    dialog.dismiss();

                    if (response.body().getStatus().equals("200"))
                    {
                        faqs.clear();
                        for (int i=0;i<response.body().getData().size();i++)
                        {
                            FAQ.Datum datum = response.body().getData().get(i);
                            faqs.add(datum);
                            setAdapter(faqs);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<FAQ> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void setAdapter(ArrayList<FAQ.Datum> faqs) {
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        faq_recyclerview.setLayoutManager(linearLayout);
        FAQ_Adapter adapter = new FAQ_Adapter(this, faqs);
        faq_recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void init() {
        faq_recyclerview = findViewById(R.id.faq_recyclerview);
        faq_back = findViewById(R.id.faq_back);
    }
}
