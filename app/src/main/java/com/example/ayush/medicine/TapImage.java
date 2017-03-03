package com.example.ayush.medicine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class TapImage extends AppCompatActivity implements View.OnClickListener{

    ImageView iv;
    TextView tv2;
    Bitmap photo;
    int x,y;
    FrameLayout layout;
    byte[] byteArray;
    public static String stringImage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_image);
        boolean network = isNetworkAvailable();
        if(network==false){
            Toast.makeText(TapImage.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        x=0; y=0;
        layout = (FrameLayout) findViewById(R.id.framelayout_select);
        tv2 = (TextView) findViewById(R.id.textView20);
        iv = (ImageView) findViewById(R.id.imageView9);
        tv2.setOnClickListener(this);
        Thread thread = new Thread(){
            public void run(){
                stringImage2 = GalleryImage.stringImage;
                byteArray = Base64.decode(stringImage2, Base64.DEFAULT);
                photo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv.setImageBitmap(photo);
                    }
                });
            }
        };
        thread.start();

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                FrameLayout mFrame = new FrameLayout(TapImage.this);
                if(x!=0 && y!=0)
                {
                    FrameLayout.LayoutParams mParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    mFrame.setLayoutParams(mParams);
                    mFrame.setPadding(x, y, 0, 0);
                    ImageView iv2 = new ImageView(TapImage.this);
                    iv2.setLayoutParams(mParams);
                    iv2.setImageDrawable(getResources().getDrawable(
                            R.drawable.arrow));
                    mFrame.addView(iv2);
                    layout.addView(mFrame);
                }
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.textView20:{
                layout.setDrawingCacheEnabled(true);
                layout.buildDrawingCache();
                photo = layout.getDrawingCache();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, out);
                byte[] byteArray = out.toByteArray();
                stringImage2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Intent intent = new Intent(TapImage.this, Quantity.class);
                intent.putExtra("Key2", "Few");
                startActivity(intent);
                break;
            }
        }
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