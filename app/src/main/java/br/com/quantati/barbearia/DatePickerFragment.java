package br.com.quantati.barbearia;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by carlos on 29/05/17.
 */

public class DatePickerFragment extends DialogFragment {

    private Calendar c;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        c = (Calendar) getArguments().getSerializable("calendar");
        if (c == null) {
            c = Calendar.getInstance();
            c.setTime(new Date());
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),
                (DatePickerDialog.OnDateSetListener)
                        getActivity(), year, month, day);
    }

}
