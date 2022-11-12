package com.example.equipmentstore.mpin_login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.equipmentstore.R;
import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.interfac.Recv_responseInterface;
import com.example.equipmentstore.models.EquipmentDetails;
import com.example.equipmentstore.models.EquipmentModel;
import com.example.equipmentstore.models.LoginDetails;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.equipmentstore.extra.constants.MyPREFERENCES;
import static com.example.equipmentstore.extra.constants.spref_password;

public class Mpin_reg extends AppCompatActivity implements Recv_responseInterface {
    EditText et_user, et_password, et_mpinnum, et_mpinconfirm;
    Button bt_reg;
    List<EquipmentModel> equipmentModelList;
    FunctionCall functionCall;
    SharedPreferences preferences;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    public Mpin_reg() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin_reg);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("MPIN Registration");
        setSupportActionBar(toolbar);

        preferences = getSharedPreferences(MyPREFERENCES, getApplicationContext().MODE_PRIVATE);

        et_user = findViewById(R.id.mpinlog_name);
        et_password = findViewById(R.id.mpinlogin_password);

        et_password.setText("");
        et_mpinnum = findViewById(R.id.et_mpin);
        et_mpinconfirm = findViewById(R.id.et_mpin_confirm);
        bt_reg = findViewById(R.id.mpinbt_login);

        equipmentModelList = new ArrayList<>();
        functionCall = new FunctionCall();

        //------------------------Mpin Registration---------------------------

        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    public void validation() {
        if (TextUtils.isEmpty(et_user.getText().toString())) {
            et_user.setError("please enter login user name");
            return;
        } else if (TextUtils.isEmpty(et_password.getText().toString())) {
            et_password.setError("please enter login user password");
            return;
        } else if (TextUtils.isEmpty(et_mpinnum.getText().toString())) {
            et_mpinnum.setError("please enter the user mpin num");
            return;
        } else if (TextUtils.isEmpty(et_mpinconfirm.getText().toString())) {
            et_mpinconfirm.setError(" please enter the user mpin confirm num ");
            return;
        }else if (!et_mpinconfirm.getText().toString().equals(et_mpinnum.getText().toString())){
            et_mpinconfirm.setError(" please enter the user correct confirm pin ");
            return;
        }
        mpin_registration(et_user.getText().toString(), et_password.getText().toString(), et_mpinnum.getText().toString());
    }

    public void mpin_registration(String username, String userpassword, String mpinnum) {
        RetroClient retroClient = new RetroClient();
        RegisterApi registerApi = retroClient.getApiService();
        LoginDetails loginDetails = new LoginDetails();
        loginDetails.setUSERNAME(username);
        loginDetails.setPASSWORD(userpassword);
        loginDetails.setMPIN(mpinnum);
        registerApi.getmpin_regNum(loginDetails).enqueue(new Callback<EquipmentDetails>() {
            @Override
            public void onResponse(Call<EquipmentDetails> call, Response<EquipmentDetails> response) {
                if (response.isSuccessful()) {
                    recvresponsesuccess(response.body());
                } else {
                    recvresponsefailure();
                }
            }

            @Override
            public void onFailure(Call<EquipmentDetails> call, Throwable t) {
                recvresponsefailure();
            }
        });
    }

    @Override
    public void recvresponsesuccess(EquipmentDetails equipmentDetails) {
        onBackPressed();
        functionCall.showToast(getApplicationContext(), "Mpin is generated Successfuly");
    }

    @Override
    public void recvresponsefailure() {
        functionCall.showToast(getApplicationContext(), "Mpin is Not generated ,please check it");
    }
}