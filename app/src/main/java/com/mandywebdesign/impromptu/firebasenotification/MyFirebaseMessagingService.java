package com.mandywebdesign.impromptu.firebasenotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.MainActivity;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData()!=null){
            sendNotification(remoteMessage.getData());
        }
    }
    private void sendNotification(Map<String, String> data) {
       SharedPreferences preferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
       String userToken = preferences.getString("Usertoken", "");
       String socailTOken = preferences.getString("Socailtoken","");
       Intent intent=null;

        if (!userToken.equalsIgnoreCase("")) {
             intent = new Intent(this, Home_Screen.class);
             intent.putExtra("type",data.get("type"));
        }else if (!socailTOken.equalsIgnoreCase(""))
        {
            intent = new Intent(this, Home_Screen.class);
            intent.putExtra("type",data.get("type"));
        }else {
             intent = new Intent(this, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo)
//                        .setContentTitle(data.get("title").toString())
//                        .setContentText(data.get("body").toString())
                        .setAutoCancel(false)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Impromptu",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Random r = new Random();
        int i1 = r.nextInt(1000 - 1) +1;
        notificationManager.notify(i1, notificationBuilder.build());
    }
}