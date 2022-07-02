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
import java.io.IOException;
import java.io.OutputStream;

public class ContactDetailActivity extends AppCompatActivity {

    // creating variables for our image view and text view and string. .
    private String name, phoneNumber, organization, email;
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

        // on below line we are getting data which 
        // we passed in our adapter class with intent.
        ContactsModal modal = getIntent().getParcelableExtra("contact");
        name = modal.getUserName();
        phoneNumber = modal.getPhoneNumber();
        organization = modal.getOrganization();
        email = modal.getEmail();

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

        nameTV.setText(name);
        phoneTV.setText(phoneNumber);
        organizationTV.setText(organization);
        emailTV.setText(email);
        contactIV.setClipToOutline(true);
        Bitmap bitmap = MainActivity.getPhoto(this, modal.getPhotoUri());
        contactIV.setImageBitmap(bitmap);
        backgroundIV.setImageBitmap(bitmap);
        backgroundIV.setBlur(2);

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

        contactIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri imageUri = getImageUriFromGallery();
                Bitmap newPhoto = getBitmapFromImageUri(imageUri);
                setPhotoByContactId(modal.getContactId(), newPhoto);
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

    private void setPhotoByContactId(String contactId, Bitmap bmp) {
        long rawContactId = getRawContactId(contactId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Uri pictureUri = Uri.withAppendedPath(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI,
                rawContactId), ContactsContract.RawContacts.DisplayPhoto.CONTENT_DIRECTORY);
        try {
            AssetFileDescriptor afd = getContentResolver().openAssetFileDescriptor(pictureUri, "rw");
            OutputStream os = afd.createOutputStream();
            os.write(byteArray);
            os.close();
            afd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long getRawContactId(String contactId) {
        String[] projection = new String[]{ContactsContract.RawContacts._ID};
        String selection = ContactsContract.RawContacts.CONTACT_ID + "=?";
        String[] selectionArgs = new String[]{contactId};
        Cursor c = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
        if (c == null) return 0;
        int rawContactId = -1;
        if (c.moveToFirst()) {
            rawContactId = c.getInt(c.getColumnIndexOrThrow(ContactsContract.RawContacts._ID));
        }
        c.close();
        return rawContactId;
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
        return null;
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        imgPathViewModel = new ViewModelProvider(this).get(ImgPathViewModel.class);
        Log.e(TAG, "onResume");
        if (imgPathViewModel.getImgPath() != null) {
            Log.e(TAG, imgPathViewModel.getImgPath());
        } else {
            Log.e(TAG, "imgPath is null");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");
    }
}