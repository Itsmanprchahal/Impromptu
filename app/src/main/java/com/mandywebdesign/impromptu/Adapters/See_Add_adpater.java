package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.BusinessUserPRofileActivity;
import com.mandywebdesign.impromptu.BusinessRegisterLogin.SeeAll_activity;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.NormalGetProfile;
import com.mandywebdesign.impromptu.ui.BookEventActivity;

import java.util.ArrayList;

public class See_Add_adpater extends RecyclerView.Adapter<See_Add_adpater.ViewHolder> {

    Context context;
    FragmentManager manager;
    ArrayList<String> userIamge = new ArrayList<>();
    ArrayList<String> userName;
    ArrayList<String> totalticketbuy;
    String isFrom;

    public See_Add_adpater(Context context, FragmentManager manager, ArrayList<String> userIamge, ArrayList<String> userName, ArrayList<String> totalticketbuy, String isFrom) {
        this.context = context;
        this.manager = manager;
        this.userIamge = userIamge;
        this.userName = userName;
        this.totalticketbuy = totalticketbuy;
        this.isFrom = isFrom;
    }

    @NonNull
    @Override
    public See_Add_adpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.custom_see_all_users, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull See_Add_adpater.ViewHolder viewHolder, final int i) {

        Glide.with(context).load(userIamge.get(i)).into(viewHolder.imageView);
        viewHolder.userName.setText(userName.get(i));
        viewHolder.bookedtickets.setText("Tickets: " + totalticketbuy.get(i));

       /* if (isFrom.equals("OWNEVENT"))
        {
            viewHolder.refund_bt_layout.setVisibility(View.VISIBLE);
        }*/

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NormalGetProfile.class);
                intent.putExtra("user_id", SeeAll_activity.userID.get(i));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return userIamge.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imageView;
        TextView userName, bookedtickets;
        LinearLayout refund_bt_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.see_all_user_image);
            userName = itemView.findViewById(R.id.see_all_user_name);
            bookedtickets = itemView.findViewById(R.id.bookedticketnumber);
            refund_bt_layout = itemView.findViewById(R.id.refund_bt_layout);
        }
    }
}
