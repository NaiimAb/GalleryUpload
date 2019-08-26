package com.spark.test.galleryupload.view.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.spark.test.galleryupload.BuildConfig;
import com.spark.test.galleryupload.R;
import com.spark.test.galleryupload.databinding.ActivityMainBinding;
import com.spark.test.galleryupload.model.ImageUploadItem;
import com.spark.test.galleryupload.utils.Common;
import com.spark.test.galleryupload.view.adapters.GalleryAdapter;
import com.spark.test.galleryupload.viewmodel.GalleryViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Naiim Ab. on 8/23/2019
 * Project: Gallery Upload
 */
public class MainActivity extends AppCompatActivity implements Observer {

    private ActivityMainBinding activityMainBinding;
    private GalleryViewModel galleryViewModel;
    private final int REQUEST_TAKE_PHOTO = 1;
    private final int REQUEST_GALLERY_PHOTO = 2;
    private final int REQUEST_UPLOAD_NEW_IMAGE = 3;
    private final int REQUEST_UPLOAD_UPDATED_IMAGE = 4;
    private File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start binding layout and data from ViewModel
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        galleryViewModel = new GalleryViewModel(this);
        activityMainBinding.setGalleryViewModel(galleryViewModel);

        createGalleryAdapter(activityMainBinding.galleryList);
        onImageUpload(activityMainBinding.uploadImage);
        setObserver(galleryViewModel);

    }

    private void createGalleryAdapter(RecyclerView recyclerView) {
        // Setting up RecycleView Adapter
        GalleryAdapter adapter = new GalleryAdapter();
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void onImageUpload(Button imageUpload) {
        imageUpload.setOnClickListener(v -> {
            final CharSequence[] items = {
                    getString(R.string.choose_dialog_take_pic), getString(R.string.choose_dialog_gallery),
                    getString(R.string.cancel)
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setItems(items, (dialog, item) -> {
                if (items[item].equals(getString(R.string.choose_dialog_take_pic))) {
                    requestGalleryPermissions(true);
                } else if (items[item].equals(getString(R.string.choose_dialog_gallery))) {
                    requestGalleryPermissions(false);
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            });
            builder.show();
        });
    }

    private void setObserver(Observable observable) {
        observable.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof GalleryViewModel) {
            GalleryAdapter galleryAdapter = (GalleryAdapter) activityMainBinding.galleryList.getAdapter();
            GalleryViewModel galleryViewModel = (GalleryViewModel) o;
            if (galleryAdapter != null) {
                galleryAdapter.setGalleryItemList(galleryViewModel.getGalleryItems());
            }
        }
    }

    /**
     * Requesting multiple permissions (storage and camera) at once
     * On permanent denial opens settings dialog
     */
    private void requestGalleryPermissions(final boolean cameraSelection) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (cameraSelection) {
                                takePicture();
                            } else {
                                selectFromLocalGallery();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(error -> Toast.makeText(getApplicationContext(), R.string.error_occurred, Toast.LENGTH_SHORT)
                        .show()
                )
                .onSameThread()
                .check();
    }

    /**
     * Capture image from camera
     */
    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = Common.createImageFile(this);
            } catch (IOException ex) {
                ex.printStackTrace();

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Uri photoToUri = Uri.fromFile(mPhotoFile);
                startImageEditor(this, photoToUri, REQUEST_UPLOAD_NEW_IMAGE);
            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();
                Uri realPhotoUri = Uri.fromFile(new File(Common.getRealPathFromUri(this, selectedImage)));
                startImageEditor(this, realPhotoUri, REQUEST_UPLOAD_NEW_IMAGE);
            } else if (requestCode == REQUEST_UPLOAD_NEW_IMAGE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                ImageUploadItem imageUploadItem = new ImageUploadItem();
                File file = new File(resultUri.getPath());
                String fileExt = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
                imageUploadItem.setUserID(1);
                imageUploadItem.setImageFile(file);
                imageUploadItem.setFileName(Common.createFileName() + fileExt);
                imageUploadItem.setUpdateImage(false);
                galleryViewModel.uploadImage(imageUploadItem);
            } else if (requestCode == REQUEST_UPLOAD_UPDATED_IMAGE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                Common.removeFile(data.getStringExtra("filePath"));
                ImageUploadItem imageUploadItem = new ImageUploadItem();
                File file = new File(resultUri.getPath());
                imageUploadItem.setUserID(1);
                imageUploadItem.setImageFile(file);
                imageUploadItem.setFileName(data.getStringExtra("fileOriginalName"));
                imageUploadItem.setUpdateImage(true);
                galleryViewModel.uploadImage(imageUploadItem);
            }
        }
        else if(resultCode != 0) {
            Toast.makeText(this, R.string.error_occurred, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Select image fro gallery
     */
    private void selectFromLocalGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_required_dialog_title);
        builder.setMessage(R.string.permission_required_dialog_desc);
        builder.setPositiveButton("Settings", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    // Start cropping image activity
    public void startImageEditor(Context context, Uri imageUri, int requestCode) {
        // start cropping activity for pre-acquired image saved on the device
        Intent intent = CropImage.activity(imageUri)
                .setAllowRotation(true)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setInitialCropWindowPaddingRatio(0)
                .getIntent(context);
        startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        galleryViewModel.reset();
    }
}
