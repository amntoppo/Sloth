package com.aman.sloth.Services;


import android.util.Log;

import com.aman.sloth.Common;
import com.aman.sloth.Utils.UserUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            Log.e("token", s);
            UserUtils.updateToken(this, s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> dataReceive = remoteMessage.getData();
        if(dataReceive != null) {
            Common.showNotification(this, new Random().nextInt(),
                    dataReceive.get(Common.NOTIFICATION_TITLE),
                    dataReceive.get(Common.NOTIFICATION_CONTENT),
                    null);
        }
    }
}
