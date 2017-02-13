package site.gbdev.walkandgoal.ui.history;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import site.gbdev.walkandgoal.R;

/**
 * Created by gavin on 07/02/2017.
 */

public class HistoryFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    Context context;
    TextView completedTextView, completedPercentTextView, goalNameTextView, goalDistanceTextView;
    Button datePickerButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
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

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
    }

    @Override
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
}