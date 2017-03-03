package com.example.ayush.medicine;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Ayush on 11/21/2016.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService{

    static final String TAG = "abc";
    public static String token;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refreshed Token" + token);
        sendRegistrationToServer(token);
    }

    public void sendRegistrationToServer(String token) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(getApplicationContext().getString(R.string.firebase_cloud_messaging_token),token);
        ed.apply();

        RegistrationTokenUpload upload = new RegistrationTokenUpload();
        upload.execute(token);
    }
}
