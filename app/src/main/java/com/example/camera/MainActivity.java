package com.example.camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FrameLayout mCameraFrame;
    private ImageView mIvCaptureImage;
    private ImageView mIvFlipCamera;
    private ImageView mIvPreviewImage;

    public static String KEY = "KEY";

    private int cameraId;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraFrame = findViewById(R.id.camera_holder);
        mIvCaptureImage = findViewById(R.id.camera_capture);
        mIvFlipCamera = findViewById(R.id.flip_camera);
        mIvPreviewImage = findViewById(R.id.iv_preview);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                startCameraPreview(true);
        }else {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
        }

        mIvCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null,null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        Bitmap captureImg = BitmapFactory.decodeByteArray(data,0,data.length);
                        saveImgToDevice(captureImg);
                    }
                });
            }
        });

        mIvFlipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBackCamera = cameraId == Camera.CameraInfo.CAMERA_FACING_BACK ? false : true;
                mCamera.stopPreview();
                startCameraPreview(isBackCamera);
            }
        });

        mIvPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent photo = new Intent(MainActivity.this,Gallery.class);
//                photo.putExtra(KEY,imageList);
//                startActivity(photo);
                startActivity(new Intent(MainActivity.this,Gallery.class));
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    startCameraPreview(true);
            }
        }
    }

    private void startCameraPreview(boolean isBackCamera){
        cameraId = isBackCamera ? Camera.CameraInfo.CAMERA_FACING_BACK:Camera.CameraInfo.CAMERA_FACING_FRONT;
        mCamera = Camera.open(cameraId);
        CameraSurfaceView cameraSurfaceView = new CameraSurfaceView(this,mCamera);
        mCameraFrame.addView(cameraSurfaceView);
    }

    private void saveImgToDevice(Bitmap captureImg){
        File myDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"MyCameraApp");
        if(!myDirectory.exists()){
            myDirectory.mkdir();
        }
        File imageName = new File(myDirectory,"IMG" + System.currentTimeMillis()+".png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageName);
            captureImg.compress(Bitmap.CompressFormat.PNG,100,fos);
        }catch (Exception e){
            e.printStackTrace();
        }
        mIvPreviewImage.setImageBitmap(captureImg);
        mCamera.startPreview();
    }

    public ArrayList<String> readImageFromDevice(){
        Uri imageURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] proj = new String[] {MediaStore.Images.Media.DATA};
        ArrayList<String> imageList = new ArrayList<>();
        Cursor cursor = getApplicationContext().getContentResolver().query(imageURI,proj,null,null,null);
        if(cursor!=null){
            for(cursor.moveToFirst();!cursor.isLast();cursor.moveToNext()){
                String image = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                imageList.add(image);
            }
        }
        return imageList;
    }
}