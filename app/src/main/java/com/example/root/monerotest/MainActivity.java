package com.example.root.monerotest;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.monerotest.MenuFragments.SendFragment;
import com.example.root.monerotest.MenuFragments.SettingsFragment;
import com.example.root.monerotest.MenuFragments.SignFragment;


public class MainActivity extends AppCompatActivity  {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DashboardFragment fragment = DashboardFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);



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
                }
                mDrawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });

        String extStore = System.getenv("EXTERNAL_STORAGE");


        boolean connected = InitWallet(extStore);

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */


    public native boolean InitWallet(String path);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_drop_menu){

            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drop_menu, menu);
        return true;
    }

}
