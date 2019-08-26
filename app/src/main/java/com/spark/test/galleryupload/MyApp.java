package com.spark.test.galleryupload;

import android.app.Application;
import android.content.Context;

import com.spark.test.galleryupload.data.GalleryDataFactory;
import com.spark.test.galleryupload.data.GalleryDataService;
import com.spark.test.galleryupload.data.ImageUploaderFactory;
import com.spark.test.galleryupload.data.ImageUploaderService;

import rx.Scheduler;
import rx.schedulers.Schedulers;


/**
 * Created by Naiim Ab. on 8/23/2019
 * Project: Gallery Upload
 */
public class MyApp extends Application {

    private Scheduler scheduler;
    private GalleryDataService galleryDataService;
    private ImageUploaderService imageUploaderService;

    public static MyApp getInstance(Context context) {
        return (MyApp) context.getApplicationContext();
    }

    public GalleryDataService getGalleryService() {
        if (galleryDataService == null) galleryDataService = GalleryDataFactory.retrieveData();
        return galleryDataService;
    }

    public ImageUploaderService getImageUploaderService() {
        if (imageUploaderService == null) imageUploaderService = ImageUploaderFactory.imageUploader();
        return imageUploaderService;
    }

    public Scheduler subscribeScheduler() {
        if (scheduler == null) scheduler = Schedulers.io();
        return scheduler;
    }
}

