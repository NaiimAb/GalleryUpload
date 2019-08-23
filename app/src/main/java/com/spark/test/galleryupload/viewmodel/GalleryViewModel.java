package com.spark.test.galleryupload.viewmodel;

import android.content.Context;
import android.view.View;

import com.spark.test.galleryupload.model.GalleryItem;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

/**
 * Created by Naiim Ab. on 8/23/2019
 * Project: Gallery Upload
 */
public class GalleryViewModel extends Observable {

    public ObservableInt galleryProgress;
    public ObservableField<String> messageLabel;
    public ObservableInt galleryList;
    public ObservableInt galleryLabel;
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

    }

    public List<GalleryItem> getGalleryItems() {
        return galleryItems;
    }

    private void unSubscribeFromObservable() {

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
