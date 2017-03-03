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

public class Quantity extends AppCompatActivity implements View.OnClickListener {

    EditText et;
    TextView tv1, tv2;
    public static String order;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity);
        boolean network = isNetworkAvailable();
        if(network==false){
            Toast.makeText(Quantity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        et = (EditText) findViewById(R.id.editText8);
        tv1 = (TextView) findViewById(R.id.textView22);
        tv2 = (TextView) findViewById(R.id.textView23);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        Bundle b = getIntent().getExtras();
        key = b.getString("Key2");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.textView22:{
                finish();
                break;
            }

            case R.id.textView23:{
                order = et.getText().toString();
                Intent intent = new Intent(Quantity.this, Delivery.class);
                intent.putExtra("Key3",key);
                startActivity(intent);
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