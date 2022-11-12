package com.example.equipmentstore.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.equipmentstore.R;
import com.example.equipmentstore.models.EquipmentModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ApproveHolder> implements Filterable {
    private List<EquipmentModel> viewComplaintList;
    private Context context;
    private List<EquipmentModel> filterList;
    private OnClickListinear onClickListinear;

    public ComplaintAdapter(Context context1, List<EquipmentModel> complaintList) {
        this.viewComplaintList = complaintList;
        this.context = context1;
        this.filterList = complaintList;
    }

    public void setOnClickListinear(OnClickListinear onClickListinear) {
        this.onClickListinear = onClickListinear;
    }

    @NonNull
    @Override
    public ApproveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewcomplaintadapter, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ApproveHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveHolder holder, int position) {
        holder.tv_id.setText(filterList.get(position).getID());
        holder.tv_name.setText(filterList.get(position).getUSER_NAME());
        holder.tv_subdiv.setText(filterList.get(position).getSUBDIV_CODE());
        holder.tv_equipments.setText(filterList.get(position).getITEMS());
        holder.tv_mobile_no.setText(filterList.get(position).getMOBILE_NO());
        holder.tv_req_date.setText(filterList.get(position).getREQUESTED_DATE());
        if (!TextUtils.isEmpty(filterList.get(position).getREMARK())) {
            holder.tv_reamrk.setText(filterList.get(position).getREMARK());
        } else holder.remark.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(filterList.get(position).getAPPROVED_DATE())) {
            holder.tv_app_date.setText(filterList.get(position).getAPPROVED_DATE());
        } else holder.tv_app_date.setText("Pending");
        if (!TextUtils.isEmpty(filterList.get(position).getRECEIVED_DATE())) {
            holder.tv_rec_date.setText(filterList.get(position).getRECEIVED_DATE());
        } else holder.tv_rec_date.setText("Pending");

        holder.status.setText("View Status");
        holder.status.setBackground(context.getResources().getDrawable(R.drawable.pending_bg));
        animate(holder);
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    public String ConvertJsonDate_to_date(String date) {
        String date1 = date.replace("/Date(", "").replace(")/", "");
        long time = Long.parseLong(date1);
        Date d = new Date(time);
        return new SimpleDateFormat("MM/dd/yyyy").format(d);
    }

    @Override
    public int getItemCount() {
        return viewComplaintList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if (search.isEmpty())
                    filterList = viewComplaintList;
                else {
                    ArrayList<EquipmentModel> filterlist = new ArrayList<>();
                    for (int i = 0; i < viewComplaintList.size(); i++) {
                        EquipmentModel equipmentDetails = viewComplaintList.get(i);
                        if (equipmentDetails.getID().contains(search)) {
                            filterlist.add(equipmentDetails);
                        }
                    }
                    filterList = filterlist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterList = (ArrayList<EquipmentModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ApproveHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_id, tv_name, tv_subdiv, tv_mobile_no, tv_equipments,
                tv_reamrk, tv_req_date, tv_app_date, tv_rec_date, tv_call, status;
        LinearLayout main_layout, management, remark, approved, received, received_btn;

        public ApproveHolder(@NonNull View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.txt_id);
            tv_name = itemView.findViewById(R.id.txt_user_name);
            tv_subdiv = itemView.findViewById(R.id.txt_subdiv_code);
            tv_mobile_no = itemView.findViewById(R.id.txt_mob_no);
            tv_equipments = itemView.findViewById(R.id.txt_equipments);
            tv_reamrk = itemView.findViewById(R.id.txt_remark);
            tv_req_date = itemView.findViewById(R.id.txt_req_date);
            tv_app_date = itemView.findViewById(R.id.txt_approved_date);
            tv_rec_date = itemView.findViewById(R.id.txt_received_date);
            management = itemView.findViewById(R.id.lin_management);
            main_layout = itemView.findViewById(R.id.lin_main_layout);
            main_layout.setOnClickListener(this);
            remark = itemView.findViewById(R.id.lin_remark);
            approved = itemView.findViewById(R.id.lin_approved_date);
            received = itemView.findViewById(R.id.lin_received_date);
            //  received_btn = itemView.findViewById(R.id.lin_received_btn);
            tv_call = itemView.findViewById(R.id.txt_call);
            tv_call.setOnClickListener(this);
            status = itemView.findViewById(R.id.status);
            status.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (view.getId() == R.id.txt_call) {
                String mobile_no = viewComplaintList.get(pos).getMOBILE_NO();
                if (!TextUtils.isEmpty(mobile_no)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mobile_no, null));
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Please Insert Ur Sim because Mobile Number Is Not Available.", Toast.LENGTH_SHORT).show();
                }

            } else if (view.getId() == R.id.status) {
                onClickListinear.onItemClickLisnernter(filterList.get(pos));
            }
        }
    }

    public interface OnClickListinear {
        public void onItemClickLisnernter(EquipmentModel equipmentModel);
    }
}
