package site.gbdev.walkandgoal.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.db.FitnessDbWrapper;
import site.gbdev.walkandgoal.models.Goal;
import site.gbdev.walkandgoal.ui.AddGoalActivity;

/**
 * Created by gavin on 07/02/2017.
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.GoalViewHolder> {

    List<Goal> goals;
    Context context;
    HomeFragment homeFragment;

    HomeRecyclerViewAdapter(List<Goal> goals, Context context, HomeFragment homeFragment){
        this.goals = goals;
        this.context = context;
        this.homeFragment = homeFragment;
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout item;
        TextView goalName;
        TextView goalDistance;
        ImageView goalOptions;

        GoalViewHolder(View itemView) {
            super(itemView);
            item = (RelativeLayout) itemView.findViewById(R.id.item_home);
            goalName = (TextView) itemView.findViewById(R.id.item_goal_name);
            goalDistance = (TextView) itemView.findViewById(R.id.item_goal_distance);
            goalOptions = (ImageView) itemView.findViewById(R.id.item_goal_options);
        }
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home, viewGroup, false);
        int height = viewGroup.getMeasuredHeight() / 4;
        viewGroup.setMinimumHeight(height);
        GoalViewHolder pvh = new GoalViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final GoalViewHolder goalViewHolder, int i) {
        goalViewHolder.goalName.setText(goals.get(i).getName());
        goalViewHolder.goalDistance.setText(goals.get(i).getDisplayDistance());
        goalViewHolder.goalOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context wrapper = new ContextThemeWrapper(context, R.style.MyPopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, goalViewHolder.goalOptions);
                popup.inflate(R.menu.menu_item_home);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = goalViewHolder.getAdapterPosition();
                        switch (item.getItemId()) {
                            case R.id.menu_item_home_active:
                                FitnessDbWrapper.setActive(goals.get(id), context);
                                homeFragment.updateRecyclerView();
                                break;
                            case R.id.menu_item_home_edit:

                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                                boolean editGoals = sharedPreferences.getBoolean("pref_edit_goals", true);

                                if (editGoals) {
                                    Intent intent = new Intent(context, AddGoalActivity.class);
                                    intent.putExtra("goal", goals.get(id));
                                    context.startActivity(intent);
                                }
                                break;
                            case R.id.menu_item_home_delete:
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                alert.setTitle("Delete Goal");
                                alert.setMessage("Are you sure you want to delete this goal?");
                                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        FitnessDbWrapper.deleteGoal(goals.get(goalViewHolder.getAdapterPosition()), context);
                                        homeFragment.updateRecyclerView();
                                    }
                                });
                                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alert.show();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();

            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
