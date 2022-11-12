package com.example.equipmentstore.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.equipmentstore.R;
import com.example.equipmentstore.adapters.AttendanceRepAdapter;
import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.models.AttendanceSummary;
import com.example.equipmentstore.models.LoginDetails;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.equipmentstore.extra.constants.MyPREFERENCES;
import static com.example.equipmentstore.extra.constants.sPref_ID;

public class AttendanceReport extends Fragment  {

    View view;
    ProgressDialog progressdialog;
    Toolbar toolbar;
    List<AttendanceSummary> attendanceSummaryList;
    RecyclerView recyclerView;
    FunctionCall functionCall;
    LinearLayout layout;
    EditText edt_date;
    ImageView select_date;
    private int day, month, year;
    private Calendar mcalender;
    String dd, date,user_id;
    AttendanceRepAdapter adapter;
    LinearLayout linear_1;
    List<LoginDetails> loginList;
    SharedPreferences settings;

    public AttendanceReport() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendance_report, container, false);
        settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = (settings.getString(sPref_ID, "0"));
        attendanceSummaryList = new ArrayList<>();
        functionCall = new FunctionCall();
        recyclerView = view.findViewById(R.id.rec_att_report);
        progressdialog = new ProgressDialog(getContext());
        edt_date = view.findViewById(R.id.et_date);
        select_date = view.findViewById(R.id.img_sel_date);
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                DateDialog();
            }
        });

        return view;
    }

    //------------------------------------------------------------------------------------------------------------------------------
    private void DateDialog() {
        mcalender = Calendar.getInstance();
        day = mcalender.get(Calendar.DAY_OF_MONTH);
        year = mcalender.get(Calendar.YEAR);
        month = mcalender.get(Calendar.MONTH);

        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {

            String month1 = String.valueOf(month + 1);
            if(month1.length() == 1){
                month1 = "0" + month1;
            }
            date =  month1 +""+year ;
            // date = functionCall.Parse_Date5(dd);
            edt_date.setText(date);
            functionCall.showprogressdialog("Please wait to complete", progressdialog, "Data Loading");
            getempattsummary(date);

        };
        DatePickerDialog dpdialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        mcalender.add(Calendar.MONTH, -1);
        dpdialog.show();
    }

    private void getempattsummary(String date){

        RetroClient1 retroClient1 = new RetroClient1();
        RegisterApi api = retroClient1.getApiService();
        api.getempsummary(date,user_id).enqueue(new Callback<List<AttendanceSummary>>() {
            @Override
            public void onResponse(@NonNull Call<List<AttendanceSummary>> call, @NonNull Response<List<AttendanceSummary>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    attendanceSummaryList = response.body();
                    setValue(attendanceSummaryList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AttendanceSummary>> call, @NonNull Throwable t) {
                functionCall.showToast(getActivity(),  "No Data Found");
                progressdialog.dismiss();
            }
        });

    }

    private void setValue(List<AttendanceSummary> list){
        adapter = new AttendanceRepAdapter( list,getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        recyclerView.setVisibility(View.VISIBLE);
        progressdialog.dismiss();
    }
}