package com.example.ayush.medicine;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class Fragment_Previous_Order extends Fragment {

    ListView lv;
    View view;
    AlertDialog.Builder dialogBuilder;
    String digit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_previous_order, container, false);
        lv = (ListView) view.findViewById(R.id.list2);
        dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage("Have you received your order");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ReceiveOrder receiveOrder = new ReceiveOrder(getActivity());
                receiveOrder.execute(digit);
            }
        });
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        new PreviousOrder(getActivity(), lv).execute();
        return view;
    }

    public class PreviousOrder extends AsyncTask<String, Void, String> implements OnItemClickListener {

        ListView lv1;
        Activity mcontext;
        ArrayList<HashMap<String, String>> arrayList;
        ProgressDialog pg;

        public PreviousOrder(Activity a, ListView lv) {
            this.mcontext = a;
            this.lv1 = lv;
        }

        @Override
        protected String doInBackground(String... params) {
            arrayList = new ArrayList<>();
            StringBuilder sb = null;
            try {
                URL url = new URL("http://minorprojectf5.esy.es/previousOrder.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                StringBuilder hashString = new StringBuilder();
                hashString.append(URLEncoder.encode("mobile", "UTF-8"));
                hashString.append("=");
                hashString.append(URLEncoder.encode(homepage.mobile, "UTF-8"));
                OutputStream out = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(hashString.toString());
                writer.flush();
                writer.close();
                out.close();

                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
                sb = new StringBuilder();
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                reader.close();
                in.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String jsonStr = sb.toString();
            try {
                JSONArray orders = new JSONArray(jsonStr);
                for(int i=0;i<orders.length();i++){
                    JSONObject order = orders.getJSONObject(i);
                    String id = order.getString("id");
                    String imagepath = order.getString("imagepath");
                    String shopname = order.getString("shopname");
                    String shopmobile = order.getString("shopmobile");
                    String date = order.getString("date");
                    String time = order.getString("time");
                    HashMap<String, String> ord = new HashMap<>();
                    ord.put("id",id);
                    ord.put("imagepath", imagepath);
                    ord.put("shopname",shopname);
                    ord.put("shopmobile",shopmobile);
                    ord.put("date",date);
                    ord.put("time",time);
                    arrayList.add(ord);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pg = new ProgressDialog(getActivity());
            pg.setTitle("Loading");
            pg.setMessage("Please Wait...");
            pg.setCancelable(false);
            pg.show();
        }

        @Override
        protected void onPostExecute(String s) {
            pg.dismiss();
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), arrayList,
                    R.layout.order_list_view, new String[]{"id", "shopname", "shopmobile",
                    "date","time"}, new int[]{R.id.order_id, R.id.order_shopname,R.id.order_shopmobile, R.id.order_date, R.id.order_time});
            lv1.setAdapter(adapter);
            lv1.setOnItemClickListener(this);

            lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, String> select = (HashMap<String, String>) parent.getItemAtPosition(position);
                    digit = select.get("id");
                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                    return true;
                }
            });
            super.onPostExecute(s);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, String> select = (HashMap<String, String>) lv1.getItemAtPosition(position);
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.select_order_image, null);
            alertdialog.setView(dialogView);
            ImageView image = (ImageView) dialogView.findViewById(R.id.imageView7);
            byte[] decodedString = Base64.decode(select.get("imagepath"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
            AlertDialog alert = alertdialog.create();
            alert.show();
        }


    }
}