package com.example.equipmentstore.extra;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.equipmentstore.MainActivity;
import com.example.equipmentstore.R;
import com.example.equipmentstore.models.PostOfficeModel;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.equipmentstore.extra.constants.MyPREFERENCES;
import static com.example.equipmentstore.extra.constants.sPref_USERNAME;

public class SignatureTracking extends BottomSheetDialogFragment {

    View view;
    TextView txt_id, txt_type_post, txt_letter_from;
    LinearLayout subDivLinear, divLinear;
    private RadioGroup subDivRadioGroup, divRadioGroup;
    Button btnSubDivisionSignature, btnDivisionSignature;
    FunctionCall functionCall;
    ProgressDialog progressDialog;
    public static final int DAG_SUB_DIVISION_APPROVAL = 1;
    AlertDialog alertDialog = null;
    SharedPreferanceStore sharedPreferanceStore;
    EditText et_remarks_div, et_remarks_subdiv;
    PostOfficeModel postOfficeModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.signature_bottom_sheet, container, false);
        Bundle bundle = getArguments();
        postOfficeModel = (PostOfficeModel) Objects.requireNonNull(bundle).getSerializable("Data");
        functionCall = new FunctionCall();
        progressDialog = new ProgressDialog(getActivity());
        sharedPreferanceStore = SharedPreferanceStore.getInstance(getActivity());

        subDivRadioGroup = view.findViewById(R.id.radioGroup);
        subDivRadioGroup.clearCheck();
        divRadioGroup = view.findViewById(R.id.radioGroup1);
        divRadioGroup.clearCheck();
        txt_id = view.findViewById(R.id.txt_id);
        txt_id.setText(postOfficeModel.getID());
        txt_type_post = view.findViewById(R.id.txt_type_post);
        txt_type_post.setText(postOfficeModel.getType_of_post());
        txt_letter_from = view.findViewById(R.id.txt_letter_from);
        txt_letter_from.setText(postOfficeModel.getLetterFrom());
        et_remarks_div = view.findViewById(R.id.et_remarks_div);
        et_remarks_subdiv = view.findViewById(R.id.et_remarks_subdiv);
        subDivLinear = view.findViewById(R.id.linearLayout7);
        divLinear = view.findViewById(R.id.linearLayout6);
        btnSubDivisionSignature = view.findViewById(R.id.btnSubDivisionSignature);
        btnDivisionSignature = view.findViewById(R.id.btnDivisionSignature);

        btnDivisionSignature.setOnClickListener(v -> divisionApproval());
        btnSubDivisionSignature.setOnClickListener(v -> subDivApproval());

        if (postOfficeModel.getSDO_STATUS().trim().equals("N")) {
            subDivLinear.setVisibility(View.VISIBLE);
            divLinear.setVisibility(View.GONE);
        } else {
            subDivLinear.setVisibility(View.GONE);
            divLinear.setVisibility(View.VISIBLE);
        }

        if (postOfficeModel.getDIV_STATUS().trim().equals("Y")) {
            subDivLinear.setVisibility(View.GONE);
            divLinear.setVisibility(View.GONE);
        }

        subDivRadioGroup.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
            RadioButton rb = (RadioButton) group.findViewById(checkedId);
            if (null != rb) {
                if (rb.getText().toString().trim().equals("PENDING")) {
                    et_remarks_subdiv.setVisibility(View.VISIBLE);
                    editTextAnim(et_remarks_subdiv.getHeight(),0,et_remarks_subdiv);
                } else{
                    et_remarks_subdiv.setVisibility(View.GONE);
                    editTextAnim(0,et_remarks_subdiv.getHeight(),et_remarks_subdiv);
                }
            }
        });

        divRadioGroup.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
            RadioButton rb = (RadioButton) group.findViewById(checkedId);
            if (null != rb) {
                if (rb.getText().toString().trim().equals("PENDING")) {
                    editTextAnim(et_remarks_div.getHeight(),0,et_remarks_div);
                    et_remarks_div.setVisibility(View.VISIBLE);
                } else {
                    editTextAnim(0,et_remarks_div.getHeight(),et_remarks_div);
                    et_remarks_div.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    private void editTextAnim(float fromYD,float toTD,View view){
        TranslateAnimation animate = new TranslateAnimation(0, 0, fromYD, toTD);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }


    private void divisionApproval(){
        RadioButton rb = divRadioGroup.findViewById(divRadioGroup.getCheckedRadioButtonId());
        if (rb == null) {
            Toast.makeText(getContext(), "Choose Signature from Division", Toast.LENGTH_LONG).show();
            return;
        }

        if (rb.getText().toString().trim().equals("YES")) {
            functionCall.progressdialog("Wait for seconds", progressDialog, "Data Submitting");
            RetroClient retroClient = new RetroClient();
            RegisterApi api = retroClient.getApiService();
            api.divisionApproval(postOfficeModel.getID()).enqueue(new Callback<PostOfficeModel>() {
                @Override
                public void onResponse(Call<PostOfficeModel> call, Response<PostOfficeModel> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getMessage().equals("Sign Approved Successfully")) {
                            sharedPreferanceStore.clear();
                            showDialog(DAG_SUB_DIVISION_APPROVAL, 0);
                        } else {
                            Toast.makeText(getContext(), "Approval Failed Please Try Again", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PostOfficeModel> call, Throwable t) {
                    Toast.makeText(getContext(), "Approval Failed Please Try Again", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        } else if (rb.getText().toString().trim().equals("PENDING")) {
            if (TextUtils.isEmpty(et_remarks_div.getText().toString())) {
                et_remarks_div.setError("Enter Remark");
                return;
            }
            sharedPreferanceStore.put(SharedPreferanceStore.Key.REMARK_FLG, "1");
            sharedPreferanceStore.put(SharedPreferanceStore.Key.REMARK_DATE, functionCall.dateSet());

            SharedPreferences settings = Objects.requireNonNull(getActivity()).getSharedPreferences(MyPREFERENCES,
                    Context.MODE_PRIVATE);/// Retrieve the Subdiv code from shared preference....
            String username = settings.getString(sPref_USERNAME, "1");
            postOfficeModel.setPostID(postOfficeModel.getID());
            postOfficeModel.setRemarks(et_remarks_div.getText().toString());
            postOfficeModel.setUserName(username);
            postOfficeRemarks(postOfficeModel);

        }
    }

    private void subDivApproval(){
        RadioButton rb = subDivRadioGroup.findViewById(subDivRadioGroup.getCheckedRadioButtonId());
        if (rb == null) {
            Toast.makeText(getContext(), "Choose Signature from Sub Division", Toast.LENGTH_LONG).show();
            return;
        }

        if (rb.getText().toString().trim().equals("YES")) {
            functionCall.progressdialog("Wait for seconds", progressDialog, "Data Submitting");
            RetroClient retroClient = new RetroClient();
            RegisterApi api = retroClient.getApiService();
            api.subDivisionApproval(postOfficeModel.getID()).enqueue(new Callback<PostOfficeModel>() {
                @Override
                public void onResponse(Call<PostOfficeModel> call, Response<PostOfficeModel> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body().getMessage().equals("Sign Approved Successfully")) {
                            sharedPreferanceStore.put(SharedPreferanceStore.Key.ID, postOfficeModel.getID());
                            sharedPreferanceStore.put(SharedPreferanceStore.Key.CURRENT_DATE, functionCall.dateSet());
                            showDialog(DAG_SUB_DIVISION_APPROVAL, 1);
                        } else {
                            Toast.makeText(getContext(), "Approval Failed Please Try Again", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PostOfficeModel> call, Throwable t) {
                    Toast.makeText(getContext(), "Approval Failed Please Try Again", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        } else if (rb.getText().toString().trim().equals("PENDING")) {
            if (TextUtils.isEmpty(et_remarks_subdiv.getText().toString())) {
                et_remarks_subdiv.setError("Enter Remark");
                return;
            }
            SharedPreferences settings = Objects.requireNonNull(getActivity()).getSharedPreferences(MyPREFERENCES,
                    Context.MODE_PRIVATE);/// Retrieve the Subdiv code from shared preference....
            String username = settings.getString(sPref_USERNAME, "1");
            sharedPreferanceStore.put(SharedPreferanceStore.Key.REMARK_FLG, "1");
            sharedPreferanceStore.put(SharedPreferanceStore.Key.REMARK_DATE, functionCall.dateSet());
            postOfficeModel.setPostID(postOfficeModel.getID());
            postOfficeModel.setRemarks(et_remarks_subdiv.getText().toString());
            postOfficeModel.setUserName(username);
            postOfficeRemarks(postOfficeModel);
        }
    }

    private void showDialog(int dialogView, int DIVSUBDIVFLAF) {

        if (dialogView == DAG_SUB_DIVISION_APPROVAL) {
            AlertDialog.Builder subDivSignatureDlg = new AlertDialog.Builder(getActivity());
            ConstraintLayout received_layout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dlg_signaturetime, null);
            subDivSignatureDlg.setView(received_layout);
            subDivSignatureDlg.setCancelable(false);
            alertDialog = subDivSignatureDlg.create();
            ImageView imageView = received_layout.findViewById(R.id.imageView3);
            imageView.setImageResource(R.drawable.approval_success);
            TextView textView3 = received_layout.findViewById(R.id.textView3);
            if (DIVSUBDIVFLAF == 1)
                textView3.setText("Approval from Sub Division as taken Successfully and Five Days to Take Division Approval");
            else textView3.setText("Approval from Division as taken Successfully!!");
            Button button = received_layout.findViewById(R.id.btn_submit);

            button.setOnClickListener(v -> {
                alertDialog.dismiss();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            });

            alertDialog.show();
        }
    }

    public void postOfficeRemarks(PostOfficeModel postOfficeModel) {
        functionCall.progressdialog("Wait for seconds", progressDialog, "Data Submitting");
        RetroClient retroClient = new RetroClient();
        RegisterApi api = retroClient.getApiService();
        api.postOfficeRemark(postOfficeModel).enqueue(new Callback<PostOfficeModel>() {
            @Override
            public void onResponse(Call<PostOfficeModel> call, Response<PostOfficeModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getMessage().equals("Saved Successfully")) {
                        functionCall.showToast(getActivity(), "Success!!");
                        startActivity(new Intent(getContext(), MainActivity.class));
                        Objects.requireNonNull(getActivity()).finish();
                    } else functionCall.showToast(getActivity(), "Failed...Please try again!!");
                }
            }

            @Override
            public void onFailure(Call<PostOfficeModel> call, Throwable t) {
                progressDialog.dismiss();
                functionCall.showToast(getActivity(), "Failed...Please try again!!");
            }
        });
    }
}
