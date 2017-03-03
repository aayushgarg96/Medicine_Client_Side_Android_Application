package com.example.ayush.medicine;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class GalleryImage extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE=1;
    static final int SELECTED_PICTURE=2;
    ImageView iv;
    TextView tv1, tv2;
    int flag;
    Bitmap photo, decoded;
    public static String stringImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_image);
        boolean network = isNetworkAvailable();
        if(network==false){
            Toast.makeText(GalleryImage.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        iv = (ImageView) findViewById(R.id.imageView8);
        tv1 = (TextView) findViewById(R.id.textView17);
        tv2 = (TextView) findViewById(R.id.textView18);

        Bundle b = getIntent().getExtras();
        String value = b.getString("Key");
        if(value.equals("Cam")){
            flag = 1;
        }
        else if(value.equals("Gal")){
            flag = 2;
        }
        switch(flag){
            case 1:{
                Intent getPic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(getPic, REQUEST_IMAGE_CAPTURE);
                break;
            }

            case 2:{
                Intent getGal = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getGal.setType("image/*");
                startActivityForResult(getGal, SELECTED_PICTURE);
                break;
            }
        }

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stringImage.isEmpty()){
                    Toast.makeText(GalleryImage.this, "NULL", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(GalleryImage.this, "FULL", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(GalleryImage.this, Quantity.class);
                intent.putExtra("Key2", "All");
                startActivity(intent);
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryImage.this, TapImage.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Thread thread = new Thread(){
            @TargetApi(Build.VERSION_CODES.KITKAT)
            public void run(){
                switch(requestCode) {
                    case REQUEST_IMAGE_CAPTURE:{
                        if(resultCode==RESULT_OK && data!=null){
                            photo = (Bitmap) data.getExtras().get("data");
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                            //Log.i("OUTPUT3", String.valueOf(decoded.getAllocationByteCount()));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv.setImageBitmap(decoded);
                                }
                            });
                            ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                            decoded.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                            byte[] byteArray = out2.toByteArray();
                            //Log.i("OUTPUT4", String.valueOf(byteArray.length));
                            stringImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        }
                        else {
                            finish();
                        }
                        break;
                    }

                    case SELECTED_PICTURE:{
                        if(resultCode == RESULT_OK && data!=null){
                            Uri pickedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePath[0]);
                            String picturePath = cursor.getString(columnIndex);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            photo = BitmapFactory.decodeFile(picturePath, options);
                            //Log.i("OUTPUT2", String.valueOf(photo.getAllocationByteCount()));
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                            //Log.i("OUTPUT3", String.valueOf(decoded.getAllocationByteCount()));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv.setImageBitmap(decoded);
                                }
                            });
                            ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                            decoded.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                            byte[] byteArray = out2.toByteArray();
                            //Log.i("OUTPUT4", String.valueOf(byteArray.length));
                            stringImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            //Log.i("OUTPUT5", String.valueOf(stringImage.length()));
                        }
                        else{
                            finish();
                        }
                        break;
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }
}