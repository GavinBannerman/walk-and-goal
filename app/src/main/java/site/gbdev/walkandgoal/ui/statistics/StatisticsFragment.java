package site.gbdev.walkandgoal.ui.statistics;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.ui.AddGoalActivity;
import site.gbdev.walkandgoal.ui.history.DatePickerFragment;

/**
 * Created by gavin on 07/02/2017.
 */

public class StatisticsFragment extends Fragment {

    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final LinearLayout fromToDateSection = (LinearLayout) getView().findViewById(R.id.from_to_section);

        Button fromButton = (Button) getView().findViewById(R.id.button_from_date);
        Button toButton = (Button) getView().findViewById(R.id.button_to_date);

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


        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner_show);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.period_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                Intent intent = new Intent(context, AddGoalActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}