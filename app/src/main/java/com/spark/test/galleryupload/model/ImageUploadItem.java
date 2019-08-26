package com.spark.test.galleryupload.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;

/**
 * Created by Naiim Ab. on 8/24/2019
 * Project: Gallery Upload
 */
public class ImageUploadItem {

    @SerializedName("file")
    @Expose
    private File imageFile;

    @SerializedName("user_id")
    @Expose
    private Integer userID;

    @SerializedName("name")
    @Expose
    private String fileName;

    @SerializedName("update")
    @Expose
    private Boolean updateImage;


    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File image) {
        this.imageFile = image;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getFileName() { return fileName;}

    public void setFileName(String fileName) { this.fileName = fileName;}

    public Boolean getUpdateImage() { return updateImage;}

    public void setUpdateImage(Boolean updateImage) { this.updateImage = updateImage;}
}
