package com.crossover.crosschat.android;

import com.arellomobile.mvp.RegisterMoxyReflectorPackages;
import com.crossover.crosschat.android.core.CoreApplication;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 2/8/18.
 */
@RegisterMoxyReflectorPackages("com.crossover.crosschat.android.core.moxy")
public class CrossChatApp extends CoreApplication {

    public static final String TAG = CrossChatApp.class.getSimpleName();

    private static CrossChatApp instance;

    public CrossChatApp() {
        instance = this;
    }

    public static CrossChatApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
