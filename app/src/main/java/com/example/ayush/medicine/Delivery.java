package com.example.ayush.medicine;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Delivery extends AppCompatActivity {

    EditText et1, et2, et3, et4;
    TextView tv;
    public static String address, city, dateString, timeString, description, stringImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        boolean network = isNetworkAvailable();
        if (network==false){
            Toast.makeText(Delivery.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        et1 = (EditText) findViewById(R.id.editText9);
        et2 = (EditText) findViewById(R.id.editText10);
        et3 = (EditText) findViewById(R.id.editText11);
        et4 = (EditText) findViewById(R.id.editText12);
        tv = (TextView) findViewById(R.id.textView24);

        description = Quantity.order;
        Bundle b = getIntent().getExtras();
        String key = b.getString("Key3");
        if(key.equals("All")){
            stringImage = GalleryImage.stringImage;
        }
        else{
            stringImage = TapImage.stringImage2;
        }
        if(stringImage.isEmpty()){
            Toast.makeText(Delivery.this, "NULL", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(Delivery.this, "FULL", Toast.LENGTH_SHORT).show();
        }
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            address = et1.getText().toString();
            city = et2.getText().toString();
            dateString = et3.getText().toString();
            timeString = et4.getText().toString();
            Intent intent = new Intent(Delivery.this, Test.class);
            startActivity(intent);
            }
        });
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