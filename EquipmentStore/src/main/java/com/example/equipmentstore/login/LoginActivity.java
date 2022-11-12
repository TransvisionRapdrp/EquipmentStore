package com.example.equipmentstore.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.MainActivity;
import com.example.equipmentstore.R;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Login_responseInterface {
    EditText et_username, et_password;
    Button bt_login;
    LoginModel loginList;
    FunctionCall functionCall;
    SharedPreferences preferences;
    ProgressDialog progressDialog;
    FrameLayout layout;
    List<LoginDetails> loginModelList;
    AlertDialog alertDialog = null;
    private static final int DLG_APK_UPDATE_SUCCESS = 5;
    private static final int DLG_APK_DOWNLOAD_PROGRESS = 7;
    private TextView tv_progress_update;
    private TextView tv_per_progress;

    private Handler login_handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case APK_FILE_DOWNLOADED:
                    // Updated application file has been downloaded
                    alertDialog.dismiss();
                    // Updating application after downloading the file
                    functionCall.updateApp(getApplicationContext(), new File(functionCall.filepath(DIR_APK) +
                            File.separator + APK_NAME + loginModelList.get(0).getEMP_ATTENDENCE() + FILE_APK_FORMAT));
                    break;

                case APK_FILE_NOT_FOUND:
                    // Updated application file is not found to download from FTP
                    alertDialog.dismiss();
                    functionCall.showToast(getApplicationContext(),"No APK found to update!!");
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initialisation();
    }

    public void initialisation() {
        preferences = getSharedPreferences(MyPREFERENCES, getApplication().MODE_PRIVATE);//shared preference initialization
        progressDialog = new ProgressDialog(this);
        functionCall = new FunctionCall();
        layout = findViewById(R.id.login_layout);
        loginModelList = new ArrayList<>();
        loginList =  new LoginModel();
        registerReceiver(receiver, new IntentFilter(DOWNLOAD_RECEIVER));
        et_username = findViewById(R.id.login_name);
        et_password = findViewById(R.id.login_password);
        bt_login = findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.bt_login){
            if(functionCall.isInternetOn(this)){
              validate();
            }else NoInternet();
        }
    }

    public void NoInternet() {
        final AlertDialog alertDialog;
        AlertDialog.Builder alertdialog= new AlertDialog.Builder(LoginActivity.this);
        @SuppressLint("InflateParams")
        LinearLayout manual_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.internet_not_found1, null);
        alertdialog.setView(manual_layout);
        alertdialog.setCancelable(false);
        Button yes = functionCall.btnId(manual_layout, R.id.login_btn1);
        yes.setText(getResources().getString(R.string.close));
        alertDialog = alertdialog.create();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }



    public void validate() {
        if (TextUtils.isEmpty(et_username.getText().toString())) {
            et_username.setError("please enter the User name");
            return;
        } else if (TextUtils.isEmpty(et_password.getText().toString())) {
            et_password.setError("please enter the password");
            return;
        }
        functionCall.showprogressdialog("Please Wait to complete..",progressDialog,"");
        loginDetails(this);
    }

    public void loginDetails(Login_responseInterface loginresponseInterface) {
        LoginDetails loginModel = new LoginDetails();
        loginModel.setUSERNAME(et_username.getText().toString());
        loginModel.setPASSWORD(et_password.getText().toString());
        RetroClient retroClient1 = new RetroClient();
        RegisterApi api = retroClient1.getApiService();
        api.getlogin(loginModel).enqueue(new Callback<List<LoginDetails>>() {
            @Override
            public void onResponse(Call<List<LoginDetails>> call, Response<List<LoginDetails>> response) {
                if(response.isSuccessful()) {
                    loginModelList.clear();
                    loginModelList = response.body();
                    loginresponseInterface.responseSuccess(response.body().get(0));
                }else loginresponseInterface.responseFailure();

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<LoginDetails>> call, Throwable t) {
                loginresponseInterface.responseFailure();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void responseSuccess(LoginDetails loginModel) {
        SharedPreferences.Editor editor = preferences.edit();
        if (functionCall.compare(version, loginModel.getEMP_ATTENDENCE()))
            appUpdate(DLG_APK_UPDATE_SUCCESS);
        else {
            editor.putBoolean("hasLoggedIn", true);
            editor.putString(sPref_SUBDIVCODE, loginModel.getSUBDIVCODE());
            editor.putString(sPref_USERNAME, loginModel.getUSERNAME());
            editor.putString(sPref_Group, loginModel.getGROUPS());
            editor.putString(sPref_ID, loginModel.getUSERID());
            editor.putString(sPref_MOBILE_NO, loginModel.getMOBILE_NO());
            editor.apply();
            Intent i = new Intent(getApplication(), MainActivity.class);
            startActivity(i);
            functionCall.showToast(getApplication(), "Login Success");
        }
        // ((MainActivity) Objects.requireNonNull(getApplicationContext())).switchContent(MainActivity.Steps.FORM0, value);
    }

    @Override
    public void responseFailure() {
        functionCall.showToast(this, "Pls Try After Sometime!!!");
    }

    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            super.onBackPressed();
        }
    }

    private void appUpdate(int showdailog) {
        LinearLayout dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_message_layout, null);
        TextView tv_msg = functionCall.textId(dialog_layout, R.id.dialog_message);
        Button btn_positive = functionCall.btnId(dialog_layout, R.id.dialog_positive_btn);
        Button btn_negative = functionCall.btnId(dialog_layout, R.id.dialog_negative_btn);

        if (showdailog == DLG_APK_UPDATE_SUCCESS) {
            AlertDialog.Builder app_update = new AlertDialog.Builder(LoginActivity.this);
            app_update.setTitle(getResources().getString(R.string.app_update_title));
            app_update.setCancelable(false);
            app_update.setView(dialog_layout);
            btn_positive.setText(getResources().getString(R.string.select_update));
            tv_msg.setText(String.format("%s %s\n\n%s %s", getResources().getString(R.string.app_update_msg_1),
                    version, getResources().getString(R.string.app_update_msg_2), loginModelList.get(0).getEMP_ATTENDENCE()));
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
            AlertDialog.Builder apk_download = new AlertDialog.Builder(LoginActivity.this);
            apk_download.setTitle(getResources().getString(R.string.apk_download_title));
            apk_download.setCancelable(false);
            @SuppressLint("InflateParams")
            ConstraintLayout apk_layout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_down_up_progress_layout, null);
            apk_download.setView(apk_layout);
            TextView tv_progress_message = functionCall.textId(apk_layout, R.id.dlg_progress_message);
            tv_progress_update = functionCall.textId(apk_layout, R.id.dlg_progress_update);
            tv_per_progress = functionCall.textId(apk_layout, R.id.dlg_per_progress_update);
            ProgressBar apk_progressBar = apk_layout.findViewById(R.id.dlg_apk_progress);
            tv_progress_message.setText(getResources().getString(R.string.apk_downloading_msg));
            alertDialog = apk_download.create();
            new Thread(new FTPAPI().new Download_apk(getApplicationContext(), login_handler, loginModelList.get(0).getEMP_ATTENDENCE(),
                    apk_progressBar)).start();
            alertDialog.show();
        }
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
            unregisterReceiver(receiver);
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