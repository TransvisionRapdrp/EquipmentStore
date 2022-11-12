package com.example.equipmentstore.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.equipmentstore.R;
import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.interfac.Recv_responseInterface;
import com.example.equipmentstore.interfac.ResponseInterface;
import com.example.equipmentstore.models.EquipmentDetails;
import com.example.equipmentstore.models.EquipmentModel;
import com.example.equipmentstore.models.VisitorModel;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Visitors_Fragment extends Fragment implements View.OnClickListener, Recv_responseInterface {
    EditText et_visitdate, et_visitorname, et_fromvisitor, et_purposevisit, et_tomeet, et_contactNo, et_priorityapointed, et_mailid, et_intime, et_outtime, et_visitorremark, et_incharge;
    Button bt_submit;
    View view;
    String dateselected, visitorname, fromvisitor, purposvsit, tomeet, contactno, priorityapointed, mailid, intime, outtime, visitorremark, incharge;
    FunctionCall functionCall;
    Recv_responseInterface recv_responseInterface;
    String time;
    private int mHour, mMinute;
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_visitors_, container, false);
        Intialization();
        functionCall = new FunctionCall();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Intialization() {
        progressDialog = new ProgressDialog(getContext());
        et_visitdate = view.findViewById(R.id.et_visitdate);
        et_visitdate.setOnClickListener(this);
        et_visitdate.setShowSoftInputOnFocus(false);
        et_visitorname = view.findViewById(R.id.et_visitorname);
        et_fromvisitor = view.findViewById(R.id.et_fromvisitor);
        et_purposevisit = view.findViewById(R.id.et_purposevisit);
        et_tomeet = view.findViewById(R.id.et_tomeet);
        et_contactNo = view.findViewById(R.id.et_contactNo);
        et_priorityapointed = view.findViewById(R.id.et_priorityapointed);
        et_mailid = view.findViewById(R.id.et_mailid);
        et_intime = view.findViewById(R.id.et_intime);
        et_intime.setShowSoftInputOnFocus(false);
        et_intime.setOnClickListener(this);
        et_outtime = view.findViewById(R.id.et_outtime);
        et_outtime.setOnClickListener(this);
        et_outtime.setShowSoftInputOnFocus(false);
        et_visitorremark = view.findViewById(R.id.et_visitorremark);
        et_incharge = view.findViewById(R.id.et_incharge);
        bt_submit = view.findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);

        Date today = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        et_visitdate.setText(format.format(today));
        et_visitorname.requestFocus();
    }

    @Override
    public void onClick(View view) {

        visitorname = et_visitorname.getText().toString();
        fromvisitor = et_fromvisitor.getText().toString();
        purposvsit = et_purposevisit.getText().toString();
        tomeet = et_tomeet.getText().toString();
        contactno = et_contactNo.getText().toString();
        priorityapointed = et_priorityapointed.getText().toString();
        mailid = et_mailid.getText().toString();
        intime = et_intime.getText().toString();
        outtime = et_outtime.getText().toString();
        visitorremark = et_visitorremark.getText().toString();
        incharge = et_incharge.getText().toString();

        int s = view.getId();
        if (s == R.id.et_intime) {
            timing();
        }
        if (s == R.id.et_visitdate) {
            date();
        }
        if (s == R.id.bt_submit) {
            validation();
        }

        if (s == R.id.et_outtime)
            OutTime();
    }

    public void validation() {
        if (visitorname.isEmpty()) {
            et_visitorname.setError("please enter Your Name");
            return;
        } else if (fromvisitor.isEmpty()) {
            et_fromvisitor.setError("please enter the Visitor  from Address ");
            return;
        } else if (purposvsit.isEmpty()) {
            et_purposevisit.setError("please enter the Purpose of Visit");
            return;
        } else if (tomeet.isEmpty()) {
            et_tomeet.setError("please enter  too meet person");
            return;
        } else if (contactno.isEmpty()) {
            et_contactNo.setError("please enter the Contact num");
            return;
        } else if (contactno.length() != 10) {
            et_contactNo.setError("please enter correct num");
            return;
        } else if (priorityapointed.isEmpty()) {
            et_priorityapointed.setError("please enter the priority appointed");
            return;
        } else if (mailid.isEmpty()) {
            et_mailid.setError("please enter the  Your Email id");
            return;
        }
        if (!isValidEmailId(mailid.trim())) {
            et_mailid.setError("Valid Email Address.");
            return;
        } else if (intime.isEmpty()) {
            et_intime.setError("please enter the In time");
            return;
        } else if (outtime.isEmpty()) {
            et_outtime.setError("please enter your out time");
            return;
        } else if (visitorremark.isEmpty()) {
            et_visitorremark.setError("please enter your Remarks");
            return;
        } else if (incharge.isEmpty()) {
            et_incharge.setError("please enter the Incharger name");
            return;
        }

        functionCall.showprogressdialog("Please Wait to submiting the visitor information..", progressDialog, "");
        Visitorinfo(dateselected, visitorname, fromvisitor, purposvsit, tomeet, contactno, priorityapointed, mailid, intime, outtime, visitorremark, incharge, this);

    }

    private boolean isValidEmailId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private void timing() {
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
                        time = hourOfDay + ":" + mm_precede + minute + AM_PM;

                        et_intime.setText(time);

                    }
                }, mHour, mMinute, false);

        timePickerDialog.show();
    }

    private void OutTime() {
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
                        time = hourOfDay + ":" + mm_precede + minute + AM_PM;

                        et_outtime.setText(time);

                    }
                }, mHour, mMinute, false);

        timePickerDialog.show();
    }

    public void date() {
        final int[] date = new int[1];
        final int[] month = new int[1];
        final int[] year = new int[1];
        Calendar cal = Calendar.getInstance();
        year[0] = cal.get(Calendar.YEAR);
        month[0] = cal.get(Calendar.MONTH);
        date[0] = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(getActivity(), dateSetListener, year[0], month[0], date[0]);
        // Setting date selection should be current date
        dp.getDatePicker().setMaxDate(cal.getTimeInMillis());
        dp.show();
    }

    public void Visitorinfo(String date, String name, String fromvisitor, String purposvsit, String tomeet, String contactno, String appointed, String mailid, String intime, String outtime, String remarks, String incharge, Recv_responseInterface recvinf) {
        RetroClient retroClient = new RetroClient();
        RegisterApi api = retroClient.getApiService();
        VisitorModel visitorModel = new VisitorModel();
        visitorModel.setDateofvisit(date);
        visitorModel.setVisitorsName(name);
        visitorModel.setRomVisitor(fromvisitor);
        visitorModel.setPurposeofVisit(purposvsit);
        visitorModel.setToMeet(tomeet);
        visitorModel.setContactNumber(contactno);
        visitorModel.setPriorityAppointed(appointed);
        visitorModel.setMailid(mailid);
        visitorModel.setInTime(intime);
        visitorModel.setOuttime(outtime);
        visitorModel.setRemarks(remarks);
        visitorModel.setIncharge(incharge);

        api.getvisitorinfo(visitorModel).enqueue(new Callback<EquipmentDetails>() {
            @Override
            public void onResponse(Call<EquipmentDetails> call, Response<EquipmentDetails> response) {
                if (response.isSuccessful()) {
                    recvinf.recvresponsesuccess(response.body());
                } else {
                    recvinf.recvresponsefailure();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<EquipmentDetails> call, Throwable t) {
                recvinf.recvresponsefailure();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void recvresponsesuccess(EquipmentDetails equipmentDetails) {
        if (equipmentDetails.getMessage().equals("Saved Successfully")){
            clearData();
            Toast.makeText(getContext(), "Submitted Your Data...", Toast.LENGTH_SHORT).show();
        }else Toast.makeText(getContext(), "Your Data has not submitted", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override
    public void recvresponsefailure() {
        clearData();
        Toast.makeText(getContext(), "Your Data has not submitted", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Date Starttime = null;
            et_visitdate.setText("");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Starttime = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        .parse(("" + dayOfMonth + "/" + "" + (monthOfYear + 1) + "/" + "" + year));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateselected = sdf.format(Objects.requireNonNull(Starttime));
            et_visitdate.setText(dateselected);
            et_visitdate.setSelection(Objects.requireNonNull(et_visitdate.getText()).length());
        }
    };

    private void clearData() {
        et_visitdate.setText("");
        et_visitorname.setText("");
        et_fromvisitor.setText("");
        et_purposevisit.setText("");
        et_tomeet.setText("");
        et_contactNo.setText("");
        et_priorityapointed.setText("");
        et_mailid.setText("");
        et_intime.setText("");
        et_outtime.setText("");
        et_visitorremark.setText("");
        et_incharge.setText("");
    }
}
