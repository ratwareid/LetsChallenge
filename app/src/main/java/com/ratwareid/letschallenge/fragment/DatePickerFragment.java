package com.ratwareid.letschallenge.fragment;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    //Interface created for communicating this dialog fragment events to called fragment
    public interface DatePickerDialogFragmentEvents{
        void onDateSelected(String date);
    }

    DatePickerDialogFragmentEvents dpdfe;

    public void setDatePickerDialogFragmentEvents(DatePickerDialogFragmentEvents dpdfe){
        this.dpdfe = dpdfe;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());
        dpdfe.onDateSelected(formattedDate); //Changed
    }
}
