package com.mandywebdesign.impromptu.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.Retrofit.RefundEventRequest;
import com.mandywebdesign.impromptu.Retrofit.RefundList;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.RefundRequests;

import java.util.ArrayList;

public class EventWiseRequestAdapter extends RecyclerView.Adapter<EventWiseRequestAdapter.ViewHolder> {

    Context context;
    ArrayList<RefundEventRequest.Datum> data = new ArrayList<>();
    Dialog dialog;
    StripeTransferIF stripeTransferIF;


    public void setStripeTransferIF(StripeTransferIF stripeTransferIF) {
        this.stripeTransferIF = stripeTransferIF;
    }

    public EventWiseRequestAdapter(Context context, ArrayList<RefundEventRequest.Datum> data1) {
        this.context = context;
        this.data = data1;
    }

    @NonNull
    @Override
    public EventWiseRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.request_custom,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventWiseRequestAdapter.ViewHolder holder, int position) {

        holder.outsatandingbalnce.setText("Pay outstanding balance: £ "+data.get(position).getOutstandingBalance());
        holder.outsatandingbalnce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(data.get(position).getEventId());
            }
        });

        holder.eventname.setText(data.get(position).getEventName());
        holder.refundrequests.setText("Refund Request: "+data.get(position).getRefundCount());
        Glide.with(context).load(data.get(position).getEvent_image()).into(holder.eventimage);
    }

    private void showDialog(Integer eventId) {
    dialog = new Dialog(context);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.deletedraft);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView textView = dialog.findViewById(R.id.deletedialogtext);
        Button yesdialog = dialog.findViewById(R.id.yesdialog);
        textView.setText("Do you want to redeem your outstanding balance into your stripe account");
        yesdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stripeTransferIF.stripeTransfer(String.valueOf(eventId));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView eventimage;
        TextView eventname,refundrequests;
        Button outsatandingbalnce;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventimage = itemView.findViewById(R.id.eventimage);
            eventname = itemView.findViewById(R.id.eventname);
            refundrequests = itemView.findViewById(R.id.refundrequests);
            outsatandingbalnce = itemView.findViewById(R.id.outsatandingbalnce);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, RefundRequests.class);
                    intent.putExtra("eventid",data.get(getAdapterPosition()).getEventId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
