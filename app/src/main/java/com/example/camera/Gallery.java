package com.example.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Gallery extends AppCompatActivity {
    private RecyclerView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mPhotoView = findViewById(R.id.photo_preview);
        mPhotoView.setLayoutManager(new GridLayoutManager(this,3));

//        Bundle data = getIntent().getExtras();
//        ArrayList<String> photo = data.getStringArrayList(MainActivity.KEY);

        MainActivity activity = new MainActivity();
        ArrayList<String> photo = activity.readImageFromDevice();

        PhotoAdapter adapter = new PhotoAdapter(this,photo);
        mPhotoView.setAdapter(adapter);

    }
}