package com.example.kargobike.firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.Objects;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    Context mContext = this;
    private static final String TAG = "MyFirebaseIIDService";
    private String loginToken = "";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // send the Instance ID token to your app server.
        registerNotificationsToken(refreshedToken, loginToken);
    }

    private void registerNotificationsToken(String notificationsToken, String loginToken){

        // Get Token from Shared Pref
        if(Objects.equals(loginToken, ""))
            return;
    }

    //update token
    public void updateToken(String token){
        loginToken = token;
        onTokenRefresh();
    }
}
