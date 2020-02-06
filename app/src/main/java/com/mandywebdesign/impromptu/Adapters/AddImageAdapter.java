package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AddEvents.Add_Event_Activity;
import com.mandywebdesign.impromptu.R;

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
                Add_Event_Activity.part.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, addImagePojos.size());

            }
        });
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

            imageView = itemView.findViewById(R.id.add_image);
            deleteImage = itemView.findViewById(R.id.delete_image);
        }
    }
}
