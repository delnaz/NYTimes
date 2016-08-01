package com.codepath.nytimes.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    public DatePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("nytimes", Context.MODE_PRIVATE);

        mYear = prefs.getInt("year", mYear);
        mMonth = prefs.getInt("month", mMonth);
        mDay = prefs.getInt("day", mDay);

        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getTargetFragment();

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, mYear, mMonth, mDay);
    }

}
