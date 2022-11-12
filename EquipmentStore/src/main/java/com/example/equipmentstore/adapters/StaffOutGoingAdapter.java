package com.example.equipmentstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.equipmentstore.R;
import com.example.equipmentstore.models.Staff_OutGoingModel;

import java.util.ArrayList;

public class StaffOutGoingAdapter extends BaseAdapter {

    private ArrayList<Staff_OutGoingModel> arrayList;
    private LayoutInflater layoutInflater;
    public StaffOutGoingAdapter(ArrayList<Staff_OutGoingModel> arrayList1, Context context){
        this.arrayList = arrayList1;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.spinner_items2, null);
        TextView tv_role = convertView.findViewById(R.id.spinner_txt2);
        tv_role.setText(arrayList.get(position).getModeoftransport());
        return convertView;
    }
}
