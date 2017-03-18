package site.gbdev.walkandgoal.ui.history;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import site.gbdev.walkandgoal.R;

/**
 * Created by gavin on 14/02/2017.
 */

public class CompletionDialogFragment extends DialogFragment {

    int mNum;
    int selectedValue;

    static CompletionDialogFragment newInstance(int num, int val) {
        CompletionDialogFragment f = new CompletionDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putInt("val", val);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");
        selectedValue = getArguments().getInt("val");

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_layout, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Spinner spinner = (Spinner) getView().findViewById(R.id.spinner_completion);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.completion_array, R.layout.spinner_layout);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
        spinner.setSelection(mNum);

        final SeekBar seekBar = (SeekBar) getView().findViewById(R.id.completion_seekbar);
        final TextView percentageTextView = (TextView) getView().findViewById(R.id.completion_percentage);
        Button selectButton = (Button) getView().findViewById(R.id.dismiss_button);

        seekBar.setProgress(selectedValue);
        percentageTextView.setText(selectedValue + "%");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                selectedValue = progressValue;
                percentageTextView.setText(progressValue + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position == 0) {
                    seekBar.setVisibility(View.GONE);
                    percentageTextView.setVisibility(View.GONE);
                } else if (position == 1) {
                    seekBar.setVisibility(View.VISIBLE);
                    percentageTextView.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    seekBar.setVisibility(View.VISIBLE);
                    percentageTextView.setVisibility(View.VISIBLE);
                } else if (position == 3) {
                    seekBar.setVisibility(View.GONE);
                    percentageTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Button filterCompletionButton = (Button) getActivity().findViewById(R.id.button_filter_completion);
                String returnString = "";
                if (spinner.getSelectedItemPosition() == 1 || spinner.getSelectedItemPosition() == 2){
                    returnString = spinner.getSelectedItem().toString() + " " + selectedValue + "%";
                } else {
                    returnString = spinner.getSelectedItem().toString();
                }

                HistoryFragment parentFragment = (HistoryFragment) getActivity().getSupportFragmentManager().findFragmentByTag("history");
                parentFragment.filterValue = selectedValue;

                parentFragment.selectedFilter = spinner.getSelectedItemPosition();

                filterCompletionButton.setText(returnString);
                parentFragment.updateRecyclerView();
                dismiss();
            }
        });
    }
}