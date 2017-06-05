package br.com.quantati.barbearia;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by carlos on 29/05/17.
 */

public class TimePickerFragment extends DialogFragment {

    private Calendar c;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        c = (Calendar) getArguments().getSerializable("calendar");
        if (c == null) {
            c = Calendar.getInstance();
            c.setTime(new Date());
        }
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);

        return new TimePickerDialog(getActivity(),
                (TimePickerDialog.OnTimeSetListener)
                        getActivity(), hour, minute, true);
    }

}
