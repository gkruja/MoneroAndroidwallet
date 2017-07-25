package com.example.root.monerotest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.root.monerotest.MenuFragments.SettingsFragment;

public class SettingActivity extends AppCompatActivity{





    public static final String PREF_FILE = "user_pref.dat";
    public static final String EXTRA_STATE = "EXTRA_STATE";
    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    public static final String EXTRA_IP = "EXTRA_IP";
    public static final String EXTRA_NETWORK_PREF = "EXTRA_NET_PREF";
    public static final String EXTRA_SYNC_SLEEP = "EXTRA_SYNC_SLEEP";





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = null;
        SharedPreferences pref = getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        // if state != 0 then IP:Host saved, use that to launch main activity.
        if(pref.getInt(EXTRA_STATE, 0) != 0){
             intent= new Intent(this, MainActivity.class);
        }
        if(getIntent() != null && getIntent().getIntExtra(EXTRA_STATE, 0) == 0){
            if(intent != null)
                startActivity(intent);
        }

        setContentView(R.layout.setting_activity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save_settings){

            SettingsFragment fragment = (SettingsFragment) getFragmentManager().
                                                            findFragmentById(R.id.setting_fragment);
            if(fragment == null)
                return false;

            if(fragment.isNodeAddressValid()){

                String ipPort = fragment.getIpPort();


                //Notify to app that we have a ip:port from user.
               SharedPreferences pref = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putInt(EXTRA_STATE, 1);
                edit.putString(EXTRA_IP, ipPort);
                edit.apply();

                //Launch main activity since it wasn't opened by main acitvity itself.
                if(getIntent() != null && getIntent().getIntExtra(EXTRA_STATE, 0) == 0){
                        //Launch main activity for first time.
                        Intent mainActivity = new Intent(this, MainActivity.class);
                        startActivity(mainActivity);
                        return true;
                }

                //Activity was opened from the main Activity.

                //We assume wallet2 is already loaded. since Settings was opened from MainActivity.
                //ReInitWallet(ipPort);
                finish();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);

        //This is the only way I've found to change the color of the icon in Action Bar.
        Drawable yourdrawable = menu.getItem(0).getIcon(); // change 0 with 1,2 ...
        yourdrawable.mutate();
        yourdrawable.setColorFilter(getResources().getColor(R.color.menu_font_color), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }
}
