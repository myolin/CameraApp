package com.myolin.optimiser;

import android.content.Context;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by mzlmy on 3/31/2017.
 */

public class Memo extends EditText {

    public Memo(Context context, String id, MyPrefs myPrefs){
        super(context);

        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        setSingleLine(false);
        setLines(5);
        setMaxLines(20);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        setBackgroundResource(R.drawable.border);
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        setVerticalScrollBarEnabled(true);
        setMovementMethod(ScrollingMovementMethod.getInstance());
        setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
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
