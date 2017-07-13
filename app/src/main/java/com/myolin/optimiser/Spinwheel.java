package com.myolin.optimiser;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by mzlmy on 3/22/2017.
 */

public class Spinwheel extends Spinner {

    public Spinwheel(Context context, Integer[] dataArray, int defaultValue){
        super(context);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(context, R.layout.support_simple_spinner_dropdown_item, dataArray);
        this.setAdapter(adapter);

        int spinnerPositon = adapter.getPosition(defaultValue);
        this.setSelection(spinnerPositon);


    }


}
