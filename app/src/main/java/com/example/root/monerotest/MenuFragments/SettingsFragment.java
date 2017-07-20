package com.example.root.monerotest.MenuFragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.monerotest.MainActivity;
import com.example.root.monerotest.R;



public class SettingsFragment extends PreferenceFragment {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }

    public boolean isNodeAddressValid(){

        EditTextPreference editText = (EditTextPreference) findPreference("editTextAddress");

        String text = editText.getText();

        if(text.isEmpty())
            return false;

        if(!text.contains(":"))
            return false;

        //TODO: more validation. Unit testing plz.


        return true;
    }

    public String getIpPort(){

        EditTextPreference editText = (EditTextPreference) findPreference("editTextAddress");
        String text = editText.getText();

        if(!text.isEmpty()){
            String ip = text.substring(0, text.lastIndexOf(":"));
            String port = text.substring(text.lastIndexOf(":")+1, text.length());
            return ip +":"+port;
        }
        return null;
    }

}
