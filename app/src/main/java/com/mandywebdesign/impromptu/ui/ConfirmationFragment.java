package com.mandywebdesign.impromptu.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.Events;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroGetEventData;
import com.mandywebdesign.impromptu.Utils.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmationFragment extends Fragment {


    View view;
    ImageView QRIMAge, confirm_image, confirm_close;
    Button gotomyEvents;
    Bundle bundle;
    TextView eventTitle, eventAddress, eventPrice;
    SharedPreferences preferences;
    String userToken, eventId, eventImage;
    FragmentManager fragmentManager;
    ProgressDialog progressDialog;
    public static String id, image, date, postcode, ticktprice, timefrom, timeto, title, location, city, gender, username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_confirmation, container, false);

        fragmentManager = getFragmentManager();
        bundle = getArguments();
        eventId = bundle.getString("eventID");
        preferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = preferences.getString("Socailtoken", "");


        progressDialog = new ProgressDialog(getContext());
        Drawable drawable = new ProgressBar(getContext()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(drawable);

        init();
        listerners();
        getEventData(eventId);

        return view;
    }

    private void getEventData(String eventId) {
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Call<RetroGetEventData> call = WebAPI.getInstance().getApi().getEvents("Bearer " + userToken, "application/json", eventId);
        call.enqueue(new Callback<RetroGetEventData>() {
            @Override
            public void onResponse(Call<RetroGetEventData> call, Response<RetroGetEventData> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("200")) {
                        RetroGetEventData getEventData = response.body();
                        Log.d("123456", "" + getEventData.getData().size());
                        List<RetroGetEventData.Datum> datumList = getEventData.getData();
                        Log.d("events", "" + response.code() + " " + datumList.size());
                        for (RetroGetEventData.Datum datum : datumList) {
                            location = datum.getAddressline1();
                            if (datum.getPrice().equals("0"))
                            {
                                ticktprice = "Free";
                            }else {
                                ticktprice ="$ "+datum.getPrice();
                            }
                            title = datum.getTitle();
                            timeto = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt()));
                            image = String.valueOf(datum.getFile().get(0).getImg().toString());

                            eventTitle.setText(title);
                            eventAddress.setText(location + "\n" + timeto);
                            eventPrice.setText( ticktprice);
                            Log.d("image", image);

                            eventImage = preferences.getString("eventImage", "");
                            Log.d("eventImage", "" + eventImage);
                            Glide.with(getActivity()).load(eventImage).into(confirm_image);


                            //MAke QR code here.....................................

                            String paid = bundle.getString("paid");
                            String userId = bundle.getString("userId");

                            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                            try {
                                BitMatrix bitMatrix = multiFormatWriter.encode("Paid" + "\n", BarcodeFormat.QR_CODE, 700, 700);
                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                QRIMAge.setImageBitmap(bitmap);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();

                        }
                    } else if (response.body().getStatus().equals("400")) {
                        Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RetroGetEventData> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void listerners() {

        gotomyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                transaction2.replace(R.id.home_frame_layout, new Events());
                transaction2.addToBackStack(null);
                transaction2.commit();
            }
        });

        confirm_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                transaction2.replace(R.id.home_frame_layout, new Events());
                transaction2.addToBackStack(null);
                transaction2.commit();
            }
        });
    }

    private void init() {
        QRIMAge = (ImageView) view.findViewById(R.id.confirm_qrCode);
        gotomyEvents = (Button) view.findViewById(R.id.confirm_button);
        confirm_image = (ImageView) view.findViewById(R.id.confirm_image);
        eventTitle = (TextView) view.findViewById(R.id.confirm_name);
        eventAddress = (TextView) view.findViewById(R.id.confirm_address);
        eventPrice = (TextView) view.findViewById(R.id.confirm_price);
        confirm_close = (ImageView) view.findViewById(R.id.confirm_close);
    }
}
