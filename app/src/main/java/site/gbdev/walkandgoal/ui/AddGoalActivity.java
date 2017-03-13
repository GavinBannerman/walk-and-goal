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
import site.gbdev.walkandgoal.models.Units;

/**
 * Created by gavin on 12/02/2017.
 */

public class AddGoalActivity extends AppCompatActivity {

    TextInputEditText name, distance;
    Spinner units;
    Button addButton;
    Goal goal;
    boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        units = (Spinner) findViewById(R.id.spinner_units);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        units.setAdapter(adapter);

        name = (TextInputEditText) findViewById(R.id.input_name);
        distance = (TextInputEditText) findViewById(R.id.input_distance);
        addButton = (Button) findViewById(R.id.button_add);

        if (getIntent().hasExtra("goal")){
            edit = true;
            setTitle("Edit Goal");
            addButton.setText("Save");
            goal = (Goal) getIntent().getSerializableExtra("goal");
            name.setText(goal.getName());
            distance.setText(String.valueOf(Units.convertFromSteps(goal.getDistance(), goal.getUnit())));
            units.setSelection(goal.getUnit());
        }

        final Activity activity = this;

        if (edit){

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int unitID = Units.getIdFromString(units.getSelectedItem().toString());

                    double distanceValue = Units.convertToSteps(Double.valueOf(distance.getText().toString()), unitID);

                    goal.setName(name.getText().toString());
                    goal.setDistance(distanceValue);
                    goal.setUnit(unitID);

                    FitnessDbWrapper.updateGoal(goal, activity);
                    finish();
                }
            });

        } else {

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
}
