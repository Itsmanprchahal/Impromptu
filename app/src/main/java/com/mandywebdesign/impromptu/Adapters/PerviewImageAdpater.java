package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.Home_Screen_Fragments.AddEvents.PerviewEventActivity;
import com.mandywebdesign.impromptu.R;

import java.util.List;

public class PerviewImageAdpater extends PagerAdapter {

    int[] image;
    List<String> addImagePojos;
    LayoutInflater inflater;
    Context context;

    public PerviewImageAdpater(PerviewEventActivity perviewEventActivity, List<String> addImagePojos) {
        this.context = perviewEventActivity;
        this.addImagePojos = addImagePojos;
    }


    @Override
    public int getCount() {
        return addImagePojos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        RoundedImageView trailimg;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemview = inflater.inflate(R.layout.item, container, false);
        trailimg = itemview.findViewById(R.id.trailImage);
        Glide.with(context).load(addImagePojos.get(position)).into(trailimg);

        container.addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }

}
