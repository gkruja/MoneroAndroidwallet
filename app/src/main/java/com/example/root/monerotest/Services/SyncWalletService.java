package com.example.root.monerotest.Services;


import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.root.monerotest.Utils.NotificationUtils;


public class SyncWalletService extends Service {

    static {
        System.loadLibrary("native-lib");
    }

    private Notification mNotification;

    @Override
    public void onCreate() {
        super.onCreate();

        String extStore = System.getenv("EXTERNAL_STORAGE");
        InitWallet(extStore);

        mNotification = NotificationUtils.getSyncWalletNotification(getApplicationContext());
        Toast.makeText(this, String.valueOf(mNotification.when), Toast.LENGTH_SHORT).show();
        startForeground(0x101, mNotification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new SyncServiceBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        Toast.makeText(this, String.valueOf(mNotification.when), Toast.LENGTH_SHORT).show();
    }

    /**
     * JNI CALLS
     */
    public native boolean InitWallet(String path);

    /**
     * Binder to hook client-server  between main activity and service.
     */
    public class SyncServiceBinder extends Binder {
        public SyncWalletService getService(){
            return SyncWalletService.this;
        }
    }
}
