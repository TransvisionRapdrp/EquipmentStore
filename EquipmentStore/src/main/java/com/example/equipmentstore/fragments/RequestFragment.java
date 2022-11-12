package com.example.equipmentstore.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.example.equipmentstore.MainActivity;
import com.example.equipmentstore.R;

import com.example.equipmentstore.adapters.CheckBoxAdapter;


import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.interfac.ResponseInterface;
import com.example.equipmentstore.models.EquipmentModel;
import com.example.equipmentstore.models.LoginDetails;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.equipmentstore.extra.constants.MyPREFERENCES;
import static com.example.equipmentstore.extra.constants.sPref_MOBILE_NO;
import static com.example.equipmentstore.extra.constants.sPref_SUBDIVCODE;
import static com.example.equipmentstore.extra.constants.sPref_USERNAME;


public class RequestFragment extends Fragment implements ResponseInterface, View.OnClickListener {

    View view;
    FunctionCall functionsCall;
    EditText item_names, mob_no, remark, stock_rolls;
    boolean checkbox_selected = false;
    String item_list = "";
    StringBuilder stringBuilder;
    Button submit;
    ProgressDialog progressDialog;
    TextView user_name, subdiv_code;
    CheckBoxAdapter checkboxAdapter;
    List<EquipmentModel> equipmentModelList;
    EquipmentModel equipmentModel1;
    List<LoginDetails> loginList;
    ArrayList<Integer> selectedItems;
    SharedPreferences settings;

    public RequestFragment() {
        //required empty constructor in fragments
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_request, container, false);
        intialize();

        return view;
    }

    public void intialize() {
        settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(getContext());
        functionsCall = new FunctionCall();
        user_name = view.findViewById(R.id.txt_user_name);
        selectedItems = new ArrayList<>();
        user_name.setText(settings.getString(sPref_USERNAME, "0"));// getting the User from Shared Preference
        subdiv_code = view.findViewById(R.id.txt_subdivcode);
        subdiv_code.setText(settings.getString(sPref_SUBDIVCODE, "1"));// getting the Subdiv from Shared Preference
        mob_no = view.findViewById(R.id.et_txtMobileNumber);
        mob_no.setText(settings.getString(sPref_MOBILE_NO,"0"));
        mob_no.setEnabled(false);
        remark = view.findViewById(R.id.et_remarks);
        stock_rolls = view.findViewById(R.id.et_stocks);
        item_names = view.findViewById(R.id.et_item_names);
        submit = view.findViewById(R.id.btn_submit);
        submit.setOnClickListener(this);
        loginList = new ArrayList<>();
        equipmentModel1 = new EquipmentModel();
        equipmentModelList = new ArrayList<>();
        EquipmentNames(this);
        item_names.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailog();
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit) {
            submit();
        }
    }

    public void EquipmentNames(ResponseInterface responseInterface) {
        functionsCall.showprogressdialog("Please Wait...",progressDialog,"Fatch Data");
        RetroClient r = new RetroClient();
        RegisterApi api = r.getApiService();
        api.getEquipmentName().enqueue(new Callback<List<EquipmentModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<EquipmentModel>> call, @NonNull Response<List<EquipmentModel>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    equipmentModelList.clear();
                    equipmentModelList = response.body();
                } else {
                    responseInterface.responseFailure();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<List<EquipmentModel>> call, @NonNull Throwable t) {
                responseInterface.responseFailure();
                progressDialog.dismiss();
            }
        });
    }


    public void submit() {
        if (TextUtils.isEmpty(mob_no.getText()) || mob_no.getText().length() < 10) {
            Toast.makeText(getContext(), "Please enter valid mobile number!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(item_names.getText())) {
            Toast.makeText(getContext(), "Please select items ", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(remark.getText())) {
            Toast.makeText(getContext(), "Plase enter ur remarks ", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(stock_rolls.getText())) {
            Toast.makeText(getContext(), "Please enter your stock rolls ", Toast.LENGTH_LONG).show();
            return;
        }
        functionsCall.showprogressdialog("Please Wait Submitting Your Data...",progressDialog,"Submit");
        /*equipmentInsert(user_name.getText().toString(), mob_no.getText().toString(), subdiv_code.getText().toString(),
                item_names.getText().toString(), remark.getText().toString(), stock_rolls.getText().toString(), this);*/
        equipmentInsert(this);
    }

    public void equipmentInsert(ResponseInterface responseInterface) {
        equipmentModel1 = new EquipmentModel();
        equipmentModel1.setUSER_NAME(user_name.getText().toString());
        equipmentModel1.setMOBILE_NO(mob_no.getText().toString());
        equipmentModel1.setSUBDIV_CODE(subdiv_code.getText().toString());
        equipmentModel1.setITEMS(item_names.getText().toString());
        equipmentModel1.setREMARK(remark.getText().toString());
        equipmentModel1.setSTOCK_ROLLS(stock_rolls.getText().toString());
        RetroClient retroClient = new RetroClient();
        RegisterApi api = retroClient.getApiService();
        api.getequipmentInsert(equipmentModel1).enqueue(new Callback<EquipmentModel>() {
            @Override
            public void onResponse(@NonNull Call<EquipmentModel> call, @NonNull Response<EquipmentModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                   // equipmentModelList.clear();
                    equipmentModel1 = response.body();
                  //  equipmentModelList = response.body();
                    responseInterface.responseSuccess1(response.body());
                    progressDialog.dismiss();
                } else {
                    responseInterface.responseFailure1();
                }
            }

            @Override
            public void onFailure(@NonNull Call<EquipmentModel> call, @NonNull Throwable t) {
                responseInterface.responseFailure1();
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public void responseSuccess(EquipmentModel equipmentModel) {

    }

    @Override
    public void responseFailure() {

    }

    @Override
    public void responseSuccess1(EquipmentModel equipmentModel) {

        if(equipmentModel.getMessage().equals("Saved Successfully"))
        {
            Toast.makeText(getContext(),"Inserted Successfully",Toast.LENGTH_LONG).show();
            clearEditText();
        }
      /*  final android.app.AlertDialog alertDialog;
        android.app.AlertDialog.Builder request_dialog = new android.app.AlertDialog.Builder(getContext());
        request_dialog.setTitle("Equipment Request");
        request_dialog.setMessage("Your Equipment Request Has Been Submitted Successfully & your request id is :" + equipmentModel.getID());
        request_dialog.setCancelable(false);
        request_dialog.setPositiveButton("ViewRequest", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((MainActivity) Objects.requireNonNull(getActivity())).switchContent(MainActivity.Steps.FORM2, "View");
                clearEditText();
            }
        });
        request_dialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                clearEditText();
                ((MainActivity) Objects.requireNonNull(getActivity())).switchContent(MainActivity.Steps.FORM5, "Main");
            }
        });
        alertDialog = request_dialog.create();
        alertDialog.show();*/


    }

    @Override
    public void responseFailure1() {
        final android.app.AlertDialog alertDialog;
        android.app.AlertDialog.Builder request_dialog = new android.app.AlertDialog.Builder(getContext());
        request_dialog.setTitle("Equipment Request");
        request_dialog.setMessage("Equipment Request has been failed!!");
        request_dialog.setCancelable(false);
        request_dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                clearEditText();
               dialog.cancel();

            }
        });
        alertDialog = request_dialog.create();
        alertDialog.show();
    }

    public void showDailog(){
        AlertDialog alertDialog;
        // final CharSequence[] items = {"A4 Size Paper", "Catridge-1020", "Catridge-M203", "Notebook", "Pen", "Pencil", "Eraser", "Steppler&Pins", "PrinerRools"};
        CharSequence[] items = new CharSequence[0];
        if (equipmentModelList != null){
            items = new CharSequence[equipmentModelList.size()];
            for (int i = 0; i < equipmentModelList.size() ; i++ )
                items[i] = equipmentModelList.get(i).getNAMES();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Your Employee Equipments:");
        CharSequence[] finalItems = items;
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            selectedItems.add(which);
                        } else if (selectedItems.contains(which)) {
                            selectedItems.remove(Integer.valueOf(which));
                        }else{
                            selectedItems.remove(Integer.valueOf(which));
                        }

                    }
                }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                stringBuilder = new StringBuilder();
                for (int i = 0; i < selectedItems.size(); i++) {
                    if (i == selectedItems.size() - 1) {
                        stringBuilder.append(finalItems[(selectedItems.get(i))]);
                    } else stringBuilder.append(finalItems[selectedItems.get(i)]).append(",");
                }

                item_names.setText(stringBuilder.toString());
                selectedItems.clear();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void clearEditText(){
        mob_no.setText("");
        remark.setText("");
        stock_rolls.setText("");
        item_names.setText("");
    }
}