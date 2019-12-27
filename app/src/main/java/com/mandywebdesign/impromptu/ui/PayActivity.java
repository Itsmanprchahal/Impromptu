package com.mandywebdesign.impromptu.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEventDetailAcitvity;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.NormalPayment;
import com.mandywebdesign.impromptu.Retrofit.Rating;
import com.mandywebdesign.impromptu.Retrofit.SavedCardsResponse;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayActivity extends AppCompatActivity {

    CheckBox savethiscard;
    View view;
    Button pay;
    RatingBar ratingBar;
    Button dialogratingshare_button;
    EditText feedback;
    ImageView close;
    TextView total_price, ticket_price, tickt_num,event_Titletv;
    EditText CardNumber, CardName, Card_ExpiryDate,pay_expiry_year ,Card_CSV;
    FragmentManager fragmentManager;
    DatePickerDialog.OnDateSetListener dateSetListener;
    Calendar calendar;
    Dialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String userToken;
    String tokenPay, eventId, tot;
    static String total_tickets, ticketprice, totalprice, paid, eventID,event_Title,tickettype,imagerecieve;
    Intent intent;
    boolean saveStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        fragmentManager = getSupportFragmentManager();

        preferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        userToken = preferences.getString("Socailtoken", "");
        editor = preferences.edit();

        progressDialog = ProgressBarClass.showProgressDialog(this);
        progressDialog.dismiss();
        Drawable drawable = new ProgressBar(PayActivity.this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(PayActivity.this, R.color.colorTheme),
                PorterDuff.Mode.SRC_IN);


        intent = getIntent();

        Toolbar toolbar = findViewById(R.id.pay_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        calendar = Calendar.getInstance();

        init();
        listeners();

        totalprice = intent.getStringExtra("total_price");

        total_price.setText("£ " + intent.getStringExtra("total_price"));
        ticketprice = intent.getStringExtra("ticket_Price");
        total_tickets = intent.getStringExtra("total_tickets");
        event_Title = intent.getStringExtra("event_Title");
        tickettype = intent.getStringExtra("tickettype");
        imagerecieve = intent.getStringExtra("imagesend");


        ticket_price.setText("£ " + ticketprice);
        tickt_num.setText(total_tickets);
        event_Titletv.setText(event_Title);


    }

    private void listeners() {

        GetSavdCards(userToken);
        savethiscard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    saveStatus = true;
                }else {
                    saveStatus = false;
                }

            }
        });


        Card_ExpiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count==2)
                {
                    int month = Integer.parseInt(Card_ExpiryDate.getText().toString());
                    if (month>12)
                    {
                        Card_ExpiryDate.setError("Invalid Month");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                String format = "MM/yy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
                Card_ExpiryDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();

            }
        });


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardnuber = CardNumber.getText().toString();
                String Cardname = CardName.getText().toString();
                String E_Date = Card_ExpiryDate.getText().toString();
                String E_YEar = pay_expiry_year.getText().toString();
                String csv = Card_CSV.getText().toString();

                if (cardnuber.isEmpty() && Cardname.isEmpty() && E_Date.isEmpty() && E_YEar.isEmpty() && csv.isEmpty()) {
                    CardNumber.setError("Enter Card Number");
                    CardName.setError("Enter Card Name");
                    Card_ExpiryDate.setError("Enter Card Expiry Date");
                    pay_expiry_year.setError("Enter Card Expiry Year");
                    Card_CSV.setError("Enter Card CSV sumber");
                } else if (cardnuber.isEmpty()) {
                    CardNumber.setError("Enter Card Number");
                } else if (Cardname.isEmpty()) {
                    CardName.setError("Enter Card Name");
                } else if (E_Date.isEmpty()) {
                    Card_ExpiryDate.setError("Enter Card Expiry Date");
                } else if (E_YEar.isEmpty()) {
                    pay_expiry_year.setError("Enter Card Expiry Year");
                } else if (csv.isEmpty()) {
                    Card_CSV.setError("Enter Card CSV number");
                }else if (CardNumber.length()<16)
                {
                    CardNumber.setError("Enter Valid Card");
                }else {
                    progressDialog.show();
                    String exp_month = Card_ExpiryDate.getText().toString();
                    String exp_year = pay_expiry_year.getText().toString();
                    String cardholdername = CardName.getText().toString();
                    eventId = intent.getStringExtra("event_id");
                    onClickSomething(cardnuber, exp_month, exp_year, csv,cardholdername);
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

        Stripe stripe = new Stripe(PayActivity.this, "pk_test_NUze3lWY5JhW6P0xmCebM00s000LALfIfF");
        stripe.createToken(card, new TokenCallback() {
            @Override
            public void onError(@NonNull Exception error) {
                Log.d("card_token", "card " + error);
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(@NonNull final Token token) {
                Log.d("card_token1", "card " + token.toString());
                Log.d("token", "fff " + token.getId());

                tokenPay = token.getId();

                SetData(userToken, eventId, totalprice, tokenPay, total_tickets,tickettype,cardNumber,cardExpMonth,cardExpYear,cardholdername,saveStatus);
            }
        });

    }


    public void SetData(final String userToken, final String eventID, String totalprice, String tokenPay, String total_tickets,String tickettype,String card_number,String month,String year,String cardholdername,boolean savecard) {

        Call<NormalPayment> call = WebAPI.getInstance().getApi().normalPayment("Bearer " + userToken, eventID, totalprice, tokenPay, total_tickets,tickettype,card_number,month,year,cardholdername,savecard);
        call.enqueue(new Callback<NormalPayment>() {
            @Override
            public void onResponse(Call<NormalPayment> call, Response<NormalPayment> response) {

                progressDialog.dismiss();
                if (response.body()!=null) {
//                    dialog();
                    Log.d("data+++", String.valueOf(response.body().getData().get(0)));
                    Intent intent = new Intent(PayActivity.this,ConfirmationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("eventID",eventID);
                    intent.putExtra("paid","Paid");

                    if (imagerecieve.equals("BUA"))
                    {
                        editor.putString("eventImage", BusinessEventDetailAcitvity.image.get(0));
                    }else {
                        editor.putString("eventImage", BookEventActivity.image.get(0));
                    }
                    editor.apply();
                    startActivity(intent);
                    finish();

                } else {
                    Intent intent = new Intent(PayActivity.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<NormalPayment> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PayActivity.this, "Book Error=> " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {
        savethiscard = findViewById(R.id.savethiscard);
        pay = (Button) findViewById(R.id.pay_button);
        CardNumber = (EditText) findViewById(R.id.pay_card_number);
        CardName = (EditText) findViewById(R.id.pay_card_name);
        Card_ExpiryDate = (EditText) findViewById(R.id.pay_expiry_date);
        Card_CSV = (EditText) findViewById(R.id.pay_csv);
        total_price = (TextView) findViewById(R.id.pay_total_price);
        close = (ImageView) findViewById(R.id.pay_close);
        ticket_price = (TextView) findViewById(R.id.pay_ticket_price);
        tickt_num = (TextView) findViewById(R.id.pay_ticket_type);
        event_Titletv = (TextView)findViewById(R.id.event_Title);
        pay_expiry_year = findViewById(R.id.pay_expiry_year);
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
                           savethiscard.setChecked(true);
                        CardName.setText(response.body().getData().getCard_holder_name());
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<SavedCardsResponse> call, Throwable t) {

            }
        });
    }

    public void dialog() {
        final Dialog dialog = new Dialog(PayActivity.this);
        dialog.setContentView(R.layout.custom_rating_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ratingBar = dialog.findViewById(R.id.rating_bar);
        feedback = dialog.findViewById(R.id.feedback);
        dialogratingshare_button = dialog.findViewById(R.id.dialogratingshare_button);
        dialog.show();

        dialogratingshare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rating = String.valueOf(ratingBar.getRating());
                String feedbck = feedback.getText().toString();
                if (rating.equals("") | feedbck.equals("")) {
                    Toast.makeText(PayActivity.this, "Add Rating  and reviews", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    Call<Rating> call = WebAPI.getInstance().getApi().rating("Bearer " + userToken, eventId, rating, feedbck);
                    call.enqueue(new Callback<Rating>() {
                        @Override
                        public void onResponse(Call<Rating> call, Response<Rating> response) {
                            if (response.body()!=null)
                            {
                                dialog.dismiss();
                                progressDialog.dismiss();

                                Intent intent = new Intent(PayActivity.this,ConfirmationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("eventID",eventId);
                                intent.putExtra("paid","Paid");
                                editor.putString("eventImage", BookEventActivity.image.get(0));
                                editor.apply();

                            }else {
                                dialog.dismiss();
                                Intent intent = new Intent(PayActivity.this, NoInternetScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Rating> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(PayActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
    }
}
