package site.gbdev.walkandgoal.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Date;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.db.FitnessDbWrapper;
import site.gbdev.walkandgoal.models.Goal;
import site.gbdev.walkandgoal.util.Units;

/**
 * Created by gavin on 12/02/2017.
 */

public class AddGoalActivity extends AppCompatActivity {

    TextInputEditText name, distance;
    Spinner units;
    Button addButton;
    Goal goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        units = (Spinner) findViewById(R.id.spinner_units);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        units.setAdapter(adapter);

        name = (TextInputEditText) findViewById(R.id.input_name);
        distance = (TextInputEditText) findViewById(R.id.input_distance);
        addButton = (Button) findViewById(R.id.button_add);

        final Activity activity = this;

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int unitID = Units.getIdFromString(units.getSelectedItem().toString());

                double distanceValue = Units.convertToSteps(Double.valueOf(distance.getText().toString()), unitID);
                goal = new Goal(-1, name.getText().toString(), distanceValue, unitID, new Date());
                FitnessDbWrapper.addAGoal(goal, activity);
                finish();
            }
        });
    }
}
