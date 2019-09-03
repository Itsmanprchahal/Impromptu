package com.mandywebdesign.impromptu.BusinessRegisterLogin;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Adapters.See_Add_adpater;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.Booked_User;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class See_all extends Fragment {

    RecyclerView recyclerView;
    View view;
    FragmentManager manager;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    public static ArrayList<String> userImage = new ArrayList<>();
    public static ArrayList<String> totalticketbuy = new ArrayList<>();
    public static ArrayList<String> userName = new ArrayList<>();

    static String BToken,S_Token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_see_all, container, false);

        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");
        manager = getFragmentManager();

        progressDialog = new ProgressDialog(getActivity());
        Drawable drawable = new ProgressBar(getContext()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(drawable);

        progressDialog.setMessage("Please wait ");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.see_all_recycler_view);

        Bundle bundle = getArguments();
        String value = bundle.getString("value");


        Toolbar toolbar = view.findViewById(R.id.see_all_toolvbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
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

        return view;
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

                        See_Add_adpater add_adpater = new See_Add_adpater(getContext(),manager,userImage,userName,totalticketbuy);
                        recyclerView.setAdapter(add_adpater);

                    }else {
                        progressDialog.dismiss();
                    }
                }else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<Booked_User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
