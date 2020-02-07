package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandywebdesign.impromptu.Home_Screen_Fragments.Setting;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RetroTerms;
import com.mandywebdesign.impromptu.ui.Join_us;
import com.mandywebdesign.impromptu.ui.NoInternet;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsAndConditionsActivityy extends AppCompatActivity {

    TextView terms;
    View view;
    SharedPreferences sharedPreferences;
    String user, social_token;
    Dialog progressDialog;
    ImageView back;
    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    TextView text6;
    TextView text7;
    TextView text8;
    TextView text9;
    TextView text10;
    TextView text11;
    TextView text12;
    TextView text13;
    TextView text14;
    TextView text15;
    TextView text16;
    TextView text17;
    TextView text18;
    TextView text19;
    TextView text20;
    TextView text21;
    TextView text22;
    TextView text23;
    TextView text24;
    TextView text25;
    TextView text26;
    TextView text27;
    TextView text28;
    TextView text29;
    TextView text30;
    TextView text31;
    TextView text32;
    TextView text33;
    TextView text34;
    TextView text35;
    TextView text36;
    TextView text37;
    TextView text38;
    TextView text39;
    TextView text40;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.gc();
        setContentView(R.layout.activity_terms_and_conditions_activityy);
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        user =  sharedPreferences.getString("Usertoken", "");
        social_token = sharedPreferences.getString("Socailtoken", "");

        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();

        init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            text1.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text2.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text3.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text4.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text5.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text6.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text7.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text8.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text9.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text10.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text11.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text12.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text13.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text14.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text15.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text16.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text17.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text18.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text19.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text20.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text21.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text22.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text23.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text24.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text25.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text26.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text27.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text28.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text29.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text30.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text31.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text32.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text33.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text34.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text35.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text36.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text37.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text38.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text39.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            text40.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
        listeners();


        if (!user.equalsIgnoreCase("")) {
            //B_tandC(user);
        } else if (!social_token.equalsIgnoreCase("")){
            //B_tandC(social_token);
        }


    }

    private void B_tandC(String user) {
        progressDialog.show();
        Call<RetroTerms> call = WebAPI.getInstance().getApi().terms("Bearer "+user, "application/json");
        call.enqueue(new Callback<RetroTerms>() {
            @Override
            public void onResponse(Call<RetroTerms> call, Response<RetroTerms> response) {

                if (response.body() != null)
                {
                    if (response.body().getStatus().equals("200")) {
                        String term = response.body().getData().get(0).getContent();
//                 Toast.makeText(getContext(), ""+term, Toast.LENGTH_SHORT).show();
                        terms.setText(Html.fromHtml(term));
                        progressDialog.dismiss();
                    } else if (response.body().getStatus().equals("401")) {
                        //Toast.makeText(getContext(), ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(TermsAndConditionsActivityy.this, Join_us.class);
                        startActivity(intent);
                        finish();

                    }
                }else {
                    Intent intent = new Intent(TermsAndConditionsActivityy.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<RetroTerms> call, Throwable t) {

                    progressDialog.dismiss();

                    NoInternet.dialog(TermsAndConditionsActivityy.this);

            }
        });
    }

    private void listeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });
    }

    private void init() {
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);
        text7 = findViewById(R.id.text7);
        text8 = findViewById(R.id.text8);
        text9 = findViewById(R.id.text9);
        text10 = findViewById(R.id.text10);
        text11= findViewById(R.id.text11);
        text12 = findViewById(R.id.text12);
        text13 = findViewById(R.id.text13);
        text14 = findViewById(R.id.text14);
        text15 = findViewById(R.id.text15);
        text16 = findViewById(R.id.text16);
        text17 = findViewById(R.id.text17);
        text18 = findViewById(R.id.text18);
        text19 = findViewById(R.id.text19);
        text20 = findViewById(R.id.text20);
        text21 = findViewById(R.id.text21);
        text22 = findViewById(R.id.text22);
        text23 = findViewById(R.id.text23);
        text24 = findViewById(R.id.text24);
        text25 = findViewById(R.id.text25);
        text26 = findViewById(R.id.text26);
        text27 = findViewById(R.id.text27);
        text28 = findViewById(R.id.text28);
        text29 = findViewById(R.id.text29);
        text30 = findViewById(R.id.text30);
        text31 = findViewById(R.id.text31);
        text32 = findViewById(R.id.text32);
        text33 = findViewById(R.id.text33);
        text34 = findViewById(R.id.text34);
        text35 = findViewById(R.id.text35);
        text36 = findViewById(R.id.text36);
        text37 = findViewById(R.id.text37);
        text38 = findViewById(R.id.text38);
        text39 = findViewById(R.id.text39);
        text40 = findViewById(R.id.text40);
        terms = findViewById(R.id.terms_text);
        back = findViewById(R.id.back_on_Teramandcondition);
    }

    public static void justify(final TextView textView) {

        final AtomicBoolean isJustify = new AtomicBoolean(false);

        final String textString = textView.getText().toString();

        final TextPaint textPaint = textView.getPaint();

        final SpannableStringBuilder builder = new SpannableStringBuilder();

        textView.post(new Runnable() {
            @Override
            public void run() {

                if (!isJustify.get()) {

                    final int lineCount = textView.getLineCount();
                    final int textViewWidth = textView.getWidth();

                    for (int i = 0; i < lineCount; i++) {

                        int lineStart = textView.getLayout().getLineStart(i);
                        int lineEnd = textView.getLayout().getLineEnd(i);

                        String lineString = textString.substring(lineStart, lineEnd);

                        if (i == lineCount - 1) {
                            builder.append(new SpannableString(lineString));
                            break;
                        }

                        String trimSpaceText = lineString.trim();
                        String removeSpaceText = lineString.replaceAll(" ", "");

                        float removeSpaceWidth = textPaint.measureText(removeSpaceText);
                        float spaceCount = trimSpaceText.length() - removeSpaceText.length();

                        float eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount;

                        SpannableString spannableString = new SpannableString(lineString);
                        for (int j = 0; j < trimSpaceText.length(); j++) {
                            char c = trimSpaceText.charAt(j);
                            if (c == ' ') {
                                Drawable drawable = new ColorDrawable(0x00ffffff);
                                drawable.setBounds(0, 0, (int) eachSpaceWidth, 0);
                                ImageSpan span = new ImageSpan(drawable);
                                spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }

                        builder.append(spannableString);
                    }

                    textView.setText(builder);
                    isJustify.set(true);
                }
            }
        });
    }
}
