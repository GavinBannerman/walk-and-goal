package site.gbdev.walkandgoal.ui;

import android.app.Activity;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.db.FitnessDbWrapper;
import site.gbdev.walkandgoal.models.Units;

public class AddProgressActivity extends AppCompatActivity {

    TextInputEditText distance;
    Spinner units;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        units = (Spinner) findViewById(R.id.spinner_units);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        units.setAdapter(adapter);

        distance = (TextInputEditText) findViewById(R.id.input_distance);
        addButton = (Button) findViewById(R.id.button_add);

        final Activity activity = this;

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int unitID = Units.getIdFromString(units.getSelectedItem().toString());

                double distanceValue = Units.convertToSteps(Double.valueOf(distance.getText().toString()), unitID);
                FitnessDbWrapper.addActivity(distanceValue, null, activity);
                finish();
            }
        });
    }
}
