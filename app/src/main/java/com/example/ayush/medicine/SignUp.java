package com.example.ayush.medicine;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    Button btn1;
    EditText et1, et2, et3, et4;
    TextView tv1, tv2;
    private String name, email, mobile, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.setTitle("Sign Up");
        boolean network = isNetworkAvailable();
        if(network==false){
            Toast.makeText(SignUp.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn1 = (Button) findViewById(R.id.button5);
        et1 = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);
        et3 = (EditText) findViewById(R.id.editText3);
        et4 = (EditText) findViewById(R.id.editText4);
        tv1 = (TextView) findViewById(R.id.textView6);
        tv2 = (TextView) findViewById(R.id.textView7);
        btn1.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button5:{

                name = et1.getText().toString();
                email = et2.getText().toString();
                mobile = et3.getText().toString();
                password = et4.getText().toString();

                if(name.isEmpty())
                    Toast.makeText(SignUp.this, "Please enter your Name:", Toast.LENGTH_SHORT).show();
                else if(email.isEmpty())
                    Toast.makeText(SignUp.this, "Please enter you Email", Toast.LENGTH_SHORT).show();
                else if(mobile.isEmpty())
                    Toast.makeText(SignUp.this, "Please enter your Mobile", Toast.LENGTH_SHORT).show();
                else if(password.isEmpty())
                    Toast.makeText(SignUp.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                else
                {
                    boolean out = isValidEmail(email);
                    if(out==false)
                        Toast.makeText(SignUp.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    else{
                        if(mobile.length()!=10){
                            Toast.makeText(getApplicationContext(),"Invalid Mobile Number",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(password.length()<8 || password.length()>15){
                                Toast.makeText(getApplicationContext(),"Password should between 8-15 characters",Toast.LENGTH_SHORT).show();

                            }
                            else {
                                InsertIntoTable insert = new InsertIntoTable(SignUp.this);
                                insert.execute(name, email, mobile, password);
                            }
                        }

                    }
                }
                break;
            }
            case R.id.textView6:{
                Intent i = new Intent(SignUp.this,LogIn.class);
                startActivity(i);
                finish();
                break;
            }
            case R.id.textView7:{
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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