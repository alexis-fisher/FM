package com.example.alyx.controller;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.example.alyx.server.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Alyx on 4/14/17.
 */

public class SearchAdapter extends BaseExpandableListAdapter {
    private SearchAdapter(){}

    private Context myContext;
    private List<String> data;
    private HashMap<String,List<String>> childData;
    public SearchAdapter (Context context, List<String> data,
                          HashMap<String, List<String>> childData){
        this.myContext = context;
        this.data = data;
        this.childData = childData;
    }


    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childData.get(data.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(data.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // SET ME UP!
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition,childPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView = inflater.inflate(R.layout.listitemperson, null);
        }

        // CUSTOMIZE ICONS BASED ON WHAT DATA WE GET!
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
