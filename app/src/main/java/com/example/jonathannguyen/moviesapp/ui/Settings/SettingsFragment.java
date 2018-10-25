package com.example.jonathannguyen.moviesapp.ui.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.example.jonathannguyen.moviesapp.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences sharedPreferences;
    PreferenceScreen preferenceScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_settings);
        sharedPreferences = getPreferenceScreen().getSharedPreferences();
        preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();
        for (int i = 0;i < count;i++){
            Preference p = preferenceScreen.getPreference(i);
            if(!(p instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(p.getKey(),"");
                setPreferenceSummary(p,value);
            }
        }
    }

    private void setPreferenceSummary(Preference preference,String value){
        if(preference instanceof ListPreference){
            ListPreference listPreference =(ListPreference)preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if(prefIndex >= 0){
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if(preference != null){
            if(!(preference instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(preference.getKey(),"");
                setPreferenceSummary(preference,value);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
