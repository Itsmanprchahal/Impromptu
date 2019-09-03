package com.mandywebdesign.impromptu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandywebdesign.impromptu.R;

public class SliderAdapter extends PagerAdapter {


    public int[] slide_images = {
            R.drawable.screen1,
            R.drawable.screen2,
            R.drawable.screen3
    };
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    // Arrays


    public int[] logo_ = {
            R.drawable.logo,
            R.drawable.logo,
            R.drawable.logo
    };

    public int[] slide_descs = {
            R.string.skdetwo,
            R.string.slideone,
            R.string.slidethree

    };

    public int[] slide_text = {

            R.string.slideonetext,
            R.string.slidetwotext,
            R.string.slidethreetext
    };


    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (ConstraintLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);
        ImageView slide_image = view.findViewById(R.id.slide_image);
        TextView textView = view.findViewById(R.id.slide_text);
        TextView textView1 = view.findViewById(R.id.slide_text1);
        ImageView logo = view.findViewById(R.id.logo);


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
