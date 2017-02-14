package site.gbdev.walkandgoal.ui.history;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.ui.DatePickerFragment;

/**
 * Created by gavin on 07/02/2017.
 */

public class HistoryFragment extends Fragment {

    Context context;
    TextView completedTextView, completedPercentTextView, goalNameTextView, goalDistanceTextView;
    Button datePickerButton;

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
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        datePickerButton = (Button) getActivity().findViewById(R.id.history_day_button);

        final DatePickerDialog.OnDateSetListener pickDateListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month, int day) {

                TextView completedTextView = (TextView) getView().findViewById(R.id.history_day_completed);
                TextView completedPercentTextView = (TextView) getView().findViewById(R.id.history_day_percent_completed);
                TextView goalNameTextView = (TextView) getView().findViewById(R.id.history_day_goal_name);
                TextView goalDistanceTextView = (TextView) getView().findViewById(R.id.history_day_goal_distance);
                Button datePickerButton = (Button) getView().findViewById(R.id.history_day_button);

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yy");
                String formattedDate = simpleDateFormat.format(calendar.getTime());

                completedTextView.setText("2185 Steps");
                completedPercentTextView.setText("73%");
                goalNameTextView.setText("My Old Fitness Goal");
                goalDistanceTextView.setText("3000 Steps");
                datePickerButton.setText(formattedDate);
            }
        };

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setListener(pickDateListener);
                DialogFragment dialogFragment = datePickerFragment;
                dialogFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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