package com.example.equipmentstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.equipmentstore.R;
import com.example.equipmentstore.fragments.PostOffice;
import com.example.equipmentstore.models.PostOfficeModel;

import java.util.ArrayList;

public class TypeOfPostsAdapter extends BaseAdapter {

    private ArrayList<PostOfficeModel> arrayList;
    private LayoutInflater inflater;

    public TypeOfPostsAdapter(ArrayList<PostOfficeModel> arrayList, Context context) {
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);
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
        convertView = inflater.inflate(R.layout.spinner_items, null);
        TextView tv_role = convertView.findViewById(R.id.spinner_txt);
        tv_role.setText(arrayList.get(position).getRole());
        return convertView;
    }
}
