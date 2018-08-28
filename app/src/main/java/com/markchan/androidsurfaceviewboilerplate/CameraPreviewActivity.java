package com.markchan.androidsurfaceviewboilerplate;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class CameraPreviewActivity extends AppCompatActivity {

    private FrameLayout mContainer;
    private Camera mCamera;
    private CameraSurfaceView mPreview;

    final RxPermissions mRxPermissions = new RxPermissions(this); // where this is an Activity or Fragment instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
        mContainer = findViewById(R.id.camera_preview_aty_fl);

        // Must be done during an initialization phase like onCreate
        mRxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(Boolean granted) throws Exception {
                        // Create an instance of Camera
                        mCamera = getCameraInstance();
                        // Create our Preview view and set it as the content of our activity.
                        mPreview = new CameraSurfaceView(CameraPreviewActivity.this, mCamera);
                        mContainer.addView(mPreview);
                    }
                });
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera(); // release the camera immediately on pause event
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }
    }
}
