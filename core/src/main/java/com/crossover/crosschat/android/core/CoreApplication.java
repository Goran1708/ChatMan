package com.crossover.crosschat.android.core;

import android.app.Application;

public class CoreApplication extends Application {

    private static CoreApplication instance;

    public CoreApplication() {
        instance = this;
    }

    public static CoreApplication getInstance() {
        return instance;
    }

}
