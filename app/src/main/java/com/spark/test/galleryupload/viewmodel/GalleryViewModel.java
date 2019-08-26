package com.spark.test.galleryupload.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.spark.test.galleryupload.MyApp;
import com.spark.test.galleryupload.R;
import com.spark.test.galleryupload.data.GalleryDataService;
import com.spark.test.galleryupload.data.ImageUploaderService;
import com.spark.test.galleryupload.model.GalleryItem;
import com.spark.test.galleryupload.model.ImageStatusItem;
import com.spark.test.galleryupload.model.ImageUploadItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Naiim Ab. on 8/23/2019
 * Project: Gallery Upload
 */
public class GalleryViewModel extends Observable {

    public ObservableInt galleryProgress;
    public ObservableField<String> messageLabel;
    private ObservableInt galleryList;
    private ObservableInt galleryLabel;
    private Context context;
    private List<GalleryItem> galleryItems;
    private Subscription subscription;

    public GalleryViewModel(Context context) {
        this.context = context;

        galleryProgress = new ObservableInt(View.VISIBLE);
        messageLabel = new ObservableField<>("");
        galleryList = new ObservableInt(View.GONE);
        galleryLabel = new ObservableInt(View.GONE);

        galleryItems = new ArrayList<>();

        fetchData();
    }


    private void fetchData() {
        unSubscribeFromObservable();
        galleryItems.clear();
        MyApp myApp = MyApp.getInstance(context);
        GalleryDataService dataService = myApp.getGalleryService();
        subscription = dataService.fetchGallery()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(myApp.subscribeScheduler())
                .subscribe(galleryItems -> {
                    galleryProgress.set(View.GONE);
                    galleryLabel.set(View.GONE);
                    galleryList.set(View.VISIBLE);
                    changeGallery(galleryItems);
                }, throwable -> {
                    galleryProgress.set(View.GONE);
                    galleryLabel.set(View.VISIBLE);
                    galleryList.set(View.GONE);
                    messageLabel.set(context.getString(R.string.error_loading_gallery));
                    throwable.printStackTrace();
                });
    }

    public void uploadImage(final ImageUploadItem imageUploadItem) {

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), imageUploadItem.getUserID().toString());
        RequestBody image = RequestBody.create(MediaType.parse("*/*"), imageUploadItem.getImageFile());
        RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), imageUploadItem.getFileName());
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", imageUploadItem.getFileName(), image);


        MyApp myApp = MyApp.getInstance(context);
        ImageUploaderService imageUploaderService = myApp.getImageUploaderService();
        final Call<ImageStatusItem> response = imageUploaderService.uploadImage(userId, fileToUpload, fileName);

        response.enqueue(new Callback<ImageStatusItem>() {
            @Override
            public void onResponse(@NonNull Call<ImageStatusItem> call, @NonNull Response<ImageStatusItem> response) {
                ImageStatusItem imageStatusItem = response.body();
                if (imageStatusItem != null) {
                    Toast.makeText(context, imageStatusItem.getMessage(), Toast.LENGTH_LONG).show();
                    fetchData();
                } else {
                    Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_LONG).show();
                    try {
                        Log.e("Response", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImageStatusItem> call, @NonNull Throwable t) {
                Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_LONG).show();
                Log.e("Response Error:", t.getMessage());
                if (t instanceof IOException) {
                    Log.v("", "Network Error");
                }
            }

        });
    }

    public List<GalleryItem> getGalleryItems() {
        return galleryItems;
    }

    private void unSubscribeFromObservable() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void changeGallery(List<GalleryItem> galleryItems) {
        this.galleryItems.addAll(galleryItems);
        setChanged();
        notifyObservers();
    }

    public void reset() {
        unSubscribeFromObservable();
        subscription = null;
        context = null;
    }
}
