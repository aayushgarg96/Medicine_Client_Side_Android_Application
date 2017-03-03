package com.example.ayush.medicine;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GetImage extends AppCompatActivity implements View.OnClickListener{

    ImageView iv;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);
        boolean network = isNetworkAvailable();
        if(network == false){
            Toast.makeText(GetImage.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        iv = (ImageView) findViewById(R.id.imageView5);
        ll = (LinearLayout) findViewById(R.id.linear_gal);
        iv.setOnClickListener(this);
        ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.imageView5:{
                Intent i = new Intent(GetImage.this, GalleryImage.class);
                i.putExtra("Key","Cam");
                this.startActivity(i);
                break;
            }

            case R.id.linear_gal: {
                Intent i = new Intent(GetImage.this, GalleryImage.class);
                i.putExtra("Key","Gal");
                this.startActivity(i);
                break;
            }
        }
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