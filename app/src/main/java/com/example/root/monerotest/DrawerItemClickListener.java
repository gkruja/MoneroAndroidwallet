package com.example.root.monerotest;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.root.monerotest.MenuFragments.SendFragment;
import com.example.root.monerotest.MenuFragments.SettingsFragment;
import com.example.root.monerotest.MenuFragments.SignFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Andrea Abdelnour
 * MDF III - 0517
 * Java file name:  DrawerItemClickListener
 * 6/15/17
 */

class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {


    private final Context mContext;

    private HashMap<Integer, Fragment> mContent;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    public DrawerItemClickListener(Context context, DrawerLayout drawerlayout, ListView list) {
        mContext = context;
        mDrawerLayout = drawerlayout;
        mDrawerList = list;

        init();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mContext, String.valueOf(position), Toast.LENGTH_SHORT).show();

        selectItem(position);
    }


    private void selectItem(int position){

        //Get the fragment for that position.
        Fragment fragment = mContent.get(position);

        if(fragment != null){
            ((MainActivity) mContext).getFragmentManager().beginTransaction().
                        replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }


    private void init(){
        //TODO: hold into a data object all the fragments in a map with their postions as key.

        mContent = new HashMap<>();
        DashboardFragment dashboardFragment = DashboardFragment.newInstance();

        SendFragment sendFragment = SendFragment.newInstance();
        SignFragment signFragment = SignFragment.newInstance();
        SettingsFragment settingsFragment = SettingsFragment.newInstance();

        mContent.put(0, dashboardFragment);
        mContent.put(1, sendFragment);
        mContent.put(2, signFragment);
        mContent.put(3, settingsFragment);

    }
}
