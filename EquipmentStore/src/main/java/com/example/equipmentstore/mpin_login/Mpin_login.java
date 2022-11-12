package com.example.equipmentstore.mpin_login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.equipmentstore.MainActivity;
import com.example.equipmentstore.R;
import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.interfac.Login_responseInterface;
import com.example.equipmentstore.models.LoginDetails;
import com.example.equipmentstore.network.FTPAPI;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.equipmentstore.SplashScreen.version;
import static com.example.equipmentstore.extra.constants.APK_FILE_DOWNLOADED;
import static com.example.equipmentstore.extra.constants.APK_FILE_NOT_FOUND;
import static com.example.equipmentstore.extra.constants.APK_NAME;
import static com.example.equipmentstore.extra.constants.DIR_APK;
import static com.example.equipmentstore.extra.constants.DOWNLOAD_PERCENTAGE;
import static com.example.equipmentstore.extra.constants.DOWNLOAD_RECEIVER;
import static com.example.equipmentstore.extra.constants.DOWNLOAD_SIZE;
import static com.example.equipmentstore.extra.constants.DOWNLOAD_UPDATE;
import static com.example.equipmentstore.extra.constants.FILE_APK_FORMAT;
import static com.example.equipmentstore.extra.constants.MyPREFERENCES;
import static com.example.equipmentstore.extra.constants.sPref_Group;
import static com.example.equipmentstore.extra.constants.sPref_ID;
import static com.example.equipmentstore.extra.constants.sPref_MOBILE_NO;
import static com.example.equipmentstore.extra.constants.sPref_SUBDIVCODE;
import static com.example.equipmentstore.extra.constants.sPref_USERNAME;
import static com.example.equipmentstore.extra.constants.spref_password;

public class Mpin_login extends Fragment implements Login_responseInterface {

    EditText otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four;
    ImageButton verify_otp;
    View view;
    TextView tv_mpin_otp;
    SharedPreferences preferences;
    List<LoginDetails> loginList;
    FunctionCall functionsCall;
    String password;
    ProgressDialog progressDialog;
    private static final int DLG_APK_UPDATE_SUCCESS = 5;
    private static final int DLG_APK_UPDATE_FAILURE = 6;
    private static final int DLG_APK_DOWNLOAD_PROGRESS = 7;
    private TextView tv_progress_update;
    private TextView tv_per_progress;
    LoginDetails loginDetails = new LoginDetails();
    AlertDialog alertDialog = null;

    private Handler login_handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case APK_FILE_DOWNLOADED:
                    // Updated application file has been downloaded
                    alertDialog.dismiss();
                    // Updating application after downloading the file
                    functionsCall.updateApp(getActivity(), new File(functionsCall.filepath(DIR_APK) +
                            File.separator + APK_NAME + loginDetails.getEMP_ATTENDENCE() + FILE_APK_FORMAT));
                    break;

                case APK_FILE_NOT_FOUND:
                    // Updated application file is not found to download from FTP
                    alertDialog.dismiss();
                    functionsCall.showToast(getActivity(), "No APK found to update!!");
                    break;
            }
            return false;
        }
    });

    public Mpin_login() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mpin, container, false);
        preferences = getActivity().getSharedPreferences(MyPREFERENCES, getActivity().MODE_PRIVATE);
        loginList = new ArrayList<>();
        otp_textbox_one = view.findViewById(R.id.otp_edit_box1);
        otp_textbox_two = view.findViewById(R.id.otp_edit_box2);
        otp_textbox_three = view.findViewById(R.id.otp_edit_box3);
        otp_textbox_four = view.findViewById(R.id.otp_edit_box4);
        verify_otp = view.findViewById(R.id.verify_otp_btn);
        tv_mpin_otp = view.findViewById(R.id.mpin_otp);
        progressDialog = new ProgressDialog(getContext());
        tv_mpin_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Mpin_reg.class);
                startActivity(intent);
            }
        });
        getActivity().registerReceiver(receiver, new IntentFilter(DOWNLOAD_RECEIVER));
        functionsCall = new FunctionCall();
        EditText[] edit = {otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four};

        password = preferences.getString(spref_password, "0");

        otp_textbox_one.addTextChangedListener(new GenericTextWatcher(edit, otp_textbox_one));
        otp_textbox_two.addTextChangedListener(new GenericTextWatcher(edit, otp_textbox_two));
        otp_textbox_three.addTextChangedListener(new GenericTextWatcher(edit, otp_textbox_three));
        otp_textbox_four.addTextChangedListener(new GenericTextWatcher(edit, otp_textbox_four));


        otp_textbox_one.setOnKeyListener(new GenericKeyEvent(otp_textbox_one, null));
        otp_textbox_two.setOnKeyListener(new GenericKeyEvent(otp_textbox_two, otp_textbox_one));
        otp_textbox_three.setOnKeyListener(new GenericKeyEvent(otp_textbox_three, otp_textbox_two));
        otp_textbox_four.setOnKeyListener(new GenericKeyEvent(otp_textbox_four, otp_textbox_three));


        otp_textbox_four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    String pin = otp_textbox_one.getText().toString() + otp_textbox_two.getText().toString() + otp_textbox_three.getText().toString() + otp_textbox_four.getText().toString();
                    if (pin.length() == 4) {
                        mpinlogin(pin);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = otp_textbox_one.getText().toString() + otp_textbox_two.getText().toString() + otp_textbox_three.getText().toString() + otp_textbox_four.getText().toString();
                mpinlogin(pin);
            }
        });

        return view;
    }

    public void mpinlogin(String mpin) {

        RetroClient retroClient1 = new RetroClient();
        RegisterApi api = retroClient1.getApiService();
        LoginDetails loginDetails = new LoginDetails();
        loginDetails.setUSERNAME(preferences.getString(sPref_USERNAME, "NA"));
        loginDetails.setMPIN(mpin);
        functionsCall.showprogressdialog("Please Wait Submitting Your Data...", progressDialog, "Submit");
        api.getmpinlogin(loginDetails).enqueue(new Callback<List<LoginDetails>>() {
            @Override
            public void onResponse(Call<List<LoginDetails>> call, Response<List<LoginDetails>> response) {
                if (response.isSuccessful()) {
                    loginList.clear();
                    loginList = response.body();
                    responseSuccess(response.body().get(0));
                } else {
                    responseFailure();
                }
            }

            @Override
            public void onFailure(Call<List<LoginDetails>> call, Throwable t) {
                responseFailure();
            }
        });
    }

    @Override
    public void responseSuccess(LoginDetails loginDetails) {
        SharedPreferences.Editor editor = preferences.edit();
        this.loginDetails = loginDetails;
        //data insert to shared preference
        progressDialog.dismiss();
        if (functionsCall.compare(version, loginDetails.getEMP_ATTENDENCE()))
            appUpdate(DLG_APK_UPDATE_SUCCESS);
        else {
            editor.putBoolean("hasLoggedIn", true);
            editor.putString(sPref_SUBDIVCODE, loginDetails.getSUBDIVCODE());
            editor.putString(sPref_USERNAME, loginDetails.getUSERNAME());
            editor.putString(spref_password, password);
            editor.putString(sPref_Group, loginDetails.getGROUPS());
            editor.putString(sPref_ID, loginDetails.getUSERID());
            editor.putString(sPref_MOBILE_NO, loginDetails.getMOBILE_NO());
            editor.apply();
            cleardata();
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
            functionsCall.showToast(getContext(), "Login Success");
        }
    }

    @Override
    public void responseFailure() {
        progressDialog.dismiss();
        functionsCall.showToast(getContext(), "Login Failure");
    }

    private void appUpdate(int showdailog) {
        LinearLayout dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_message_layout, null);
        TextView tv_msg = functionsCall.textId(dialog_layout, R.id.dialog_message);
        Button btn_positive = functionsCall.btnId(dialog_layout, R.id.dialog_positive_btn);
        Button btn_negative = functionsCall.btnId(dialog_layout, R.id.dialog_negative_btn);

        if (showdailog == DLG_APK_UPDATE_SUCCESS) {
            AlertDialog.Builder app_update = new AlertDialog.Builder(getActivity());
            app_update.setTitle(getResources().getString(R.string.app_update_title));
            app_update.setCancelable(false);
            app_update.setView(dialog_layout);
            btn_positive.setText(getResources().getString(R.string.select_update));
            tv_msg.setText(String.format("%s %s\n\n%s %s", getResources().getString(R.string.app_update_msg_1),
                    version, getResources().getString(R.string.app_update_msg_2), loginDetails.getEMP_ATTENDENCE()));
            alertDialog = app_update.create();
            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    appUpdate(DLG_APK_DOWNLOAD_PROGRESS);
                }
            });
            btn_negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else if (showdailog == DLG_APK_DOWNLOAD_PROGRESS) {
            // This dialog to shown for the downloading apk progress
            AlertDialog.Builder apk_download = new AlertDialog.Builder(getActivity());
            apk_download.setTitle(getResources().getString(R.string.apk_download_title));
            apk_download.setCancelable(false);
            @SuppressLint("InflateParams")
            ConstraintLayout apk_layout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_down_up_progress_layout, null);
            apk_download.setView(apk_layout);
            TextView tv_progress_message = functionsCall.textId(apk_layout, R.id.dlg_progress_message);
            tv_progress_update = functionsCall.textId(apk_layout, R.id.dlg_progress_update);
            tv_per_progress = functionsCall.textId(apk_layout, R.id.dlg_per_progress_update);
            ProgressBar apk_progressBar = apk_layout.findViewById(R.id.dlg_apk_progress);
            tv_progress_message.setText(getResources().getString(R.string.apk_downloading_msg));
            alertDialog = apk_download.create();
            new Thread(new FTPAPI().new Download_apk(getActivity(), login_handler, loginDetails.getEMP_ATTENDENCE(),
                    apk_progressBar)).start();
            alertDialog.show();
        }
    }

    public void cleardata() {
        otp_textbox_one.setText("");
        otp_textbox_two.setText("");
        otp_textbox_three.setText("");
        otp_textbox_four.setText("");
    }

    // This broadcast receiver is to show the update of downloading progress on the dialog
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                tv_progress_update.setText(String.format("%s / %s", intent.getStringExtra(DOWNLOAD_UPDATE), intent.getStringExtra(DOWNLOAD_SIZE)));
                tv_per_progress.setText(String.format("%s %s", intent.getStringExtra(DOWNLOAD_PERCENTAGE), getResources().getString(R.string.percentage)));
            } catch (NullPointerException e) {
                tv_progress_update.setText(String.format("%s / %s", "0", "0"));
                tv_per_progress.setText(String.format("%s %s", "0", getResources().getString(R.string.percentage)));
            }
        }
    };

    // This is method is used to unregister the receiver
    private void unRegisterReceiver() {
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        login_handler.removeCallbacksAndMessages(null);
        unRegisterReceiver();
    }
}