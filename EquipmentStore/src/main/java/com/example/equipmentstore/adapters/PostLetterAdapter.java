package com.example.equipmentstore.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.equipmentstore.R;
import com.example.equipmentstore.models.EquipmentDetails;
import com.example.equipmentstore.models.PostOfficeModel;

import java.util.List;

public class PostLetterAdapter extends RecyclerView.Adapter<PostLetterAdapter.PostHolder>{

    List<PostOfficeModel> modelList;
    Context context;
    Camerainterface camerainterface;

    public PostLetterAdapter(List<PostOfficeModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    public void adopterlistenr(Camerainterface camerainterface) {
        this.camerainterface = camerainterface;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.postletters_items, parent, false);
        return new PostLetterAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.txt_id.setText(modelList.get(position).getID());
        holder.txt_letter_from.setText(modelList.get(position).getLetterFrom());
        holder.txt_type_post.setText(modelList.get(position).getType_of_post());
        if (TextUtils.isEmpty(modelList.get(position).getStatus())){
            holder.btn_submit.setVisibility(View.VISIBLE);
            holder.status.setVisibility(View.GONE);
        } else if (modelList.get(position).getStatus().trim().equals("Y")){
            holder.btn_submit.setVisibility(View.GONE);
            holder.status.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txt_id,txt_type_post,txt_letter_from,status;
        Button btn_submit;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            txt_id = itemView.findViewById(R.id.txt_id);
            txt_type_post = itemView.findViewById(R.id.txt_type_post);
            txt_letter_from = itemView.findViewById(R.id.txt_letter_from);
            btn_submit = itemView.findViewById(R.id.btn_submit);
            btn_submit.setOnClickListener(this);
            status = itemView.findViewById(R.id.status);
            status.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (view.getId() == R.id.btn_submit) {
                PostOfficeModel objectt = modelList.get(pos);
                camerainterface.getreceiveddata(objectt);
            } else if (view.getId() == R.id.status) {
                camerainterface.getPendingResult(modelList.get(pos));
            }
        }
    }

    public interface Camerainterface {
        public void getreceiveddata(PostOfficeModel details);
        public void getPendingResult(PostOfficeModel postOfficeModel);
    }
}
