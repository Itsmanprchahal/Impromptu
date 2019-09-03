package com.mandywebdesign.impromptu.BusinessRegisterLogin;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.GusetCheckIns;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class QrScanFragment extends Fragment {

    SurfaceView surfaceView;
    CameraSource source;
    BarcodeDetector barcodeDetector;
    TextView textView;
    ImageView back;
    View view;
    FragmentManager manager;
    SharedPreferences sharedPreferences;
    String BToken, S_Token, id;
    ProgressDialog progressDialog;

    public QrScanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_qr_scan, container, false);
        manager = getFragmentManager();

        progressDialog = new ProgressDialog(getActivity());
        Drawable drawable = new ProgressBar(getContext()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(drawable);

        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        BToken = sharedPreferences.getString("Usertoken", "");
        S_Token = sharedPreferences.getString("Socailtoken", "");

        Bundle bundle = getArguments();
        id = bundle.getString("value");

        init();

        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.QR_CODE).build();


        source = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(640, 480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)
                                getContext(), Manifest.permission.CAMERA)) {


                        } else {
                            ActivityCompat.requestPermissions((Activity) getContext(),
                                    new String[]{Manifest.permission.CAMERA},
                                    110);
                        }

                    }
                    return;
                }
                try {
                    source.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                source.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> qrcode = detections.getDetectedItems();

                if (qrcode.size() != 0) {

                    textView.post(new Runnable() {
                        @Override
                        public void run() {

                            textView.setText(qrcode.valueAt(0).displayValue);
                            String bar_values = qrcode.valueAt(0).displayValue;

                            String s = bar_values;

                            String[] arrayString = s.split("\n");

                            String paid = arrayString[0];
                            String event_id = arrayString[1];
                            String event_title = arrayString[2];
                            String booked_user_id = arrayString[3];

                            Log.e("Value_of_bar", paid + "");
                            Log.e("Value_of_bar", event_id + "");
                            Log.e("Value_of_bar", booked_user_id + "");
                            Log.e("Value_of_bar", event_title + "");

                            //  Toast.makeText(getContext(), ""+bar_values, Toast.LENGTH_SHORT).show();


                            if (!BToken.equals("")) {
                                if (id.equals(event_id)) {

                                    //QR event ID
                                    guestCheckIn(BToken, event_id, "1", booked_user_id);
                                } else {
                                    Toast.makeText(getContext(), "Not Valid QR Code", Toast.LENGTH_SHORT).show();
                                }
                            } else if (!S_Token.equals("")) {
                                if (id.equals(event_id)) {
                                    guestCheckIn(S_Token, event_id, "1", booked_user_id);
                                } else {
                                    Toast.makeText(getContext(), "Not Valid QR Code", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    });


                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_frame_layout, new CheckGuestFragment());
                transaction.commit();
            }
        });

        return view;

    }

    private void guestCheckIn(String s_token, String event_id, String s, String booked_user_id) {

        progressDialog.show();
        source.stop();
        Call<GusetCheckIns> checkInsCall = WebAPI.getInstance().getApi().guestCheckIns("Bearer " + s_token, event_id, s, booked_user_id);
        checkInsCall.enqueue(new Callback<GusetCheckIns>() {

            @SuppressLint("MissingPermission")
            @Override
            public void onResponse(Call<GusetCheckIns> call, Response<GusetCheckIns> response) {

                if (response.body()!=null) {
                    progressDialog.dismiss();

                    if (response.body().getStatus().equals("200")) {
                        Toast.makeText(getContext(), "Checked In ", Toast.LENGTH_SHORT).show();

                        Vibrator v = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            v = (Vibrator) Objects.requireNonNull(getActivity()).getSystemService(Context.VIBRATOR_SERVICE);
                        }
                        // Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(500);
                        }
                    }

                } else {
                    Intent intent = new Intent(getContext(), NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<GusetCheckIns> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "141 " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        surfaceView = view.findViewById(R.id.cameraPerview);
        textView = (TextView) view.findViewById(R.id.qr_text);
        back = (ImageView) view.findViewById(R.id.back_on_QR);
    }


}
