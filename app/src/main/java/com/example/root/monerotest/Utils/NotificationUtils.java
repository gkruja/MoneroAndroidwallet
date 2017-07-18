package com.example.root.monerotest.Utils;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.root.monerotest.MainActivity;
import com.example.root.monerotest.R;


public class NotificationUtils {


    public static Notification getSyncWalletNotification(Context _context){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(_context);

        builder.setSmallIcon(R.drawable.ic_notification_sync_32_32);
        builder.setContentTitle("Syncing Monero Wallet");

        builder.setWhen(System.currentTimeMillis());
        builder.setUsesChronometer(true);

        Intent launchMainActivity = new Intent(_context, MainActivity.class);
        PendingIntent pendingLaunchActivity = PendingIntent.getActivity(_context, 0,
                                    launchMainActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingLaunchActivity);

        return builder.build();
    }
}
