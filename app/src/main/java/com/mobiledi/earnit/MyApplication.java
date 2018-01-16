package com.mobiledi.earnit;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

/**
 * Created by mobile-di on 4/10/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Iconify
                .with(new FontAwesomeModule());
    }
}
