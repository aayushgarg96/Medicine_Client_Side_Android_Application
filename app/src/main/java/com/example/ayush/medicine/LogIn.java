package com.example.ayush.medicine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    Button btn1;
    EditText et1, et2;
    TextView tv1, tv2, tv3;
    public static String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Log in");
        boolean network = isNetworkAvailable();
        if(network==false){
            Toast.makeText(LogIn.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        btn1 = (Button) findViewById(R.id.button8);
        et1 = (EditText) findViewById(R.id.editText5);
        et2 = (EditText) findViewById(R.id.editText6);
        tv1 = (TextView) findViewById(R.id.textView9);
        tv2 = (TextView) findViewById(R.id.textView10);
        tv3 = (TextView) findViewById(R.id.textView15);
        btn1.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String Data = sp.getString("sp_status","No Data");
        if(!Data.equals("No Data")){
            Intent i = new Intent(LogIn.this,homepage.class);
            startActivity(i);
            i.putExtra("name", Data);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button8:{
                username = et1.getText().toString();
                password = et2.getText().toString();
                if(username.isEmpty())
                    Toast.makeText(LogIn.this, "Please enter your Email or Mobile", Toast.LENGTH_SHORT).show();
                else if(password.isEmpty())
                    Toast.makeText(LogIn.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                else{
                    SelectFromTable select = new SelectFromTable(LogIn.this);
                    select.execute(username,password);
                }
                break;
            }
            case R.id.textView9:{
                Intent i = new Intent(LogIn.this,ForgotPassword.class);
                startActivity(i);
                break;
            }
            case R.id.textView15:{
                Intent i = new Intent(LogIn.this, SignUp.class);
                startActivity(i);
                finish();
                break;
            }
            case R.id.textView10:{
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
