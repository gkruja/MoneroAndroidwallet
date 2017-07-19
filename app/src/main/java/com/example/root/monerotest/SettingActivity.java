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

public class SettingActivity extends AppCompatActivity {

    public static final String PREF_FILE = "user_pref.dat";
    public static final String EXTRA_STATE = "EXTRA_STATE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mainActivity = null;
        SharedPreferences pref = getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);

        // if state != 0 then IP:Host saved, use that to launch main activity.
        if(pref.getInt(EXTRA_STATE, 0) != 0){
             mainActivity = new Intent(this, MainActivity.class);
        }

        if(getIntent() != null && getIntent().getIntExtra(EXTRA_STATE, 0) == 0){
            //TODO: open settings normally.
            if(mainActivity != null)
                startActivity(mainActivity);
        }


        setContentView(R.layout.setting_activity);

        //Task for IP:PORT

        //Validate input

        //confirm save with action bar icon.

        //Verify again input

        //If ip:port valid, start main activity using IP:PORT. to start main activity.

        //Ask for a valid node and port.


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save_settings){

            //Launch main activity since it wasn't opened by main acitvity itself.
            if(getIntent() != null && getIntent().getIntExtra(EXTRA_STATE, 0) == 0){
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                return true;
            }

            //Close settings and show activity in the back stack. (previous one)
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        // change color for icon 0
        Drawable yourdrawable = menu.getItem(0).getIcon(); // change 0 with 1,2 ...
        yourdrawable.mutate();
        yourdrawable.setColorFilter(getResources().getColor(R.color.menu_font_color), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }
}
