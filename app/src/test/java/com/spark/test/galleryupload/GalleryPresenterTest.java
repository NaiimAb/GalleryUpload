package com.spark.test.galleryupload;

import com.spark.test.galleryupload.data.GalleryDataService;
import com.spark.test.galleryupload.model.GalleryItem;
import com.spark.test.galleryupload.utils.Common;
import com.spark.test.galleryupload.viewmodel.GalleryViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static rx.Observable.just;

/**
 * Created by Naiim Ab. on 8/26/2019
 * Project: Gallery Upload
 */
public class GalleryPresenterTest {
    @Mock
    private GalleryDataService galleryDataService;


    @Mock
    private GalleryViewModel galleryViewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fetchValidDataShouldLoadIntoView() {

        GalleryItem galleryItem = new GalleryItem(1, Common.BASE_URL+"uploads/images/image1.jpg");
        List<GalleryItem> galleryItems= new ArrayList<>();
        galleryItems.add(galleryItem);

        when(galleryDataService.fetchGallery(1)).thenReturn(Observable.just(galleryItems));

    }
}
