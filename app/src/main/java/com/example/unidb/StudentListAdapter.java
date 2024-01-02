package com.example.unidb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class StudentListAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap> data;
    private static LayoutInflater inflater = null;
    int[] bgColors = new int[]{0xFFe5c3c6, 0xFFe1e9b7,0xFFbcd2d0,0xFF9bedff,0xFFfffd8d};

    public StudentListAdapter(Context context, ArrayList<HashMap> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getColor() {
        int i = new Random().nextInt(bgColors.length);
        return bgColors[i];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, null);
        }
        // Assign elements
        TextView id = view.findViewById(R.id.id);
        TextView name = view.findViewById(R.id.name);
        TextView surname = view.findViewById(R.id.surname);
        LinearLayout wrapper = view.findViewById(R.id.wrapper);
        // Change element background
        wrapper.setBackgroundColor(getColor());
        // Change list item data
        id.setText((String) data.get(position).get("id"));
        name.setText((String) data.get(position).get("name"));
        surname.setText((String) data.get(position).get("surname"));
        // Return view of the list item
        return view;
    }
}