package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.SavedCardsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailActivity extends AppCompatActivity {

    ImageButton back_on_paymentdetail_activity;

    EditText CardNumber,Card_ExpiryDate,pay_expiry_year,pay_card_name;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        preferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = preferences.getString("Socailtoken", "");
        back_on_paymentdetail_activity = findViewById(R.id.back_on_paymentdetail_activity);
        CardNumber  = findViewById(R.id.pay_card_number);
        Card_ExpiryDate = findViewById(R.id.pay_expiry_date);
        pay_expiry_year = findViewById(R.id.pay_expiry_year);
        pay_card_name = findViewById(R.id.pay_card_name);
        GetSavdCards(userToken);

        listerners();
    }

    private void listerners() {
        back_on_paymentdetail_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void GetSavdCards(String token)
    {
        Call<SavedCardsResponse> cardsResponseCall =  WebAPI.getInstance().getApi().savedCard("Bearer "+token);
        cardsResponseCall.enqueue(new Callback<SavedCardsResponse>() {
            @Override
            public void onResponse(Call<SavedCardsResponse> call, Response<SavedCardsResponse> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getStatus()==200)
                    {
                        if (response.body().getData().getCardNumber()!=null && response.body().getData().getCardEdate()!=null && response.body().getData().getCardSdate()!=null&& response.body().getData().getCard_holder_name()!=null)
                        {
                            CardNumber.setText(response.body().getData().getCardNumber().toString());
                            Card_ExpiryDate.setText(response.body().getData().getCardSdate());
                            pay_expiry_year.setText(response.body().getData().getCardEdate().toString());
                            pay_card_name.setText(response.body().getData().getCard_holder_name().toString());
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<SavedCardsResponse> call, Throwable t) {

            }
        });
    }
}
