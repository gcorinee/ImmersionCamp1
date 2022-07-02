package com.example.immersioncamp1;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.immersioncamp1.utils.ImgPathViewModel;

import com.jgabrielfreitas.core.BlurImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class ContactDetailActivity extends AppCompatActivity {

    // creating variables for our image view and text view and string.
    private ContactsModal modal;
    private String contactId, name, phoneNumber, organization, email;
    private TextView phoneTV, nameTV, organizationTV, emailTV;
    private ImageView contactIV;
    private LinearLayout topBar;
    private CardView callCV, messageCV;
    private BlurImageView backgroundIV;

    public static ImgPathViewModel imgPathViewModel;

    private final String TAG = "+ContactDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contact_detail);

        modal = getIntent().getParcelableExtra("contact");
        Log.e(null, "modal: " + modal.getUserName() + "  and  " + modal.getContactId());
        contactId = modal.getContactId();
        // initializing our views.
        nameTV = findViewById(R.id.idTVName);
        contactIV = findViewById(R.id.idIVContact);
        phoneTV = findViewById(R.id.idTVPhone);
        organizationTV = findViewById(R.id.idTVOrganization);
        emailTV = findViewById(R.id.idTVEmail);
        callCV = findViewById(R.id.idCVCall);
        messageCV = findViewById(R.id.idCVMessage);
        topBar = findViewById(R.id.topBar);
        backgroundIV = findViewById(R.id.idIVBackground);
        setView();



        // on below line adding click listener for our calling image view.
        callCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to make a call.
                makeCall(phoneNumber);
            }
        });

        // on below line adding on click listener for our message image view.
        messageCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to send message
                sendMessage(phoneNumber);
            }
        });

        topBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // on below line adding on click listener for custom gallery.
        contactIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomGalleryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendMessage(String phoneNumber) {
        // in this method we are calling an intent to send sms.
        // on below line we are passing our contact number.
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
        intent.putExtra("sms_body", "Enter your message");
        startActivity(intent);
    }

    private void makeCall(String phoneNumber) {
        // this method is called for making a call.
        // on below line we are calling an intent to make a call.
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        // on below line we are setting data to it.
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        // on below line we are checking if the calling permissions are granted not.
        if (ActivityCompat.checkSelfPermission(ContactDetailActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // at last we are starting activity.
        startActivity(callIntent);
    }

    private Bitmap getBitmapFromImageUri(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

//    USE THIS TO UPDATE PHOTO !!!
    private Uri getImageUriFromGallery(){
        return Uri.fromFile(new File(imgPathViewModel.getImgPath()));
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        imgPathViewModel = new ViewModelProvider(this).get(ImgPathViewModel.class);
        Log.e(TAG, "onResume");
        if (imgPathViewModel.getImgPath() != null) {
            Log.e(TAG, imgPathViewModel.getImgPath());
            Uri imageUri = getImageUriFromGallery();
            Bitmap newPhoto = getBitmapFromImageUri(imageUri);
            Log.e(null, "thisis: " + contactId);
            MainActivity.setPhotoByContactId(this, contactId, newPhoto);
            Log.e(TAG, "kvcvmvv");
            Log.e(TAG, "z,mzmmzz");
            try { TimeUnit.MILLISECONDS.sleep(100); } catch (Exception e) { return; }
            setView();
        } else {
            Log.e(TAG, "imgPath is null");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");
    }

    private void setView() {

        // on below line we are getting data which
        // we passed in our adapter class with intent.
        name = modal.getUserName();
        phoneNumber = modal.getPhoneNumber();
        organization = modal.getOrganization();
        email = modal.getEmail();


        nameTV.setText(name);
        phoneTV.setText(phoneNumber);
        organizationTV.setText(organization);
        emailTV.setText(email);
        contactIV.setClipToOutline(true);
        Bitmap bitmap = MainActivity.getPhoto(this, modal.getPhotoUri());
        contactIV.setImageBitmap(bitmap);
        backgroundIV.setImageBitmap(bitmap);
        backgroundIV.setBlur(2);
    }
}