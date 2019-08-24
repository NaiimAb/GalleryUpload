package com.spark.test.galleryupload.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.spark.test.galleryupload.MyApp;
import com.spark.test.galleryupload.R;
import com.spark.test.galleryupload.data.GalleryDataService;
import com.spark.test.galleryupload.model.GalleryItem;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import rx.android.schedulers.AndroidSchedulers;

import rx.Subscription;
import rx.functions.Action1;

import static androidx.constraintlayout.widget.Constraints.TAG;

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
        MyApp myApp = MyApp.getInstance(context);
        GalleryDataService dataService = myApp.getGalleryService();
        subscription = dataService.fetchGallery()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(myApp.subscribeScheduler())
                .subscribe(new Action1<List<GalleryItem>>() {
                    @Override
                    public void call(List<GalleryItem> galleryItems) {
                        galleryProgress.set(View.GONE);
                        galleryLabel.set(View.GONE);
                        galleryList.set(View.VISIBLE);
                        changeGallery(galleryItems);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        messageLabel.set(context.getString(R.string.error_loading_gallery));
                        galleryProgress.set(View.GONE);
                        galleryLabel.set(View.VISIBLE);
                        galleryList.set(View.GONE);
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
