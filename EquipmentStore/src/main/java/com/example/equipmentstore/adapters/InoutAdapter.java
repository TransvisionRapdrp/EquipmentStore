package com.example.equipmentstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.equipmentstore.R;
import com.example.equipmentstore.models.PostOfficeModel;

import java.util.ArrayList;

public class InoutAdapter extends BaseAdapter {
    private ArrayList<PostOfficeModel> arrayList1;
    private LayoutInflater inflater1;

    public InoutAdapter(ArrayList<PostOfficeModel> arrayList1, Context context1) {
        this.arrayList1 = arrayList1;
        inflater1 = LayoutInflater.from(context1);
    }

    @Override
    public int getCount() {
        return arrayList1.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater1.inflate(R.layout.spinner_items1, null);
        TextView tv_role = convertView.findViewById(R.id.spinner_txt1);
        tv_role.setText(arrayList1.get(position).getRole());
        return convertView;
    }
}
