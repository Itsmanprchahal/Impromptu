package com.mandywebdesign.impromptu.BusinessRegisterLogin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroLoginPojo;
import com.mandywebdesign.impromptu.Retrofit.RetroRegisterPojo;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessCharityRegister extends AppCompatActivity {

    TextView mLogin, forgot_password,requestemail_tv;
    Button mloginDialog, resetPassword_bt, requestotpbt;
    Button letStart, okconfirmdialog;
    EditText mBusinessName, mCharityNumber, mEmail, mPassword, mConfirmPassword, mBusinessLoginET, mBusinessPasswordEt, requestOtpemial,
    OTP,c_newpassword,newpassword;
    Dialog dialog, confirmdialog;
    ImageView closeLOginDialog, closeconfirmdialog;
    public static String s;
    Dialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Dialog popup, resetpopup;
    ImageView close;
    CheckBox showpassword;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_charity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();

        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);

        init();
        listeners();
    }


    private void init() {

        mLogin = findViewById(R.id.business_login);
        letStart = findViewById(R.id.business_register_letsstartBT);
        mBusinessName = findViewById(R.id.business_Name_Et);
        mCharityNumber = findViewById(R.id.business_CharityNumber_Et);
        mEmail = findViewById(R.id.business_Email_Et);
        mPassword = findViewById(R.id.business_Password_Et);
        mConfirmPassword = findViewById(R.id.business_Confirm_Password_Et);

    }

    private void listeners() {


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        letStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String BusinessName = mBusinessName.getText().toString();
                String CharityNumber = mCharityNumber.getText().toString();
                String Email = mEmail.getText().toString();
                String Password = mPassword.getText().toString();
                String ConfirmPassword = mConfirmPassword.getText().toString();

                if (BusinessName.isEmpty() && Email.isEmpty() && Password.isEmpty() && ConfirmPassword.isEmpty()) {
                    mBusinessName.setError("Enter Business Name");
                    mEmail.setError("Enter Email");
                    mPassword.setError("Enter Password");
                    mConfirmPassword.setError("Enter Confirm Password");
                } else if (BusinessName.isEmpty()) {
                    mBusinessName.setError("Enter Business Name");
                    mBusinessName.requestFocus();
                } else if (Email.isEmpty()) {
                    mEmail.setError("Enter Email");
                    mEmail.requestFocus();
                } else if (Password.isEmpty()) {
                    mPassword.setError("Enter Password");
                    mPassword.requestFocus();
                } else if (ConfirmPassword.isEmpty()) {
                    mConfirmPassword.setError("Enter Confirm Password");
                    mConfirmPassword.requestFocus();
                } else if (!Password.matches(ConfirmPassword)) {
                    mPassword.setError("passwords do not match");
                    mConfirmPassword.setError("passwords do not match");
                } else if (mPassword.getText().toString().length()<8)
                {
                    mPassword.setError("Enter Strong Password");
                }else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()) {
                    mEmail.setError("Enter Valid email address");
                } else {
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    Call<RetroRegisterPojo> call = WebAPI.getInstance().getApi().RegisterUser(BusinessName, Email, Password, CharityNumber, "business");
                    call.enqueue(new Callback<RetroRegisterPojo>() {
                        @Override
                        public void onResponse(Call<RetroRegisterPojo> call, Response<RetroRegisterPojo> response) {

                            if (response.body() != null) {
                                if (response.body().getStatus().equals("200")) {
                                    progressDialog.dismiss();
                                    mBusinessName.setText("");
                                    mEmail.setText("");
                                    mPassword.setText("");
                                    mCharityNumber.setText("");
                                    mConfirmPassword.setText("");
                                    confirmaccountdialog();
                                } else if (response.body().getStatus().equals("400")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(BusinessCharityRegister.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                } else if (response.body().getStatus().equals("401")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(BusinessCharityRegister.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    // Toast.makeText(BusinessCharityRegister.this, "Already Registered", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Intent intent = new Intent(BusinessCharityRegister.this, NoInternetScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }


                        }

                        @Override
                        public void onFailure(Call<RetroRegisterPojo> call, Throwable t) {
                            Toast.makeText(BusinessCharityRegister.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            if (isOnline() == false) {
                                progressDialog.dismiss();
                                NoInternet.dialog(BusinessCharityRegister.this);
                            } else if (isOnline() == true) {
                                progressDialog.dismiss();
                                Log.d("regis", "" + t.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private void confirmaccountdialog() {
        confirmdialog = new Dialog(this);
        Window window = confirmdialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        confirmdialog.setContentView(R.layout.confirmaccount);
        confirmdialog.setCancelable(false);
        confirmdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmdialog.show();

        closeconfirmdialog = confirmdialog.findViewById(R.id.close_confirmdialog);
        okconfirmdialog = confirmdialog.findViewById(R.id.businessconfirmdialogbtt);


        closeconfirmdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmdialog.dismiss();
            }
        });

        okconfirmdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmdialog.dismiss();
            }
        });
    }

    private void dialog() {

        dialog = new Dialog(this);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.businesslogindialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        mBusinessLoginET = dialog.findViewById(R.id.businesslogin_email_Et);
        mBusinessPasswordEt = dialog.findViewById(R.id.business_login_password_Et);
        mloginDialog = dialog.findViewById(R.id.businessdialognextbtt);
        closeLOginDialog = dialog.findViewById(R.id.close_logindialog);
        forgot_password = dialog.findViewById(R.id.forgot_password);
        showpassword = dialog.findViewById(R.id.showpassword);
        showpassword.setVisibility(View.GONE);

        closeLOginDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        showpassword.setButtonDrawable(R.drawable.ic_visibility_off_black_24dp);
        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (showpassword.isChecked())
                {
                    showpassword.setButtonDrawable(R.drawable.ic_visibility_off_black_24dp);
                    mBusinessPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    showpassword.setButtonDrawable(R.drawable.ic_visibility_black_24dp);
                    mBusinessPasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        mloginDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String BusinessLoginEmail = mBusinessLoginET.getText().toString();
                final String BusinessPasswordEt = mBusinessPasswordEt.getText().toString();

                if (BusinessLoginEmail.isEmpty() && BusinessPasswordEt.isEmpty()) {
                    mBusinessLoginET.setError("Enter Email");
                    mBusinessPasswordEt.setError("Enter Password");
                } else if (BusinessLoginEmail.isEmpty()) {
                    mBusinessLoginET.setError("Enter Email");
                } else if (BusinessPasswordEt.isEmpty()) {
                    mBusinessPasswordEt.setError("Enter Password");
                } else {
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String token = instanceIdResult.getToken();
                            Log.e("token",token);
                            BussinessLogin(BusinessLoginEmail,BusinessPasswordEt,token);

                        }
                    });
                }
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                forgotDialog();
            }
        });
    }

    private void BussinessLogin(String mBusinessLoginET, String mBusinessPasswordEt, String token) {
        Call<RetroLoginPojo> call = WebAPI.getInstance().getApi().LoginUser(mBusinessLoginET, mBusinessPasswordEt,token);

        call.enqueue(new Callback<RetroLoginPojo>() {
            @Override
            public void onResponse(Call<RetroLoginPojo> call, Response<RetroLoginPojo> response) {

                if (response.body() !=null) {
                    Log.d("tokenUser", "" + response.body().getData().getToken());
                    if (response.body().getStatus().equals("200")) {
                        progressDialog.dismiss();
                        s = response.body().getData().getToken();
                        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
                        Log.d("logintoken", "" + s);
                        editor = sharedPreferences.edit();
                        editor.putString("Usertoken", s);
                        editor.putString("userID", response.body().getData().getId().toString());
                        editor.putString("id", response.body().getData().getId().toString());
                        editor.putString("Username", response.body().getData().getName());
                        editor.apply();
                        Intent intent = new Intent(BusinessCharityRegister.this, Home_Screen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    } else if (response.body().getStatus().equals("400")) {
                        progressDialog.dismiss();
                        Toast.makeText(BusinessCharityRegister.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (response.body().getStatus().equals("401")) {
                        progressDialog.dismiss();
                        Toast.makeText(BusinessCharityRegister.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(BusinessCharityRegister.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RetroLoginPojo> call, Throwable t) {
                Log.d("body", "" + t);
                if (isOnline() == false) {
                    progressDialog.dismiss();
                    NoInternet.dialog(BusinessCharityRegister.this);
                } else if (isOnline() == true) {
                    progressDialog.dismiss();
                    NoInternet.dialog(BusinessCharityRegister.this);
                } else {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void forgotDialog() {
        popup = new Dialog(this);
        Window window = popup.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setContentView(R.layout.requestpassworddialog);
        popup.setCancelable(false);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.show();

        requestOtpemial = popup.findViewById(R.id.resetemail);
        requestotpbt = popup.findViewById(R.id.resetsubmit_bt);
        close = popup.findViewById(R.id.imageView_close);


        requestotpbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = requestOtpemial.getText().toString();
                popup.dismiss();
                resetpopup();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
            }
        });
    }

    private void resetpopup() {

        resetpopup = new Dialog(this);
        Window window = resetpopup.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        resetpopup.setContentView(R.layout.resetpassworddialog);
        resetpopup.setCancelable(false);
        resetpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        resetpopup.show();

        requestemail_tv = resetpopup.findViewById(R.id.requestemail_tv);
        OTP = resetpopup.findViewById(R.id.resetotp);
        newpassword = resetpopup.findViewById(R.id.resetpassword);
        c_newpassword = resetpopup.findViewById(R.id.resetc_password);
        resetPassword_bt = resetpopup.findViewById(R.id.resetpassword_bt);
        close = resetpopup.findViewById(R.id.imageView_close);

        requestemail_tv.setText(email);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetpopup.dismiss();
            }
        });

    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        //Toast.makeText(BusinessCharityRegister.this, "No Internet connection!,Unable to Register", Toast.LENGTH_LONG).show();
        return netInfo != null && netInfo.isConnected() && netInfo.isAvailable();
    }
}
