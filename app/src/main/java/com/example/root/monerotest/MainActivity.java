package com.example.root.monerotest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.monerotest.MenuFragments.ReceiveFragment;
import com.example.root.monerotest.MenuFragments.SendFragment;
import com.example.root.monerotest.MenuFragments.SettingsFragment;
import com.example.root.monerotest.MenuFragments.SignFragment;
import com.example.root.monerotest.QRGenerator.QRGeneratorActivity;
import com.example.root.monerotest.QRReader.QRReaderActivity;
import com.example.root.monerotest.Services.SyncWalletService;


public class MainActivity extends AppCompatActivity implements ServiceConnection, SyncWalletService.Callbacks {

    // Used to load the 'native-lib' library on application startup.


    private boolean mBound;
    private SyncWalletService mService;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    private ProgressBar mSyncProgressBar;
    private TextView mHeightValue;

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mHandler = new Handler();

        //Launch dashboard intent.
        DashboardFragment fragment = DashboardFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        //Set up left menu listeners to items.
        setNavigationDrawerLayoutListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBound = false;
        Intent serviceIntent = new Intent(this, SyncWalletService.class);
        bindService(serviceIntent, this, BIND_AUTO_CREATE);

    }
    @Override
    protected void onStop() {
        super.onStop();
//        unbindService(this);
//        mBound = false;
    }


//    private BroadcastReceiver mBroadcast = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if(intent.getAction().equals(SyncWalletService.ACTION_SYNC_DONE)){
//
//                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                View customActionBar = inflater.inflate(R.layout.ab_main, null);
//
//                setCustomActionBar(customActionBar);
//            }
//        }
//    };

    /**
     * Service Connection's required methods.
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        SyncWalletService.SyncServiceBinder binder = (SyncWalletService.SyncServiceBinder)
                service;
        mService = binder.getService();
        mService.registerClient(this);
        mBound = true;

        mService.checkHeight();
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {
        mBound = false;
    }


    /**
     * Init and setup most UI and listeners.
     */
    private void setNavigationDrawerLayoutListener(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.item_dashboard:
                        Toast.makeText(MainActivity.this, "dashboard", Toast.LENGTH_SHORT).show();

                        DashboardFragment dashboardFragment = DashboardFragment.newInstance();
                        setTitle("Dashboard");
                        getFragmentManager().beginTransaction().replace(R.id.main_content, dashboardFragment).commit();
                        break;
                    case R.id.item_send:
                        Toast.makeText(MainActivity.this, "send", Toast.LENGTH_SHORT).show();

                        SendFragment fragmentSend = SendFragment.newInstance();
                        setTitle("Send");
                        getFragmentManager().beginTransaction().replace(R.id.main_content, fragmentSend).commit();

                        break;
                    case R.id.item_sign:
                        Toast.makeText(MainActivity.this, "sign", Toast.LENGTH_SHORT).show();

                        SignFragment fragmentSign = SignFragment.newInstance();
                        setTitle("Sign");
                        getFragmentManager().beginTransaction().replace(R.id.main_content, fragmentSign).commit();
                        break;
                    case R.id.item_settings:
                        Toast.makeText(MainActivity.this, "settings", Toast.LENGTH_SHORT).show();
                        SettingsFragment settingsFragment = SettingsFragment.newInstance();
                        setTitle("Settings");
                        getFragmentManager().beginTransaction().replace(R.id.main_content, settingsFragment).commit();
                        break;

                    case R.id.item_receive:
                        ReceiveFragment fragment = ReceiveFragment.newInstance();
                        setTitle("Receive");
                        getFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
                        break;
                }
                mDrawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });
    }

    public void setCustomActionBar(View view){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null)
            return;

        View previousCustomView = actionBar.getCustomView();

        if(previousCustomView != null){
            actionBar.setCustomView(null);
        }


        (view.findViewById(R.id.action_open_menu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: open the navigation drawer.
                if(mDrawerLayout == null)
                    return;

                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        ImageButton qrGeneratorIcon = (ImageButton) view.findViewById(R.id.action_open_qr_generator);
        if(qrGeneratorIcon != null){

            qrGeneratorIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startQRGenActivity = new Intent(MainActivity.this, QRGeneratorActivity.class);
                    //TODO: pass the data to the next activity.

                    startActivity(startQRGenActivity);
                }
            });

        }

        ImageButton qrGeneratorReader = (ImageButton) view.findViewById(R.id.action_read_qr);

        if(qrGeneratorReader != null){

            qrGeneratorReader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startQRReaderActivity = new Intent(MainActivity.this, QRReaderActivity.class);
                    startActivity(startQRReaderActivity);
                }
            });

        }

        ImageButton actionSend = (ImageButton) view.findViewById(R.id.action_send);

        if(actionSend != null){

            actionSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: handle sending action.

                }
            });

        }

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setCustomView(view);
    }

    /**
     * Callbacks so the service can report back to main activity.
     */
    @Override
    public void setViewActionBar() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customActionBar = inflater.inflate(R.layout.ab_sync_progress, null);

        //Hold reference to both views in custom action bar view.
        mSyncProgressBar = (ProgressBar) customActionBar.findViewById(R.id.progress_bar_sync);
        mHeightValue = (TextView) customActionBar.findViewById(R.id.height_value);

        //TODO: hook the post handler with progress bar.

        setCustomActionBar(customActionBar);
    }

    @Override
    public void updateProgressBar(int current, int max) {

        if(mSyncProgressBar == null)
            return;


        mSyncProgressBar.setMax(max);

        runnableUpdateBar.run();


    }

    @Override
    public void restoreProgressBar() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customActionBar = inflater.inflate(R.layout.ab_main, null);

        setCustomActionBar(customActionBar);
    }

    private Runnable  runnableUpdateBar = new  Runnable() {

         @Override
         public void run() {
             try {
                 updateBar();
             } catch (Exception ignored) {

             } finally {
                 mHandler.postDelayed(runnableUpdateBar, 1000);
             }
         }
     };


    private void updateBar(){
        if(mSyncProgressBar == null)
            return;


        mSyncProgressBar.setProgress(WalletHeight());

        mHeightValue.setText(String.valueOf(WalletHeight()) + "/" +"949815");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int WalletHeight();
    public native int DaemonHeight();
}
