package com.example.equipmentstore.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.equipmentstore.R;
import com.example.equipmentstore.models.EquipmentModel;

import java.util.List;

public class ManagementAdapter extends RecyclerView.Adapter<ManagementAdapter.ApproveHolder>{
    public List<EquipmentModel> equipmentModelList;
    public List<EquipmentModel> equipmentModelListSelected;
    Context context1;

    public ManagementAdapter(List<EquipmentModel> equipmentModelList, List<EquipmentModel> equipmentModelListSelected, Context context1) {
        this.equipmentModelList = equipmentModelList;
        this.equipmentModelListSelected = equipmentModelListSelected;
        this.context1 = context1;
    }

    @NonNull
    @Override
    public ApproveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.management_approval_adapter, parent, false);
        return new ApproveHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveHolder holder, int position) {
         EquipmentModel equipmentDetails = equipmentModelList.get(position);
//        if (TextUtils.isEmpty(equipmentModelList.get(position).getAPPROVED_FLAG())){
//        if (!equipmentModelList.get(position).getAPPROVED_FLAG().equals("Y")) {
            holder.txt_id.setText(equipmentModelList.get(position).getID());
            holder.txt_user_name.setText(equipmentModelList.get(position).getUSER_NAME());
            holder.txt_equipments.setText(equipmentModelList.get(position).getITEMS());
            holder.txt_mob_no.setText(equipmentModelList.get(position).getMOBILE_NO());
            holder.txt_sudiv_code.setText(equipmentModelList.get(position).getSUBDIV_CODE());
            holder.txt_req_date.setText(equipmentModelList.get(position).getREQUESTED_DATE());
            if (!TextUtils.isEmpty(equipmentModelList.get(position).getREMARK())) {
                holder.txt_reamrk.setText(equipmentModelList.get(position).getREMARK());
            } else holder.remark.setVisibility(View.GONE);
            holder.received.setVisibility(View.GONE);
            holder.approved.setVisibility(View.GONE);
//        }

        if(equipmentModelListSelected.contains(equipmentModelList.get(position)))
            holder.main_layout.setBackgroundColor(ContextCompat.getColor(context1, R.color.purple_200));
        else
            holder.main_layout.setBackgroundColor(ContextCompat.getColor(context1, R.color.white));

        holder.tv_chiefApproval.setChecked(false);
        holder.tv_chiefApproval.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                equipmentDetails.setSelected(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return equipmentModelList.size();
    }

    public class ApproveHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_id, txt_user_name, txt_equipments, txt_mob_no, txt_sudiv_code, txt_reamrk, txt_req_date, txt_call;
        CheckBox tv_chiefApproval;
        LinearLayout main_layout, management, remark, approved, received, received_btn;

        public ApproveHolder(@NonNull View itemView) {
            super(itemView);
            txt_id = itemView.findViewById(R.id.txt_id);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            txt_equipments = itemView.findViewById(R.id.txt_equipments);
            txt_mob_no = itemView.findViewById(R.id.txt_mob_no);
            txt_sudiv_code = itemView.findViewById(R.id.txt_subdiv_code);
            txt_reamrk = itemView.findViewById(R.id.txt_remark);
            txt_req_date = itemView.findViewById(R.id.txt_req_date);
            tv_chiefApproval = itemView.findViewById(R.id.txt_chief_approval);
            management = itemView.findViewById(R.id.lin_management);
            main_layout = itemView.findViewById(R.id.lin_main_layout);
            remark = itemView.findViewById(R.id.lin_remark);
            approved = itemView.findViewById(R.id.lin_approved_date);
            received = itemView.findViewById(R.id.lin_received_date);
             txt_call = itemView.findViewById(R.id.txt_call);
            txt_call.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(view.getId() == R.id.txt_call){
                String mobile_no = equipmentModelList.get(position).getMOBILE_NO();
                if (!TextUtils.isEmpty(mobile_no)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mobile_no, null));
                    context1.startActivity(intent);
                } else {
                    Toast.makeText(context1, "Mobile number is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public List<EquipmentModel> getApprovedListData() {
        return equipmentModelList;
    }
}
