package com.myolin.cameraapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by mzlmy on 3/14/2017.
 */

public class CustomLayout extends LinearLayout {

    private final int tooltipcolumn = 20;
    private final int labelcolumn = 2;
    private final int listcolumn = 5;
    private final int duallistcolumn = 16;
    private final int controlcolumn = 4;
    private final int comboboxcolumn = 5;
    private final int defaultcolumn = 17;
    private final int lowcolumn = 13;
    private final int highcolumn = 14;
    private final int idcolumn = 3;
    private final int childrencolumn = 6;

    LinearLayout labelView;
    LinearLayout controlView;
    ReadAsset asset;
    ImageView imageView;

    int start;
    int end;

    //private SharedPreferences preferences;
    MyPrefs myPrefs;

    public CustomLayout(final Context context, int width, int rows, int start, int end, ReadAsset asset, final MyPrefs myPrefs){
        super(context);
        this.asset = asset;
        this.myPrefs = myPrefs;
        this.start = start;
        this.end = end;
        labelView = new LinearLayout(context);
        controlView = new LinearLayout(context);

        //preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        labelView.setLayoutParams(new LayoutParams(width/2 - 65, LayoutParams.MATCH_PARENT));
        controlView.setLayoutParams(new LayoutParams(width/2, LayoutParams.MATCH_PARENT));

        //labelView.setBackgroundResource(R.drawable.border);
        //controlView.setBackgroundResource(R.drawable.border);

        this.setOrientation(LinearLayout.HORIZONTAL);
        labelView.setOrientation(LinearLayout.VERTICAL);
        controlView.setOrientation(LinearLayout.VERTICAL);

        TextView[] labelArray = new TextView[end - start];

        int index = 0;
        for(int i=start; i<end; i++){
            String label =asset.getStringDataCell(labelcolumn,i);
            TextView textView = new TextView(context);
            textView.setText(label);
            LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.4f);
            lp.setMargins(0,25,0,25);
            textView.setLayoutParams(lp);
            //textView.setPadding(0,35,0,35);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setId(start);
            labelArray[index] = textView;
            labelView.addView(textView);

            final String tooltip = asset.getStringDataCell(tooltipcolumn,i);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, tooltip, Toast.LENGTH_SHORT).show();
                }
            });

            index++;

        }

        int index2 = 0;
        for(int i=start; i<end; i++) {
            String label = asset.getStringDataCell(controlcolumn, i);
            final String id = asset.getStringDataCell(idcolumn, i);
            if (label.equals("TextBox")) {
                final TextBox textBox = new TextBox(context, id, myPrefs, labelArray[index2]);
                if(asset.getStringDataCell(labelcolumn,i).equals("Phone")){
                    //textBox.setNumType();
                    PhoneNumberFormattingTextWatcher mWatcher = new PhoneNumberFormattingTextWatcher();
                    textBox.addTextChangedListener(mWatcher);
                }else{
                    textBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                }
                controlView.addView(textBox);
            }else if(label.equals("Date")) {
                Date date = new Date(context, id, myPrefs);
                controlView.addView(date);
            }else if(label.equals("Memo")) {
                Memo memo = new Memo(context, id, myPrefs);
                memo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                controlView.addView(memo);
            }else if(label.equals("CheckBox")){
                final CheckBox checkBox = new CheckBox(context);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,40,0,40);
                checkBox.setLayoutParams(lp);
                //checkBox.setPadding(0,35,0,35);
                controlView.addView(checkBox);
            } else if (label.equals("ListBox")) {
                String data = asset.getStringDataCell(listcolumn, i);
                String[] dataArray = data.split(",");
                Listbox listbox = new Listbox(context, dataArray, id, myPrefs);
                controlView.addView(listbox);
            }else if(label.equals("DualListBox") || label.equals("NearDualListBox") || label.equals("NearListBox")) {
                String data = asset.getStringDataCell(duallistcolumn, i);
                String[] dataArray = data.split(",");
                Listbox listbox = new Listbox(context, dataArray, id, myPrefs);
                controlView.addView(listbox);
            }else if(label.equals("ComboBox")) {
                String data = asset.getStringDataCell(comboboxcolumn, i);
                String[] dataArray = new String[10];
                //String defaultValue = asset.getStringDataCell(12,i);
                String defaultValue = asset.getStringDataCell(defaultcolumn, i);
                ComboBox comboBox = new ComboBox(context, dataArray, defaultValue, id, myPrefs, labelArray[index2]);
                controlView.addView(comboBox);
            }else if(label.equals("Spinner") || label.equals("Spinner (overwritable)")) {
                final TextBox textBox = new TextBox(context, id, myPrefs, labelArray[index2]);
                textBox.setNumType();
                controlView.addView(textBox);
            }else if(label.equals("Lookup")){
                String data = asset.getStringDataCell(duallistcolumn,i);
                String[] dataArray = data.split(",");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, dataArray);
                Spinner spinner = new Spinner(context);
                spinner.setAdapter(dataAdapter);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,15,0,15);
                spinner.setLayoutParams(lp);
                controlView.addView(spinner);
            } else {
                TextView textView = new TextView(context);
                textView.setText("CONTROL");
                //textView.setPadding(0,30,0,30);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,45,0,45);
                textView.setLayoutParams(lp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                controlView.addView(textView);
            }
            index2++;
        }

       /* for(int i=start; i<end; i++) {
            String label = asset.getStringDataCell(controlcolumn, i);
            if (label.equals("TextBox")) {
                TextBox textBox = new TextBox(context);
                if(asset.getStringDataCell(labelcolumn,i).equals("Phone")){
                    textBox.setNumType();
                }
                controlView.addView(textBox);
            }else if(label.equals("Date")) {
                Date date = new Date(context);
                controlView.addView(date);
            }else if(label.equals("Memo")) {
                Memo memo = new Memo(context);
                controlView.addView(memo);
            }else if(label.equals("CheckBox")){
                CheckBox checkBox = new CheckBox(context);
                checkBox.setPadding(0,35,0,35);
                controlView.addView(checkBox);
            } else if (label.equals("ListBox")) {
                String data = asset.getStringDataCell(listcolumn, i);
                String[] dataArray = data.split(",");
                Listbox listbox = new Listbox(context, dataArray);
                controlView.addView(listbox);
            }else if(label.equals("DualListBox") || label.equals("NearDualListBox")) {
                String data = asset.getStringDataCell(duallistcolumn, i);
                String[] dataArray = data.split(",");
                Listbox listbox = new Listbox(context, dataArray);
                controlView.addView(listbox);
            }else if(label.equals("ComboBox")) {
                String data = asset.getStringDataCell(comboboxcolumn, i);
                String[] dataArray = new String[10];
                //String defaultValue = asset.getStringDataCell(12,i);
                String defaultValue = asset.getStringDataCell(defaultcolumn, i);
                ComboBox comboBox = new ComboBox(context, dataArray, defaultValue);
                controlView.addView(comboBox);
            }else if(label.equals("Spinner") || label.equals("Spinner(overwritable)")) {
                final TextView textView = new TextView(context);
                LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 15, 0, 15);
                textView.setLayoutParams(lp);
                textView.setBackgroundResource(R.drawable.border);
                controlView.addView(textView);
                final int min = Integer.valueOf(asset.getStringDataCell(lowcolumn, i));
                final int max = Integer.valueOf(asset.getStringDataCell(highcolumn, i));
                ;
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        numPicker(context, textView, min, max);
                    }
                });
            }else if(label.equals("Lookup")){
                String data = asset.getStringDataCell(duallistcolumn,i);
                String[] dataArray = data.split(",");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, dataArray);
                Spinner spinner = new Spinner(context);
                spinner.setAdapter(dataAdapter);
                controlView.addView(spinner);
            } else if (label.equals("Spinners")) {
                int lowLimit = Integer.parseInt(asset.getStringDataCell(8, i));
                int highLimit = Integer.parseInt(asset.getStringDataCell(9, i));
                int increment = Integer.parseInt(asset.getStringDataCell(10, i));
                //int defaultValue = Integer.parseInt(asset.getStringDataCell(12, i));
                int defaultValue = Integer.parseInt(asset.getStringDataCell(defaultcolumn,i));
                Integer[] dataArray = new Integer[(highLimit - lowLimit) / increment + 1];
                int index = 0;
                for (int j = lowLimit; j <= highLimit; j = j + increment) {
                    dataArray[index] = j;
                    index++;
                }
                Spinwheel spinwheel = new Spinwheel(context, dataArray, defaultValue);
                controlView.addView(spinwheel);
            } else {
                TextView textView = new TextView(context);
                textView.setText("CONTROL");
                textView.setPadding(0,30,0,30);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                controlView.addView(textView);
            }
        }*/

        //hier3(context);

        addImage(context);

        this.addView(labelView);
        this.addView(controlView);
    }

    public void addImage(Context context){
        imageView = new ImageView(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300);
        lp.setMargins(0,35,0,35);
        imageView.setLayoutParams(lp);
        imageView.setBackgroundResource(R.drawable.border);
        imageView.setPadding(0,35,0,35);
        //controlView.addView(imageView);
    }

    public ImageView getImageView(){
        return imageView;
    }

    public void numPicker(Context context, final TextView textView, int min, int max){
        final NumberPicker picker = new NumberPicker(context);
        picker.setMinValue(min);
        picker.setMaxValue(max);

        final FrameLayout layout = new FrameLayout(context);
        layout.addView(picker, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

        new AlertDialog.Builder(context).setView(layout).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                picker.clearFocus();
                int num = picker.getValue();
                textView.setText(Integer.toString(num));
            }
        }).setNegativeButton("Cancel", null).show();

    }

    public void hier3(final Context context){
        final LinearLayout layout = new LinearLayout(context);
        //layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setOrientation(VERTICAL);
        String parent = asset.getStringDataCell(16,170);
        String[] parentArray = parent.split(",");
        final ArrayAdapter<String> parentAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, parentArray);
        final Spinner parentSpinner = new Spinner(context);
        parentSpinner.setAdapter(parentAdapter);
        layout.addView(parentSpinner);

        final Spinner childSpinner = new Spinner(context);
        final Spinner grandchildSpinner = new Spinner(context);

        parentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                layout.removeAllViews();
                String parentValue = parent.getSelectedItem().toString();

                if(parentValue.equals("None")){
                    layout.addView(parentSpinner);
                }else {
                    int r=-1;
                    for (int i = 170; i < 180; i++) {
                        String data = asset.getStringDataCell(9, i);
                        if (parentValue.equals(data)) {
                            String child = asset.getStringDataCell(16, i);
                            String[] childArray = child.split(",");
                            ArrayAdapter<String> childAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, childArray);
                            childSpinner.setAdapter(childAdapter);
                            r = i;
                            break;
                        }
                    }
                    layout.addView(parentSpinner);
                    if(asset.hasChildren(6,r)){
                        layout.addView(childSpinner);
                    }
                }
                myPrefs.storeString("BaseHeatFuel1", parentValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        childSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                layout.removeAllViews();
                String grandparentValue = parentSpinner.getSelectedItem().toString();
                String parentValue = parent.getSelectedItem().toString();
                boolean hasGrandChild = false;

                for(int i=180; i<190; i++){
                    String grandparentData = asset.getStringDataCell(9,i);
                    String parentData = asset.getStringDataCell(11, i);
                    if(grandparentData.equals(grandparentValue) && parentData.equals(parentValue)){
                        String grandChild = asset.getStringDataCell(16,i);
                        String[] grandchildArray = grandChild.split(",");
                        ArrayAdapter<String> grandchildAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, grandchildArray);
                        grandchildSpinner.setAdapter(grandchildAdapter);
                        hasGrandChild = true;
                        break;
                    }
                }
                layout.addView(parentSpinner);
                layout.addView(childSpinner);
                if(hasGrandChild){
                    layout.addView(grandchildSpinner);
                }
                myPrefs.storeString("BaseHeatType1", "BOBO");
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        controlView.addView(layout);
    }
}
