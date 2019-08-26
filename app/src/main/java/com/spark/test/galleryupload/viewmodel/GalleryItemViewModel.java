package com.spark.test.galleryupload.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.spark.test.galleryupload.model.GalleryItem;
import com.spark.test.galleryupload.utils.Common;
import com.spark.test.galleryupload.view.activities.MainActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

/**
 * Created by Naiim Ab. on 8/23/2019
 * Project: Gallery Upload
 */
public class GalleryItemViewModel extends BaseObservable {

    private GalleryItem galleryItem;
    private Context context;
    private final int REQUEST_UPLOAD_UPDATED_IMAGE = 4;

    public GalleryItemViewModel(Context context, GalleryItem galleryItem) {
        this.context = context;
        this.galleryItem = galleryItem;
    }

    public void setGalleryItem(GalleryItem galleryItem) {
        this.galleryItem = galleryItem;
        notifyChange();
    }

    public String getImageURL() {
        return Common.BASE_URL + galleryItem.getImageURL();
    }

    @BindingAdapter({"image"})
    public static void loadGalleryImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(view);
    }

    // Getting Clickable image to start cropping or rotation
    public void onItemClick(View view) {
        // Downloading Image to device
        Glide.with(view.getContext())
                .asBitmap()
                .load(getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Saving image to user device so we can be able to edit it
                        String savingBitmap = Common.saveImage(resource);
                        Uri imageUri = Uri.fromFile(new File(savingBitmap));
                        Intent intent = CropImage.activity(imageUri)
                                .setAllowRotation(true)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setInitialCropWindowPaddingRatio(0)
                                .getIntent(context);
                        intent.putExtra("fileOriginalName",  getImageURL().substring(getImageURL().lastIndexOf("/")+1));
                        intent.putExtra("filePath", savingBitmap);
                        ((Activity) context).startActivityForResult(intent, REQUEST_UPLOAD_UPDATED_IMAGE);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

    }

}
