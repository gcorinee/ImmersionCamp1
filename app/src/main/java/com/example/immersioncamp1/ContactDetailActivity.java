package com.example.immersioncamp1;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.Context;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ContactDetailActivity extends AppCompatActivity {

    private final String TAG = "+ContactDetailActivity";

    // creating variables for our image view and text view and string.
    private ContactsModal modal;
    private String contactId, name, phoneNumber, organization, email;
    private EditText nameET, phoneET, emailET, organizationET;
    private ImageView contactIV;
    private LinearLayout topBar;
    private CardView callCV, messageCV;
    private BlurImageView backgroundIV;
    private static ImgPathViewModel imgPathViewModel;
    private Bitmap newPhoto;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contact_detail);
        context = this;

        modal = getIntent().getParcelableExtra("contact");
        Log.e(null, "modal: " + modal.getUserName() + "  and  " + modal.getContactId());
        contactId = modal.getContactId();
        // initializing our views.
        nameET = findViewById(R.id.idETName);
        contactIV = findViewById(R.id.idIVContact);
        phoneET = findViewById(R.id.idETPhone);
        organizationET = findViewById(R.id.idETOrganization);
        emailET = findViewById(R.id.idETEmail);
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

        phoneET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    updatePhone(phoneET.getText().toString());
                    setView();
                    Toast.makeText(ContactDetailActivity.this, "번호 수정 성공", Toast.LENGTH_SHORT).show();
                    phoneET.clearFocus();
                    hideKeyboard(phoneET);
                    return true;
                }
                return false;
            }
        });

        organizationET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    updateOrganization(organizationET.getText().toString());
                    setView();
                    Toast.makeText(ContactDetailActivity.this, "직업 수정 성공", Toast.LENGTH_SHORT).show();
                    organizationET.clearFocus();
                    hideKeyboard(organizationET);
                    return true;
                }
                return false;
            }
        });

        nameET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    updateName(nameET.getText().toString());
                    setView();
                    Toast.makeText(ContactDetailActivity.this, "이름 수정 성공", Toast.LENGTH_SHORT).show();
                    nameET.clearFocus();
                    hideKeyboard(nameET);
                    return true;
                }
                return false;
            }
        });

        emailET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    updateEmail(emailET.getText().toString());
                    setView();
                    Toast.makeText(ContactDetailActivity.this, "이메일 수정 성공", Toast.LENGTH_SHORT).show();
                    emailET.clearFocus();
                    hideKeyboard(emailET);
                    return true;
                }
                return false;
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
            newPhoto = getBitmapFromImageUri(imageUri);
            Log.e(null, "thisis: " + contactId);
            Thread t = new Thread(new SetPhoto());
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        Log.e(null, "setView started");
        name = modal.getUserName();
        phoneNumber = modal.getPhoneNumber();
        organization = modal.getOrganization();
        email = modal.getEmail();


        nameET.setText(name);
        phoneET.setText(phoneNumber);
        organizationET.setText(organization);
        emailET.setText(email);
        contactIV.setClipToOutline(true);
        Bitmap bitmap = MainActivity.getPhoto(this, modal.getPhotoUri());
        Log.e(null, "bitmap get");
        contactIV.setImageBitmap(bitmap);
        backgroundIV.setImageBitmap(bitmap);
        backgroundIV.setBlur(2);
    }


    public static ImgPathViewModel getImgPathViewModel() {
        return imgPathViewModel;
    }

    public static void setImgPathViewModel(ImgPathViewModel imgPathViewModel) {
        ContactDetailActivity.imgPathViewModel = imgPathViewModel;
    }

    class SetPhoto implements Runnable {
        public void run() {
            Log.e(null, "HELLODDKJDNJ");
            long rawContactId = MainActivity.getRawContactId(context, contactId);
            Log.e(null, "22HELLODDKJDNJ");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Log.e(null, "33HELLODDKJDNJ");
            newPhoto.compress(Bitmap.CompressFormat.PNG, 1, stream);
            Log.e(null, "44HELLODDKJDNJ");
            byte[] byteArray = stream.toByteArray();
            Uri pictureUri = Uri.withAppendedPath(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI,
                    rawContactId), ContactsContract.RawContacts.DisplayPhoto.CONTENT_DIRECTORY);
            try {
                AssetFileDescriptor afd = getContentResolver().openAssetFileDescriptor(pictureUri, "rw");
                OutputStream os = afd.createOutputStream();
                os.write(byteArray);
                os.close();
                afd.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e(null, "55glmgl");
        }
    }

    private void updatePhone (String newPhone) {
        String rawContactId = String.valueOf(MainActivity.getRawContactId(context, contactId));
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        String selectPhone = ContactsContract.Data.CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "='"  +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'" + " AND " + ContactsContract.CommonDataKinds.Phone.TYPE + "=?";
        String[] phoneArgs = new String[]{rawContactId, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)};
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(selectPhone, phoneArgs)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhone)
                .build());
        try { getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops); } catch (Exception e) { Log.e(null, e.toString()); }
        modal.setPhoneNumber(newPhone);
        Log.e(null, "SUCCEED with " + newPhone);
    }

    private void updateOrganization (String newOrganization) {
        String rawContactId = String.valueOf(MainActivity.getRawContactId(context, contactId));
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        String selection = ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?";
        String[] selectionArgs = new String[] { rawContactId, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE };
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(selection, selectionArgs)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, newOrganization)
                .build());
        try { getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops); } catch (Exception e) { Log.e(null, e.toString()); }
        modal.setOrganization(newOrganization);
        Log.e(null, "SUCCEED with " + newOrganization);
    }

    private void updateName (String newName) {
        String rawContactId = String.valueOf(MainActivity.getRawContactId(context, contactId));
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        String selection = ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?";
        String[] nameParams = new String[]{rawContactId,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
        ops.add(android.content.ContentProviderOperation.newUpdate(
                        android.provider.ContactsContract.Data.CONTENT_URI)
                .withSelection(selection, nameParams)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        newName).build());
        try { getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops); } catch (Exception e) { Log.e(null, e.toString()); }
        modal.setUserName(newName);
        Log.e(null, "SUCCEED with " + newName);
    }

    private void updateEmail (String newEmail) {
        String rawContactId = String.valueOf(MainActivity.getRawContactId(context, contactId));
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        String selection = ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?";
        String[] selectionArgs = new String[] { rawContactId, String.valueOf(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)};
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(selection, selectionArgs)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, newEmail)
                .build());
        try { getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops); } catch (Exception e) { Log.e(null, e.toString()); }
        modal.setEmail(newEmail);
        Log.e(null, "SUCCEED with " + newEmail);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}