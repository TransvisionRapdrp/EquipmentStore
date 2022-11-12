package com.example.equipmentstore.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.equipmentstore.MainActivity;
import com.example.equipmentstore.R;
import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.extra.SharedPreferanceStore;

import static com.example.equipmentstore.extra.constants.MyPREFERENCES;
import static com.example.equipmentstore.extra.constants.sPref_Group;

public class MainFragment extends Fragment implements View.OnClickListener {

    View view;
    RelativeLayout req_equipment, mgmnt_approval, received_approval, view_complaint, view_approvals, attendane_report, relative_attendane_insert, relative_hardware_complaint, relative_hardware_report, relative_post_recevied;

    SharedPreferences settings;
    FunctionCall functionCall;

    public MainFragment() {
        //required empty constructor in fragments
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);
        initialization();
        return view;
    }

    private void initialization() {
        req_equipment = view.findViewById(R.id.relative_request_equipment);
        req_equipment.setOnClickListener(this);
        mgmnt_approval = view.findViewById(R.id.relative_managment_approval);
        mgmnt_approval.setOnClickListener(this);
        received_approval = view.findViewById(R.id.relative_recevied_approval);
        received_approval.setOnClickListener(this);
        view_approvals = view.findViewById(R.id.relative_view_approval);
        view_approvals.setOnClickListener(this);
        attendane_report = view.findViewById(R.id.relative_attendane_report);
        attendane_report.setOnClickListener(this);
        relative_attendane_insert = view.findViewById(R.id.relative_attendane_insert);
        relative_attendane_insert.setOnClickListener(this);
//        relative_attendane_insert.setVisibility(View.GONE);
//        attendane_report.setVisibility(View.GONE);
        relative_hardware_complaint = view.findViewById(R.id.relative_hardware_complaint);
        relative_hardware_complaint.setOnClickListener(this);
        relative_post_recevied = view.findViewById(R.id.relative_post_recevied);
        relative_post_recevied.setOnClickListener(this);

        relative_hardware_report = view.findViewById(R.id.relative_hardware_report);
        relative_hardware_report.setOnClickListener(this);
        settings = requireActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (settings.getString(sPref_Group, "0").equals("Management"))
            mgmnt_approval.setVisibility(View.VISIBLE);
        else mgmnt_approval.setVisibility(View.GONE);
        functionCall = new FunctionCall();
    }

    @Override
    public void onClick(View view) {
        NavController navController = Navigation.findNavController(view);
        if (view.getId() == R.id.relative_request_equipment) {
            navController.navigate(R.id.action_main_to_equipment_insert);
           // ((MainActivity) requireActivity()).switchContent(MainActivity.Steps.FORM0, "Request Equipment");
        } else if (view.getId() == R.id.relative_view_approval) {
            navController.navigate(R.id.action_main_to_view);
           // ((MainActivity) requireActivity()).switchContent(MainActivity.Steps.FORM2, "View Approvals");
        } else if (view.getId() == R.id.relative_managment_approval) {
            navController.navigate(R.id.action_main_to_management_nav);
           // ((MainActivity) requireActivity()).switchContent(MainActivity.Steps.FORM4, "Management");
        } else if (view.getId() == R.id.relative_recevied_approval) {
            navController.navigate(R.id.action_main_to_nav_recieved);
           // ((MainActivity) requireActivity()).switchContent(MainActivity.Steps.FORM1, "Received Approval");
        } else if (view.getId() == R.id.relative_attendane_report) {
            navController.navigate(R.id.action_main_to_attendanceReport);
           // ((MainActivity) requireActivity()).switchContent(MainActivity.Steps.FORM9, "Attendance Report");
        } else if (view.getId() == R.id.relative_attendane_insert) {
            SharedPreferanceStore store = SharedPreferanceStore.getInstance(getActivity());
            if (functionCall.getDays(store.getString(SharedPreferanceStore.Key.CURRENT_DATE, functionCall.dateSet())) > 5
                && !store.getString(SharedPreferanceStore.Key.REMARK_FLG,"0").equals("1"))
                navController.navigate(R.id.action_main_to_receviedPost);
                //((MainActivity) requireActivity()).switchContent(MainActivity.Steps.FORM13, "Post Received");
            else navController.navigate(R.id.action_main_to_attendanceInsert);
                /*((MainActivity) requireActivity()).switchContent(MainActivity.Steps.FORM10, "Attendance Insert");*/
        } else if (view.getId() == R.id.relative_hardware_complaint) {
            navController.navigate(R.id.action_main_to_hardwareComplaint);
           // ((MainActivity) requireActivity()).switchContent(MainActivity.Steps.FORM11, "Hardware Complaint");
        } else if (view.getId() == R.id.relative_hardware_report) {
            navController.navigate(R.id.action_main_to_hardwareComplaintReport);
          //  ((MainActivity) requireActivity()).switchContent(MainActivity.Steps.FORM12, "Hardware Complaint Report");
        } else if (view.getId() == R.id.relative_post_recevied) {
            navController.navigate(R.id.action_main_to_receviedPost);
            //((MainActivity) requireActivity()).switchContent(MainActivity.Steps.FORM13, "Post Received");
        }
    }
}
