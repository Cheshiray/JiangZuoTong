package com.example.jiangzuotong;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notice = new Intent(context, MyCollection.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,2,notice,0);
        NotificationManager manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            String id = "channel_1";
            String description = "讲座通知";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(context, id)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("记得来看讲座通哦！")
                    .setContentText("Stay foolish, stay hungry.")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            manager.notify(1, notification);
            Toast.makeText(context, "通知已发送", Toast.LENGTH_SHORT).show();
        } else {
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle("记得来看讲座通哦！")
                    .setContentText("Stay foolish, stay hungry.")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            manager.notify(1,notification);
            Toast.makeText(context, "通知已发送", Toast.LENGTH_SHORT).show();
        }
    }
}