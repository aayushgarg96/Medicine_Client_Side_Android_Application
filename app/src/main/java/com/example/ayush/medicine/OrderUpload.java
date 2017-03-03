package com.example.ayush.medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ayush on 11/22/2016.
 */
public class OrderUpload extends AsyncTask<String,Void,String> {

    Context Con;
    String address, city, date, time, description, stringImage, shopName, custName, shopMobile, custMobile, fresult;
    ProgressDialog pg;

    public OrderUpload(Context C) {
        this.Con = C;
    }

    @Override
    protected String doInBackground(String... params) {

        HashMap<String,String> hm = new HashMap<String, String>();
        hm.put("stringImage",params[0]);
        hm.put("description",params[1]);
        hm.put("custName",params[2]);
        hm.put("custMobile",params[3]);
        hm.put("shopName",params[4]);
        hm.put("shopMobile",params[5]);
        hm.put("address",params[6]);
        hm.put("city",params[7]);
        hm.put("date",params[8]);
        hm.put("time",params[9]);

        stringImage = params[0];
        description = params[1];
        custName = params[2];
        custMobile = params[3];
        shopName = params[4];
        shopMobile = params[5];
        address = params[6];
        city = params[7];
        date = params[8];
        time = params[9];

        HttpURLConnection connection2;

        try {
            URL url2 = new URL("http://minorprojectf5.esy.es/imageUpload.php");
            connection2 = (HttpURLConnection) url2.openConnection();
            connection2.setConnectTimeout(15000);
            connection2.setReadTimeout(15000);
            connection2.setDoOutput(true);
            connection2.setDoInput(true);
            connection2.setRequestMethod("POST");
            OutputStream output = connection2.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
            writer.write(getPostDataString(hm));
            writer.flush();
            writer.close();
            output.close();

            InputStream input = connection2.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "iso-8859-1"));
            StringBuilder result = new StringBuilder();
            String line = null;
            while((line = reader.readLine())!=null){
                result.append(line+"\n");
            }
            fresult = result.toString();
            Log.i("OUTPUT", fresult);
            JSONObject root = new JSONObject(fresult);
            JSONArray response = root.getJSONArray("result");
            JSONObject finaljsonobject = response.getJSONObject(0);
            fresult = finaljsonobject.getString("output");
            reader.close();
            input.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
        pg = new ProgressDialog(Con);
        pg.setTitle("Loading");
        pg.setMessage("Please Wait...");
        pg.setCancelable(false);
        pg.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pg.dismiss();
        Toast.makeText(Con, ""+s, Toast.LENGTH_SHORT).show();
        if(s.equals("YES")){
            Toast.makeText(Con, "Order Placed successfully", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(Con, "Order Not Placed", Toast.LENGTH_SHORT).show();
        }
    }

    public String getPostDataString(HashMap<String,String> hm) throws UnsupportedEncodingException {
        StringBuilder hashString = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String,String> point : hm.entrySet()){
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