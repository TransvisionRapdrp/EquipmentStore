package com.example.equipmentstore.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.equipmentstore.MainActivity;
import com.example.equipmentstore.extra.FunctionCall;

import com.example.equipmentstore.R;
import com.example.equipmentstore.extra.RecyclerItemClickListener;
import com.example.equipmentstore.interfac.ResponseInterface;
import com.example.equipmentstore.adapters.ManagementAdapter;


import com.example.equipmentstore.models.EquipmentDetails;
import com.example.equipmentstore.models.EquipmentModel;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ManagementApproval extends Fragment implements View.OnClickListener,
        ResponseInterface {

    View view;
    FunctionCall functionsCall;
    ManagementAdapter managementapprovalAdapter;
    RecyclerView approved_recyclerview;
    List<EquipmentModel> equipmentModel;
    List<EquipmentModel> equipmentModelSelect;
    Button manag_submit_approval;
    ProgressDialog progressDialog;
    String requset_id = "";
    StringBuilder stringBuilder;
    boolean checkbox_selected = false;
    LinearLayout linear_internet_not_found;
    Button retry;
    ActionMode mActionMode;
    Menu context_menu;
    boolean isMultiSelect = false;

    public ManagementApproval() {
        //required constructor for
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_management_approval, container, false);
        intialization();
        return view;
    }

    private void intialization() {
        functionsCall = new FunctionCall();
        progressDialog = new ProgressDialog(getActivity());
        manag_submit_approval = view.findViewById(R.id.btn_submit_approval);
        manag_submit_approval.setOnClickListener(this);
        approved_recyclerview = view.findViewById(R.id.management_approval_recycler);
        equipmentModel = new ArrayList<>();
        equipmentModelSelect = new ArrayList<>();
        linear_internet_not_found = view.findViewById(R.id.lin_internet);

        if (functionsCall.isInternetOn(getActivity())) {
            functionsCall.progressdialog("Please wait", progressDialog, "Data Loading..!!");
            getEquipmentData(this);
        } else {
            linear_internet_not_found.setVisibility(View.VISIBLE);
            manag_submit_approval.setVisibility(View.GONE);
        }


        approved_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && manag_submit_approval.isShown()) {
                    manag_submit_approval.setVisibility(View.VISIBLE);
                } else if (dy < 0) {
                    manag_submit_approval.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        approved_recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), approved_recyclerview, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
                else
                    Toast.makeText(getActivity(), "Details Page", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    equipmentModelSelect = new ArrayList<EquipmentModel>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = getActivity().startActionMode(mActionModeCallback);
                    }
                }

                ((MainActivity) getActivity()).visibleGone(false);
                multi_select(position);

            }
        }));

        retry = view.findViewById(R.id.retry_btn);
        retry.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit_approval) {
            if (functionsCall.isInternetOn(getActivity())) {
                SendData();
            }
        }
        if (view.getId() == R.id.retry_btn) {
            if (functionsCall.isInternetOn(getActivity())) {
                linear_internet_not_found.setVisibility(View.GONE);
                functionsCall.showprogressdialog("Please wait to complete", progressDialog, "Fetching Data");
                getEquipmentData(this);
            }

        }
    }

    private void SendData() {
        List<EquipmentModel> approvedData = managementapprovalAdapter.getApprovedListData();
        for (int k = 0; k < approvedData.size(); k++) {
            EquipmentModel equipmentModel = approvedData.get(k);
            if (equipmentModel.isSelected()) {
                checkbox_selected = true;
            }
        }
        if (checkbox_selected) {
            stringBuilder = new StringBuilder();
            for (int i = 0; i < approvedData.size(); i++) {
                EquipmentModel equipmentModel = approvedData.get(i);
                if (equipmentModel.isSelected()) {
                    stringBuilder.append(equipmentModel.getID()).append(",");
                }
            }
            requset_id = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
            functionsCall.progressdialog("Please wait to complete", progressDialog, "Updating Ur Data");
            GettingManagementApproval(requset_id, this);
        } else {
            Toast.makeText(getActivity(), "Please first select checkbox and proceed", Toast.LENGTH_LONG).show();
        }
    }

    private void SendData1() {

        if (equipmentModelSelect.size() > 0) {
            stringBuilder = new StringBuilder();
            for (int i = 0; i < equipmentModelSelect.size(); i++) {
                EquipmentModel equipmentModel = equipmentModelSelect.get(i);
                stringBuilder.append(equipmentModel.getID()).append(",");

            }
            requset_id = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
            functionsCall.progressdialog("Please wait to complete", progressDialog, "Updating Ur Data");
            GettingManagementApproval(requset_id, this);
        } else {
            Toast.makeText(getActivity(), "SELECT FOR APPROVAL", Toast.LENGTH_LONG).show();
        }
    }

    private void getEquipmentData(ResponseInterface responseInterface) {
        RetroClient retroClient = new RetroClient();
        RegisterApi registerApi = retroClient.getApiService();
        registerApi.getData().enqueue(new Callback<List<EquipmentModel>>() {
            @Override
            public void onResponse(Call<List<EquipmentModel>> call, Response<List<EquipmentModel>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    equipmentModel.clear();
                    for (EquipmentModel equipmentDetails : response.body())
                        if (TextUtils.isEmpty(equipmentDetails.getAPPROVED_FLAG())) {
                            if (!TextUtils.isEmpty(equipmentDetails.getREQUESTED_DATE()))
                                equipmentDetails.setREQUESTED_DATE(ConvertJsonDate_to_date(equipmentDetails.getREQUESTED_DATE()));

                            equipmentModel.add(equipmentDetails);
                        }


                    Collections.reverse(equipmentModel);
                    managementapprovalAdapter = new ManagementAdapter(equipmentModel, equipmentModelSelect, getActivity());
                    approved_recyclerview.setHasFixedSize(true);
                    approved_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                    approved_recyclerview.setAdapter(managementapprovalAdapter);
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

    private void GettingManagementApproval(String id, ResponseInterface responseInterface) {
        RetroClient retroClient = new RetroClient();
        EquipmentModel equipmentModel1 = new EquipmentModel();
        equipmentModel1.setID(requset_id);

        RegisterApi registerApi1 = retroClient.getApiService();
        registerApi1.managementApproval(requset_id).enqueue(new Callback<EquipmentModel>() {
            @Override
            public void onResponse(Call<EquipmentModel> call, Response<EquipmentModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    equipmentModel.clear();
                    responseInterface.responseSuccess1(response.body());
                } else {
                    responseInterface.responseFailure1();
                }
            }

            @Override
            public void onFailure(Call<EquipmentModel> call, Throwable t) {
                responseInterface.responseFailure1();
            }
        });
    }


    @Override
    public void responseSuccess(EquipmentModel viewComplaints) {
        progressDialog.dismiss();

    }

    @Override
    public void responseFailure() {
        progressDialog.dismiss();
    }

    @Override
    public void responseSuccess1(EquipmentModel equipmentModel) {
        progressDialog.dismiss();
        if (equipmentModel.getMessage().equals("Success")) {
            Toast.makeText(getActivity(), "Finally Approved Success", Toast.LENGTH_LONG).show();
        }
        checkbox_selected = false;
        ((MainActivity) getActivity()).visibleGone(true);
        mActionMode.finish();

        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    @Override
    public void responseFailure1() {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Approved Failure", Toast.LENGTH_SHORT).show();
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (equipmentModelSelect.contains(equipmentModel.get(position)))
                equipmentModelSelect.remove(equipmentModel.get(position));
            else
                equipmentModelSelect.add(equipmentModel.get(position));

            if (equipmentModelSelect.size() > 0)
                mActionMode.setTitle("" + equipmentModelSelect.size() + " SELECTED");
            else
                mActionMode.setTitle("0 SELECTED");

            refreshAdapter();

        }
    }


    public void refreshAdapter() {
        managementapprovalAdapter.equipmentModelListSelected = equipmentModelSelect;
        managementapprovalAdapter.equipmentModelList = equipmentModel;
        managementapprovalAdapter.notifyDataSetChanged();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);
            context_menu = menu;

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_delete) {
                if (functionsCall.isInternetOn(getActivity())) {
                    SendData1();
                } else functionsCall.showToast(getActivity(), "No Internet!!");
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            ((MainActivity) getActivity()).visibleGone(true);
            equipmentModelSelect = new ArrayList<EquipmentModel>();
            refreshAdapter();
        }
    };
}