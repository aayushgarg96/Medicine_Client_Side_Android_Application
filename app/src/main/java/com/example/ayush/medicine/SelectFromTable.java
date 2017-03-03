package com.example.ayush.medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ayush on 9/29/2016.
 */
public class SelectFromTable extends AsyncTask<String,Void,String>{

    Context C;
    public String fresult, username, name, email, mobile, password;
    ProgressDialog pg;

    public SelectFromTable(Context C){
        this.C = C;
    }

    @Override
    protected String doInBackground(String... params) {
        HashMap<String,String> hm = new HashMap<String, String>();
        hm.put("username",params[0]);
        hm.put("password",params[1]);
        username = params[0];
        try {
            URL url = new URL("http://minorprojectf5.esy.es/Login.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream outstream = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream,"UTF-8"));
            writer.write(getPostDataString(hm));
            writer.flush();
            writer.close();
            outstream.close();

            InputStream instream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "iso-8859-1"));
            StringBuilder result = new StringBuilder();
            String line = null;

            while((line = reader.readLine())!=null){
                result.append(line+"\n");
            }
            fresult = result.toString();
            JSONObject root = new JSONObject(fresult);
            JSONArray response = root.getJSONArray("result");
            JSONObject finaljsonobject = response.getJSONObject(0);
            fresult = finaljsonobject.getString("output");
            email = finaljsonobject.getString("email");
            name = finaljsonobject.getString("name");
            mobile = finaljsonobject.getString("mobile");
            password = finaljsonobject.getString("password");
            reader.close();
            instream.close();
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fresult;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pg = new ProgressDialog(C);
        pg.setTitle("Logging In");
        pg.setMessage("Please Wait...");
        pg.setCancelable(false);
        pg.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pg.dismiss();
        if(s.equals("YES")) {
            Toast.makeText(C, "Login Successful.", Toast.LENGTH_SHORT).show();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(C);
            SharedPreferences.Editor ET = sp.edit();
            ET.putString("sp_status",username);
            ET.putString("sp_name",name);
            ET.putString("sp_mobile",mobile);
            ET.putString("sp_email",email);
            ET.putString("sp_password",password);
            ET.commit();
            Intent I_Go = new Intent(C, homepage.class);
            C.startActivity(I_Go);
            ((LogIn)(C)).finish();
        }
        else{
            Toast.makeText(C, "Login Failed.", Toast.LENGTH_SHORT).show();
        }
    }

    public String getPostDataString(HashMap<String, String> hm) throws UnsupportedEncodingException {
        StringBuilder hashString = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> point : hm.entrySet()){
            if(first)
                first = false;
            else
                hashString.append("&");
            hashString.append(URLEncoder.encode(point.getKey(), "UTF-8"));
            hashString.append("=");
            hashString.append(URLEncoder.encode(point.getValue(), "UTF-8"));
        }
        return hashString.toString();
    }
}
