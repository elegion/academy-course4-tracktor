package com.elegion.tracktor.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class ScreenshotMaker {

    public static Bitmap makeScreenshot(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static String toBase64(Bitmap bitmap) {
        return Base64.encodeToString(getBytes(bitmap), Base64.DEFAULT);
    }

    public static Bitmap fromBase64(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private static byte[] getBytes(Bitmap bm) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}