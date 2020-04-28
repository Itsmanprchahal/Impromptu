package com.mandywebdesign.impromptu.SettingFragmentsOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mandywebdesign.impromptu.Adapters.Accept_Refund;
import com.mandywebdesign.impromptu.Adapters.CancelRefund;
import com.mandywebdesign.impromptu.Adapters.RefundAdapter;
import com.mandywebdesign.impromptu.Interfaces.WebAPI;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RefundAPI;
import com.mandywebdesign.impromptu.Retrofit.RefundCancel;
import com.mandywebdesign.impromptu.Retrofit.RefundList;
import com.mandywebdesign.impromptu.Retrofit.RefundRequest;
import com.mandywebdesign.impromptu.Retrofit.RefundSend;
import com.mandywebdesign.impromptu.ui.BookEventActivity;
import com.mandywebdesign.impromptu.ui.NoInternetScreen;
import com.mandywebdesign.impromptu.ui.ProgressBarClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefundRequests extends AppCompatActivity {

    RecyclerView refund_recycler;
    ImageButton back;
    String socailtoken, BToken;
    SharedPreferences sharedPreferences;
    ArrayList<RefundList.Datum> refundDatalist = new ArrayList<>();
    Dialog progressDialog;
    TextView no_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_requests);
        progressDialog = ProgressBarClass.showProgressDialog(this);

        back = findViewById(R.id.back_refund);
        refund_recycler = findViewById(R.id.refund_recycler);
        no_request = findViewById(R.id.no_request);
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        socailtoken = sharedPreferences.getString("Socailtoken", "");
        BToken = sharedPreferences.getString("Usertoken", "");

        if (!socailtoken.equals("")) {
            getList(socailtoken);
        } else {
            getList(BToken);
        }

        listerners();

    }

    private void getList(String socailtoken) {
        progressDialog.show();
        Call<RefundList> refundListCall = WebAPI.getInstance().getApi().refundList("Bearer " + socailtoken,"pending");
        refundListCall.enqueue(new Callback<RefundList>() {
            @Override
            public void onResponse(Call<RefundList> call, Response<RefundList> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("200")) {
                        RefundList refundRequest = response.body();
                        List<RefundList.Datum> refundlist = refundRequest.getData();
                        refundDatalist.clear();

                        for (RefundList.Datum datum : refundlist) {
                            refundDatalist.add(datum);

                        }
                        if (refundDatalist.size() == 0) {
                            no_request.setVisibility(View.VISIBLE);
                        }
                        setData(refundDatalist, socailtoken);
                    }


                } else {
                    Intent intent = new Intent(RefundRequests.this, NoInternetScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RefundList> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RefundRequests.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(ArrayList<RefundList.Datum> refundDatalist, String socailtoken) {

         RefundAdapter refundAdapter = new RefundAdapter(RefundRequests.this, this.socailtoken, refundDatalist);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(RefundRequests.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        refund_recycler.setLayoutManager(layoutManager);
        refund_recycler.setNestedScrollingEnabled(false);
        refund_recycler.setAdapter(refundAdapter);
        refundAdapter.RefundAdapter(new CancelRefund() {
            @Override
            public void CancelRefundID(String id, String reason) {
                Cancel_Refund(socailtoken, id, reason);
            }
        });

        refundAdapter.RefundAdapter(new Accept_Refund() {
            @Override
            public void AcceptRefund(String id, String reason, String per) {
                    Accept_refund(socailtoken,id,reason,per);


            }
        });
    }

    private void Accept_refund(String socailtoken, String id, String reason, String per) {
        progressDialog.show();
        Call<RefundSend> accept_refundCall = WebAPI.getInstance().getApi().refundSend("Bearer "+socailtoken,id,per,reason);
        accept_refundCall.enqueue(new Callback<RefundSend>() {
            @Override
            public void onResponse(Call<RefundSend> call, Response<RefundSend> response) {
                if (response.isSuccessful())
                {
                 progressDialog.dismiss();

                     recreate();
                     Toast.makeText(RefundRequests.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(RefundRequests.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RefundSend> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RefundRequests.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Cancel_Refund(String socailtoken, String id, String reason) {
        progressDialog.show();
        Call<RefundCancel> refundCancelCall = WebAPI.getInstance().getApi().refundCancel("Bearer " + socailtoken, id, reason);
        refundCancelCall.enqueue(new Callback<RefundCancel>() {
            @Override
            public void onResponse(Call<RefundCancel> call, Response<RefundCancel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("200")) {
                        Toast.makeText(RefundRequests.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        recreate();
                    } else {
                        Toast.makeText(RefundRequests.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RefundCancel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RefundRequests.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listerners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
