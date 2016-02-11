package edu.dartmouth.cs.myruns;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.widget.TimePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeFragment extends DialogFragment {

    Calendar mDateAndTime = Calendar.getInstance();

    public interface TimeSetListener {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                ((TimeSetListener) getActivity()).onTimeSet(view, hourOfDay, minute);
            }
        };

        TimePickerDialog timePicker = new TimePickerDialog(getActivity(), mTimeListener,
                mDateAndTime.get(Calendar.HOUR_OF_DAY),
                mDateAndTime.get(Calendar.MINUTE), true);

        return timePicker;
    }
}