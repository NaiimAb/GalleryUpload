package com.spark.test.galleryupload.data;

import com.spark.test.galleryupload.model.GalleryItem;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Naiim Ab. on 8/23/2019
 * Project: Gallery Upload
 */
public interface GalleryDataService  {

    // Add Server parameters to fetch data
    @GET("")
    Observable<List<GalleryItem>> fetchGallery();
}
