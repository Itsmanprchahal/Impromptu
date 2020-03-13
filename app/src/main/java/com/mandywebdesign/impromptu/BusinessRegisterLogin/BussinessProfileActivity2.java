package com.mandywebdesign.impromptu.BusinessRegisterLogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.ui.Home_Screen;

public class BussinessProfileActivity2 extends AppCompatActivity {

    Button mPreviewbt, mBack;
    FragmentManager manager;
    Button facebook_button, Done, insta_button, twitter_bt;
    EditText enterURL, enter_web_url;
    RoundedImageView imageView;
    View view;
    TextView BName;
    String facebookUrl, instURL, twitterUrl, webURL;
    Bundle bundle;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String Sname, image;
    Bundle bundle1;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_profile2);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        init();

        // enter_web_url.setText("https://");
        sharedPreferences = getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        Sname = sharedPreferences.getString("name", "");
        image = sharedPreferences.getString("image", image);
        BName.setText(Sname);
        facebookUrl = BusinessUserProfile.facebookURL;
        instURL = BusinessUserProfile.instaGramURL;
        twitterUrl = BusinessUserProfile.TwitteURL;

        intent = getIntent();


        if (image != null) {
            imageView.setImageBitmap(decodeBase64(image));
        }


        if (Home_Screen.BprofileStatus.equals("1")) {
            if (intent != null) {
                String value = intent.getStringExtra("value");
                if (value.equals("2") || value.equals("back")) {
                    enter_web_url.setText(BusinessUserProfile.webURL);
                }
            }
        } else {
            enter_web_url.setText("");
        }



        final Dialog dialog = new Dialog(BussinessProfileActivity2.this);
        dialog.setContentView(R.layout.urlpopup);

        enterURL = dialog.findViewById(R.id.enter_url);
        if (enterURL.equals(""))
        {
            Toast.makeText(BussinessProfileActivity2.this, "Add all Social links", Toast.LENGTH_SHORT).show();
        }

        listeners();
    }

    private void listeners() {

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BussinessProfileActivity2.this,BussinessProfileAcitivity1.class);
                intent.putExtra("value","back");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });



        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(BussinessProfileActivity2.this);
                dialog.setContentView(R.layout.urlpopup);

                dialog.show();

                enterURL = dialog.findViewById(R.id.enter_url);
                Done = dialog.findViewById(R.id.done_pop_up_bt);

                if (Home_Screen.BprofileStatus.equals("1")) {
                    intent = getIntent();

                    if (intent != null) {
                        String value = intent.getStringExtra("value");
                        if (value.equals("2") || value.equals("back")) {
                            enterURL.setText(BusinessUserProfile.facebookURL);

                            Done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (Patterns.WEB_URL.matcher(enterURL.getText().toString()).matches())
                                    {
                                        facebookUrl = enterURL.getText().toString();
                                        dialog.dismiss();
                                    }else if (enterURL.getText().toString().equals(""))
                                    {
                                        facebookUrl = "";
                                        dialog.dismiss();
                                    }else {
                                        enterURL.setError("Add valid URL");
                                    }


                                }
                            });
                        }
                    }
                } else {
                    enterURL.setText("");
                    enterURL.setSelection(enterURL.getText().length());
                    facebookUrl = enterURL.getText().toString();
                    Done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Patterns.WEB_URL.matcher(enterURL.getText().toString()).matches())
                            {
                                facebookUrl = enterURL.getText().toString();
                               dialog.dismiss();
                            }else if (enterURL.getText().toString().equals(""))
                            {
                                facebookUrl = "";
                                dialog.dismiss();
                            }else {
                                enterURL.setError("Add valid URL");
                            }

//                            facebookUrl = enterURL.getText().toString();
//                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        insta_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(BussinessProfileActivity2.this);
                dialog.setContentView(R.layout.urlpopup);

                enterURL = dialog.findViewById(R.id.enter_url);
                Done = dialog.findViewById(R.id.done_pop_up_bt);
                dialog.show();

                if (Home_Screen.BprofileStatus.equals("1")) {
                    intent = getIntent();
                    if (intent != null) {
                        String value = intent.getStringExtra("value");
                        if (value.equals("2") || value.equals("back")) {
                            enterURL.setText(BusinessUserProfile.instaGramURL);

                            Done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (Patterns.WEB_URL.matcher(enterURL.getText().toString()).matches())
                                    {
                                        instURL = enterURL.getText().toString();
                                        dialog.dismiss();
                                    }else if (enterURL.getText().toString().equals("")){
                                        instURL = "";
                                        dialog.dismiss();
                                    }else {
                                        enterURL.setError("Add valid URL");
                                    }

                                }
                            });

                        }
                    }
                } else {
                    enterURL.setText("");
                    enterURL.setSelection(enterURL.getText().length());
                    instURL = enterURL.getText().toString();
                    Done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Patterns.WEB_URL.matcher(enterURL.getText().toString()).matches())
                            {
                                instURL = enterURL.getText().toString();
                                dialog.dismiss();
                            }else if (enterURL.getText().toString().equals("")){
                                instURL = "";
                                dialog.dismiss();
                            }else {
                                enterURL.setError("Add valid URL");
                            }
//                            instURL = enterURL.getText().toString();
//                            Toast.makeText(BussinessProfileActivity2.this, "" + instURL, Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        twitter_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(BussinessProfileActivity2.this);
                dialog.setContentView(R.layout.urlpopup);

                enterURL = dialog.findViewById(R.id.enter_url);
                Done = dialog.findViewById(R.id.done_pop_up_bt);
                dialog.show();

                if (Home_Screen.BprofileStatus.equals("1")) {
                   intent = getIntent();
                    if (intent != null) {
                        String value = intent.getStringExtra("value");
                        if (value.equals("2") || value.equals("back")) {
                            enterURL.setText(BusinessUserProfile.TwitteURL);

                            Done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (Patterns.WEB_URL.matcher(enterURL.getText().toString()).matches())
                                    {
                                        twitterUrl = enterURL.getText().toString();
                                        dialog.dismiss();
                                    }
                                    else  if (enterURL.getText().toString().isEmpty())
                                    {
                                        twitterUrl = "";
                                        dialog.dismiss();
                                    }
                                    else {
                                        enterURL.setError("Add valid URL");
                                    }
                                }
                            });
                        }
                    }
                } else {
                    enterURL.setText("");
                    enterURL.setSelection(enterURL.getText().length());
                    twitterUrl = enterURL.getText().toString();
                    Done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Patterns.WEB_URL.matcher(enterURL.getText().toString()).matches())
                            {
                                twitterUrl = enterURL.getText().toString();
                                dialog.dismiss();
                            }
                            else  if (enterURL.getText().toString().isEmpty())
                            {
                                twitterUrl = "";
                                dialog.dismiss();
                            }
                            else {
                                enterURL.setError("Add valid URL");
                            }

                          /*  twitterUrl = enterURL.getText().toString();
                           // Toast.makeText(BussinessProfileActivity2.this, "" + twitterUrl, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();*/
                        }
                    });
                }

            }
        });

        mPreviewbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                String favebok = facebookUrl;
                if (!Patterns.WEB_URL.matcher(enter_web_url.getText().toString()).matches() )
                {
                    enter_web_url.setError("Enter valid web URL");
                }else {
                    String webURL = enter_web_url.getText().toString();
                    Intent intent = new Intent(BussinessProfileActivity2.this,BussinessProfileActivity3.class);
                    intent.putExtra("facebook",favebok);
                    intent.putExtra("insta",instURL);
                    intent.putExtra("twitter",twitterUrl);
                    intent.putExtra("web",webURL);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                    sharedPreferences = getSharedPreferences("BusinessProfile2", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("weburl", webURL);
                    editor.putString("facebookurl", facebookUrl);
                    editor.putString("instagramurl", instURL);
                    editor.putString("twitterurl", twitterUrl);
                    editor.apply();
                }
            }
        });
    }

    private void init() {
        mPreviewbt = findViewById(R.id.previewBT_onbusinessprofile1);
        mBack = findViewById(R.id.businessprofile1_back_bt);
        facebook_button = findViewById(R.id.businessProfile1_facebookbt);
        insta_button = findViewById(R.id.businessProfile1_instabt);
        twitter_bt = findViewById(R.id.businessProfile1_twitterbt);
        enter_web_url = findViewById(R.id.add_webpage_edittext);
        BName = findViewById(R.id.business_profile1_user_Name);
        imageView = findViewById(R.id.business_profile1_user_Image);
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
