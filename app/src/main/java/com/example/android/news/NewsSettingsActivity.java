package com.example.android.news;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;



public class NewsSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

    }
    public static class NewsSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            Preference orderBy=findPreference(getString(R.string.ListPreferenceKey));
            bindPreferenceValueToSummary(orderBy);

            Preference section=findPreference(getString(R.string.sectionNameKey));
            bindPreferenceValueToSummary(section);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String value=o.toString();
            if(preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int indexInListPreference = listPreference.findIndexOfValue(value);
                if (indexInListPreference>=0){
                    CharSequence[] titles=listPreference.getEntries();
                    preference.setSummary(titles[indexInListPreference]);
                }
            }
            else {
                preference.setSummary(value);
            }
            return true;
        }
        private void bindPreferenceValueToSummary(Preference preference){

            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String summarystring=sharedPreferences.getString(preference.getKey(),"");
            onPreferenceChange(preference,summarystring);
        }
    }
}
