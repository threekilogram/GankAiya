package com.example.wuxio.gankexamples.main;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxio 2018-05-11:7:54
 */
public class BannerBitmapManager {

    private static List< Bitmap > sBitmaps = new ArrayList<>(5);


    public static List< Bitmap > getBitmaps() {

        return sBitmaps;
    }


    public static void put(int index, Bitmap bitmap) {

        sBitmaps.add(index, bitmap);
    }
}
