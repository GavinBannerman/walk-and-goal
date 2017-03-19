package site.gbdev.walkandgoal.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.models.Units;

/**
 * Created by gavin on 13/02/2017.
 */
public class PreferencesActivity extends AppCompatActivity {

    SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
            SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {
                    Units.setStepMapping(Double.valueOf(sharedPreferences.getString("pref_step_mapping", "0.762")));
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new PreferencesFragment()).commit();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(spChanged);
    }
}