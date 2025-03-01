package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.SeeAll_activity;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.NormalGetProfile;

import java.util.ArrayList;

public class Booked_users extends RecyclerView.Adapter<Booked_users.ViewHolder> {

    Context context;
    ArrayList<String> usersImage= new ArrayList<>();
    ArrayList<String> user_id = new ArrayList<>();
    ArrayList<String> total_tickets = new ArrayList<>();
    String count;


    public Booked_users(Context context, ArrayList<String> usersImage,ArrayList<String> user_id,ArrayList<String> total_tickets) {
        this.context = context;
        this.usersImage = usersImage;
        this.user_id = user_id;
        this.total_tickets = total_tickets;
    }

    @NonNull
    @Override
    public Booked_users.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_booked_users,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Booked_users.ViewHolder viewHolder, final int i) {
        Glide.with(context).load(usersImage.get(i)).into(viewHolder.userImage);
//        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        int count = Integer.parseInt(total_tickets.get(i));
        int count1 = count-1;

        if (total_tickets.get(i).equals("1"))
        {
            viewHolder.attendeescount.setVisibility(View.GONE);
        }else {
            viewHolder.attendeescount.setText("+"+count1);
        }
        /*if (count1==0)
        {
            viewHolder.attendeescount.setVisibility(View.GONE);
        }else {
            viewHolder.attendeescount.setText("+"+count1);
        }*/

        viewHolder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NormalGetProfile.class);
                intent.putExtra("user_id", user_id.get(i));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        if (usersImage.size()>=5)
        {
            return 4;
        }else if (usersImage.size()==4)
        {
            return 4;
        }else if (usersImage.size()==3)
        {
            return 3;
        }else if (usersImage.size()==2)
        {
            return 2;
        }else {
            return 1;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView userImage;
        TextView attendeescount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.booked_usr_image);
            attendeescount = itemView.findViewById(R.id.attendeescount);
        }
    }
}
