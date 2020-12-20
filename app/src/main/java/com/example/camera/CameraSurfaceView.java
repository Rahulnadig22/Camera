package com.example.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private android.hardware.Camera camera;
    private SurfaceHolder surfaceHolder;


    public CameraSurfaceView(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(surfaceHolder!=null){
            try {
                camera.setPreviewDisplay(holder);
                camera.setDisplayOrientation(90);
                camera.startPreview();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        if(surfaceHolder!=null){
            camera.stopPreview();
            camera.release();
        }

    }
}
