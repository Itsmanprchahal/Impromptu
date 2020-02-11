package com.mandywebdesign.impromptu.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.RegisterSlideActivity;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.NormalRetrologin;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Join_us extends AppCompatActivity {
    Button registerHere, mfacebook, mGmail;
    LoginButton facebooklogin;
    CallbackManager callbackManager;
    SharedPreferences sharedPreferences;
    GoogleSignInClient googleSignInClient;
    SignInButton signInButton;
    Dialog progressDialog;
    GoogleApiClient googleApiClient;
    public static String social_token;
    public static String fbUsername, fbToken, fbEmail, imageurl;
    public static int RC_SIGN_IN = 121;
    SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_us);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

//        locationPermission();
        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();
        Drawable drawable = new ProgressBar(this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);


        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        //SharedPrefrnce
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        initIDs();
        facebooklogin.setReadPermissions(Arrays.asList("email", "public_profile"));

        facebookLogin();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

//        getHashKey();


    }

    public void onClick(View v) {
        if (v == mfacebook) {
//            progressDialog.show();
            facebooklogin.performClick();
            if (isOnline() == false) {
                progressDialog.dismiss();
               NoInternet.dialog(Join_us.this);
            }
        }

        if (v == mGmail) {
            progressDialog.show();
            signInButton.performClick();
            if (isOnline() == false) {
                progressDialog.dismiss();
               NoInternet.dialog(Join_us.this);
            } else {
                progressDialog.dismiss();
                googleSignIn();
            }
        }
        if (v == registerHere) {
            Intent intent = new Intent(Join_us.this, RegisterSlideActivity.class);
            startActivity(intent);
        }
    }


    private void facebookLogin() {

        facebooklogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.d("API123", loggedIn + " ??");

                //getFaceBookUserProfile(AccessToken.getCurrentAccessToken());

                //==============================get Facebook Information============================

                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                if (AccessToken.getCurrentAccessToken() != null) {
                                    try {
                                        String first_name = object.getString("first_name");
                                        String last_name = object.getString("last_name");
                                        String email = "";
                                        if (object.getString("email")!=null)
                                        {
                                             email = object.getString("email");
                                            String id = object.getString("id");

                                            fbUsername = first_name + " " + last_name;
                                            Log.d("fbUsername",fbUsername);

                                            final String fbToken1 = AccessToken.getCurrentAccessToken().getToken();
                                            fbEmail = email;
                                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                                            imageurl = image_url;
                                            Log.d("first_name", fbUsername + " " + fbToken + " " + fbEmail + " " + image_url);

                                            //=============================register and login facebook here========================\
                                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                                @Override
                                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                                    String device_token = instanceIdResult.getToken();
                                                    Log.e("token",device_token);
                                                    NormalLogin("normal",fbToken,fbEmail,fbUsername,"facebook",device_token);

                                                }
                                            });
                                        }else {
                                            Toast.makeText(Join_us.this, "We Didn't recog", Toast.LENGTH_SHORT).show();
                                        }


//                                        NormalLogin("normal",fbToken,fbEmail,fbUsername,"facebook");


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,last_name,email,id");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(Join_us.this, "CANCEL", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Join_us.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void NormalLogin(String normal, String Token, String Email, String Username, String type,String device_token) {
        Call<NormalRetrologin> call = WebAPI.getInstance().getApi().normalLogin("normal", Token, Email, Username, type,device_token);
        call.enqueue(new Callback<NormalRetrologin>() {
            @Override
            public void onResponse(Call<NormalRetrologin> call, Response<NormalRetrologin> response) {

                String status = response.body().getStatus();

                if (response.body()!=null)
                {
                    Log.d("facebookdata", "" + fbToken + "\n" + fbEmail + "\n" + fbUsername);
                    if (response.body().getStatus().equals("200")) {

                        progressDialog.dismiss();
                        social_token = response.body().getData().getToken();
                        Log.d("socail", "" + social_token);

                        editor.putString("Social_username", fbUsername);
                        editor.putString("Username",fbUsername);
                        editor.putString("Social_image", imageurl);
                        editor.putString("userID",response.body().getData().getId().toString());
                        editor.putString("Socailtoken", response.body().getData().getToken());
                        editor.putString("SocailuserId", response.body().getData().getId().toString());
                        editor.putBoolean("profileStatus",response.body().getData().getProfileStatus());
                        editor.apply();

                        Intent intent = new Intent(Join_us.this, Home_Screen.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(Join_us.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    } else if (response.body().getStatus().equals("401")) {
                        progressDialog.dismiss();
                        Log.d("FACEBOOK", fbUsername + " " + fbToken + " " + fbEmail);
                        LoginManager.getInstance().logOut();
//                                                    Log.d("facebooktoken",fbToken);
                    }
                }else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(Join_us.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<NormalRetrologin> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Join_us.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void googleSignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void initIDs() {

        // mContinue = (Button) findViewById(R.id.join_us_continue_bt);
        facebooklogin = findViewById(R.id.join_us_facebook);
        mfacebook = findViewById(R.id.join_us_facebookbt);
        mGmail = findViewById(R.id.join_us_google);
        signInButton = findViewById(R.id.join_us_goolesignIn);
        registerHere = findViewById(R.id.registerHere);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);

        } catch (ApiException e) {
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {

        if (account != null) {
            final String email = account.getEmail();
            final String username = account.getDisplayName();


            if (account.getPhotoUrl()!=null) {
                final String image = account.getPhotoUrl().toString();
                final String token = account.getId();
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String device_token = instanceIdResult.getToken();
                        Log.e("token",device_token);
                        normalLogin(email,username,image,token,device_token);

                    }
                });
            } else if (account.getPhotoUrl()==null){
                final String token = account.getId();
                final String image = "data:image/png;base64,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";
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String device_token = instanceIdResult.getToken();
                        Log.e("token",device_token);
                        normalLogin(email,username,image,token,device_token);
                    }
                });
            }
        }
    }

    public void normalLogin(String email, final String username, final String image, String token,String device_token) {
        Call<NormalRetrologin> call = WebAPI.getInstance().getApi().normalLogin("normal", token, email, username, "google",device_token);
        call.enqueue(new Callback<NormalRetrologin>() {
            @Override
            public void onResponse(Call<NormalRetrologin> call, Response<NormalRetrologin> response) {

                fbUsername = username;
                imageurl = image;

                if (response.body() != null)
                {
                    Log.d("email", "" + response.body().getMessage());
                    if (response.body().getStatus().equals("200")) {
                        progressDialog.dismiss();
                        //Log.d("facebook", "" + id);
                        social_token = response.body().getData().getToken();

                        editor.putString("Social_username", fbUsername);
                        editor.putString("Username",fbUsername);
                        editor.putString("Social_image", imageurl);
                        editor.putString("userID",response.body().getData().getId().toString());
                        editor.putString("Socailtoken", response.body().getData().getToken());
                        editor.putString("SocailuserId", response.body().getData().getId().toString());
                        editor.putBoolean("profileStatus",response.body().getData().getProfileStatus());
                        editor.apply();
                        Intent intent = new Intent(Join_us.this, Home_Screen.class);
                        startActivity(intent);
                        finish();
                        Log.d("socailtoken",response.body().getData().getToken());
                        Toast.makeText(Join_us.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    } else if (response.body().getStatus().equals("401")) {
                        Toast.makeText(Join_us.this, "Your email already exist in business account", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                            }
                        });
                    }
                }else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(Join_us.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<NormalRetrologin> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Join_us.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //check internet is online or not
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(Join_us.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
        super.onStart();
    }


    //Genrate HashKey here...
    public void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.mandywebdesign.impromptu",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}

