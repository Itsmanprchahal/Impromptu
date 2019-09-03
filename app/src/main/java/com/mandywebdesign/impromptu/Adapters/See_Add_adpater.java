package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessEvent_detailsFragment;
import com.mandywebdesign.impromptu.R;

import java.util.ArrayList;

public class See_Add_adpater extends RecyclerView.Adapter<See_Add_adpater.ViewHolder> {

    Context context;
    FragmentManager manager;
    ArrayList<String> userIamge = new ArrayList<>();
    ArrayList<String> userName ;
    ArrayList<String> totalticketbuy;

    public See_Add_adpater(Context context, FragmentManager manager,ArrayList<String> userIamge,ArrayList<String> userName,ArrayList<String> totalticketbuy) {
        this.context = context;
        this.manager = manager;
        this.userIamge= userIamge;
        this.userName = userName;
        this.totalticketbuy = totalticketbuy;
    }

    @NonNull
    @Override
    public See_Add_adpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater =LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.custom_see_all_users,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull See_Add_adpater.ViewHolder viewHolder, int i) {

        Glide.with(context).load(userIamge.get(i)).into(viewHolder.imageView);
        viewHolder.userName.setText(userName.get(i));
        viewHolder.bookedtickets.setText(totalticketbuy.get(i));

    }

    @Override
    public int getItemCount() {
        return userIamge.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imageView;
        TextView userName,bookedtickets;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.see_all_user_image);
            userName = itemView.findViewById(R.id.see_all_user_name);
            bookedtickets = itemView.findViewById(R.id.bookedticketnumber);
        }
    }
}
