package com.example.testapplication.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.testapplication.R;
import com.example.testapplication.shared.pojo.Account;
import com.example.testapplication.shared.pojo.Client;
import com.example.testapplication.shared.util.AccountMapper;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class BarCodeScanner extends AppCompatActivity {

    private BarcodeDetector detector;
    private CameraSource source;
    private SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_code_scanner);
        initializeComponents();
    }

    private void initializeComponents() {
        surfaceView = findViewById(R.id.qrView);

        detector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        source = new CameraSource.Builder(this, detector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1000, 1000)
                .build();

        initializeSurfaceView();
        initializeBarcodeDetector();

    }

    private void initializeBarcodeDetector() {
        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "Scanner needs to be closed...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCode = detections.getDetectedItems();
                if (qrCode != null) {
                    try {
                        Client client = AccountMapper
                                .INSTANCE
                                .accountToClient(new GsonBuilder().create().fromJson(qrCode.valueAt(0).displayValue, Account.class));

                        if(client != null) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("data", client);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    } catch (Exception e) {
                        Log.d("BARCODE SCANNER", "receiveDetections: " + "Could not convert data to Account object...");
                    }
                }
            }
        });
    }

    private void initializeSurfaceView() {
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    source.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                source.stop();
            }
        });
    }
}
