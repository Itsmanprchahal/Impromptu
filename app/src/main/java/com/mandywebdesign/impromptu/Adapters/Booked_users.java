package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEvent_detailsFragment;
import com.mandywebdesign.impromptu.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Booked_users extends RecyclerView.Adapter<Booked_users.ViewHolder> {

    Context context;
    ArrayList<String> usersImage= new ArrayList<>();
    String count;


    public Booked_users(Context context, ArrayList<String> usersImage) {
        this.context = context;
        this.usersImage = usersImage;
    }

    @NonNull
    @Override
    public Booked_users.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.custom_booked_users,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Booked_users.ViewHolder viewHolder, int i) {
        Glide.with(context).load(usersImage.get(i)).into(viewHolder.userImage);
        viewHolder.attendeescount.setText("+"+usersImage.size());
    }

    @Override
    public int getItemCount() {
        return 1;
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