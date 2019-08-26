package com.spark.test.galleryupload;

import android.content.Context;

import com.spark.test.galleryupload.utils.Common;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.mockito.Mockito.verify;

/**
 * Created by Naiim Ab. on 8/26/2019
 * Project: Gallery Upload
 */
public class CommonTest {
    @Mock
    private Context context;
    @Test
    public void create_FILE_Test() throws IOException {

        FileOutputStream mockFos = Mockito.mock(FileOutputStream.class);
        String data = "ensure written";

        Common.createImageFile(context);

        verify(mockFos).write(data.getBytes());


    }

    @Test
    public void create_FILE_Test_With_Close() throws IOException {

        FileOutputStream mockFos = Mockito.mock(FileOutputStream.class);
        String data = "ensure closed";

        Common.createImageFile(context);

        verify(mockFos).close();


    }
}
