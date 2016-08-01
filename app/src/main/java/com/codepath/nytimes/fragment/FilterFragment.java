package com.codepath.nytimes.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.nytimes.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class FilterFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{


    @BindView(R.id.begin_date)
    EditText mEditText;
    @BindView(R.id.mySpinner)
    Spinner mSpinner;
    @BindView(R.id.checkBox1)
    CheckBox mCheckBox1;
    @BindView(R.id.checkBox2)
    CheckBox mCheckBox2;
    @BindView(R.id.checkBox3)
    CheckBox mCheckBox3;
    @BindView(R.id.button)
    Button mButton;

    int mDay, mMonth, mYear;

    private OnFragmentInteractionListener mListener;

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_filter, container, false);
        getDialog().setTitle("Filter Dialog");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("nytimes", Context.MODE_PRIVATE);

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mYear = prefs.getInt("year", mYear);
        mMonth = prefs.getInt("month", mMonth);
        mDay = prefs.getInt("day", mDay);
        mEditText.setText( (mMonth+1) +"/"+ mDay +"/"+ mYear);
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        int selected = prefs.getInt("sort", 0);
        mSpinner.setSelection(selected);

        mCheckBox1.setChecked(prefs.getBoolean("check1", false));
        mCheckBox2.setChecked(prefs.getBoolean("check2", false));
        mCheckBox3.setChecked(prefs.getBoolean("check3", false));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed();
                FilterFragment.this.dismiss();
            }
        });
    }

    // Call this method to launch the edit dialog
    private void showDatePickerDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setTargetFragment(FilterFragment.this, 300);
        datePickerFragment.show(fm, "fragment_date_picker");
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction(mYear, mMonth, mDay, mSpinner.getSelectedItemPosition(), mCheckBox1.isChecked(), mCheckBox2.isChecked(), mCheckBox3.isChecked());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        SharedPreferences.Editor editor = getActivity().getApplicationContext().getSharedPreferences("nytimes", Context.MODE_PRIVATE).edit();
        editor.putInt("year", year);
        editor.putInt("month", month);
        editor.putInt("day", day);
        editor.commit();

        mYear = year;
        mDay = day;
        mMonth = month;
        mEditText.setText( (month+1) +"/"+ day +"/"+ year);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int year, int month, int day, int sortOrder, boolean ch1, boolean ch2, boolean ch3);
    }
}
