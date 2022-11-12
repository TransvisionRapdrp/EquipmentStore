package com.example.equipmentstore.fragments;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.equipmentstore.R;
import com.example.equipmentstore.adapters.StaffOutGoingAdapter;
import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.interfac.ResponseInterface2;
import com.example.equipmentstore.models.Staff_OutGoingModel;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Staff_Outgoing extends Fragment implements ResponseInterface2, View.OnClickListener {

    View view;
    EditText name, visitto, reason;
    Button insert;
    TextView intime, outtime;
    Spinner mode_of_transport;
    public Staff_OutGoingModel staff_outGoingModel;
    FunctionCall functionsCall;
    ArrayList<Staff_OutGoingModel> staff_outGoingModelArrayList;
    private String main_role = "";
    private int mHour, mMinute;
    private int mHour1, mMinute1;
    private String intiming = "";
    private String outtiming = "";
    ProgressDialog progressDialog;
    List<Staff_OutGoingModel> staff_outGoingModelsList;
    LinearLayout layout1;
    String formattedDate = "";

    public Staff_Outgoing() {
        //required empty constructor...
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_staff__outgoing, container, false);
        intialization();
        return view;
    }

    public void intialization() {
        functionsCall = new FunctionCall();
        layout1 = view.findViewById(R.id.lin_layou1);
        progressDialog = new ProgressDialog(getContext());
        name = view.findViewById(R.id.et_name);
        visitto = view.findViewById(R.id.et_visitTo);
        reason = view.findViewById(R.id.et_reason);
        intime = view.findViewById(R.id.txt_inTime);
        intime.setOnClickListener(this);
        outtime = view.findViewById(R.id.txt_OutTime);
        outtime.setOnClickListener(this);
        mode_of_transport = view.findViewById(R.id.modeof_Transport_spinner);
        spinnerData();
        staff_outGoingModelArrayList = new ArrayList<>();
        StaffOutGoingAdapter staffOutGoingAdapter = new StaffOutGoingAdapter(staff_outGoingModelArrayList, getContext());
        mode_of_transport.setAdapter(staffOutGoingAdapter);
        mode_of_transport.setSelection(0);
        for (int i = 0; i < getResources().getStringArray(R.array.mode_of_transport).length; i++) {
            Staff_OutGoingModel staff_outGoingModel = new Staff_OutGoingModel();
            staff_outGoingModel.setModeoftransport(getResources().getStringArray(R.array.mode_of_transport)[i]);
            staff_outGoingModelArrayList.add(staff_outGoingModel);
            staffOutGoingAdapter.notifyDataSetChanged();
        }
        insert = view.findViewById(R.id.btn_submit1);
        insert.setOnClickListener(this);
        staff_outGoingModelsList = new ArrayList<>();
        staff_outGoingModel = new Staff_OutGoingModel();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_inTime:
                InTime();
                break;
            case R.id.txt_OutTime:
                OutTime();
                break;
            case R.id.btn_submit1:
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                formattedDate = df.format(c.getTime());
                if (TextUtils.isEmpty(name.getText())) {
                    setEditTextError(name, "Please enter ur name...");
                    return;
                } else if (TextUtils.isEmpty(visitto.getText())) {
                    setEditTextError(visitto, "Please enter ur visit to...");
                    return;
                } else if (TextUtils.isEmpty(reason.getText())) {
                    setEditTextError(reason, "Please enter ur reason...");
                    return;
                }
                if (main_role.equals("Select Mode of Transport")) {
                    functionsCall.setSnackBar(getContext(), layout1, "Please Select mode of transport");
                    return;
                }
                if (outtiming.isEmpty() || outtiming == null) {
                    functionsCall.setSnackBar(getContext(), layout1, "Please Choose Your OutTiming");
                    return;

                }
                if (intiming.isEmpty() || intiming == null) {
                    functionsCall.setSnackBar(getContext(), layout1, "Please Choose Your InTiming");
                    return;
                }
                functionsCall.showprogressdialog("Please Wait Submitting Your Data...", progressDialog, "Submit");
                //        insertStaff_OutGoing(formattedDate, name.getText().toString(), visitto.getText().toString(), reason.getText().toString(), main_role, outtiming, intiming,this);
                insertStaff_OutGoing(this);
                break;
        }
    }

    private void setEditTextError(EditText editText, String value) {
        editText.setError(value);
        editText.setSelection(editText.getText().length());
        editText.requestFocus();
    }

    private void InTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM = " AM";
                        String mm_precede = "";
                        if (hourOfDay >= 12) {
                            AM_PM = " PM";
                            if (hourOfDay >= 13 && hourOfDay < 24) {
                                hourOfDay -= 12;
                            } else {
                                hourOfDay = 12;
                            }
                        } else if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        if (minute < 10) {
                            mm_precede = "0";
                        }
                        intiming = hourOfDay + ":" + mm_precede + minute + AM_PM;
                        intime.setText(intiming);


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    private void OutTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour1 = c.get(Calendar.HOUR_OF_DAY);
        mMinute1 = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM = " AM";
                        String mm_precede = "";
                        if (hourOfDay >= 12) {
                            AM_PM = " PM";
                            if (hourOfDay >= 13 && hourOfDay < 24) {
                                hourOfDay -= 12;
                            } else {
                                hourOfDay = 12;
                            }
                        } else if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        if (minute < 10) {
                            mm_precede = "0";
                        }
                        outtiming = hourOfDay + ":" + mm_precede + minute + AM_PM;
                        outtime.setText(outtiming);
                    }
                }, mHour1, mMinute1, false);
        timePickerDialog.show();
    }


    private void spinnerData() {
        mode_of_transport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Staff_OutGoingModel roles = staff_outGoingModelArrayList.get(position);
                main_role = roles.getModeoftransport();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void insertStaff_OutGoing(ResponseInterface2 responseInterface2) {
        staff_outGoingModel = new Staff_OutGoingModel();
        staff_outGoingModel.setDate(formattedDate);
        staff_outGoingModel.setName(name.getText().toString());
        staff_outGoingModel.setVisit_To(visitto.getText().toString());
        staff_outGoingModel.setReason(reason.getText().toString());
        staff_outGoingModel.setMode_Of_Transport(main_role);
        staff_outGoingModel.setOut_time(outtiming);
        staff_outGoingModel.setIN__time(intiming);
        RetroClient retroClient = new RetroClient();
        RegisterApi registerApi = retroClient.getApiService();
        registerApi.insertStaffOutgoing(staff_outGoingModel).enqueue(new Callback<Staff_OutGoingModel>() {
            @Override
            public void onResponse(Call<Staff_OutGoingModel> call, Response<Staff_OutGoingModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    staff_outGoingModelsList.clear();
                    /*   staff_outGoingModelsList = response.body();*/
                    staff_outGoingModel = response.body();
                    responseInterface2.responseSuccess(response.body());
                } else {
                    responseInterface2.responseFailure();
                }
            }

            @Override
            public void onFailure(Call<Staff_OutGoingModel> call, Throwable t) {
                responseInterface2.responseFailure();
            }
        });
    }

    @Override
    public void responseSuccess(Staff_OutGoingModel staff_outGoingModel) {
        if (staff_outGoingModel.getMessage().equals("Saved Successfully")) {
            Toast.makeText(getContext(), "Response Success", Toast.LENGTH_LONG).show();
            clearEditText();
        }
        progressDialog.dismiss();
    }

    @Override
    public void responseFailure() {
        Toast.makeText(getContext(), "Response Failure because Please First Insert Data!!", Toast.LENGTH_SHORT).show();
        clearEditText();
        progressDialog.dismiss();
    }

    private void clearEditText() {
        name.setText("");
        visitto.setText("");
        reason.setText("");
        intime.setText("");
        outtime.setText("");
        mode_of_transport.setSelection(0);
    }
}
