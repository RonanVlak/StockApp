package com.example.eindopdracht;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("switch_settings_key")) {
            boolean darkModeEnabled = sharedPreferences.getBoolean(key, false);
            if (darkModeEnabled) {
                // Set dark mode theme
                getActivity().setTheme(R.style.AppThemeDark);
            } else {
                // Set light mode theme
                getActivity().setTheme(R.style.AppThemeLight);
            }
            // Recreate activity to apply the new theme
            getActivity().recreate();
        }
    }
}


