package com.myolin.cameraapp;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;

/**
 * Created by mzlmy on 3/22/2017.
 */

public class Listbox extends Spinner {

    public Listbox(Context context, String[] dataArray, String id, MyPrefs myPrefs){
        super(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, dataArray);
        setAdapter(adapter);
        //setPadding(0,35,0,35);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f);
        lp.setMargins(0,15,0,15);
        this.setLayoutParams(lp);
        setListener(id, myPrefs);
    }

    public void setListener(final String name, final MyPrefs myPrefs){
        this.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getSelectedItem().toString();
                myPrefs.storeString(name, value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
