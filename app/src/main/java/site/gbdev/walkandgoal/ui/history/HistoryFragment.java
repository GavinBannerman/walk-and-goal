package site.gbdev.walkandgoal.ui.history;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
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
import java.util.List;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.models.Goal;
import site.gbdev.walkandgoal.ui.DatePickerFragment;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by gavin on 14/02/2017.
 */

public class HistoryFragment extends Fragment {

    Context context;
    RecyclerView recyclerView;
    List<Goal> goals = new ArrayList<>();

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

        HistoryRecyclerViewAdapter adapter = new HistoryRecyclerViewAdapter(goals, context);
        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);

        final DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month, int day) {

                Button datePickerButton = (Button) getView().findViewById(R.id.button_from_date);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yy");
                String formattedDate = simpleDateFormat.format(calendar.getTime());
                datePickerButton.setText(formattedDate);
            }
        };

        final DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month, int day) {

                Button datePickerButton = (Button) getView().findViewById(R.id.button_to_date);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yy");
                String formattedDate = simpleDateFormat.format(calendar.getTime());
                datePickerButton.setText(formattedDate);
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
                DialogFragment newFragment = CompletionDialogFragment.newInstance(2);
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
                } else if (position == 1) {
                    fromToDateSection.setVisibility(View.GONE);
                } else if (position == 2) {
                    fromToDateSection.setVisibility(View.GONE);
                } else if (position == 3) {
                    fromToDateSection.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

    }

    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_statistics, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_change_units:
                // TODO Add change units code here
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}