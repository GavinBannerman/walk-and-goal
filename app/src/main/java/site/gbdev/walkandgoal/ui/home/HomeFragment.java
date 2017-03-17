package site.gbdev.walkandgoal.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.db.FitnessDbWrapper;
import site.gbdev.walkandgoal.models.Goal;
import site.gbdev.walkandgoal.models.Units;
import site.gbdev.walkandgoal.ui.AddGoalActivity;
import site.gbdev.walkandgoal.ui.RecyclerViewRefresher;

/**
 * Created by gavin on 07/02/2017.
 */

public class HomeFragment extends Fragment implements RecyclerViewRefresher {

    Context context;
    List<Goal> goals = new ArrayList<>();
    RecyclerView recyclerView;

    FloatingActionButton fab;

    Goal activeGoal;

    TextView activeGoalName, activeGoalDistance, currentDistance, currentProgressText;
    int currentProgress;
    double activityToday;
    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activeGoalName = (TextView) getView().findViewById(R.id.home_current_goal_name);
        activeGoalDistance = (TextView) getView().findViewById(R.id.home_current_goal_distance);

        currentDistance = (TextView) getView().findViewById(R.id.home_current_distance);
        currentProgressText = (TextView) getView().findViewById(R.id.home_current_progress_text);

        progressBar = (ProgressBar) getView().findViewById(R.id.home_current_progress_bar);

        recyclerView = (RecyclerView) getView().findViewById(R.id.home_recycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        updateRecyclerView();

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 && fab.isShown())
                {
                    fab.hide();
                } else {
                    fab.show();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_add_goal:
                Intent intent = new Intent(context, AddGoalActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void updateRecyclerView(){

        goals = FitnessDbWrapper.getAllInactiveUnfinishedGoals(context);

        activeGoal = FitnessDbWrapper.getActiveGoal(context);

        if (activeGoal != null){
            activeGoalName.setText(activeGoal.getName());
            activeGoalDistance.setText(activeGoal.getDisplayDistance());
            activityToday = FitnessDbWrapper.getTotalActivityForDate(Units.getUNITS()[activeGoal.getUnit()], null, context);
            DecimalFormat format = new DecimalFormat("0.#");
            currentDistance.setText(format.format(activityToday) + " " + Units.getUNITS()[activeGoal.getUnit()].getName());
            currentProgress = (int) (activityToday/activeGoal.getUnitDistance()*100);
            currentProgressText.setText(currentProgress + "%");
            progressBar.setProgress(currentProgress);
        } else {
            activeGoalName.setText("");
            activeGoalDistance.setText("");
        }

        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(goals, context, this);
        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);

        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        updateRecyclerView();
    }

    @Override
    public Date getDate(){ return null;}
}
