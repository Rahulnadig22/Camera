package com.example.camera;

import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private Context context;
    private ArrayList<String> imageList;

    public PhotoAdapter(Context context, ArrayList<String> imageList){
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoHolder(LayoutInflater.from(context).inflate(R.layout.cell_view_photo,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        String photo =imageList.get(position);
        if(photo!=null){
            Glide.with(context).load(MediaStore.Images.Media.EXTERNAL_CONTENT_URI).into(holder.mPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class PhotoHolder extends RecyclerView.ViewHolder{
        private ImageView mPhoto;
        private CardView mCardView;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            mPhoto = itemView.findViewById(R.id.photo);
            mCardView = itemView.findViewById(R.id.cv_photo);

        }
    }
}
