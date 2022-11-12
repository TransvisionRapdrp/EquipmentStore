package com.example.equipmentstore.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.equipmentstore.R;
import com.example.equipmentstore.models.Complaints;

import java.util.List;

public class ComplaintAdapter1 extends RecyclerView.Adapter<ComplaintAdapter1.ApproveHolder>{
    private final List<Complaints> viewComplaintList;

    public ComplaintAdapter1(List<Complaints> viewComplaintList) {
        this.viewComplaintList = viewComplaintList;
    }

    @NonNull
    @Override
    public ApproveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_adapter, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ComplaintAdapter1.ApproveHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveHolder approveHolder, int position) {
        Complaints attendanceSummary = viewComplaintList.get(position);
        approveHolder.tv_id.setText(String.valueOf(position + 1));
        approveHolder.tv_name.setText(attendanceSummary.getNAME());
        approveHolder.tv_subdiv.setText(attendanceSummary.getSUBDIVCODE());
        approveHolder.tv_date_time.setText(attendanceSummary.getDATE_TIME());
        approveHolder.tv_type.setText(attendanceSummary.getTYPE());
        approveHolder.tv_description.setText(attendanceSummary.getDESCRIPTION());
    }

    @Override
    public int getItemCount() {
        return viewComplaintList.size();
    }


    public class ApproveHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_subdiv, tv_date_time, tv_type, tv_id, tv_description;

        public ApproveHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.txt_user_name);
            tv_subdiv = itemView.findViewById(R.id.txt_subdiv_code);
            tv_date_time = itemView.findViewById(R.id.txt_date_time);
            tv_type = itemView.findViewById(R.id.txt_type);
            tv_id = itemView.findViewById(R.id.txt_id);
            tv_description = itemView.findViewById(R.id.txt_desc);
        }
    }
}
