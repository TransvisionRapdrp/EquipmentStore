package com.example.equipmentstore.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.equipmentstore.R;
import com.example.equipmentstore.models.AttendanceSummary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AttendanceRepAdapter extends RecyclerView.Adapter<AttendanceRepAdapter.ApproveHolder>{

    private List<AttendanceSummary> arrayList;
    private Context context;
    private static int currentPosition = 0;
    private boolean aBoolean = false;

    public AttendanceRepAdapter(List<AttendanceSummary> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ApproveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_summary, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new AttendanceRepAdapter.ApproveHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveHolder approveHolder, int position) {
        AttendanceSummary attendanceSummary = arrayList.get(position);
        approveHolder.tv_id.setText(String.valueOf(position + 1));
        approveHolder.tv_name.setText(attendanceSummary.getUSERNAME());
        approveHolder.tv_subdiv.setText(attendanceSummary.getSUBDIVCODE());
        approveHolder.tv_date_time.setText(attendanceSummary.getDATETIME());
        approveHolder.tv_remark.setText(attendanceSummary.getREMARK());
        approveHolder.tv_location.setText(attendanceSummary.getADDRESS());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ApproveHolder extends RecyclerView.ViewHolder{
        TextView tv_name, tv_subdiv, tv_date_time, tv_remark, tv_id,tv_location;
        LinearLayout layout;

        public ApproveHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.txt_user_name);
            tv_subdiv = itemView.findViewById(R.id.txt_subdiv_code);
            tv_date_time = itemView.findViewById(R.id.txt_date_time);
            tv_remark = itemView.findViewById(R.id.txt_remark);
            tv_id = itemView.findViewById(R.id.txt_id);
            tv_location= itemView.findViewById(R.id.txt_location);
        }
    }
}
