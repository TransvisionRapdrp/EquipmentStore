package com.example.equipmentstore.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.equipmentstore.R;
import com.example.equipmentstore.adapters.ComplaintAdapter;
import com.example.equipmentstore.adapters.ComplaintAdapter1;
import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.interfac.ResponseInterface1;
import com.example.equipmentstore.models.Complaints;
import com.example.equipmentstore.models.EquipmentModel;
import com.example.equipmentstore.models.PostOfficeModel;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewComplaint extends Fragment implements ResponseInterface1 {

    View view;
    LinearLayout layout;
    FunctionCall functionCall;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    List<Complaints> complaintList;
    ComplaintAdapter1 adapter;

    public ViewComplaint() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_complaint, container, false);
        initialize();
        return view;
    }

    //-------------------------------------------------------------------------------------------------------------
    private void initialize() {
        layout = view.findViewById(R.id.lin_main);
        functionCall = new FunctionCall();
        recyclerView = view.findViewById(R.id.rec_view_complaint);
        progressDialog = new ProgressDialog(getContext());
        complaintList = new ArrayList<>();

        functionCall.showprogressdialog("Please wait to complete", progressDialog, "Data Loading");
        getHardwareComplaints();
    }

    //1)Request or post data **********************************************************************************************************
    public void getHardwareComplaints() {
        RetroClient retroClient = new RetroClient();
        RegisterApi api = retroClient.getApiService();
        api.getHardwareComplaints().enqueue(new Callback<List<Complaints>>() {
            @Override
            public void onResponse(@NonNull Call<List<Complaints>> call, @NonNull Response<List<Complaints>> response) {
                if (response.isSuccessful()) {
                    complaintList.clear();
                    for (int i = 0 ; i < response.body().size() ; i ++ ){
                        Complaints equipmentModel = response.body().get(i);
                        if (!TextUtils.isEmpty(equipmentModel.getDATE_TIME()))
                            equipmentModel.setDATE_TIME(ConvertJsonDate_to_date(equipmentModel.getDATE_TIME()));
                        complaintList.add(equipmentModel);
                    }

                    adapter = new ComplaintAdapter1( complaintList);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                    responseSuccess(new PostOfficeModel());
                } else responseFailure();
            }

            @Override
            public void onFailure(@NonNull Call<List<Complaints>> call, @NonNull Throwable t) {
                responseFailure();
            }
        });
    }

    public String ConvertJsonDate_to_date(String date) {
        String date1 = date.replace("/Date(", "").replace(")/", "");
        long time = Long.parseLong(date1);
        Date d = new Date(time);
        return new SimpleDateFormat("MM/dd/yyyy").format(d);
    }

    @Override
    public void responseSuccess(PostOfficeModel postOfficeModel) {
        progressDialog.dismiss();
    }

    @Override
    public void responseFailure() {
        progressDialog.dismiss();
        functionCall.showToast(getContext(), "Data Not Found");
    }
}