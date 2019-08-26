package com.spark.test.galleryupload.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Naiim Ab. on 8/24/2019
 * Project: Gallery Upload
 */
public class Common {

    public final static String BASE_URL = "http://192.168.1.28/galleryapp/";

    /**
     * Create Random String for naming files using Date
     */
    public static String createFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        return timeStamp + "_JPEG_";
    }

    /**
     * Create file with current timestamp name
     * @throws IOException - Throws IOException on Error
     */
    public static File createImageFile(Context context) throws IOException {
        // Create an image file name

        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(createFileName(), ".jpg", storageDir);
    }

    /**
     * Get real file path from URI
     */
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Save a remote bitmap to a file in Android
     */
    public static String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "tmp_" + createFileName() + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/SparkGallery_temp");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return savedImagePath;
    }

    /**
     * Remove a file from given path
     */

    public static void removeFile(String path) {
        File fdelete = new File(path);
        if (fdelete.exists()) {
            boolean deleted = fdelete.delete();
        }
    }

}
