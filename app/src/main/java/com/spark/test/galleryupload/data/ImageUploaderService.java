package com.spark.test.galleryupload.data;

import com.spark.test.galleryupload.model.ImageStatusItem;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Naiim Ab. on 8/24/2019
 * Project: Gallery Upload
 */
public interface ImageUploaderService {
    @Multipart
    @POST("api/upload-image.php")
    Call<ImageStatusItem> uploadImage(@Part("user_id") RequestBody userId, @Part MultipartBody.Part file, @Part("name") RequestBody name,
                                      @Part("update") RequestBody update);
}
