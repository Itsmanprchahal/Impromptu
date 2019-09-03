package com.mandywebdesign.impromptu.Home_Screen_Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.Models.ChangePassword;
import com.mandywebdesign.impromptu.Retrofit.RetroLogout;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.Contact_Us;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.FAQs;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.Help;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.Privacy;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.TermsAndConditions;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.UserProfileFragment;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.Join_us;
import com.mandywebdesign.impromptu.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Setting extends Fragment {


    TextView logout, setting_help_option, terms, setting_paymentdetails_option, privancy, FAQ, changepassword, contactus, deleteAccount, invite, setting_verification_option;
    GoogleApiClient googleApiClient;
    boolean loggedOut;
    GoogleSignInAccount account;
    FragmentManager manager;
    SharedPreferences sharedPreferences, sharedPreferences1, socialpref;
    SharedPreferences.Editor editor;
    String user, token, socialtoken;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    Dialog popup, changepasswordpopup;
    EditText oldepassword, newpassword, confirmpass;
    Button changepasswordbt;
    ImageView imageView_close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_setting, container, false);
        FacebookSdk.sdkInitialize(getContext());
        manager = getFragmentManager();

        builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Coming soon...");


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        Drawable drawable = new ProgressBar(getContext()).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(drawable);


        sharedPreferences1 = getActivity().getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        user = sharedPreferences.getString("Usertoken", "");
        token = "Bearer " + sharedPreferences.getString("Usertoken", "");
        socialtoken = "Bearer " + sharedPreferences.getString("Socailtoken", "");

        account = GoogleSignIn.getLastSignedInAccount(getContext());
        //facebook check
        loggedOut = AccessToken.getCurrentAccessToken() == null;

        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        logout = view.findViewById(R.id.setting_logout_option);
        setting_help_option = (TextView) view.findViewById(R.id.setting_help_option);
        terms = view.findViewById(R.id.setting_termsandconditions_option);
        privancy = view.findViewById(R.id.setting_privancyStatement_option);
        FAQ = view.findViewById(R.id.setting_FAQ_option);
        contactus = view.findViewById(R.id.setting_contactUs_option);
        deleteAccount = view.findViewById(R.id.setting_deleteAccount_option);
        invite = view.findViewById(R.id.setting_inviteuser_option);
        setting_verification_option = view.findViewById(R.id.setting_verification_option);
        setting_paymentdetails_option = view.findViewById(R.id.setting_paymentdetails_option);

        if (account != null | !loggedOut) {
            TextView profile = view.findViewById(R.id.setting_profile_option);
            View view1 = view.findViewById(R.id.view1);
            profile.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);


            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.home_frame_layout, new UserProfileFragment());
                    transaction.commit();

                }
            });

            setting_verification_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Creating dialog box
                }
            });

            setting_paymentdetails_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            builder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            setting_help_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.beginTransaction().replace(R.id.home_frame_layout, new Help()).addToBackStack(null).commit();
                }
            });

            terms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.beginTransaction().replace(R.id.home_frame_layout, new TermsAndConditions()).addToBackStack(null).commit();
                }
            });

            privancy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.beginTransaction().replace(R.id.home_frame_layout, new Privacy()).addToBackStack(null).commit();
                }
            });

            FAQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    manager.beginTransaction().replace(R.id.home_frame_layout, new FAQ()).addToBackStack(null).commit();
                    Intent intent = new Intent(getContext(), FAQs.class);
                    startActivity(intent);
                }
            });

            contactus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    manager.beginTransaction().replace(R.id.home_frame_layout, new ContactUs()).addToBackStack(null).commit();
                    Intent intent = new Intent(getContext(), Contact_Us.class);
                    startActivity(intent);
                }
            });

            deleteAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmationDialog(socialtoken);
                }
            });

            invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.infrasoft.uboi"));
                    startActivity(intent);
                }
            });

        } else {
            TextView profile = view.findViewById(R.id.setting_profile_option);
            View view1 = view.findViewById(R.id.view1);

            profile.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);

            changepassword = view.findViewById(R.id.setting_changepassword_option);
            View view2 = view.findViewById(R.id.passwordview);
            changepassword.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
        }


        if (AccessToken.getCurrentAccessToken() != null) {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOnline() == true) {
                        LoginManager.getInstance().logOut();
                        Intent intent = new Intent(getActivity(), Join_us.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.clear();
                        editor1.commit();

                        progressDialog.setMessage("Please wait until we logout your account");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        getActivity().finish();
                    } else {
                        NoInternetdialog();
                        progressDialog.dismiss();
                    }
                }
            });

        } else if (!user.equalsIgnoreCase("")) {

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isOnline() == false) {
                        NoInternetdialog();
                        progressDialog.dismiss();
                    } else {

                        Call<RetroLogout> call = WebAPI.getInstance().getApi().logout(token, "application/json");
                        call.enqueue(new Callback<RetroLogout>() {
                            @Override
                            public void onResponse(Call<RetroLogout> call, Response<RetroLogout> response) {
                                if (response.body() != null) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();

                                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                    editor1.clear();
                                    editor1.commit();

                                    progressDialog.setMessage("Please wait until we logout your account");
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.show();

                                    Intent intent = new Intent(getActivity(), Join_us.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getActivity().startActivity(intent);


                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "" + response.message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RetroLogout> call, Throwable t) {
                                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

            changepassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changepassswordpopup();
                }
            });

            setting_help_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.beginTransaction().replace(R.id.home_frame_layout, new Help()).addToBackStack(null).commit();
                }
            });


            setting_paymentdetails_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            builder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            terms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.beginTransaction().replace(R.id.home_frame_layout, new TermsAndConditions()).addToBackStack(null).commit();
                }
            });

            privancy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.beginTransaction().replace(R.id.home_frame_layout, new Privacy()).addToBackStack(null).commit();
                }
            });

            FAQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    manager.beginTransaction().replace(R.id.home_frame_layout, new FAQ()).addToBackStack(null).commit();
                    Intent intent = new Intent(getContext(), FAQs.class);
                    startActivity(intent);
                }
            });

            contactus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    manager.beginTransaction().replace(R.id.home_frame_layout, new ContactUs()).addToBackStack(null).commit();
                    Intent intent = new Intent(getContext(), Contact_Us.class);
                    startActivity(intent);
                }
            });

            deleteAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmationDialog(token);
                }
            });

            invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.infrasoft.uboi"));
                    startActivity(intent);
                }
            });

        } else {

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOnline() == true) {

                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {

                                progressDialog.setMessage("Please wait until we logout your account");
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();

                                Intent intent = new Intent(getActivity(), Join_us.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);

                            }
                        });
                    } else {
                        NoInternetdialog();
                        progressDialog.dismiss();
                    }
                }
            });

        }


        return view;
    }


    private void changepassswordpopup() {
        changepasswordpopup = new Dialog(getContext());
        Window window = changepasswordpopup.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        changepasswordpopup.setContentView(R.layout.changepassworddialog);
        changepasswordpopup.setCancelable(false);
        changepasswordpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changepasswordpopup.show();

        oldepassword = changepasswordpopup.findViewById(R.id.oldepassword);
        newpassword = changepasswordpopup.findViewById(R.id.newpassword);
        confirmpass = changepasswordpopup.findViewById(R.id.confirmpass);
        changepasswordbt = changepasswordpopup.findViewById(R.id.changepasswordbt);
        imageView_close = changepasswordpopup.findViewById(R.id.imageView_close);

        changepasswordbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String old = oldepassword.getText().toString();
                String newp = newpassword.getText().toString();
                String cnewp = confirmpass.getText().toString();
                if (TextUtils.isEmpty(oldepassword.getText()) && TextUtils.isEmpty(newpassword.getText()) && TextUtils.isEmpty(confirmpass.getText())) {
                    oldepassword.setError("Enter Old Password");
                    newpassword.setError("Enter New Password");
                    confirmpass.setError("Enter Confirm Password");
                } else if (TextUtils.isEmpty(oldepassword.getText())) {
                    oldepassword.setError("Enter Old Password");
                } else if (TextUtils.isEmpty(newpassword.getText())) {
                    newpassword.setError("Enter New Password");
                } else if (TextUtils.isEmpty(confirmpass.getText())) {
                    confirmpass.setError("Enter Confirm Password");
                } else if (!newpassword.getText().toString().matches(confirmpass.getText().toString())) {
                    confirmpass.setError("Password not matched");
                } else {
                    progressDialog.show();
                    Call<ChangePassword> changePasswordCall = WebAPI.getInstance().getApi().changepassword(token, old, newp, cnewp);
                    changePasswordCall.enqueue(new Callback<ChangePassword>() {
                        @Override
                        public void onResponse(Call<ChangePassword> call, Response<ChangePassword> response) {
                            if (response.body() != null) {
                                progressDialog.dismiss();

                                if (response.body().getStatus().equals("200")) {
                                    changepasswordpopup.dismiss();
                                }

                                Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChangePassword> call, Throwable t) {

                        }
                    });
                }
            }
        });

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changepasswordpopup.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
        super.onStart();
    }


    //check internet is online or not
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            // Toast.makeText(getContext(), "No Internet connection!,Unable to SignOut", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void NoInternetdialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.nointernetdialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button done = dialog.findViewById(R.id.done_bt_on_no_net_dilog);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
//                System.exit(0);
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });

        dialog.show();
    }


    public void ConfirmationDialog(final String usrToken) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.confirmationdialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button yes = dialog.findViewById(R.id.yesdialog);
        Button no = dialog.findViewById(R.id.nodialog);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline() == true) {

                    progressDialog.setMessage("Please wait until we delete your account");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();


                    Call call = WebAPI.getInstance().getApi().delete(usrToken, "application/json");
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.body() != null) {
                                progressDialog.dismiss();
                                dialog.dismiss();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();

                                Toast.makeText(getContext(), "Your account removed succussfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), Join_us.class);
                                getActivity().startActivity(intent);
                                getActivity().finish();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "" + response.message(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    NoInternetdialog();
                    progressDialog.dismiss();
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public int getWifiLevel() {
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int linkSpeed = wifiManager.getConnectionInfo().getRssi();
        int level = WifiManager.calculateSignalLevel(linkSpeed, 5);
        return level;
    }

}