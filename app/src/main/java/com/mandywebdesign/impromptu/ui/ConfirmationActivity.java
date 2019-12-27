package com.mandywebdesign.impromptu.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroGetEventData;
import com.mandywebdesign.impromptu.Utils.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationActivity extends AppCompatActivity {

    ImageView QRIMAge, confirm_image, confirm_close;
    Button gotomyEvents;
    Bundle bundle;
    TextView eventTitle, eventAddress, eventPrice,confirm_date,confirm_category;
    SharedPreferences preferences;
    String userToken, eventId, eventImage;
    FragmentManager fragmentManager;
    Dialog progressDialog;
    public static String timeFrom,formattedDate,getFormattedDate,id, image, date, postcode, ticktprice, timefrom, timeto, title, location, city, gender, username;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        intent = getIntent();

        if (intent!=null) {
            eventId = intent.getStringExtra("eventID");
        }


        preferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = preferences.getString("Socailtoken", "");
        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();
        Drawable drawable = new ProgressBar(ConfirmationActivity.this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(ConfirmationActivity.this, R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);
        init();
        listerners();
        getEventData(eventId);
    }

    private void getEventData(String eventId) {
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
                            if (datum.getPrice()!=null)
                            {
                                if (datum.getPrice().equals("0"))
                                {
                                    ticktprice = "Free";
                                }else {
                                    ticktprice ="$ "+datum.getPrice();
                                }
                            }else {
                                ticktprice = "Paid";
                            }

                            title = datum.getTitle();
                            timeto = Util.convertTimeStampToTime(Long.parseLong(datum.getEventStartDt()));
                            image = String.valueOf(datum.getFile().get(0).getImg().toString());

                            timeFrom = removeLeadingZeroes(timeto);
                            if (timeFrom.contains(":00"))
                            {
                                timeFrom = removeLeadingZeroes(timeto).replace(":00","");

                            }else {
                                timeFrom=(removeLeadingZeroes(timeto));
                            }

                            Calendar c = Calendar.getInstance();

                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            formattedDate = df.format(c.getTime());
                            c.add(Calendar.DATE, 1);

                            getFormattedDate = df.format(c.getTime());

                            System.out.println("Current time ==> " + c.getTime());

                            if (formattedDate.matches(Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt())))) {
                                confirm_date.setText("Today at " + timeFrom);
                            } else if (getFormattedDate.matches(Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt())))) {
                                confirm_date.setText("Tomorrow at " + timeFrom);
                            } else {

                                String date = Util.convertTimeStampDate(Long.parseLong(datum.getEventStartDt()));
                                /*to change server date formate*/
                                String s1 = date;
                                String[] str = s1.split("/");
                                String str1 = str[0];
                                String str2 = str[1];
                                String str3 = str[2];
                                confirm_date.setText(str2 + "/" + str1 + "/" + str3 + " at " + timeFrom);
                            }

                            confirm_category.setText(datum.getCategory());
                            eventTitle.setText(title);
                            eventAddress.setText(location);
                            eventPrice.setText( ticktprice);
                            Log.d("image", image);

                            eventImage = preferences.getString("eventImage", "");
                            Log.d("eventImage", "" + eventImage);
                            Glide.with(ConfirmationActivity.this).load(eventImage).into(confirm_image);


                            //MAke QR code here.....................................

                            String paid = intent.getStringExtra("paid");
                            String userId = intent.getStringExtra("userId");

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
                        Toast.makeText(ConfirmationActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Intent intent = new Intent(ConfirmationActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RetroGetEventData> call, Throwable t) {
                Toast.makeText(ConfirmationActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void listerners() {

        gotomyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ConfirmationActivity.this,Home_Screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("bookevent","eventBooked");
                startActivity(intent);
                finish();

            }
        });

        confirm_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmationActivity.this,Home_Screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("bookevent","eventBooked");
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        QRIMAge = (ImageView) findViewById(R.id.confirm_qrCode);
        gotomyEvents = (Button) findViewById(R.id.confirm_button);
        confirm_image = (ImageView) findViewById(R.id.confirm_image);
        eventTitle = (TextView) findViewById(R.id.confirm_name);
        eventAddress = (TextView) findViewById(R.id.confirm_address);
        eventPrice = (TextView) findViewById(R.id.confirm_price);
        confirm_close = (ImageView) findViewById(R.id.confirm_close);
        confirm_date = findViewById(R.id.confirm_date);
        confirm_category = findViewById(R.id.confirm_category);
    }

    String removeLeadingZeroes(String s) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() > 0 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }

        return sb.toString();
    }
}
