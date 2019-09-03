package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Models.AddImagePojo;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.Normal_user_profile;
import com.mandywebdesign.impromptu.SettingFragmentsOptions.UserProfileFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.ViewHolder> {


    Context context;
    List<String> addImagePojos;


    public AddImageAdapter(Context context, List<String> addImagePojos) {
        this.context = context;
        this.addImagePojos = addImagePojos;
    }


    @NonNull
    @Override
    public AddImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.add_image, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddImageAdapter.ViewHolder viewHolder, final int i) {

        Glide.with(context).load(addImagePojos.get(i)).into(viewHolder.imageView);

        viewHolder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                addImagePojos.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, addImagePojos.size());

            }
        });

//        viewHolder.imageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//              Animation animation = AnimationUtils.loadAnimation(context, R.anim.shake_anim);
//              viewHolder.imageView.setAnimation(animation);
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return addImagePojos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imageView;
        ImageView deleteImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (RoundedImageView) itemView.findViewById(R.id.add_image);
            deleteImage = (ImageView) itemView.findViewById(R.id.delete_image);
        }
    }
}
