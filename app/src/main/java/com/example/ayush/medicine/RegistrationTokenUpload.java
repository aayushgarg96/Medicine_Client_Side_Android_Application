package com.example.ayush.medicine;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Ayush on 12/4/2016.
 */
public class RegistrationTokenUpload extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String token = params[0];
        String username = homepage.mobile;
        try {
            URL url = new URL("");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            StringBuilder hashString = new StringBuilder();
            hashString.append(URLEncoder.encode("token", "UTF-8"));
            hashString.append("=");
            hashString.append(URLEncoder.encode(token, "UTF-8"));
            hashString.append("&");
            hashString.append(URLEncoder.encode("username", "UTF-8"));
            hashString.append("=");
            hashString.append(URLEncoder.encode(username, "UTF-8"));
            OutputStream outstream = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream,"UTF-8"));
            writer.write(hashString.toString());
            writer.flush();
            writer.close();
            outstream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
