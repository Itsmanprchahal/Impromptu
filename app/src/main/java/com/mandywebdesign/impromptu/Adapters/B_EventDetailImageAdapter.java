package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandywebdesign.impromptu.R;

import java.util.ArrayList;

public class B_EventDetailImageAdapter extends PagerAdapter {

    //ArrayList<String> arrayList = new ArrayList<>();
    String image;
    Context context;
    LayoutInflater inflater;
    ArrayList<String> images = new ArrayList<>();

    public B_EventDetailImageAdapter(Context context,ArrayList<String> images) {
        this.context = context;
        this.images = images;

    }

    @Override
    public int getCount() {
        return images.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((RelativeLayout) o);
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final RoundedImageView trailimg;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemview = inflater.inflate(R.layout.item, container, false);
        trailimg = itemview.findViewById(R.id.trailImage);

        Log.d("images",""+images.toString());


        Glide.with(context).load(images.get(position)).into(trailimg);

        ((ViewPager) container).addView(itemview);

        return itemview;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
