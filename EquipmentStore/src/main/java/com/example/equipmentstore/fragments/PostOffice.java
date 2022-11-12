package com.example.equipmentstore.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.equipmentstore.R;
import com.example.equipmentstore.adapters.DataAdapter;
import com.example.equipmentstore.adapters.InoutAdapter;
import com.example.equipmentstore.adapters.TypeOfPostsAdapter;
import com.example.equipmentstore.extra.CustomListViewDialog;
import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.interfac.ResponseInterface1;
import com.example.equipmentstore.models.PostOfficeModel;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.equipmentstore.extra.constants.MyPREFERENCES;
import static com.example.equipmentstore.extra.constants.sPref_USERNAME;

public class PostOffice extends Fragment implements View.OnClickListener
        , ResponseInterface1, DataAdapter.RecyclerViewItemClickListener {

    View view;
    FunctionCall functionsCall;
    PostOfficeModel postOfficeModelList;
    EditText lett_from,lett_to,docket_no,postal_name,recieved_name,dispatch,authori,senders;
    private Spinner type_ofpost_spinner,inout_spinner;
    private ArrayList<PostOfficeModel> type_ofpostslist,inoutlist;
    Button insert;
    private int day,month,year;
    private String dd,date;
    private Calendar calender;
    private TextView txv_date,txv_calender;
    private String main_role="";
    private String main_role1="";
    List<PostOfficeModel> postofficemodelList,addressList;
    ProgressDialog progressDialog;
    LinearLayout layout,liner_recevied;
    String formattedDate="",to_code,from_code;
    PostOfficeModel postOfficeModel;
    CustomListViewDialog customDialog;
    boolean checkToFrom = false;
    SharedPreferences settings;
    PostOffice postOffice;


    public PostOffice() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        postOffice = this;
        view = inflater.inflate(R.layout.fragment_post_office, container, false);
        intialization();
        return view;
    }

    public void intialization() {
        progressDialog = new ProgressDialog(getContext());
        functionsCall = new FunctionCall();
        layout = view.findViewById(R.id.lin_layout);
        liner_recevied = view.findViewById(R.id.liner_recevied);
        //from below lines are for typeofpost spinner and adapter...
        type_ofpost_spinner = view.findViewById(R.id.typeof_posts_spin);
        spinner_value();
        settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        type_ofpostslist = new ArrayList<>();
        TypeOfPostsAdapter typeOfPostsAdapter = new TypeOfPostsAdapter(type_ofpostslist,getContext());
        type_ofpost_spinner.setAdapter(typeOfPostsAdapter);
        type_ofpost_spinner.setSelection(0);

        for (int i = 0; i < getResources().getStringArray(R.array.role).length; i++) {
            PostOfficeModel getSetValues = new PostOfficeModel();
            getSetValues.setRole(getResources().getStringArray(R.array.role)[i]);
            type_ofpostslist.add(getSetValues);
            typeOfPostsAdapter.notifyDataSetChanged();
        }
        //from below onwards inout spinner & adapter
        inout_spinner = view.findViewById(R.id.inout_spin1);
        spinner_value1();
        inoutlist = new ArrayList<>();
        InoutAdapter inoutAdapter = new InoutAdapter(inoutlist,getContext());
        inout_spinner.setAdapter(inoutAdapter);
        inout_spinner.setSelection(0);

        for (int i = 0; i < getResources().getStringArray(R.array.time).length; i++) {
            PostOfficeModel postOfficeModel = new PostOfficeModel();
            postOfficeModel.setRole(getResources().getStringArray(R.array.time)[i]);
            inoutlist.add(postOfficeModel);
            inoutAdapter.notifyDataSetChanged();
        }

        lett_from = view.findViewById(R.id.et_letterFrom);
        lett_to = view.findViewById(R.id.et_letterTo);
        docket_no = view.findViewById(R.id.et_docketNo);
        postal_name = view.findViewById(R.id.et_postalName);
        recieved_name = view.findViewById(R.id.et_recievedName);
        dispatch = view.findViewById(R.id.et_dispatch);
        authori = view.findViewById(R.id.et_authorized);
        senders = view.findViewById(R.id.et_senders);
        insert = view.findViewById(R.id.btn_insert);
        insert.setOnClickListener(this);
        senders.setText(settings.getString(sPref_USERNAME, "0"));
        senders.setEnabled(false);
        postofficemodelList = new ArrayList<>();
        postOfficeModel = new PostOfficeModel();
        getAddressList();
        lett_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataAdapter dataAdapter = new DataAdapter(addressList, postOffice);
                customDialog = new CustomListViewDialog(getActivity(), dataAdapter);
                customDialog.show();
                customDialog.setCanceledOnTouchOutside(false);
                checkToFrom = true;
            }
        });

        lett_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataAdapter dataAdapter = new DataAdapter(addressList, postOffice);
                customDialog = new CustomListViewDialog(getActivity(), dataAdapter);
                customDialog.show();
                customDialog.setCanceledOnTouchOutside(false);
                checkToFrom = false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_insert) {
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formattedDate = df.format(c.getTime());
            if (main_role.equals("Select TypeOf Posts")) {
                functionsCall.setSnackBar(getContext(), layout, "Please Select Type of Posts");
                return;
            }
            if (main_role1.equals("Select In/Out")) {
                functionsCall.setSnackBar(getContext(), layout, "Please Select Your In/Out");
                return;
            }
            if (TextUtils.isEmpty(lett_from.getText())) {
                setEditTextError(lett_from, "Please enter letter from...");
                return;
            }
            if (TextUtils.isEmpty(lett_to.getText())) {
                setEditTextError(lett_to, "Please enter letter to...");
                return;
            }
            if (TextUtils.isEmpty(docket_no.getText())) {
                setEditTextError(docket_no, "Please enter docket number...");
                return;
            }
            if (TextUtils.isEmpty(postal_name.getText())) {
                setEditTextError(postal_name, "Please enter postal name...");
                return;
            }
            if (TextUtils.isEmpty(dispatch.getText())) {
                setEditTextError(dispatch, "Please enter dispatch...");
                return;
            }
            if (TextUtils.isEmpty(authori.getText())) {
                setEditTextError(authori, "Please enter authorized...");
                return;
            }
            if (TextUtils.isEmpty(senders.getText())) {
                setEditTextError(senders, "Please enter letter from...");
                return;
            }
            functionsCall.showprogressdialog("Please Wait Submitting Your Data...", progressDialog, "Submit");
            //insertPostData(main_role,main_role1,lett_from.getText().toString(),lett_to.getText().toString(),docket_no.getText().toString(),postal_name.getText().toString(),recieved_name.getText().toString(),dispatch.getText().toString(),authori.getText().toString(),senders.getText().toString(),formattedDate,this);
            insertPostData(this);
        }
    }


    private void spinner_value() {
        type_ofpost_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PostOfficeModel roles = type_ofpostslist.get(position);
                main_role = roles.getRole();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void spinner_value1() {
        inout_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PostOfficeModel roles = inoutlist.get(position);
                main_role1 = roles.getRole();
                if (main_role1.equals("Out")){
                    liner_recevied.setVisibility(View.GONE);
                    recieved_name.setVisibility(View.GONE);
                    recieved_name.setText("0");
                }else {
                    recieved_name.setVisibility(View.VISIBLE);
                    recieved_name.setText("");
                    liner_recevied.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void getAddressList(){
        RetroClient retroClient = new RetroClient();
        RegisterApi registerApi = retroClient.getApiService();
        registerApi.getAddresses().enqueue(new Callback<List<PostOfficeModel>>() {
            @Override
            public void onResponse(Call<List<PostOfficeModel>> call, Response<List<PostOfficeModel>> response) {
                if (response.isSuccessful()){
                    addressList = response.body();
                }else responseFailure();
            }

            @Override
            public void onFailure(Call<List<PostOfficeModel>> call, Throwable t) {
                responseFailure();
            }
        });
    }

    public void insertPostData(ResponseInterface1 responseInterface1){
        postOfficeModel = new PostOfficeModel();
        postOfficeModel.setType_of_post(main_role);
        postOfficeModel.setInOut(main_role1);
        postOfficeModel.setLetterFrom(from_code.trim());
        postOfficeModel.setLetterTo(to_code.trim());
        postOfficeModel.setDocketNo(docket_no.getText().toString());
        postOfficeModel.setPostalName(postal_name.getText().toString());
        postOfficeModel.setReceivedName(recieved_name.getText().toString());
        postOfficeModel.setDispatch(dispatch.getText().toString());
        postOfficeModel.setAuthorized(authori.getText().toString());
        postOfficeModel.setSenders(senders.getText().toString());
        postOfficeModel.setDate(formattedDate);
        RetroClient retroClient = new RetroClient();
        RegisterApi registerApi = retroClient.getApiService();
        registerApi.insertPostLetter(postOfficeModel).enqueue(new Callback<PostOfficeModel>() {
            @Override
            public void onResponse(Call<PostOfficeModel> call, Response<PostOfficeModel> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    postofficemodelList.clear();
               //     postofficemodelList = response.body();
                    postOfficeModel = response.body();
                    responseInterface1.responseSuccess(response.body());
                    progressDialog.dismiss();
                }else{
                    responseInterface1.responseFailure();
                }
            }

            @Override
            public void onFailure(Call<PostOfficeModel> call, Throwable t) {
                responseInterface1.responseFailure();
            }
        });

    }

    @Override
    public void responseSuccess(PostOfficeModel postOfficeModel) {
        if(postOfficeModel.getMessage().equals("Saved Successfully")){
            Toast.makeText(getContext(),"Your Post is inserted..",Toast.LENGTH_LONG).show();
            clearEditText();
        }
        progressDialog.dismiss();
    }

    @Override
    public void responseFailure() {
        Toast.makeText(getContext(),"Your Post is not inserted...",Toast.LENGTH_LONG).show();
        clearEditText();
        progressDialog.dismiss();
    }

    private void clearEditText(){
        lett_from.setText("");
        lett_to.setText("");
        docket_no.setText("");
        postal_name.setText("");
        recieved_name.setText("");
        type_ofpost_spinner.setSelection(0);
        inout_spinner.setSelection(0);

    }

    private void setEditTextError(EditText editText, String value) {
        editText.setError(value);
        editText.setSelection(editText.getText().length());
        editText.requestFocus();
    }

    @Override
    public void clickOnItem(PostOfficeModel data) {

        if (checkToFrom){
            lett_from.setText(data.getBranch_Name());
            from_code = data.getSUBDIVCODE();
        } else{
            lett_to.setText(data.getBranch_Name());
            to_code = data.getSUBDIVCODE();
        }

        if (customDialog != null){
            customDialog.dismiss();
        }
    }
}