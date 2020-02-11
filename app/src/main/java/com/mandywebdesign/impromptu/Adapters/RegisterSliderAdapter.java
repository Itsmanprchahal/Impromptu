package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandywebdesign.impromptu.R;

public class RegisterSliderAdapter extends PagerAdapter {
    // Arrays
    public int[] slide_text = {
            R.string.bussDesc1,
            R.string.bussDesc2
    };

    Context context;
    LayoutInflater layoutInflater;
    public int[] slide_images = {
            R.drawable.busineesimage,
            R.drawable.busineesimage
    };

    public int[] logo_ = {
            R.drawable.logo,
            R.drawable.logo
    };

    public int[] slide_descs = {
            R.string.businesstext1,
            R.string.businesstext2
    };


    public RegisterSliderAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return slide_text.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.registerslide, container, false);
        ImageView slide_image = view.findViewById(R.id.business_slide_image);
        TextView textView = view.findViewById(R.id.businessslide_text);
        TextView textView1 = view.findViewById(R.id.bussinessslide_text1);
        ImageView logo = view.findViewById(R.id.businesslogo);

        slide_image.setImageResource(slide_images[position]);
        textView.setText(slide_descs[position]);
        textView1.setText(slide_text[position]);
        logo.setImageResource(logo_[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);


    }
}