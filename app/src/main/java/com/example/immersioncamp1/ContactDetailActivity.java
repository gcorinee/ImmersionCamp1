package com.example.immersioncamp1;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ContactDetailActivity extends AppCompatActivity {

    // creating variables for our image view and text view and string. .
    private String name, phoneNumber, organization, email;
    private TextView phoneTV, nameTV, organizationTV, emailTV;
    private ImageView contactIV, callIV, messageIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contact_detail);

        // on below line we are getting data which 
        // we passed in our adapter class with intent.
        name = getIntent().getStringExtra("name");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        organization = getIntent().getStringExtra("organization");
        email = getIntent().getStringExtra("email");
        byte[] byteArray = getIntent().getByteArrayExtra("photo");
        Log.d(null, "length is: "+ byteArray.length);

        // initializing our views.
        nameTV = findViewById(R.id.idTVName);
        contactIV = findViewById(R.id.idIVContact);
        phoneTV = findViewById(R.id.idTVPhone);
        organizationTV = findViewById(R.id.idTVOrganization);
        emailTV = findViewById(R.id.idTVEmail);
        nameTV.setText(name);
        phoneTV.setText(phoneNumber);
        organizationTV.setText(organization);
        emailTV.setText(email);
        callIV = findViewById(R.id.idIVCall);
        messageIV = findViewById(R.id.idIVMessage);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Log.d(null, "ldldld is: " + bitmap.getByteCount());
        contactIV.setImageBitmap(bitmap);

        // on below line adding click listener for our calling image view.
        callIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to make a call.
                makeCall(phoneNumber);
            }
        });

        // on below line adding on click listener for our message image view.
        messageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to send message
                sendMessage(phoneNumber);
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
}