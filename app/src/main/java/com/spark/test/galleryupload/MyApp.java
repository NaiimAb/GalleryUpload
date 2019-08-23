package com.spark.test.galleryupload;

import android.app.Application;
import android.content.Context;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Naiim Ab. on 8/23/2019
 * Project: Gallery Upload
 */
public class MyApp extends Application {

    private Scheduler scheduler;

    public static MyApp getInstance(Context context) {
        return (MyApp) context.getApplicationContext();
    }

    public Scheduler subscribeScheduler() {
        if (scheduler == null) scheduler = Schedulers.io();
        return scheduler;
    }
}

