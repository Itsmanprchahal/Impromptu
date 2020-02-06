package com.mandywebdesign.impromptu.ui;

import android.app.Application;

import com.bumptech.glide.Glide;

/**
 * Created by yarolegovich on 08.03.2017.
 */

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DiscreteScrollViewOptions.init(this);
    }

    @Override
    public void onLowMemory() {
        System.gc();
        Glide.get(this).clearMemory();

        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        if (level == TRIM_MEMORY_UI_HIDDEN){
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
        super.onTrimMemory(level);
    }
}
