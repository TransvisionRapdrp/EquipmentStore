package com.example.equipmentstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.equipmentstore.R;
import com.example.equipmentstore.models.EquipmentModel;

import java.util.List;

public class CheckBoxAdapter extends BaseAdapter {

    private List<EquipmentModel> equipmentModelList;
    private Context context;
    private LayoutInflater inflater;
    private boolean isFromView = false;

    public CheckBoxAdapter(List<EquipmentModel> arrayList, Context context) {
        this.equipmentModelList = arrayList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
       return equipmentModelList.size();
    }

    @Override
    public Object getItem(int i) {

        return equipmentModelList.get(i);
    }

    @Override
    public long getItemId(int i)
    {

        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(context);
            view = layoutInflator.inflate(R.layout.role_spinner_items, null);
            holder = new ViewHolder();
            holder.mTextView = view.findViewById(R.id.text);
            holder.mCheckBox = view.findViewById(R.id.checkbox);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTextView.setText(equipmentModelList.get(i).getNAMES());
        isFromView = true;
        holder.mCheckBox.setChecked(equipmentModelList.get(i).isSelected());
        isFromView = false;

        if ((i == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(i);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isFromView) {
                    equipmentModelList.get(i).setSelected(isChecked);
                }
            }
        });
        return view;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }

    public List<EquipmentModel> getApprovedList() {
        return equipmentModelList;
    }
}
