package com.example.testapplication.shared.util;

import android.graphics.Bitmap;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public enum QrGenerator {
    INSTANCE;

    public Bitmap getQrCode(String data){
        QRGEncoder encoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 1000);
        try {
            return encoder.encodeAsBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
