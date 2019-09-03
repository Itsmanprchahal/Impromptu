package com.mandywebdesign.impromptu.BusinessRegisterLogin;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Adapters.CheckGuestAdapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Models.CheckInGuest;
import com.mandywebdesign.impromptu.Models.TotalCheckIn;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckGuestFragment extends Fragment {


    RecyclerView recyclerView;
    public static TextView QRData;
    View view;
    CheckGuestAdapter adapter;
    public int MY_PERMISSIONS_REQUEST_CAMERA = 1001;
    ImageView scanner;
    ImageButton close;
    String BToken, S_Token;
    FragmentManager manager;
    ArrayList<CheckInGuest.Datum> checkinguset = new ArrayList<>();
    SharedPreferences sharedPreferences;
    TextView checkInguset;
   static   String id,eventType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_check_guest, container, false);
        manager = getFragmentManager();

        init();
        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");
        final Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString("value");
            eventType = bundle.getString("eventType");
            if (eventType.equalsIgnoreCase("history"))
            {
                scanner.setVisibility(View.GONE);
            }
        }


        if (!BToken.equalsIgnoreCase("")) {

            getBookedUSers(BToken);
            getTotalCheckin(BToken, id);

        } else if (!S_Token.equalsIgnoreCase("")) {
            getBookedUSers(S_Token);
            getTotalCheckin(S_Token, id);
        }


        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle1 = new Bundle();
                bundle1.putString("value",id);

                QrScanFragment qrScanFragment = new QrScanFragment();
                qrScanFragment.setArguments(bundle1);

                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_frame_layout,  qrScanFragment);
                transaction.commit();

            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });


        return view;
    }

    private void getBookedUSers(String Token) {

        Call<CheckInGuest> checkInGuestCall = WebAPI.getInstance().getApi().checkinguest("Bearer " + Token, BusinessEvent_detailsFragment.value, "", "");
        checkInGuestCall.enqueue(new Callback<CheckInGuest>() {
            @Override
            public void onResponse(Call<CheckInGuest> call, Response<CheckInGuest> response) {

                if (response.body()!=null) {

                    if (response.body().getStatus().equals("200")) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            checkinguset.add(response.body().getData().get(i));

                            setDat(checkinguset);

                        }

                    }
                    Log.d("Hello1234", "1" + response.message());
                } else
                {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<CheckInGuest> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalCheckin(String s, String eventID) {

        Call<TotalCheckIn> call = WebAPI.getInstance().getApi().totleTickets("Bearer " +s, eventID);
        call.enqueue(new Callback<TotalCheckIn>() {
            @Override
            public void onResponse(Call<TotalCheckIn> call, Response<TotalCheckIn> response) {
                if (response.body()!=null)
                {
                    checkInguset.setText(response.body().getData().getCheckinTotal() + "/" + response.body().getData().getTotal() + " Checked in");
                }else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                   }

            @Override
            public void onFailure(Call<TotalCheckIn> call, Throwable t) {
            }
        });
    }

    private void setDat(ArrayList<CheckInGuest.Datum> checkinguset) {

        adapter = new CheckGuestAdapter(getContext(), checkinguset);
        LinearLayoutManager linearLayoune = new LinearLayoutManager(getActivity());
        linearLayoune.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoune);
        recyclerView.setAdapter(adapter);
    }

    private void init() {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.check_guest_recycler);
        scanner = (ImageView) view.findViewById(R.id.check_guest_qrcode);
        close = (ImageButton) view.findViewById(R.id.check_guest_close);
        QRData = (TextView) view.findViewById(R.id.QRData);
        checkInguset = view.findViewById(R.id.check_guest_text);

    }

}
