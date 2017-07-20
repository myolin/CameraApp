package com.myolin.optimiser;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mzlmy on 7/19/2017.
 */

public class CustomListViewAdapter extends ArrayAdapter {

    Context context;
    private SparseBooleanArray selectedListItemsIds;
    List multipleSelectionList;

    public CustomListViewAdapter(Context context, int resourceId, List items){
        super(context, resourceId, items);
        this.context = context;
        selectedListItemsIds = new SparseBooleanArray();
        this.multipleSelectionList = items;
    }

    private class ViewHolder {
        TextView txtProjectName;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        Project p = (Project) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.project_layout, null);
            holder = new ViewHolder();
            holder.txtProjectName = (TextView) convertView.findViewById(R.id.pName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtProjectName.setText(p.getName());

        return convertView;
    }

    public void remove(Project object) {
        multipleSelectionList.remove(object);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position){
        selectView(position, !selectedListItemsIds.get(position));
    }

    public void removeSelection(){
        selectedListItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value){
        if(value){
            selectedListItemsIds.put(position, value);
        }else{
            selectedListItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount(){
        return selectedListItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds(){
        return selectedListItemsIds;
    }


}
