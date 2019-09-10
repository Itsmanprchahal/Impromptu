package com.mandywebdesign.impromptu.BusinessRegisterLogin;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.ui.Home_Screen;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessProfileFragment1 extends Fragment {

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_business_profile1, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        init();

        // enter_web_url.setText("https://");
        sharedPreferences = getActivity().getSharedPreferences("BusinessProfile1", Context.MODE_PRIVATE);
        Sname = sharedPreferences.getString("name", "");
        image = sharedPreferences.getString("image", image);
        BName.setText(Sname);
        facebookUrl = BusinessUserProfile.facebookURL;
        instURL = BusinessUserProfile.instaGramURL;
        twitterUrl = BusinessUserProfile.TwitteURL;

        if (image != null) {
            imageView.setImageBitmap(decodeBase64(image));
        }


        if (Home_Screen.BprofileStatus.equals("1")) {
            bundle1 = getArguments();
            if (bundle1 != null) {
                String value = bundle1.getString("value");
                if (value.equals("2") || value.equals("back")) {
                    enter_web_url.setText(BusinessUserProfile.webURL);
                }
            }
        } else {
            enter_web_url.setText("https://");
        }

        manager = getFragmentManager();


        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.urlpopup);

        enterURL = dialog.findViewById(R.id.enter_url);
        if (enterURL.equals(""))
        {
            Toast.makeText(getContext(), "Add all Social links", Toast.LENGTH_SHORT).show();
        }

        listeners();

        return view;
    }

    private void listeners() {

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String value = "back";
                bundle.putString("value", value);

                BusinessProfileFragment businessProfileFragment = new BusinessProfileFragment();
                businessProfileFragment.setArguments(bundle);


                manager.beginTransaction().replace(R.id.home_frame_layout, businessProfileFragment).addToBackStack(null).commit();

            }
        });



            facebook_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.urlpopup);

                    dialog.show();

                    enterURL = dialog.findViewById(R.id.enter_url);
                    Done = dialog.findViewById(R.id.done_pop_up_bt);

                    if (Home_Screen.BprofileStatus.equals("1")) {
                        bundle1 = getArguments();

                        if (bundle1 != null) {
                            String value = bundle1.getString("value");
                            if (value.equals("2") || value.equals("back")) {
                                enterURL.setText(BusinessUserProfile.facebookURL);

                                Done.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (Patterns.WEB_URL.matcher(enterURL.getText().toString()).matches())
                                        {
                                            facebookUrl = enterURL.getText().toString();
                                            Toast.makeText(getContext(), "" + facebookUrl, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }else {
                                            enterURL.setError("Add valid URL");
                                        }


                                    }
                                });
                            }
                        }
                    } else {
                        enterURL.setText("https://www.facebook.com/");
                        enterURL.setSelection(enterURL.getText().length());
                        facebookUrl = enterURL.getText().toString();
                        Done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                facebookUrl = enterURL.getText().toString();
                                dialog.dismiss();
                            }
                        });
                    }
                }
            });

        insta_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.urlpopup);

                enterURL = dialog.findViewById(R.id.enter_url);
                Done = dialog.findViewById(R.id.done_pop_up_bt);
                dialog.show();

                if (Home_Screen.BprofileStatus.equals("1")) {
                    bundle1 = getArguments();
                    if (bundle1 != null) {
                        String value = bundle1.getString("value");
                        if (value.equals("2") || value.equals("back")) {
                            enterURL.setText(BusinessUserProfile.instaGramURL);

                            Done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (Patterns.WEB_URL.matcher(enterURL.getText().toString()).matches())
                                    {
                                        instURL = enterURL.getText().toString();
                                        Toast.makeText(getContext(), "" + instURL, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }else {
                                        enterURL.setError("Add valid URL");
                                    }

                                }
                            });

                        }
                    }
                } else {
                    enterURL.setText("https://www.instagram.com/");
                    enterURL.setSelection(enterURL.getText().length());
                    instURL = enterURL.getText().toString();
                    Done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            instURL = enterURL.getText().toString();
                            Toast.makeText(getContext(), "" + instURL, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        twitter_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.urlpopup);

                enterURL = dialog.findViewById(R.id.enter_url);
                Done = dialog.findViewById(R.id.done_pop_up_bt);
                dialog.show();

                if (Home_Screen.BprofileStatus.equals("1")) {
                    bundle1 = getArguments();
                    if (bundle1 != null) {
                        String value = bundle1.getString("value");
                        if (value.equals("2") || value.equals("back")) {
                            enterURL.setText(BusinessUserProfile.TwitteURL);

                            Done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (Patterns.WEB_URL.matcher(enterURL.getText().toString()).matches())
                                    {
                                        twitterUrl = enterURL.getText().toString();
                                        Toast.makeText(getContext(), "" + twitterUrl, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }else {
                                        enterURL.setError("Add valid URL");
                                    }
                                }
                            });
                        }
                    }
                } else {
                    enterURL.setText("https://www.twitter.com/");
                    enterURL.setSelection(enterURL.getText().length());
                    twitterUrl = enterURL.getText().toString();
                    Done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            twitterUrl = enterURL.getText().toString();
                            Toast.makeText(getContext(), "" + twitterUrl, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
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
                String webURL = enter_web_url.getText().toString();
                if (webURL.isEmpty())
                {
                    enter_web_url.setError("Enter Web URL");
                }else {
                    bundle.putString("facebook", favebok);
                    bundle.putString("insta", instURL);
                    bundle.putString("twitter", twitterUrl);
                    bundle.putString("web", webURL);

                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.home_frame_layout,new Business_PublishProfileFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();

//                    manager.beginTransaction().replace(R.id.home_frame_layout, new Business_PublishProfileFragment()).addToBackStack(null).commit();
                    sharedPreferences = getActivity().getSharedPreferences("BusinessProfile2", Context.MODE_PRIVATE);
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
        Home_Screen.bottomNavigationView.setVisibility(View.VISIBLE);
        mPreviewbt = view.findViewById(R.id.previewBT_onbusinessprofile1);
        mBack = view.findViewById(R.id.businessprofile1_back_bt);
        facebook_button = view.findViewById(R.id.businessProfile1_facebookbt);
        insta_button = view.findViewById(R.id.businessProfile1_instabt);
        twitter_bt = view.findViewById(R.id.businessProfile1_twitterbt);
        enter_web_url = (EditText) view.findViewById(R.id.add_webpage_edittext);
        BName = (TextView) view.findViewById(R.id.business_profile1_user_Name);
        imageView = (RoundedImageView) view.findViewById(R.id.business_profile1_user_Image);
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }


}
