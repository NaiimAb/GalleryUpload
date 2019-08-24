package com.spark.test.galleryupload.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Naiim Ab. on 8/23/2019
 * Project: Gallery Upload
 */
public class GalleryItem implements Serializable {


    @SerializedName("id")
    @Expose
    private Integer ID;

    @SerializedName("path")
    @Expose
    private String imageURL;

    public GalleryItem(Integer ID, String imageUrl) {

        this.ID = ID;
        this.imageURL = imageUrl;
    }

    public Integer getID() { return ID;}

    public String getImageURL() { return imageURL;}

}
