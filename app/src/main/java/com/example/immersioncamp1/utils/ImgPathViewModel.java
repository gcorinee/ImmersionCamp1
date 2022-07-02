package com.example.immersioncamp1.utils;

import androidx.lifecycle.ViewModel;

public class ImgPathViewModel extends ViewModel {
    private String imgPath = null;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
