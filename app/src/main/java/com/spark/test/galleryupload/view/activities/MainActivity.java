package com.spark.test.galleryupload.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.spark.test.galleryupload.R;
import com.spark.test.galleryupload.databinding.ActivityMainBinding;
import com.spark.test.galleryupload.viewmodel.GalleryViewModel;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Naiim Ab. on 8/23/2019
 * Project: Gallery Upload
 */
public class MainActivity extends AppCompatActivity implements Observer {

    private ActivityMainBinding activityMainBinding;
    private GalleryViewModel galleryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start binding layout and data from ViewModel
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        galleryViewModel = new GalleryViewModel(this);
        activityMainBinding.setGalleryViewModel(galleryViewModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        galleryViewModel.reset();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
