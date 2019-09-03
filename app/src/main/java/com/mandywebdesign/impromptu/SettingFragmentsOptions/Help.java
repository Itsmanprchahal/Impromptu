package com.mandywebdesign.impromptu.SettingFragmentsOptions;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Setting;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroHelp;
import com.mandywebdesign.impromptu.ui.Join_us;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Help extends Fragment {
    TextView help;
    View view;
    SharedPreferences sharedPreferences;
    String user,social_token;
    ProgressDialog progressDialog;
    ImageView back;
    FragmentManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_help, container, false);

        manager = getFragmentManager();
        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        user =  sharedPreferences.getString("Usertoken", "");
        social_token =  sharedPreferences.getString("Socailtoken", "");

        progressDialog = ProgressBarClass.showProgressDialog(getContext(),"please wait...");

        init();
        listeners();

        if (!user.equalsIgnoreCase(""))
        {
            help(user);
        }else if (!social_token.equalsIgnoreCase(""))
        {
            help(social_token);
        }



        return view;
    }



    private void help(String user) {

        Call<RetroHelp> call = WebAPI.getInstance().getApi().help("Bearer "+user,"application/json");
        call.enqueue(new Callback<RetroHelp>() {
            @Override
            public void onResponse(Call<RetroHelp> call, Response<RetroHelp> response) {

                if (response.body() != null)
                {
                    if (response.body().getStatus().equals("200"))
                    {
                        String helpdata = response.body().getData().get(0).getContent();
                        // Toast.makeText(getContext(), ""+helpdata, Toast.LENGTH_SHORT).show();
                        help.setText(helpdata);
                        progressDialog.dismiss();
                    }else if (response.body().getStatus().equals("401"))
                    {
                        //Toast.makeText(getContext(), ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(getActivity(), Join_us.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();

                    }
                }else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<RetroHelp> call, Throwable t) {
                if (NoInternet.isOnline(getContext())==false)
                {
                    progressDialog.dismiss();

                    NoInternet.dialog(getContext());
                }
            }
        });
    }


    private void listeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    manager.beginTransaction().replace(R.id.home_frame_layout,new Setting()).commit();
            }
        });
    }

    private void init() {

        help = view.findViewById(R.id.help_text);
        back = view.findViewById(R.id.back_on_help);
    }

}
