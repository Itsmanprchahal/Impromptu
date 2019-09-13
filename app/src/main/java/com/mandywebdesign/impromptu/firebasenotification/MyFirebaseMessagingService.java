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
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mandywebdesign.impromptu.R;
import com.mandywebdesign.impromptu.ui.Home_Screen;
import com.mandywebdesign.impromptu.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    JSONObject jsonObject;
    String title,message,click_action,invoiceId;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String data = remoteMessage.getData().get("message");

        try {
            jsonObject = new JSONObject(data);
            title = jsonObject.getString("title");
            message = jsonObject.getString("body");
            click_action = jsonObject.getString("click_action");
            invoiceId = jsonObject.get("invoice_id").toString();
            Log.d("++++++++++data", jsonObject.get("title").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("data+++++++", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        //In case when notification was send in "notification" parameter we need to check wheather data is null or not.

        if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            Log.d("data++++++++++", "" + remoteMessage.getData());
            Log.d("data++++++++++", "" + data);


            sendNotification(remoteMessage.getData());


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(Map<String, String> data) {


        SharedPreferences preferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE);
        String userToken = preferences.getString("Usertoken", "");
        String socailTOken = preferences.getString("Socailtoken", "");
        Intent intent = null;

        if (!userToken.equalsIgnoreCase("")) {
            intent = new Intent(this, Home_Screen.class);
            intent.putExtra("type", data.get("type"));
        } else if (!socailTOken.equalsIgnoreCase("")) {
            intent = new Intent(this, Home_Screen.class);
            intent.putExtra("type", data.get("type"));
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "121";
        CharSequence channelName = "Impromptu";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(getResources().getColor(R.color.colorTheme));
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(notificationChannel);

         intent = new Intent(click_action);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Inid", invoiceId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        notificationManager.notify(0, notificationBuilder.build());
    }
}