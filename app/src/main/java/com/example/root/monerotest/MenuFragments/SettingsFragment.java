package com.example.root.monerotest.MenuFragments;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.monerotest.MainActivity;
import com.example.root.monerotest.R;
import com.example.root.monerotest.SettingActivity;

import java.util.List;


public class SettingsFragment extends PreferenceFragment implements EditTextPreference.OnPreferenceChangeListener {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        SharedPreferences pref = getActivity().getSharedPreferences(SettingActivity.PREF_FILE, Context.MODE_PRIVATE);

        //set listener.
        EditTextPreference editText = (EditTextPreference) findPreference("editTextAddress");
        if(editText != null){
            editText.setOnPreferenceChangeListener(this);
            //restore ip:port title.
            if(!pref.getString(SettingActivity.EXTRA_IP, "").isEmpty()){
                editText.setTitle(pref.getString(SettingActivity.EXTRA_IP, ""));
            }
        }

        // restore list pref.
        ListPreference list = (ListPreference) findPreference("network_list_key");
        if(list != null){
            list.setOnPreferenceChangeListener(this);
            list.setTitle(list.getEntry());
        }

        // restore switch pref.
        SwitchPreference switchPreference = (SwitchPreference) findPreference("switch_sync_night");
        if(switchPreference != null){
            boolean state = pref.getBoolean(SettingActivity.EXTRA_SYNC_SLEEP, false);
            if(state){
                //TODO: restore switch pref state.
            }

        }
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

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if(preference.getKey().equals("editTextAddress")){
            preference.setTitle(newValue.toString());
            return true;
        }

        if(preference.getKey().equals("network_list_key")){
            if(preference instanceof ListPreference){
                ListPreference pref = (ListPreference) preference;
                preference.setTitle(newValue.toString());
                if(newValue.toString().equals("Carrier Data")){
                    getActivity().getSharedPreferences(SettingActivity.PREF_FILE, Context.MODE_PRIVATE)
                            .edit().putInt(SettingActivity.EXTRA_NETWORK_PREF, 1).apply();
                }else{
                    getActivity().getSharedPreferences(SettingActivity.PREF_FILE, Context.MODE_PRIVATE)
                            .edit().putInt(SettingActivity.EXTRA_NETWORK_PREF, 0).apply();
                }

                return true;
            }
        }

        if(preference.getKey().equals("switch_sync_night")){
            if(preference instanceof SwitchPreference){
                boolean state =((SwitchPreference) preference).isChecked();

                getActivity().getSharedPreferences(SettingActivity.PREF_FILE, Context.MODE_PRIVATE)
                                    .edit().putBoolean(SettingActivity.EXTRA_SYNC_SLEEP, state).apply();

            }
        }

        return false;
    }
}
