package com.myolin.cameraapp;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by mzlmy on 3/31/2017.
 */

public class ComboBox extends AutoCompleteTextView {

    public ComboBox(Context context, String[] dataArray, String defaultValue, String id, MyPrefs myPrefs, TextView textView){
        super(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, dataArray);
        setThreshold(1);
        setEms(3);
        setSingleLine(true);
        setAdapter(adapter);
        setText(defaultValue);
        //RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,15,0,15);
        setLayoutParams(lp);
        //setPadding(0,35,0,35);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        setListener(id, myPrefs);
    }

    public void setListener(final String id, final MyPrefs myPrefs){
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                myPrefs.storeString(id, getText().toString());
            }
        });
    }
}
