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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.R;

import java.util.ArrayList;

public class RefundAdapter extends RecyclerView.Adapter<RefundAdapter.ViewHolder> {

    Context context;
    ArrayList<String> arrayList = new ArrayList<>();
    Dialog refund_dialog;
    TextView refund_dailog_ticket_type,refund_dailog_ticket_price,refund_dailog_total_price;
    Spinner refund_ticketype_spinner,refund_ticketCount_dailog_spinner,percentage_spinner;
    EditText refundreason;
    Button refund_dailog_button;
    String social_toke;

    public RefundAdapter(Context context, String socailtoken) {
        this.context = context;
        this.social_toke = socailtoken;
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


        holder.refund_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptDialog(social_toke);
            }
        });
    }

    private void AcceptDialog(String social_toke) {

        refund_dialog = new Dialog(context);
        Window window = refund_dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        refund_dialog.setContentView(R.layout.refund_dialog);
        refund_dialog.setCancelable(true);
        refund_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        refund_dialog.show();

        findIDs(refund_dialog);
        if (!social_toke.equals(""))
        {
            refund_dailog_ticket_type.setVisibility(View.VISIBLE);
            refund_ticketype_spinner.setVisibility(View.GONE);
        }
    }

    private void findIDs(Dialog refund_dialog) {
        refund_dailog_ticket_type = refund_dialog.findViewById(R.id.refund_dailog_ticket_type);
        refund_ticketype_spinner = refund_dialog.findViewById(R.id.refund_ticketype_spinner);
        refund_ticketCount_dailog_spinner = refund_dialog.findViewById(R.id.refund_ticketCount_dailog_spinner);
        refund_dailog_ticket_price = refund_dialog.findViewById(R.id.refund_dailog_ticket_price);
        refund_dailog_total_price = refund_dialog.findViewById(R.id.refund_dailog_total_price);
        percentage_spinner = refund_dialog.findViewById(R.id.percentage_spinner);
        refundreason = refund_dialog.findViewById(R.id.refundreason);
        refund_dailog_button = refund_dialog.findViewById(R.id.refund_dailog_button);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView see_all_user_image;
        TextView refundevent_name,see_all_user_name,bookedticketnumber;
        Button refund_accept,refund_cancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            see_all_user_image = itemView.findViewById(R.id.see_all_user_image);
            refundevent_name = itemView.findViewById(R.id.refundevent_name);
            see_all_user_name = itemView.findViewById(R.id.see_all_user_name);
            bookedticketnumber = itemView.findViewById(R.id.bookedticketnumber);
            refund_accept = itemView.findViewById(R.id.refund_accept);
            refund_cancel = itemView.findViewById(R.id.refund_cancel);

        }
    }
}
