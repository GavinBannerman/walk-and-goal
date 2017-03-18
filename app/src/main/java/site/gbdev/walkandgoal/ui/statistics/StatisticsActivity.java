package site.gbdev.walkandgoal.ui.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.db.FitnessDbWrapper;
import site.gbdev.walkandgoal.models.Goal;
import site.gbdev.walkandgoal.models.HistoricGoal;
import site.gbdev.walkandgoal.models.Units;

/**
 * Created by gavin on 18/03/2017.
 */

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String goalNameStr = getIntent().getStringExtra("name");
        setTitle(goalNameStr);
        Date fromDate = (Date) getIntent().getSerializableExtra("fromDate");
        Date toDate = (Date) getIntent().getSerializableExtra("toDate");
        Units.Unit units = (Units.Unit) getIntent().getSerializableExtra("units");

        TextView goalName = (TextView) findViewById(R.id.goal_name);
        TextView goalEntries = (TextView) findViewById(R.id.goal_entries);
        TextView avgGoalDistance = (TextView) findViewById(R.id.statistics_avg_goal_distance_val);
        TextView minGoal = (TextView) findViewById(R.id.statistics_min_goal_val);
        TextView avgGoal = (TextView) findViewById(R.id.statistics_avg_goal_val);
        TextView maxGoal = (TextView) findViewById(R.id.statistics_max_goal_val);

        goalName.setText(goalNameStr);

        List<Goal> goals = FitnessDbWrapper.getAllFinishedGoalsWithName(goalNameStr, units, fromDate, toDate, this);
        List<Double> progress = FitnessDbWrapper.getActivityForHistory(units, this, goals);

        List<HistoricGoal> historicGoals = new ArrayList<>();


        double goalTotal = 0;
        for (int i = 0; i < goals.size(); i++){
            goalTotal = goalTotal + goals.get(i).getUnitDistance();
            historicGoals.add(new HistoricGoal(goals.get(i), progress.get(i)));
        }

        int minCompletion = Integer.MAX_VALUE;
        int maxCompletion = Integer.MIN_VALUE;
        int totalCompletion = 0;
        for (HistoricGoal historicGoal : historicGoals){
            totalCompletion = totalCompletion + historicGoal.getPercentageCompleted();

            if (historicGoal.getPercentageCompleted() < minCompletion){
                minCompletion = historicGoal.getPercentageCompleted();
            }
            if (historicGoal.getPercentageCompleted() > maxCompletion){
                maxCompletion = historicGoal.getPercentageCompleted();
            }
        }


        DecimalFormat format = new DecimalFormat("0.#");
        avgGoalDistance.setText(format.format(goalTotal/((double) goals.size())) + " " + Units.getUNITS()[goals.get(0).getUnit()].getName());

        goalEntries.setText("Entries: " + goals.size());

        minGoal.setText(minCompletion + "%");
        maxGoal.setText(maxCompletion + "%");
        avgGoal.setText(totalCompletion/historicGoals.size() + "%");
    }


}
