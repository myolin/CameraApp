package com.myolin.optimiser;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by mzlmy on 3/31/2017.
 */

public class Date extends TextView {

    Calendar calendar = Calendar.getInstance();

    public Date(final Context context, final String id, final MyPrefs myPrefs){
        super(context);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f);
        lp.setMargins(0,15,0,15);
        this.setLayoutParams(lp);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        setBackgroundResource(R.drawable.border);


        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setText((month+1) + "/" + dayOfMonth + "/" + year);
                myPrefs.storeString(id, (month+1) + "/" + dayOfMonth + "/" + year);
            }
        };

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                        Calendar.DAY_OF_MONTH).show();
            }
        });
    }



}
