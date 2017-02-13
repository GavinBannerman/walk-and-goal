package site.gbdev.walkandgoal.ui.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import site.gbdev.walkandgoal.R;

/**
 * Created by gavin on 13/02/2017.
 */

public class PreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}