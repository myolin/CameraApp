package com.myolin.cameraapp;

import android.content.Context;
import android.widget.ImageView;
import android.widget.ScrollView;

/**
 * Created by mzlmy on 3/16/2017.
 */

public class ScrollLayout extends ScrollView {

    CustomLayout2 customLayout;
    ImageView imageView;

    public ScrollLayout(Context context, int width, int row, int start, int end, ReadAsset asset, MyPrefs myPrefs) {
        super(context);

        customLayout = new CustomLayout2(context, width, row, start, end, asset, myPrefs);

        imageView = customLayout.getImageView();

        this.addView(customLayout);
    }

    public ImageView getImageView(){
        return imageView;
    }
}
