package com.example.immersioncamp1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

// HHJ
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
/// HHJ


public class MainActivity extends AppCompatActivity {

    ///HHJ
    String[][] hold = new String[7][6];
    public int TO_GRID = 0;
    String latitude, longitude;
    final ArrayList<Weather> data = new ArrayList<>();
    String finalBase_time;
    String finalDate;
    public static Context mContext;
    static final int PERMISSIONS_REQUEST = 0x0000001;
    ///HHJ

    // creating variables for our array lust, recycler view progress bar and adapter.
    private ArrayList<ContactsModal> contactsModalArrayList;
    private RecyclerView contactRV;
    private ContactRVAdapter contactRVAdapter;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // on below line we are initializing our variables.
        contactsModalArrayList = new ArrayList<>();
        contactRV = findViewById(R.id.idRVContacts);
        loadingPB = findViewById(R.id.idPBLoading);

        // calling method to prepare our recycler view.
        prepareContactRV();
        // calling a method to request permissions.
        requestPermissions();

        widget();
        // widget click event
        ListView listView = findViewById(R.id.weather_widget);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SubActivity.class); // SubActivity 호출
                startActivity(intent);
            }
        });

    }

    private void prepareContactRV() {
        // in this method we are preparing our recycler view with adapter.
        contactRVAdapter = new ContactRVAdapter(this, contactsModalArrayList);
        // on below line we are setting layout manager.
        contactRV.setLayoutManager(new LinearLayoutManager(this));
        // on below line we are setting adapter to our recycler view.
        contactRV.setAdapter(contactRVAdapter);
    }

    private void requestPermissions() {
        // below line is use to request
        // permission in the current activity.
        Dexter.withContext(this)
                // below line is use to request the number of
                // permissions which are required in our app.
                .withPermissions(Manifest.permission.READ_CONTACTS,
                        // below is the list of permissions
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.SEND_SMS, Manifest.permission.WRITE_CONTACTS)
                // after adding permissions we are
                // calling an with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // this method is called when all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            getContacts();
//                            Toast.makeText(MainActivity.this, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                        }
                        // check for permanent denial of any permission
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently,
                            // we will show user a dialog message.
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        // this method is called when user grants some
                        // permission and denies some of them.
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError -> {
                    // we are displaying a toast message for error message.
                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                })
                // below line is use to run the permissions
                // on same thread and to check the permissions
                .onSameThread().check();
    }

    // below is the shoe setting dialog
    // method which is use to display a
    // dialogue message.
    private void showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // below line is the title
        // for our alert dialog.
        builder.setTitle("Need Permissions");

        ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {

                    }
                }
        );

        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            // this method is called on click on positive
            // button and on clicking shit button we
            // are redirecting our user from our app to the
            // settings page of our app.
            dialog.cancel();
            // below is the intent from which we
            // are redirecting our user.
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityResult.launch(intent);
        });
        builder.setNegativeButton("Cancel", (dialog, i) -> {
            // this method is called when
            // user click on negative button.
            dialog.cancel();
        });
        // below line is used
        // to display our dialog
        builder.show();
    }

    public static long getRawContactId(Context context, String contactId) {
        String[] projection = new String[]{ContactsContract.RawContacts._ID};
        String selection = ContactsContract.RawContacts.CONTACT_ID + "=?";
        String[] selectionArgs = new String[]{contactId};
        Cursor c = context.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
        if (c == null) return 0;
        int rawContactId = -1;
        if (c.moveToFirst()) {
            rawContactId = c.getInt(c.getColumnIndexOrThrow(ContactsContract.RawContacts._ID));
        }
        c.close();
        return rawContactId;

    }

    private String getCompanyName(String rawContactId) {
        try {
            String orgWhere = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            String[] orgWhereParams = new String[]{rawContactId,
                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
            Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    null, orgWhere, orgWhereParams, null);
            if (cursor == null) return "";
            String name = null;
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.COMPANY));
            }
            cursor.close();
            return name;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public Uri getPhotoUri(String contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
        return contactUri;
    }

    public static Bitmap getPhoto(Context context, Uri uri, String name) {
        Log.e(null, "getPhoto started");
        ContentResolver cr = context.getContentResolver();
        Bitmap bitmap;
        try {
            InputStream photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri, true);
            bitmap = BitmapFactory.decodeStream(photo_stream);
            Log.e(null, "size is:" + uri.toString());
            return cropBitmap(bitmap);
        } catch (Exception e) {
            Log.e(null, "ppp: " + e);
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getRandomColor();
            String tex = name.length() >= 2 ? name.substring(0, 2) : name.substring(0, 1);
            TextDrawable drawable = TextDrawable.builder().beginConfig()
                    .width(100)  // width in px
                    .height(100) // height in px
                    .endConfig()
                    // as we are building a circular drawable
                    // we are calling a build round method.
                    // in that method we are passing our text and color.
                    .buildRoundRect(tex, color, 0);
            bitmap = drawableToBitmap(drawable);
            return bitmap;
        }
    }

    private static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }


        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 96; // Replaced the 1 by a 96
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 96; // Replaced the 1 by a 96

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private static Bitmap cropBitmap(Bitmap srcBmp) {
        Bitmap dstBmp;
        if (srcBmp.getWidth() >= srcBmp.getHeight()){
            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );
        }else{
            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }
        return dstBmp;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getContacts() {
        // this method is use to read contact from users device.
        // on below line we are creating a string variables for
        // our contact id and display name.
        contactsModalArrayList.clear();
        String contactId = "";
        String displayName = "";
        // on below line we are calling our content resolver for getting contacts
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        // on below line we are checking the count for our cursor.
        if (cursor.getCount() > 0) {
            // if the count if greater than 0 then we are running a loop to move our cursor to next.
            while (cursor.moveToNext()) {
                // on below line we are getting the phone number.
                    contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    int hasPhoneNumber = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                    if (hasPhoneNumber > 0) {
                        // we are checking if the hasPhoneNumber is > 0
                        // on below line we are getting our contact id and user name for that contact
                        displayName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        // on below line we are calling a content solver and making a query
                        String phoneNumber = "";
                        String organization = "";
                        String email = "";
                        Uri photoUri = null;
                        Cursor phoneCursor = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{contactId},
                                null);
                        // on below line we are moving our cursor to next position.
                        if (phoneCursor.moveToNext()) {
                            // on below line we are getting the phone number for our users and then adding the name along with phone number in array list.
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }

                        String rawContactId = String.valueOf(getRawContactId(MainActivity.this, contactId));
                        organization = getCompanyName(rawContactId);

                        Cursor emailCursor = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{contactId}, null);
                        while (emailCursor.moveToNext()) {
                            //to get the contact names
                            email = emailCursor.getString(emailCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA));
                        }

                        if (email == null || email == "") { email = "정보 없음"; }
                        if (organization == null || organization == "") { organization = "정보 없음"; }

                        photoUri = getPhotoUri(contactId);

                        Log.e(null, "wowowow: " + contactId);
                        contactsModalArrayList.add(new ContactsModal(contactId, displayName, phoneNumber, organization, email, photoUri));
                        // on below line we are closing our phone cursor.
                        phoneCursor.close();
                        emailCursor.close();
                }
            }
        }
        // on below line we are closing our cursor.
        cursor.close();
        contactsModalArrayList.add(null);
        // on below line we are hiding our progress bar and notifying our adapter class.
        loadingPB.setVisibility(View.GONE);
        contactRVAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        int checkSelfPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS);
//        if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
//            // 권한 없음
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
//        }
//        getContacts();
        requestPermissions();
    }
    
    public static void setPhotoByContactId(Context context, String contactId, Bitmap bmp) {
        Log.e(null, "HELLODDKJDNJ");
        long rawContactId = getRawContactId(context, contactId);
        Log.e(null, "22HELLODDKJDNJ");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Log.e(null, "33HELLODDKJDNJ");
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Log.e(null, "44HELLODDKJDNJ");
        byte[] byteArray = stream.toByteArray();
        Uri pictureUri = Uri.withAppendedPath(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI,
                rawContactId), ContactsContract.RawContacts.DisplayPhoto.CONTENT_DIRECTORY);
        try {
            AssetFileDescriptor afd = context.getContentResolver().openAssetFileDescriptor(pictureUri, "rw");
            OutputStream os = afd.createOutputStream();
            os.write(byteArray);
            os.close();
            afd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(null, "55glmgl");
    }

    public void widget() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한 있는지 확인 -> 하나라도 없으면 아래 실행
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 이전에 권한 요청을 거부한 경우 실행
                Toast.makeText(this, "앱 실행을 위해서는 권한을 설정해야 합니다", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST);
            }
        }
        else{
            weather_window();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "앱 실행을 위한 권한이 설정 되었습니다", Toast.LENGTH_LONG).show();
                    weather_window();
                } else {
                    Toast.makeText(this, "권한이 허용되지 않아 실행 불가", Toast.LENGTH_LONG).show();
                    moveTaskToBack(true); // 태스크를 백그라운드로 이동
                    finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기
                    System.exit(0);
                }
                break;
        }
    }


    public void weather_window() {

        System.out.println("###weather window 실행");
        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"권한이 허용되지 않아 fusedLocation 실행 불가",Toast.LENGTH_LONG).show();
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        System.out.println("###onSuccess");
                        if (location != null) {
                            MainActivity.LatXLngY tmp = convertGRID_GPS(TO_GRID, location.getLatitude(), location.getLongitude());
                            latitude = Integer.toString((int) tmp.x);
                            longitude = Integer.toString((int) tmp.y);
                        } else {
                            System.out.println("### location is null");
                        }
                        Callback();
                    }
                });
    }

    public void Callback() {
        int is_daytime;
        if(latitude==null || longitude==null){
            Toast.makeText(this,"위치 정보 가져오기 실패",Toast.LENGTH_LONG).show();
        }
        System.out.println("###Callback");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat _date = new SimpleDateFormat("yyyyMMdd");
        String date = _date.format(calendar.getTime());
        SimpleDateFormat _time = new SimpleDateFormat("HHmm");
        String time = _time.format(calendar.getTime());

        if (time.compareTo("0600") >= 0 && time.compareTo("1800") < 0) {
            is_daytime = 1;
        }else{
            is_daytime = 0;
        }
        String base_time = "0200";

        // basetime 조정
        if (time.compareTo("0000") >= 0 && time.compareTo("0211") < 0) {
            date = Integer.toString(Integer.parseInt(date) - 1);
            base_time = "2300";
        } else if (time.compareTo("0211") >= 0 && time.compareTo("0511") < 0) {
            base_time = "0200";
        } else if (time.compareTo("0511") >= 0 && time.compareTo("0811") < 0) {
            base_time = "0500";
        } else if (time.compareTo("0811") >= 0 && time.compareTo("1111") < 0) {
            base_time = "0800";
        } else if (time.compareTo("1111") >= 0 && time.compareTo("1411") < 0) {
            base_time = "1100";
        } else if (time.compareTo("1411") >= 0 && time.compareTo("1711") < 0) {
            base_time = "1400";
        } else if (time.compareTo("1711") >= 0 && time.compareTo("2011") < 0) {
            base_time = "1700";
        } else if (time.compareTo("2011") >= 0 && time.compareTo("2311") < 0) {
            base_time = "2000";
        } else {
            base_time = "2300";
        }

        finalBase_time = base_time;
        finalDate = date;
        System.out.println("### Final DATE: " + finalDate);
        System.out.println("### Final TIME: " + finalBase_time);

        fill_data(is_daytime);
        System.out.println("###FINAL DATA:" + data);

        widgetAdapter adapter = new widgetAdapter(data);
        ListView listView = findViewById(R.id.weather_widget);
        listView.setAdapter(adapter);
    }


        /* 리스트뷰 클릭 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Weather item = data.get(position);
                Toast.makeText(getApplicationContext(),
                        item.getCity()+ " " +item.getTemp()+"도" ,Toast.LENGTH_SHORT).show();
            }
        });
        */

    public void fill_data(int is_daytime){ // hold 하나만 쓰는걸로 줄이기

        WeatherData weatherdata = new WeatherData();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("###Tread 시작");
                try {
                    hold[0] = weatherdata.getWeather(latitude,longitude, finalDate, finalBase_time,"json",is_daytime); // 현재위치
                    data.add(new Weather("현재 위치", hold[0][0]+"℃", hold[0][1],hold[0][2]+"%",hold[0][3],hold[0][4],hold[0][5]));
                    hold[1] = weatherdata.getWeather("60","127", finalDate,finalBase_time,"json",is_daytime); // 서울
                    data.add(new Weather("서울", hold[1][0]+"℃", hold[1][1],hold[1][2]+"%",hold[1][3],hold[1][4],hold[1][5]));
                    hold[2] = weatherdata.getWeather("98","76", finalDate,finalBase_time,"json",is_daytime); // 부산
                    data.add(new Weather("부산", hold[2][0]+"℃", hold[2][1],hold[2][2]+"%",hold[2][3],hold[2][4],hold[2][5]));
                    hold[3] = weatherdata.getWeather("89","90",finalDate,finalBase_time,"json",is_daytime); // 대구
                    data.add(new Weather("대구", hold[3][0]+"℃", hold[3][1],hold[3][2]+"%",hold[3][3],hold[3][4],hold[3][5]));
                    hold[4] = weatherdata.getWeather("55","124",finalDate,finalBase_time,"json",is_daytime); // 인천
                    data.add(new Weather("인천", hold[4][0]+"℃", hold[4][1],hold[4][2]+"%",hold[4][3],hold[4][4],hold[4][5]));
                    hold[5] = weatherdata.getWeather("58","74",finalDate,finalBase_time,"json",is_daytime); // 광주
                    data.add(new Weather("광주", hold[5][0]+"℃", hold[5][1],hold[5][2]+"%",hold[5][3],hold[5][4],hold[5][5]));
                    hold[6] = weatherdata.getWeather("53","38",finalDate,finalBase_time,"json",is_daytime); // 제주
                    data.add(new Weather("제주", hold[6][0]+"℃", hold[6][1],hold[6][2]+"%",hold[6][3],hold[6][4],hold[6][5]));
                } catch (IOException e) {
                    System.out.println("IOE에러   " + e);
                } catch (JSONException e) {
                    System.out.println("JSON에러  " + e);
                }
            }
        });
        thread.start();
        try {
            thread.join();
            System.out.println("###Tread 끝 ");
        }catch (InterruptedException e){
            System.out.println("###동기화 오류");
        }
    }

    private LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y )
    {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //


        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        MainActivity.LatXLngY rs = new MainActivity.LatXLngY();

        if (mode == TO_GRID) {
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        }
        else {
            rs.x = lat_X;
            rs.y = lng_Y;
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            }
            else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                }
                else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }
        return rs;
    }
class LatXLngY
{
    public double lat;
    public double lng;

    public double x;
    public double y;
}
}
