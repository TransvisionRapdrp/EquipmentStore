package com.example.equipmentstore.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.equipmentstore.extra.FunctionCall;

import com.example.equipmentstore.R;
import com.example.equipmentstore.extra.TrackBottomSheet;
import com.example.equipmentstore.interfac.ResponseInterface;
import com.example.equipmentstore.adapters.ComplaintAdapter;

import com.example.equipmentstore.models.EquipmentModel;
import com.example.equipmentstore.models.LoginDetails;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewFragment extends Fragment implements ResponseInterface,
        View.OnClickListener, ComplaintAdapter.OnClickListinear {

    View view;
    FunctionCall functionCall;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    List<EquipmentModel> equipmentModelList;
    ComplaintAdapter viewadapter;
    private List<LoginDetails> loginDetails;
    Context context;
    LinearLayout linear_internet_not_found;
    Button retry;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    ViewFragment viewFragment;

    public ViewFragment() {
        //required empty constructor for fragments.....
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_view, container, false);
        viewFragment = this;

        intialization();

        return view;

    }

    private void intialization() {
        context = getActivity();
        functionCall = new FunctionCall();
        linearLayout = view.findViewById(R.id.linear);
        progressDialog = new ProgressDialog(getActivity());
        linear_internet_not_found = view.findViewById(R.id.lin_internet);
        recyclerView = view.findViewById(R.id.rec_view_complaint);
        equipmentModelList = new ArrayList<>();
        viewadapter = new ComplaintAdapter(getContext(), equipmentModelList);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.HORIZONTAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(viewadapter);
        viewadapter.setOnClickListinear(viewFragment);
        if (functionCall.isInternetOn(getActivity())) {
            GettingEquipmentData(this);
        } else {
            linear_internet_not_found.setVisibility(View.VISIBLE);
        }

        retry = view.findViewById(R.id.retry_btn);
        retry.setOnClickListener(this);



    }

    public void GettingEquipmentData(ResponseInterface responseInterface) {
        RetroClient retroClient = new RetroClient();
        RegisterApi registerApi = retroClient.getApiService();
        registerApi.getData().enqueue(new Callback<List<EquipmentModel>>() {
            @Override
            public void onResponse(Call<List<EquipmentModel>> call, Response<List<EquipmentModel>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                     response.body();
                     equipmentModelList.clear();
                    for (int i = 0 ; i < response.body().size() ; i ++ ){
                        EquipmentModel equipmentModel = response.body().get(i);
                        if (!TextUtils.isEmpty(equipmentModel.getREQUESTED_DATE()))
                        equipmentModel.setREQUESTED_DATE(ConvertJsonDate_to_date(equipmentModel.getREQUESTED_DATE()));
                        if (!TextUtils.isEmpty(equipmentModel.getAPPROVED_DATE()))
                        equipmentModel.setAPPROVED_DATE(ConvertJsonDate_to_date(equipmentModel.getAPPROVED_DATE()));
                        if (!TextUtils.isEmpty(equipmentModel.getRECEIVED_DATE()))
                        equipmentModel.setRECEIVED_DATE(ConvertJsonDate_to_date(equipmentModel.getRECEIVED_DATE()));
                        equipmentModelList.add(equipmentModel);
                    }

                    Collections.reverse(equipmentModelList);
                    recyclerView = view.findViewById(R.id.rec_view_complaint);
                    viewadapter = new ComplaintAdapter(getContext(), equipmentModelList);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(viewadapter);
                    viewadapter.setOnClickListinear(viewFragment);
                    responseInterface.responseSuccess(response.body().get(0));
                    progressDialog.dismiss();
                } else {
                    responseInterface.responseFailure();
                }
            }

            @Override
            public void onFailure(Call<List<EquipmentModel>> call, Throwable t) {
                responseInterface.responseFailure();
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
    public void responseSuccess(EquipmentModel equipmentModel) {
        Toast.makeText(getActivity(), "View Your Data", Toast.LENGTH_LONG).show();
    }

    @Override
    public void responseFailure() {
        Toast.makeText(getActivity(), "Response Failure", Toast.LENGTH_LONG).show();
    }

    @Override
    public void responseSuccess1(EquipmentModel equipmentModel) {
    }

    @Override
    public void responseFailure1() {
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.retry_btn){
            if(functionCall.isInternetOn(getActivity())){
                linear_internet_not_found.setVisibility(View.VISIBLE);
                functionCall.showprogressdialog("Plase wait to complete ",progressDialog,"Fetching Data");
                GettingEquipmentData(this);
            }
        }
    }

    @Override
    public void onItemClickLisnernter(EquipmentModel equipmentModel) {
        TrackBottomSheet trackBottomSheet = new TrackBottomSheet();
        if (getFragmentManager() != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("Data",equipmentModel);
            trackBottomSheet.setArguments(bundle);
            trackBottomSheet.show(getFragmentManager(),trackBottomSheet.getTag());

        }


    }
}


