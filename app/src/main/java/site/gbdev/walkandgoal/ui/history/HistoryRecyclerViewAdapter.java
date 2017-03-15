package site.gbdev.walkandgoal.ui.history;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.models.Goal;

/**
 * Created by gavin on 14/02/2017.
 */

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.GoalViewHolder> {

    List<Goal> goals;
    Context context;
    List<Double> progress;

    HistoryRecyclerViewAdapter(List<Goal> goals, Context context, List<Double> progress){
        this.goals = goals;
        this.context = context;
        this.progress = progress;
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout item;
        TextView goalName;
        TextView goalDistance, goalDay, goalMonth, goalPercentage, goalCompleted;

        GoalViewHolder(View itemView) {
            super(itemView);
            item = (RelativeLayout) itemView.findViewById(R.id.item_history);
            goalName = (TextView) itemView.findViewById(R.id.item_goal_name);
            goalDistance = (TextView) itemView.findViewById(R.id.item_goal_distance);
            goalDay = (TextView) itemView.findViewById(R.id.item_date_day);
            goalMonth = (TextView) itemView.findViewById(R.id.item_date_month_year);
            goalPercentage = (TextView) itemView.findViewById(R.id.item_goal_percentage);
            goalCompleted = (TextView) itemView.findViewById(R.id.item_goal_distance_completed);
        }
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_history, viewGroup, false);
        int height = viewGroup.getMeasuredHeight() / 4;
        viewGroup.setMinimumHeight(height);
        GoalViewHolder pvh = new GoalViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final GoalViewHolder goalViewHolder, int i) {
        goalViewHolder.goalName.setText(goals.get(i).getName());
        String displayDistance = "/" + goals.get(i).getDisplayDistance();
        goalViewHolder.goalDistance.setText(displayDistance);
        long formattableTime = goals.get(i).getDate().getTime()/1000;
        SimpleDateFormat format1 = new SimpleDateFormat("dd");
        SimpleDateFormat format2 = new SimpleDateFormat("MMM yy");
        goalViewHolder.goalDay.setText(format1.format(formattableTime));
        goalViewHolder.goalMonth.setText(format2.format(formattableTime).toUpperCase());

        //TODO Read progress from DB
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
