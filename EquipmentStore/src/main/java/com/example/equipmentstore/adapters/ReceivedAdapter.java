package com.example.equipmentstore.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.equipmentstore.R;
import com.example.equipmentstore.models.EquipmentDetails;

import java.util.List;

public class ReceivedAdapter extends RecyclerView.Adapter<ReceivedAdapter.Approvals_ViewHolder> {
    Context context;
    CameraInterface camerainterface;
    private List<EquipmentDetails> arrayList;

    public ReceivedAdapter(Context context, List<EquipmentDetails> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public void adopterListener(CameraInterface camerainterface) {
        this.camerainterface = camerainterface;
    }

    @NonNull
    @Override
    public Approvals_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.approval_adapter, parent, false);
        return new Approvals_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Approvals_ViewHolder holder, int i) {
        final EquipmentDetails equipmentDetails = arrayList.get(i);

        holder.tv_id.setText(equipmentDetails.getID());
        holder.tv_username.setText(equipmentDetails.getUSER_NAME());
        holder.tv_sudivcode.setText(equipmentDetails.getSUBDIV_CODE());
        holder.tv_equipments.setText(equipmentDetails.getITEMS());
        holder.tv_approveddate.setText(equipmentDetails.getAPPROVED_DATE());

        holder.management.setVisibility(View.GONE);
        holder.remark.setVisibility(View.GONE);
        holder.received.setVisibility(View.GONE);
        holder.request.setVisibility(View.GONE);
        holder.mob_no.setVisibility(View.GONE);
        if (TextUtils.isEmpty(equipmentDetails.getAPPROVED_DATE()))
            holder.button.setVisibility(View.GONE);
        else holder.button.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface CameraInterface {
        public void getReceivedData(EquipmentDetails details);
    }

    public class Approvals_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_id, tv_username, tv_equipments, tv_sudivcode, tv_approveddate;
        Button button;
        LinearLayout main_layout, management, remark, received, request, mob_no;

        public Approvals_ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.txt_id);
            tv_username = itemView.findViewById(R.id.txt_user_name);
            tv_equipments = itemView.findViewById(R.id.txt_equipments);
            tv_sudivcode = itemView.findViewById(R.id.txt_subdiv_code);
            tv_approveddate = itemView.findViewById(R.id.txt_approved_date);
            button = itemView.findViewById(R.id.btn_submit);
            button.setOnClickListener(this);
            main_layout = itemView.findViewById(R.id.lin_main_layout);
            management = itemView.findViewById(R.id.lin_management);
            remark = itemView.findViewById(R.id.lin_remark);
            received = itemView.findViewById(R.id.lin_received_date);
            request = itemView.findViewById(R.id.lin_req_date);
            mob_no = itemView.findViewById(R.id.lin_mob_no);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (view.getId() == R.id.btn_submit) {
                EquipmentDetails objectt = arrayList.get(pos);
                camerainterface.getReceivedData(objectt);
            }
        }
    }
}



