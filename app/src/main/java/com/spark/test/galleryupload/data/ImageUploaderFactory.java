package com.spark.test.galleryupload.data;

import com.spark.test.galleryupload.utils.Common;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Naiim Ab. on 8/24/2019
 * Project: Gallery Upload
 */
public class ImageUploaderFactory  {

    public static ImageUploaderService imageUploader() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Common.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(ImageUploaderService.class);
    }
}
