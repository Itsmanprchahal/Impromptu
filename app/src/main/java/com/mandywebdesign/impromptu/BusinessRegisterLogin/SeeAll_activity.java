package com.mandywebdesign.impromptu.BusinessRegisterLogin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Adapters.See_Add_adpater;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.Booked_User;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAll_activity extends AppCompatActivity {

    ImageView backonseeall;
    RecyclerView recyclerView;
    FragmentManager manager;
    Dialog progressDialog;
    SharedPreferences sharedPreferences;
    public static ArrayList<String> userImage = new ArrayList<>();
    public static ArrayList<String> totalticketbuy = new ArrayList<>();
    public static ArrayList<String> userName = new ArrayList<>();
    String BToken,S_Token;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_activity);


        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");

        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();
        Drawable drawable = new ProgressBar(SeeAll_activity.this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(SeeAll_activity.this, R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        backonseeall = findViewById(R.id.backonseeall);
        recyclerView = findViewById(R.id.see_all_recycler_view);

        intent = getIntent();

        String value = intent.getStringExtra("value");


        final LinearLayoutManager layoutManager = new LinearLayoutManager(SeeAll_activity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);


        if (!S_Token.equalsIgnoreCase(""))
        {
            getUsers(S_Token,value);
        }else if (!BToken.equalsIgnoreCase(""))
        {
            getUsers(BToken,value);
        }

        backonseeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getUsers(String s_token, String value) {

        Call<Booked_User> call = WebAPI.getInstance().getApi().booked_users("Bearer " + s_token, value);
        call.enqueue(new Callback<Booked_User>() {
            @Override
            public void onResponse(Call<Booked_User> call, Response<Booked_User> response) {

                progressDialog.dismiss();
                if (response.body()!=null)
                {
                    Booked_User booked_users = response.body();
                    List<Booked_User.Datum> bookedUsersList = booked_users.getData();
                    userImage.clear();
                    totalticketbuy.clear();

                    if (response.body().getStatus().equals("200")) {
                        for (int i = 0; i < bookedUsersList.size(); i++) {
                            userImage.add(response.body().getData().get(i).getFile());
                            userName.add(response.body().getData().get(i).getUsername());
                            totalticketbuy.add(response.body().getData().get(i).getTotalTickets().toString());

                            Log.d("userImage", "" + response.body().getData().get(i).getFile());
                            progressDialog.dismiss();

                        }

                        See_Add_adpater add_adpater = new See_Add_adpater(SeeAll_activity.this,manager,userImage,userName,totalticketbuy);
                        recyclerView.setAdapter(add_adpater);

                    }else {
                        progressDialog.dismiss();
                    }
                }else {
                    Intent intent = new Intent(SeeAll_activity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<Booked_User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SeeAll_activity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
