package edu.dartmouth.cs.myruns;

import android.os.Bundle;
import android.app.Fragment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        //basic preference fragment initialization
        addPreferencesFromResource(R.xml.preferences);

        // handling various preference screen clicks
        PreferenceScreen commentSetting = (PreferenceScreen) findPreference("comment_key");
        commentSetting.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                new CommentFragment().show(getFragmentManager(), "tag");
                return true;
            }
        });

        PreferenceScreen unitSetting = (PreferenceScreen) findPreference("unit_key");
        unitSetting.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                new UnitFragment().show(getFragmentManager(), "tag");
                return true;
            }
        });

        ((CreatedListener) getActivity()).unitUpdate();

    }

    public interface CreatedListener{
        public void unitUpdate();
    }

}