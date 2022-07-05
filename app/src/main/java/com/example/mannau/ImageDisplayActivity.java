package com.example.mannau;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mannau.fragments.PictureBrowserFragment;
import com.example.mannau.utils.ItemClickListener;
import com.example.mannau.utils.MarginDecoration;
import com.example.mannau.utils.PicHolder;
import com.example.mannau.utils.PictureAdapter;
import com.example.mannau.utils.PictureFacer;

import java.util.ArrayList;

/**
 * Author CodeBoy722
 * <p>
 * This Activity get a path to a folder that contains images from the CustomGalleryActivity Intent and displays
 * all the images in the folder inside a RecyclerView
 */

public class ImageDisplayActivity extends AppCompatActivity implements ItemClickListener {

    private final String TAG = "+ImageDisplayActivity";

    RecyclerView imageRecycler;
    ArrayList<PictureFacer> allPictures;
    ProgressBar load;
    String folderPath;
    TextView folderName;
//    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        folderName = findViewById(R.id.foldername);
        folderName.setText(getIntent().getStringExtra("folderName"));

        folderPath = getIntent().getStringExtra("folderPath");
        allPictures = new ArrayList<>();
        imageRecycler = findViewById(R.id.recycler);
        imageRecycler.addItemDecoration(new MarginDecoration(this));
        imageRecycler.hasFixedSize();
        load = findViewById(R.id.loader);


        if (allPictures.isEmpty()) {
            load.setVisibility(View.VISIBLE);
            allPictures = getAllImagesByFolder(folderPath);
            imageRecycler.setAdapter(new PictureAdapter(allPictures, ImageDisplayActivity.this, this));
            load.setVisibility(View.GONE);
        } else {

        }
    }

    /**
     * @param holder   The ViewHolder for the clicked picture
     * @param position The position in the grid of the picture that was clicked
     * @param pics     An ArrayList of all the items in the Adapter
     */
    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<PictureFacer> pics) {
        PictureBrowserFragment browser = PictureBrowserFragment.newInstance(pics, position, ImageDisplayActivity.this);

//        Bundle args = new Bundle();
//        args.putString("SENDER_KEY", null);
//        args.putString("PATH_KEY", null);
//        browser.setArguments(args);

        // Note that we need the API version check here because the actual transition classes (e.g. Fade)
        // are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
        // ARE available in the support library (though they don't do anything on API < 21)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //browser.setEnterTransition(new Slide());
            //browser.setExitTransition(new Slide()); uncomment this to use slide transition and comment the two lines below
            browser.setEnterTransition(new Fade());
            browser.setExitTransition(new Fade());
        }

        getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.picture, position + "picture")
                .add(R.id.displayContainer, browser)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {

    }

    /**
     * This Method gets all the images in the folder paths passed as a String to the method and returns
     * and ArrayList of pictureFacer a custom object that holds data of a given image
     *
     * @param path a String corresponding to a folder path on the device external storage
     */
    public ArrayList<PictureFacer> getAllImagesByFolder(String path) {
        ArrayList<PictureFacer> images = new ArrayList<>();
        Uri allVideosUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = ImageDisplayActivity.this.getContentResolver().query(allVideosUri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
        try {
            cursor.moveToFirst();
            do {
                PictureFacer pic = new PictureFacer();

                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                images.add(pic);
            } while (cursor.moveToNext());
            cursor.close();
            ArrayList<PictureFacer> reSelection = new ArrayList<>();
            for (int i = images.size() - 1; i > -1; i--) {
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ContactDetailActivity.getImgPathViewModel().getImgPath() != null) {
            finish();
        }
    }
}
