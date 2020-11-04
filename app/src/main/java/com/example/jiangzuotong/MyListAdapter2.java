package com.example.jiangzuotong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyListAdapter2 extends ArrayAdapter {

    public MyListAdapter2(Context context, int resource, ArrayList<HashMap<String, String>> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.mylist2, parent, false);
        }

        Map<String, String> map = (Map<String, String>) getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.listTitle2);
        TextView detail = (TextView) itemView.findViewById(R.id.listDate2);
        title.setText(map.get("ItemTitle"));
        detail.setText(map.get("ItemDate"));
        return itemView;
    }
}