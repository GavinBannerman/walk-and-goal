package site.gbdev.walkandgoal.ui.history;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.db.FitnessDbWrapper;
import site.gbdev.walkandgoal.models.Goal;
import site.gbdev.walkandgoal.models.HistoricGoal;
import site.gbdev.walkandgoal.models.Units;
import site.gbdev.walkandgoal.ui.DatePickerFragment;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by gavin on 14/02/2017.
 */

public class HistoryFragment extends Fragment {

    Context context;
    RecyclerView recyclerView;
    List<Goal> goals = new ArrayList<>();
    List<Double> progress = new ArrayList<>();
    public int selectedFilter = 0;
    int filterValue = 0;
    Date fromDate = null, toDate = null;
    Units.Unit selectedUnits = Units.Unit.STEPS;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String unitPreference = sharedPreferences.getString("pref_history_units", "Steps");
        selectedUnits = Units.getUNITS()[Units.getIdFromString(unitPreference)];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final LinearLayout fromToDateSection = (LinearLayout) getView().findViewById(R.id.from_to_section);

        Button fromButton = (Button) getView().findViewById(R.id.button_from_date);
        Button toButton = (Button) getView().findViewById(R.id.button_to_date);
        Button completionButton = (Button) getView().findViewById(R.id.button_filter_completion);

        recyclerView = (RecyclerView) getView().findViewById(R.id.history_other_recycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        updateRecyclerView();

        final DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month, int day) {

                Button datePickerButton = (Button) getView().findViewById(R.id.button_from_date);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                fromDate = calendar.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yy");
                String formattedDate = simpleDateFormat.format(calendar.getTime());
                datePickerButton.setText(formattedDate);
                updateRecyclerView();
            }
        };

        final DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month, int day) {

                Button datePickerButton = (Button) getView().findViewById(R.id.button_to_date);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                toDate = calendar.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yy");
                String formattedDate = simpleDateFormat.format(calendar.getTime());
                datePickerButton.setText(formattedDate);
                updateRecyclerView();
            }
        };

        fromButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setListener(fromDateListener);
                DialogFragment dialogFragment = datePickerFragment;
                dialogFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        toButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setListener(toDateListener);
                DialogFragment dialogFragment = datePickerFragment;
                dialogFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        completionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                DialogFragment newFragment = CompletionDialogFragment.newInstance(selectedFilter, filterValue);
                newFragment.show(ft, "dialog");
            }
        });

        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner_show);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.period_array_goals, R.layout.spinner_layout);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    fromToDateSection.setVisibility(View.GONE);
                    fromDate = null;
                    toDate = null;
                } else if (position == 1) {
                    fromToDateSection.setVisibility(View.GONE);
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, -7);
                    fromDate = calendar.getTime();
                    toDate = null;
                } else if (position == 2) {
                    fromToDateSection.setVisibility(View.GONE);
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, -28);
                    fromDate = calendar.getTime();
                    toDate = null;
                } else if (position == 3) {
                    fromToDateSection.setVisibility(View.VISIBLE);
                }
                updateRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String spinnerPreference = sharedPreferences.getString("pref_history_time", "from all history");

        spinner.setSelection(spinnerAdapter.getPosition(spinnerPreference));
    }

    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_history, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_change_units:

                View menuItemView = getActivity().findViewById(R.id.menu_change_units); // SAME ID AS MENU ID
                Context wrapper = new ContextThemeWrapper(context, R.style.MyPopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, menuItemView);
                popup.inflate(R.menu.menu_units);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                     @Override
                     public boolean onMenuItemClick(MenuItem item) {
                         switch (item.getItemId()) {
                             case R.id.menu_units_steps:
                                 selectedUnits = Units.Unit.STEPS;
                                 break;
                             case R.id.menu_units_metres:
                                 selectedUnits = Units.Unit.METRES;
                                 break;
                             case R.id.menu_units_yards:
                                 selectedUnits = Units.Unit.YARDS;
                                 break;
                             case R.id.menu_units_km:
                                 selectedUnits = Units.Unit.KM;
                                 break;
                             case R.id.menu_units_miles:
                                 selectedUnits = Units.Unit.MILES;
                                 break;
                         }
                         updateRecyclerView();
                         return false;
                     }
                 });
                // ...
                popup.show();
                return true;

            case R.id.menu_delete_history:
                Context wrapper2 = new ContextThemeWrapper(context, R.style.MyPopupMenu);
                AlertDialog.Builder alert = new AlertDialog.Builder(wrapper2);

                alert.setTitle("Delete History");
                alert.setMessage("Are you sure you want to permanently delete history?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        FitnessDbWrapper.deleteHistory(context);
                        updateRecyclerView();
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateRecyclerView(){

        goals = FitnessDbWrapper.getAllFinishedGoals(selectedUnits, fromDate, toDate, context);

        progress = FitnessDbWrapper.getActivityForHistory(selectedUnits, context, goals);

        List<HistoricGoal> historicGoals = new ArrayList<>();

        for (int i = 0; i < goals.size(); i++){
            historicGoals.add(new HistoricGoal(goals.get(i), progress.get(i)));
        }

        HistoryRecyclerViewAdapter adapter = new HistoryRecyclerViewAdapter(context, filterByCompletion(historicGoals));
        recyclerView.setAdapter(adapter);
    }

    private List<HistoricGoal> filterByCompletion(List<HistoricGoal> historicGoals){

        for (Iterator<HistoricGoal> iterator = historicGoals.iterator(); iterator.hasNext();){
            HistoricGoal historicGoal = iterator.next();

            switch (selectedFilter){

                case 1:
                    if (historicGoal.getPercentageCompleted() >= filterValue){
                        iterator.remove();
                    }
                    break;

                case 2:
                    if (historicGoal.getPercentageCompleted() <= filterValue){
                        iterator.remove();
                    }
                    break;

                case 3:
                    if (historicGoal.getPercentageCompleted() < 100){
                        iterator.remove();
                    }
                    break;
            }
        }

        return historicGoals;
    }

}