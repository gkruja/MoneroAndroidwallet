package com.example.root.monerotest.MenuFragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.monerotest.MainActivity;
import com.example.root.monerotest.R;



public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        MainActivity mainActivity = (MainActivity) getActivity();
//        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View customActionBar = inflater.inflate(R.layout.ab_main, null);
//        mainActivity.setCustomActionBar(customActionBar);
//    }
}
