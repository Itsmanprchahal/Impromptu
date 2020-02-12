package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.SaveNewCard;
import com.mandywebdesign.impromptu.Retrofit.SavedCardsResponse;
import com.mandywebdesign.impromptu.Utils.Util;
import com.mandywebdesign.impromptu.ui.PayActivity;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailActivity extends AppCompatActivity implements View.OnClickListener {



    EditText CardNumber, Card_ExpiryDate, pay_expiry_year, pay_card_name, csv_number;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String userToken;
    Button save;
    Dialog dialog;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        dialog = ProgressBarClass.showProgressDialog(this);
        dialog.dismiss();
        preferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = preferences.getString("Socailtoken", "");
        CardNumber = findViewById(R.id.pay_card_number);
        Card_ExpiryDate = findViewById(R.id.pay_expiry_date);
        pay_expiry_year = findViewById(R.id.pay_expiry_year);
        pay_card_name = findViewById(R.id.pay_card_name);

        back=(ImageView)findViewById(R.id.back);
        save = findViewById(R.id.save_payment_bt);
        csv_number = findViewById(R.id.csv_number);
        GetSavdCards(userToken);
        back.setOnClickListener(this);
        listerners();
    }

    private void listerners() {



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(CardNumber.getText().toString()) && TextUtils.isEmpty(pay_card_name.getText().toString()) &&
                        TextUtils.isEmpty(Card_ExpiryDate.getText().toString()) && TextUtils.isEmpty(pay_expiry_year.getText().toString())
                        && TextUtils.isEmpty(csv_number.getText().toString())) {
                    CardNumber.setError("Enter Card Number");
                    pay_card_name.setError("Enter Card Holder Name");
                    Card_ExpiryDate.setError("Enter Expiry Month");
                    pay_expiry_year.setError("Enter Expiry Year");
                    csv_number.setError("Enter CSV Number");
                } else if (TextUtils.isEmpty(CardNumber.getText().toString())) {
                    CardNumber.setError("Enter Card Number");
                } else if (TextUtils.isEmpty(pay_card_name.getText().toString())) {
                    pay_card_name.setError("Enter Card Holder Name");
                } else if (TextUtils.isEmpty(Card_ExpiryDate.getText().toString())) {
                    Card_ExpiryDate.setError("Enter Expiry Month");
                } else if (TextUtils.isEmpty(pay_expiry_year.getText().toString())) {
                    pay_expiry_year.setError("Enter Expiry Year");
                } else if (TextUtils.isEmpty(csv_number.getText().toString())) {
                    csv_number.setError("Enter CSV Number");
                } else {
                    dialog.show();
                    onClickSomething(CardNumber.getText().toString(), Card_ExpiryDate.getText().toString(), pay_expiry_year.getText().toString(), csv_number.getText().toString(), pay_card_name.getText().toString());
                }
            }
        });
    }


    public void onClickSomething(final String cardNumber, final String cardExpMonth, final String cardExpYear, final String cardCVC, final String cardholdername) {
        Card card = new Card(
                cardNumber,
                Integer.valueOf(cardExpMonth),
                Integer.valueOf(cardExpYear),
                cardCVC
        );

        card.validateNumber();
        card.validateCVC();
        //pk_test_NUze3lWY5JhW6P0xmCebM00s000LALfIfF,,,,,,,,,,,,,pk_live_7M5piE9lr63i8sIn6Km4uzL700iuLHEESD

        Stripe stripe = new Stripe(PaymentDetailActivity.this, "pk_live_7M5piE9lr63i8sIn6Km4uzL700iuLHEESD");
        stripe.createToken(card, new TokenCallback() {
            @Override
            public void onError(@NonNull Exception error) {
                Log.d("card_token", "card " + error);
                Toast.makeText(PaymentDetailActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onSuccess(@NonNull final Token token) {

                Log.d("card_token1", "card " + token.toString());
                Log.d("token", "fff " + token.getId());

                String tokenPay = token.getId();
                if (!tokenPay.equals(""))
                {
                    setData("Bearer "+userToken,cardNumber,cardExpMonth,cardExpYear,cardCVC,cardholdername);
                }else {
                    dialog.dismiss();
                    CardNumber.setError("Enter Valid details");
                    pay_card_name.setError("Enter Valid details");
                    pay_expiry_year.setError("Enter Valid details");
                    Card_ExpiryDate.setError("Enter Valid details");
                    csv_number.setError("Enter Valid details");
                }


            }
        });

    }

    private void setData(String s, String cardNumber, String cardExpMonth, String cardExpYear, String cardCVC, String cardholdername) {
        Call<SaveNewCard> saveNewCardCall = WebAPI.getInstance().getApi().savenewcard(s,cardNumber,cardholdername,cardExpMonth,cardExpYear);
        saveNewCardCall.enqueue(new Callback<SaveNewCard>() {
            @Override
            public void onResponse(Call<SaveNewCard> call, Response<SaveNewCard> response) {
                dialog.dismiss();
                if (response.isSuccessful())
                {
                    if (response.body().getStatus().equals("200"))
                    {
                        Toast.makeText(PaymentDetailActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(PaymentDetailActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SaveNewCard> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public void GetSavdCards(String token) {
        Call<SavedCardsResponse> cardsResponseCall = WebAPI.getInstance().getApi().savedCard("Bearer " + token);
        cardsResponseCall.enqueue(new Callback<SavedCardsResponse>() {
            @Override
            public void onResponse(Call<SavedCardsResponse> call, Response<SavedCardsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        if (response.body().getData().getCardNumber() != null && response.body().getData().getCardEdate() != null && response.body().getData().getCardSdate() != null && response.body().getData().getCard_holder_name() != null) {
                            CardNumber.setText(response.body().getData().getCardNumber());
                            Card_ExpiryDate.setText(response.body().getData().getCardSdate());
                            pay_expiry_year.setText(response.body().getData().getCardEdate());
                            pay_card_name.setText(response.body().getData().getCard_holder_name());
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<SavedCardsResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                onBackPressed();
                break;
        }
    }
}
