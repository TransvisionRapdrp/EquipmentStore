package com.example.equipmentstore.extra;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.equipmentstore.R;
import com.example.equipmentstore.models.EquipmentModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TrackBottomSheet extends BottomSheetDialogFragment {

    View view_order_placed, view_order_confirmed, view_order_processed,
            con_divider, placed_divider;
    ImageView img_orderconfirmed, orderprocessed;
    TextView text_confirmed, textorderprocessed, txt_request_equipment, txt_managment_approval_date,
            txt_received_date;
    View view;

    public TrackBottomSheet() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.bottom_sheet_approval, container, false);
        Bundle bundle = getArguments();
        EquipmentModel equipmentModel = (EquipmentModel) bundle.getSerializable("Data");
        TextView name = view.findViewById(R.id.name);
        name.setText(equipmentModel.getUSER_NAME());
        TextView txt_subdiv_code = view.findViewById(R.id.txt_subdiv_code);
        txt_subdiv_code.setText(equipmentModel.getSUBDIV_CODE());
        TextView txt_item_list = view.findViewById(R.id.txt_item_list);
        txt_item_list.setText(equipmentModel.getITEMS());
        view_order_placed = view.findViewById(R.id.view_order_placed);
        view_order_confirmed = view.findViewById(R.id.view_order_confirmed);
        view_order_processed = view.findViewById(R.id.view_order_processed);
        placed_divider = view.findViewById(R.id.placed_divider);
        con_divider = view.findViewById(R.id.con_divider);
        text_confirmed = view.findViewById(R.id.text_confirmed);
        textorderprocessed = view.findViewById(R.id.textorderprocessed);
        img_orderconfirmed = view.findViewById(R.id.img_orderconfirmed);
        orderprocessed = view.findViewById(R.id.orderprocessed);

        txt_request_equipment = view.findViewById(R.id.txt_request_equipment);
        txt_request_equipment.setText(equipmentModel.getREQUESTED_DATE());
        txt_managment_approval_date = view.findViewById(R.id.txt_managment_approval_date);
        String requestDate = equipmentModel.getREQUESTED_DATE();
        String approvalDate = equipmentModel.getAPPROVED_DATE();
        String receviedDate = equipmentModel.getRECEIVED_DATE();

        if (TextUtils.isEmpty(approvalDate) && TextUtils.isEmpty(receviedDate))
            getOrderStatus("0");
        else if (!TextUtils.isEmpty(approvalDate) && TextUtils.isEmpty(receviedDate))
            getOrderStatus("1");
        else if (!TextUtils.isEmpty(approvalDate) && !TextUtils.isEmpty(receviedDate))
            getOrderStatus("2");

        if (TextUtils.isEmpty(equipmentModel.getAPPROVED_DATE()))
            txt_managment_approval_date.setText("Pending");
        else txt_managment_approval_date.setText(equipmentModel.getAPPROVED_DATE());
        txt_received_date = view.findViewById(R.id.txt_received_date);

        if (TextUtils.isEmpty(equipmentModel.getRECEIVED_DATE()))
            txt_received_date.setText("Pending");
        else txt_received_date.setText(equipmentModel.getRECEIVED_DATE());

        return view;
    }

    private void getOrderStatus(String orderStatus) {
        switch (orderStatus) {
            case "0": {
                float alfa = (float) 0.5;
                setStatus(alfa);
                break;
            }
            case "1": {
                float alfa = (float) 1;
                setStatus1(alfa);
                break;
            }
            case "2": {
                float alfa = (float) 1;
                setStatus2(alfa);
                break;
            }
        }
    }


    private void setStatus(float alfa) {
        float myf = (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        orderprocessed.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        placed_divider.setAlpha(alfa);
        img_orderconfirmed.setAlpha(alfa);
        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);
    }

    private void setStatus1(float alfa) {
        float myf = (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(myf);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        img_orderconfirmed.setAlpha(alfa);

        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(myf);
    }

    private void setStatus2(float alfa) {
        float myf = (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        img_orderconfirmed.setAlpha(alfa);

        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);

    }
}
