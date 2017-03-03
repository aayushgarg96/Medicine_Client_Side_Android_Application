package com.example.ayush.medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Ayush on 12/19/2016.
 */
public class UpdatePassword extends AsyncTask<String, Void, String> {

    String newpassword, mobile, fresult;
    ProgressDialog pg;
    Context C;

    public UpdatePassword(final Context C){this.C = C;}

    @Override
    protected String doInBackground(String... params) {
        newpassword = params[0];
        mobile = params[1];
        try {
            URL url = new URL("http://minorprojectf5.esy.es/updatePassword.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            //Exception thrown if no data available for reading in the set time.
            conn.setConnectTimeout(15000);
            //Exception thrown if no connection is made by the web server.
            conn.setRequestMethod("POST");
            //sets the request method for the url request.
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream outstream = conn.getOutputStream();
            //Opening a stream with an intention of writing data to server.
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream, "UTF-8"));
            //BufferedWriter provides a memory buffer and improves the speed.
            //OutputStreamWriter encodes the character written to it to bytes. Encoded data takes less space.
            StringBuilder hashString = new StringBuilder();
            hashString.append(URLEncoder.encode("password", "UTF-8"));
            hashString.append("=");
            hashString.append(URLEncoder.encode(newpassword, "UTF-8"));
            hashString.append("&");
            hashString.append(URLEncoder.encode("mobile", "UTF-8"));
            hashString.append("=");
            hashString.append(URLEncoder.encode(mobile, "UTF-8"));
            writer.write(String.valueOf(hashString));
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
            reader.close();
            instream.close();
            conn.disconnect();
        }catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fresult;
    }

    @Override
    protected void onPreExecute() {
        pg = new ProgressDialog(C);
        pg.setTitle("Signing Up");
        pg.setMessage("Please Wait...");
        pg.setCancelable(false);
        pg.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pg.dismiss();
        if(s.equals("YES")){
            Toast.makeText(C, "Password Changed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(C, "Try Again", Toast.LENGTH_SHORT).show();
        }
    }
}
