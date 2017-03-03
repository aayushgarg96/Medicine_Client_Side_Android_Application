package com.example.ayush.medicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Test extends AppCompatActivity {

    private ListView lv;
    ArrayList<HashMap<String, String>> contactList;
    public static ProgressDialog pDialog;
    HttpURLConnection conn;
    private String responseDistance = null;
    AlertDialog.Builder alert;
    String shopSelect, image, shopMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        boolean network = isNetworkAvailable();
        if(network==false){
            Toast.makeText(Test.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        contactList = new ArrayList<>();
        image = Delivery.stringImage;
        if(image.isEmpty()){
            Toast.makeText(Test.this, "NULL", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(Test.this, "FULL", Toast.LENGTH_SHORT).show();
        }
        alert = new AlertDialog.Builder(Test.this);
        alert.setTitle("PLace order");
        alert.setMessage("Do you confirm your order?");
        alert.setCancelable(false);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        OrderUpload ui = new OrderUpload(Test.this);
                        ui.execute(image, Delivery.description, homepage.name, homepage.mobile, shopSelect, shopMobile, Delivery.address, Delivery.city, Delivery.dateString, Delivery.timeString);
                    }
                });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        lv = (ListView) findViewById(R.id.list);
        new GetContact().execute();
    }

    private class GetContact extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Test.this);
            pDialog.setMessage("Loading");
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall();
            if(jsonStr!=null){
                try {

                    JSONArray contacts = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String username = c.getString("username");
                        String name = c.getString("name");
                        String mobile = c.getString("mobile");
                        String price = c.getString("price");
                        String address = c.getString("address");
                        String distString = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+Delivery.address+"&destinations="+address+"&key=AIzaSyArZHf0P1S8G8ReuSWio0UBRAaNubUvYlc";
                        String distString2 = distString.replaceAll(" ","+");
                        URL distURL = new URL(distString2);
                        conn = (HttpURLConnection) distURL.openConnection();
                        conn.setRequestMethod("GET");
                        InputStream in = conn.getInputStream();
                        responseDistance = convertStreamToString(in);
                        JSONObject distObj = new JSONObject(responseDistance);
                        JSONArray distArray = distObj.getJSONArray("rows");
                        JSONObject distObj2 = distArray.getJSONObject(0);
                        JSONArray distArray2 = distObj2.getJSONArray("elements");
                        JSONObject distObj3 = distArray2.getJSONObject(0);
                        JSONObject distObj4 = distObj3.getJSONObject("distance");
                        String distance = distObj4.getString("text");
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("username", username);
                        contact.put("name", name);
                        contact.put("mobile", mobile);
                        contact.put("price", price);
                        contact.put("address", address);
                        contact.put("distance", distance);
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(
                    Test.this, contactList,
                    R.layout.list_view, new String[]{"username", "name", "mobile",
                    "address","price","distance"}, new int[]{R.id.username_retailer, R.id.name_retailer, R.id.mobile_retailer, R.id.address_retailer, R.id.min_order, R.id.distance});

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    HashMap<String, String> selected = (HashMap<String, String>) parent.getItemAtPosition(position);
                    shopSelect = selected.get("username");
                    shopMobile = selected.get("mobile");
                    AlertDialog al = alert.create();
                    al.show();
                }
            });
        }

        private String convertStreamToString(InputStream is) throws UnsupportedEncodingException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
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