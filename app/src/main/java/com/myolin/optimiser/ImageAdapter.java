package com.myolin.optimiser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by mzlmy on 5/2/2017.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    public ImageAdapter(Context c){
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150,150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
        }else{
            imageView = (ImageView)convertView;
        }
        imageView.setImageResource(mThumbIds[position]);

        return imageView;
    }

    public Integer[] mThumbIds = {
            R.drawable.auditor, R.drawable.owner,
            R.drawable.buildin, R.drawable.dimensions,
            R.drawable.multi, R.drawable.foundations,
            R.drawable.layout, R.drawable.windows,
            R.drawable.doors, R.drawable.walls,
            R.drawable.attics, R.drawable.ceilings,
            R.drawable.framefloors, R.drawable.crawl,
            R.drawable.basement, R.drawable.slab,
            R.drawable.infiltration, R.drawable.ventilation,
            R.drawable.thermostat, R.drawable.heating,
            R.drawable.cooling, R.drawable.deliverysystems,
            R.drawable.coolingdelivery, R.drawable.coolingdelivery2,
            R.drawable.lighting, R.drawable.refrigerators,
            R.drawable.freezers, R.drawable.dhw,
            R.drawable.renewables, R.drawable.api
    };

}
