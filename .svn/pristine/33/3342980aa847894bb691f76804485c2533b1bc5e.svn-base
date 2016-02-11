package edu.dartmouth.cs.myruns;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.widget.DatePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class DateFragment extends DialogFragment {

    Calendar mDateAndTime = Calendar.getInstance();

    public interface DateSetListener {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ((DateSetListener) getActivity()).onDateSet(view, year, monthOfYear, dayOfMonth);
            }
        };

        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), mDateListener,
                mDateAndTime.get(Calendar.YEAR),
                mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH));


        return datePicker;
    }
}

