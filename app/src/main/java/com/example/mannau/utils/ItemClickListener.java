package com.example.mannau.utils;

import java.util.ArrayList;

public interface ItemClickListener {
    /**
     * Called when a picture is clicked
     * @param holder The ViewHolder for the clicked picture
     * @param position The position in the grid of the picture that was clicked
     */
    void onPicClicked(PicHolder holder, int position, ArrayList<PictureFacer> pics);
    void onPicClicked(String pictureFolderPath,String folderName);
}

