package com.spark.test.galleryupload.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.spark.test.galleryupload.R;
import com.spark.test.galleryupload.databinding.GalleryItemBinding;
import com.spark.test.galleryupload.model.GalleryItem;
import com.spark.test.galleryupload.viewmodel.GalleryItemViewModel;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Naiim Ab. on 8/23/2019
 * Project: Gallery Upload
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryAdapterViewHolder> {

    private List<GalleryItem> galleryItemList;

    public void GallerAdapter() {
        this.galleryItemList = Collections.emptyList();
    }

    public void setGalleryItemList(List<GalleryItem> galleryItemList) {
        this.galleryItemList = galleryItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GalleryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GalleryItemBinding itemPhotoBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.gallery_item,
                parent, false);
        return new GalleryAdapterViewHolder(itemPhotoBinding);
    }

    @Override
    public void onBindViewHolder(GalleryAdapterViewHolder holder, int position) {
        holder.bindPhoto(galleryItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return galleryItemList.size();
    }

    static class GalleryAdapterViewHolder extends RecyclerView.ViewHolder {

        GalleryItemBinding galleryItemBinding;

        GalleryAdapterViewHolder(GalleryItemBinding galleryItemBinding) {
            super(galleryItemBinding.itemPhoto);
            this.galleryItemBinding = galleryItemBinding;
        }


        void bindPhoto(GalleryItem galleryItem) {

        }
    }
}
