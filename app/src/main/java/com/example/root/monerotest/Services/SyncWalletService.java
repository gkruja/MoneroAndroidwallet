package com.example.root.monerotest.Services;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.root.monerotest.MainActivity;
import com.example.root.monerotest.R;
import com.example.root.monerotest.Utils.NotificationUtils;

import java.io.File;


public class SyncWalletService extends Service {

    static {
        System.loadLibrary("native-lib");
    }

    private Notification mNotification;
    private Callbacks mCallbacks;
    private Thread mThread;

    @Override
    public void onCreate() {
        super.onCreate();

        initWallet();
    }

    public void initWallet(){
        //String extStore = System.getenv("EXTERNAL_STORAGE");
        File externalStorage = Environment.getExternalStorageDirectory();
        boolean success = InitWallet(externalStorage.getAbsolutePath());



        if(success){
            Toast.makeText(this, "asdasdadsasda", Toast.LENGTH_SHORT).show();
            //checkHeight();
        }else{

            MainActivity x;
            if(getApplicationContext() instanceof MainActivity){
                //TODO: fix change toast for alertdialog.
                x = (MainActivity) getApplicationContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(x);
                builder.setMessage(getString(R.string.alert_dialog_no_connection_to_daemon));
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }else{
                Toast.makeText(this, "CANT CONNECT OT THE FKING DAMEON", Toast.LENGTH_SHORT).show();
                stopSelf();
            }

        }
    }

    public void checkHeight(){

        int walletHeight = 0;
        int daemonHeigh = 0;

        walletHeight = WalletHeight();
        daemonHeigh = DaemonHeight();

        if(daemonHeigh == 0)
            return;

        if(mCallbacks == null)
            return;


        if(walletHeight < daemonHeigh){
            // change the toolbar view to sync one.
            mCallbacks.setViewActionBar();
            //Update progress bar
            mCallbacks.updateProgressBar(walletHeight, daemonHeigh);
            syncWalletToDaemon();
        }
    }

    public void syncWalletToDaemon(){
        mNotification = NotificationUtils.getSyncWalletNotification(getApplicationContext());
        Toast.makeText(this, String.valueOf(mNotification.when), Toast.LENGTH_SHORT).show();
        startForeground(0x101, mNotification);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                WalletRefresh();
            }
        };

        mThread = new Thread(runnable);
        mThread.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new SyncServiceBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        if(mThread != null)
            mThread.interrupt();
    }

    public void registerClient(Activity activity){
        mCallbacks = (Callbacks)activity;
    }

    /**
     * JNI CALLS
     */
    public native boolean InitWallet(String path);
    public native int WalletHeight();
    public native int DaemonHeight();
    public native void WalletRefresh();


    /**
     * Interface to communicate Service-Client.
     */

    public interface Callbacks{
        void setViewActionBar();
        void updateProgressBar(int current, int max);

    }
    /**
     * Binder to hook client-server  between main activity and service.
     */
    public class SyncServiceBinder extends Binder {
        public SyncWalletService getService(){
            return SyncWalletService.this;
        }
    }
}
