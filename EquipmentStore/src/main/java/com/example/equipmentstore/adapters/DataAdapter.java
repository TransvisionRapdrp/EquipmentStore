package com.example.equipmentstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.equipmentstore.R;
import com.example.equipmentstore.models.PostOfficeModel;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.AddressViewHolder>{

    private List<PostOfficeModel> mDataset;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public DataAdapter(List<PostOfficeModel> mDataset, RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.mDataset = mDataset;
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adress_tem, parent, false);

        AddressViewHolder vh = new AddressViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        holder.tv_address.setText(mDataset.get(position).getBranch_Name());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_address;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_address = itemView.findViewById(R.id.textView);
            tv_address.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.clickOnItem(mDataset.get(getAdapterPosition()));
        }
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(PostOfficeModel data);
    }
}
