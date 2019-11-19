package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.CheckGuestActivity;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.SeeAll_activity;
import com.mandywebdesign.impromptu.Models.CheckInGuest;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.NormalGetProfile;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        final CheckInGuest.Datum datum = categoryArrayList.get(i);
        Glide.with(context).load(datum.getFile()).into(viewHolder.imageView);
        viewHolder.count.setVisibility(View.GONE);

//        viewHolder.name.setText(datum.getUsername());
        String userName = datum.getUsername();

        if (userName != null) {
            String[] name = userName.split(" ");
            if (name.length == 1) {
                String Fname = name[0];
                viewHolder.name.setText(Fname + " ");
            } else {
                String Fname = name[0];
                String Lname = name[1];
                viewHolder.name.setText(Fname + " " + Lname.subSequence(0, 1));
            }


        } else {
            viewHolder.name.setText(userName);
        }

        viewHolder.ticket.setText(datum.getTotal_tickets_by_user()+" tickets");
        if (datum.getCheckin().equals("1")) {
            viewHolder.custom_guest_check.setImageResource(R.drawable.ic_checked);
            viewHolder.custom_guest_check.setColorFilter(ContextCompat.getColor(context, R.color.colorTheme), android.graphics.PorterDuff.Mode.MULTIPLY);

        } else {
            viewHolder.custom_guest_check.setImageResource(R.drawable.ic_checked);
        }



        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NormalGetProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("user_id", CheckGuestActivity.userID.get(i));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        ImageView custom_guest_check;
        TextView name, ticket,count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.custom_guest_image);
            name = itemView.findViewById(R.id.custom_guest_name);
            ticket = itemView.findViewById(R.id.custom_guest_ticket);
            custom_guest_check = itemView.findViewById(R.id.custom_guest_check);
            count = itemView.findViewById(R.id.count);
        }
    }
}
