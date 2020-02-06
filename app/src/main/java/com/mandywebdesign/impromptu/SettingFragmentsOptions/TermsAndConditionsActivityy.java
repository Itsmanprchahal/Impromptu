package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
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
