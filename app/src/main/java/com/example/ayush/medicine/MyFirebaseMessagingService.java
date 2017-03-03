package com.example.ayush.medicine;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Ayush on 11/21/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    static final String TAG = "abc";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG,"From" + remoteMessage.getFrom());
        Log.i(TAG,"Notification Mesage Body" + remoteMessage.getNotification().getBody());
        //showNotification(remoteMessage.getNotification().getBody());
    }
}
