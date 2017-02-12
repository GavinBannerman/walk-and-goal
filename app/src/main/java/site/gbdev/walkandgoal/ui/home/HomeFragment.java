package site.gbdev.walkandgoal.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.models.Goal;

/**
 * Created by gavin on 07/02/2017.
 */

public class HomeFragment extends Fragment {

    Context context;
    List<Goal> goals = new ArrayList<>();;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();

        goals.add(new Goal("Also A Goal", 10, "Miles"));
        goals.add(new Goal("Walk The Dog", 800, "Metres"));
        goals.add(new Goal("Just Another Goal", 300, "Steps"));
        goals.add(new Goal("Goal Number 4", 100, "Yards"));
        goals.add(new Goal("More Goals", 25, "Miles"));
        goals.add(new Goal("This is a really long name for a goal", 10, "Steps"));
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

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.home_recycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(goals);
        recyclerView.setAdapter(adapter);

    }

    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }
}
