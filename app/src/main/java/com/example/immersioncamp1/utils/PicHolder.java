package com.example.immersioncamp1.utils;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.immersioncamp1.R;


public class PicHolder extends RecyclerView.ViewHolder {
    public ImageView picture;

    PicHolder(@NonNull View itemView) {
        super(itemView);

        picture = itemView.findViewById(R.id.image);
    }
}
