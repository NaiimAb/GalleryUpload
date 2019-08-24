package com.spark.test.galleryupload.viewmodel;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.spark.test.galleryupload.data.GalleryDataFactory;
import com.spark.test.galleryupload.model.GalleryItem;

import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

/**
 * Created by Naiim Ab. on 8/23/2019
 * Project: Gallery Upload
 */
public class GalleryItemViewModel extends BaseObservable {

    private GalleryItem galleryItem;
    private Context context;

    public GalleryItemViewModel(Context context, GalleryItem galleryItem) {
        this.context = context;
        this.galleryItem = galleryItem;
    }

    public void setGalleryItem(GalleryItem galleryItem) {
        this.galleryItem = galleryItem;
        notifyChange();
    }

    public String getImageURL() {
        return GalleryDataFactory.BASE_URL + galleryItem.getImageURL();
    }

    @BindingAdapter({"image"})
    public static void loadGalleryImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }



}
