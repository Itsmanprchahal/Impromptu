package com.mandywebdesign.impromptu.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RefundList;

import java.util.ArrayList;

public class RefundAdapter extends RecyclerView.Adapter<RefundAdapter.ViewHolder> {

    Context context;
    ArrayList<String> arrayList = new ArrayList<>();
    Dialog refund_dialog;
    TextView refund_dailog_ticket_type,refund_dailog_ticket_price,refund_dailog_total_price,refund_ticketCount_dailog_tv;
    Spinner refund_ticketype_spinner,refund_ticketCount_dailog_spinner,percentage_spinner;
    EditText refundreason;
    Button refund_dailog_button;
    String social_toke;
    ArrayList<RefundList.Datum> re = new ArrayList<>();
    String[] percentage = new String[]{"25","50","75","100"};
    ArrayAdapter<String> arrayAdapter;
    CancelRefund cancelRefund;
    Accept_Refund accept_refund;
    RelativeLayout ticketperlayout;
    String per;

    public void RefundAdapter(CancelRefund cancelRefund) {
        this.cancelRefund = cancelRefund;
    }

    public void RefundAdapter(Accept_Refund accept_refund) {
        this.accept_refund = accept_refund;
    }

    public RefundAdapter(Context context, String socailtoken, ArrayList<RefundList.Datum> refundDatalist) {
        this.context = context;
        this.social_toke = socailtoken;
        this.re = refundDatalist;
    }

    @NonNull
    @Override
    public RefundAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_refund,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RefundAdapter.ViewHolder holder, int position) {

        holder.refundevent_name.setText(re.get(position).getEventName());
        holder.see_all_user_name.setText(re.get(position).getUsername());
        holder.bookedticketnumber.setText(re.get(position).getNumberoftickets().toString());
        Glide.with(context).load(re.get(position).getAvatar()).into(holder.see_all_user_image);

        holder.refund_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptDialog(social_toke,position,"accept");
            }
        });

        holder.refund_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AcceptDialog(social_toke,position,"cancel");
            }
        });
    }

    private void AcceptDialog(String social_toke, int position,String s) {

        refund_dialog = new Dialog(context);
        Window window = refund_dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        refund_dialog.setContentView(R.layout.refund_dialog);
        refund_dialog.setCancelable(true);
        refund_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        refund_dialog.show();
        findIDs(refund_dialog);
        if (s.equals("cancel"))
        {
            ticketperlayout.setVisibility(View.GONE);
        }

        refund_dailog_ticket_type.setText(re.get(position).getTicketType());
        refund_dailog_ticket_price.setText("£ "+re.get(position).getTicketPrice());
        refund_dailog_total_price.setText("£ "+re.get(position).getTotalTicketAmount());
        int totalTicket = re.get(position).getNumberoftickets();
        refund_ticketCount_dailog_tv.setText(re.get(position).getNumberoftickets().toString());


        arrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, percentage);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        percentage_spinner.setAdapter(arrayAdapter);
        percentage_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
per = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        refund_dailog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (refundreason.getText().toString().equals(""))
                {
                    refundreason.setError("Enter Reason");
                }else {
                    if (s.equals("accept"))
                    {
                        accept_refund.AcceptRefund(String.valueOf(re.get(position).getRefundId()),refundreason.getText().toString(),per);
                        refund_dialog.dismiss();
                    }else if (s.equals("cancel"))
                    {
                        cancelRefund.CancelRefundID(String.valueOf(re.get(position).getRefundId()),refundreason.getText().toString());
                        refund_dialog.dismiss();
                    }

                }
            }
        });


    }

    private void findIDs(Dialog refund_dialog) {
        refund_ticketCount_dailog_tv = refund_dialog.findViewById(R.id.refund_ticketCount_dailog_tv);
        refund_dailog_ticket_type = refund_dialog.findViewById(R.id.refund_dailog_ticket_type);
        refund_ticketCount_dailog_spinner = refund_dialog.findViewById(R.id.refund_ticketCount_dailog_spinner);
        refund_dailog_ticket_price = refund_dialog.findViewById(R.id.refund_dailog_ticket_price);
        refund_dailog_total_price = refund_dialog.findViewById(R.id.refund_dailog_total_price);
        percentage_spinner = refund_dialog.findViewById(R.id.percentage_spinner);
        refundreason = refund_dialog.findViewById(R.id.refundreason);
        refund_dailog_button = refund_dialog.findViewById(R.id.refund_dailog_button);
        ticketperlayout = refund_dialog.findViewById(R.id.ticketperlayout);
    }

    @Override
    public int getItemCount() {
        return re.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView see_all_user_image;
        TextView refundevent_name,see_all_user_name,bookedticketnumber;
        Button refund_accept,refund_cancel;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            see_all_user_image = itemView.findViewById(R.id.refund_user_image);
            refundevent_name = itemView.findViewById(R.id.refundevent_name);
            see_all_user_name = itemView.findViewById(R.id.refund_user_name);
            bookedticketnumber = itemView.findViewById(R.id.refundticketnumber);
            refund_accept = itemView.findViewById(R.id.refund_accept);
            refund_cancel = itemView.findViewById(R.id.refund_cancel);

        }
    }
}
