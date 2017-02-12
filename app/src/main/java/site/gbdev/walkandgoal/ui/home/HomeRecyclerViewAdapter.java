package site.gbdev.walkandgoal.ui.home;

import android.content.Context;
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
import site.gbdev.walkandgoal.models.Goal;

/**
 * Created by gavin on 07/02/2017.
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.GoalViewHolder> {

    List<Goal> goals;
    Context context;

    HomeRecyclerViewAdapter(List<Goal> goals, Context context){
        this.goals = goals;
        this.context = context;
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
        String displayDistance = goals.get(i).getDistance() + " " + goals.get(i).getUnit();
        goalViewHolder.goalDistance.setText(displayDistance);
        goalViewHolder.goalOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context wrapper = new ContextThemeWrapper(context, R.style.MyPopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, goalViewHolder.goalOptions);
                popup.inflate(R.menu.menu_item_home);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_item_home_active:
                                //handle menu1 click
                                break;
                            case R.id.menu_item_home_edit:
                                //handle menu2 click
                                break;
                            case R.id.menu_item_home_delete:
                                //handle menu3 click
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
