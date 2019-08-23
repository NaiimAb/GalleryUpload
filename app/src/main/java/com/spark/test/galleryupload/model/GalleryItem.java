package com.spark.test.galleryupload.model;

import java.io.Serializable;

/**
 * Created by Naiim Ab. on 8/23/2019
 * Project: Gallery Upload
 */
public class GalleryItem implements Serializable {


    private Integer ID;

    private String imageURL;

    private String thumbnailUrl;

    public GalleryItem(Integer ID, String imageUrl, String thumbnailUrl) {

        this.ID = ID;
        this.imageURL = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Integer getID() { return ID;}

    public String getImageURL() { return imageURL;}

    public String getThumbnailUrl() { return thumbnailUrl;}
}
