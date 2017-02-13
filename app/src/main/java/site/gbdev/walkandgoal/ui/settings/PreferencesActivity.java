package site.gbdev.walkandgoal.ui.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import site.gbdev.walkandgoal.R;

/**
 * Created by gavin on 13/02/2017.
 */
public class PreferencesActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new PreferencesFragment()).commit();
    }
}