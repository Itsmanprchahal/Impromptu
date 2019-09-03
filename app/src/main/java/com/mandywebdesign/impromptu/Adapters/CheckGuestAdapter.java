package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Models.CheckInGuest;
import com.mandywebdesign.impromptu.R;

import java.util.ArrayList;

public class CheckGuestAdapter extends RecyclerView.Adapter<CheckGuestAdapter.ViewHolder> {


    Context context;
    ArrayList<CheckInGuest.Datum> categoryArrayList;

    public CheckGuestAdapter(Context context, ArrayList<CheckInGuest.Datum> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_guest_check, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        CheckInGuest.Datum datum = categoryArrayList.get(i);


        Glide.with(context).load(datum.getFile()).into(viewHolder.imageView);


        viewHolder.name.setText(datum.getUsername());
        viewHolder.ticket.setText(datum.getTicketType());
        if (datum.getCheckin().equals("1")) {
            viewHolder.custom_guest_check.setImageResource(R.drawable.ic_checked);
            viewHolder.custom_guest_check.setColorFilter(ContextCompat.getColor(context, R.color.colorTheme), android.graphics.PorterDuff.Mode.MULTIPLY);

        } else {
            viewHolder.custom_guest_check.setImageResource(R.drawable.ic_checked);
        }

    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        ImageView custom_guest_check;
        TextView name, ticket;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.custom_guest_image);
            name = itemView.findViewById(R.id.custom_guest_name);
            ticket = itemView.findViewById(R.id.custom_guest_ticket);
            custom_guest_check = itemView.findViewById(R.id.custom_guest_check);
        }
    }
}
