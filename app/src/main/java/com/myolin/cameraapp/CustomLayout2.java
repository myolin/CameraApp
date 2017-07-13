package com.myolin.cameraapp;

import android.content.Context;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class CustomLayout2 extends LinearLayout {

    private final int tooltipcolumn = 20;
    private final int labelcolumn = 2;
    private final int listcolumn = 5;
    private final int duallistcolumn = 16;
    private final int controlcolumn = 4;
    private final int comboboxcolumn = 5;
    private final int defaultcolumn = 17;
    private final int idcolumn = 3;

    ReadAsset asset;
    MyPrefs myPrefs;
    ImageView imageView;

    int start;
    int end;

    public CustomLayout2(final Context context, int width, int rows, int start, int end, ReadAsset asset, MyPrefs myPrefs){
        super(context);
        this.asset = asset;
        this.myPrefs = myPrefs;
        this.start = start;
        this.end = end;

        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.setOrientation(VERTICAL);

        TextView[] labelArray = new TextView[end - start];

        int index = 0;
        for(int i=start; i<end; i++){
            String label = asset.getStringDataCell(labelcolumn,i);
            TextView textView = new TextView(context);
            textView.setText(label);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setId(start);
            labelArray[index] = textView;

            final String tooltip = asset.getStringDataCell(tooltipcolumn,i);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, tooltip, Toast.LENGTH_SHORT).show();
                }
            });
            index++;
        }

        index = 0;
        for(int i=start; i<end; i++) {
            RelativeLayout relativeLayout = new RelativeLayout(context);
            LayoutParams relativeLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            relativeLayoutParams.setMargins(0,15,0,15);
            relativeLayout.setLayoutParams(relativeLayoutParams);
            RelativeLayout.LayoutParams labelParams = new RelativeLayout.LayoutParams(width/2 - 65, RelativeLayout.LayoutParams.WRAP_CONTENT);
            labelParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            RelativeLayout.LayoutParams controlParams = new RelativeLayout.LayoutParams(width/2, RelativeLayout.LayoutParams.WRAP_CONTENT);
            controlParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

            relativeLayout.addView(labelArray[index], labelParams);

            String label = asset.getStringDataCell(controlcolumn, i);
            final String id = asset.getStringDataCell(idcolumn, i);
            if (label.equals("TextBox")) {
                final TextBox textBox = new TextBox(context, id, myPrefs, labelArray[index]);
                if(asset.getStringDataCell(labelcolumn,i).equals("Phone")){
                    //textBox.setNumType();
                    PhoneNumberFormattingTextWatcher mWatcher = new PhoneNumberFormattingTextWatcher();
                    textBox.addTextChangedListener(mWatcher);
                }else{
                    textBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                }
                relativeLayout.addView(textBox, controlParams);
            }else if(label.equals("Date")) {
                Date date = new Date(context, id, myPrefs);
                relativeLayout.addView(date, controlParams);
            }else if(label.equals("Memo")) {
                Memo memo = new Memo(context, id, myPrefs);
                memo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                relativeLayout.addView(memo, controlParams);
            }else if(label.equals("CheckBox")){
                final CheckBox checkBox = new CheckBox(context);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,40,0,40);
                checkBox.setLayoutParams(lp);
                relativeLayout.addView(checkBox, controlParams);
            } else if (label.equals("ListBox")) {
                String data = asset.getStringDataCell(listcolumn, i);
                String[] dataArray = data.split(",");
                Listbox listbox = new Listbox(context, dataArray, id, myPrefs);
                relativeLayout.addView(listbox, controlParams);
            }else if(label.equals("DualListBox") || label.equals("NearDualListBox") || label.equals("NearListBox")) {
                String data = asset.getStringDataCell(duallistcolumn, i);
                String[] dataArray = data.split(",");
                Listbox listbox = new Listbox(context, dataArray, id, myPrefs);
                relativeLayout.addView(listbox, controlParams);
            }else if(label.equals("ComboBox")) {
                String data = asset.getStringDataCell(comboboxcolumn, i);
                String[] dataArray = new String[10];
                //String defaultValue = asset.getStringDataCell(12,i);
                String defaultValue = asset.getStringDataCell(defaultcolumn, i);
                ComboBox comboBox = new ComboBox(context, dataArray, defaultValue, id, myPrefs, labelArray[index]);
                relativeLayout.addView(comboBox, controlParams);
            }else if(label.equals("Spinner") || label.equals("Spinner (overwritable)")) {
                final TextBox textBox = new TextBox(context, id, myPrefs, labelArray[index]);
                textBox.setNumType();
                relativeLayout.addView(textBox, controlParams);
            }else if(label.equals("Lookup")){
                String data = asset.getStringDataCell(duallistcolumn,i);
                String[] dataArray = data.split(",");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, dataArray);
                Spinner spinner = new Spinner(context);
                spinner.setAdapter(dataAdapter);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,15,0,15);
                spinner.setLayoutParams(lp);
                relativeLayout.addView(spinner, controlParams);
            } else {
                TextView textView = new TextView(context);
                textView.setText("CONTROL");
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,45,0,45);
                textView.setLayoutParams(lp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                relativeLayout.addView(textView, controlParams);
            }
            this.addView(relativeLayout);
            index++;
        }

        addImage(context, width);

    }

    public void addImage(Context context, int width){
        RelativeLayout relativeLayout = new RelativeLayout(context);
        LayoutParams relativeLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        relativeLayoutParams.setMargins(0,15,0,15);
        relativeLayout.setLayoutParams(relativeLayoutParams);
        RelativeLayout.LayoutParams labelParams = new RelativeLayout.LayoutParams(width/2 - 65, RelativeLayout.LayoutParams.WRAP_CONTENT);
        labelParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        RelativeLayout.LayoutParams controlParams = new RelativeLayout.LayoutParams(width/2, RelativeLayout.LayoutParams.WRAP_CONTENT);
        controlParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        TextView textView = new TextView(context);
        textView.setText("Camera");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        imageView = new ImageView(context);
        imageView.setBackgroundResource(R.drawable.border);

        relativeLayout.addView(textView, labelParams);
        relativeLayout.addView(imageView, controlParams);

        this.addView(relativeLayout);
    }

    public ImageView getImageView(){
        return imageView;
    }
}
