package com.example.equipmentstore.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.equipmentstore.R;
import com.example.equipmentstore.adapters.InoutAdapter;
import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.interfac.ResponseInterface1;
import com.example.equipmentstore.models.Complaints;
import com.example.equipmentstore.models.EquipmentDetails;
import com.example.equipmentstore.models.LoginDetails;
import com.example.equipmentstore.models.PostOfficeModel;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.example.equipmentstore.extra.constants.MyPREFERENCES;
import static com.example.equipmentstore.extra.constants.sPref_MOBILE_NO;
import static com.example.equipmentstore.extra.constants.sPref_SUBDIVCODE;
import static com.example.equipmentstore.extra.constants.sPref_USERNAME;

public class HardwareComplaint extends Fragment implements View.OnClickListener ,
        AdapterView.OnItemSelectedListener , ResponseInterface1 {


    Toolbar toolbar;
    LinearLayout layout;
    static FunctionCall functionCall;
    ProgressDialog progressDialog;
    InoutAdapter roleAdapter;
    EquipmentDetails equipmentDetails;
    List<EquipmentDetails> detailsList;
    Spinner spinner;
    String HARDWARE;
    Button submit;
    EditText mobile_no, description;
    TextView tv_user_name, tv_subdivision;
    String cons_imageextension = "", cons_ImgAdd = "", employee_id = "", file_encode = "", ImageDecode = "";
    static String pathname = "", pathextension = "", filename = "";
    private static Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    ImageView imageView;
    List<Complaints> complaintList;
    private ArrayList<PostOfficeModel> inoutlist;
    File mediaFile;
    SharedPreferences settings;

    View view;
    public HardwareComplaint() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hardware_complaint, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        Intent intent = getActivity().getIntent();
        settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        inoutlist = new ArrayList<>();
        layout = view.findViewById(R.id.lin_main);
        functionCall = new FunctionCall();
        detailsList = new ArrayList<>();
        complaintList = new ArrayList<>();
//        toolbar = view.findViewById(R.id.toolbar);
//        toolbar.setTitle(getResources().getString(R.string.hardware_complaint));
//        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setNavigationIcon(R.drawable.ic_close);
//        toolbar.setNavigationOnClickListener(v -> getActivity().finish());
        progressDialog = new ProgressDialog(getContext());
        spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        submit = view.findViewById(R.id.btn_submit);
        submit.setOnClickListener(this);
        mobile_no = view.findViewById(R.id.et_mob_nbr);
        mobile_no.setText(settings.getString(sPref_MOBILE_NO,"0"));
        mobile_no.setEnabled(false);
        description = view.findViewById(R.id.et_description);
        tv_user_name = view.findViewById(R.id.txt_user_name);
        tv_user_name.setText(settings.getString(sPref_USERNAME, "0"));
        tv_subdivision = view.findViewById(R.id.txt_subdiv_code);
        tv_subdivision.setText(settings.getString(sPref_SUBDIVCODE, "1"));
        imageView = view.findViewById(R.id.image);
        imageView.setOnClickListener(this);
        SetSpinnerValues();

    }

    //--------------------------------------------------------------------------------------------------------------------------------------
    private void SetSpinnerValues() {
        for (int i = 0; i < getResources().getStringArray(R.array.hardware_type).length; i++) {
            PostOfficeModel postOfficeModel = new PostOfficeModel();
            postOfficeModel.setRole(getResources().getStringArray(R.array.hardware_type)[i]);
            inoutlist.add(postOfficeModel);
        }
        roleAdapter = new InoutAdapter(inoutlist, getContext());
        spinner.setAdapter(roleAdapter);
        roleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            validation();
        }
        if (v.getId() == R.id.image) {
            captureImage();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------
    private void validation() {
        file_encode = functionCall.encoded(ImageDecode);
        if (TextUtils.isEmpty(mobile_no.getText().toString())) {
            functionCall.setSnackBar(getContext(), layout, "Please Enter Mobile Number");
            return;
        }
        if (HARDWARE.equals("SELECT")) {
            functionCall.setSnackBar(getContext(), layout, "Please Select Hardware Type");
            return;
        }
        if (TextUtils.isEmpty(description.getText().toString())) {
            functionCall.setSnackBar(getContext(), layout, "Please Enter Description");
            return;
        }
        functionCall.showprogressdialog("Please wait to complete", progressDialog, "Inserting Data");
        complaintInsert(tv_user_name.getText().toString(), tv_subdivision.getText().toString(), mobile_no.getText().toString(), HARDWARE, description.getText().toString(),
                filename, file_encode);
    }

    //1)Request or post data **********************************************************************************************************
    public void complaintInsert(String NAME, String SUBDIVCODE, String MOBILE, String TYPE, String DESCRIPTION, String FILENAME, String ENCODE) {
        RetroClient retroClient = new RetroClient();
        RegisterApi api = retroClient.getApiService();
        final Complaints[] complaints = {new Complaints()};
        complaints[0].setNAME(NAME);
        complaints[0].setSUBDIVCODE(SUBDIVCODE);
        complaints[0].setMOBILE(MOBILE);
        complaints[0].setTYPE(TYPE);
        complaints[0].setDESCRIPTION(DESCRIPTION);
        complaints[0].setFILENAME(FILENAME);
        complaints[0].setEnodedFile(ENCODE);

        api.insertComplaints(complaints[0]).enqueue(new Callback<Complaints>() {
            @Override
            public void onResponse(Call<Complaints> call, Response<Complaints> response) {
                if (response.isSuccessful()) {
                    complaintList.clear();
                    complaintList.add(response.body());
                    responseSuccess(new PostOfficeModel());
                } else responseFailure();
            }

            @Override
            public void onFailure(Call<Complaints> call, Throwable t) {
                responseFailure();
            }
        });
    }

    public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, getActivity());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", Objects.requireNonNull(getOutputMediaFile(type)));
        else return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner) {
            HARDWARE = inoutlist.get(position).getRole();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(android.os.Environment.getExternalStorageDirectory(), FunctionCall.Appfoldername() + File.separator +
                "Item_Pics");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".jpg");
            pathname = mediaStorageDir.getPath() + File.separator + timeStamp + ".jpg";
            pathextension = timeStamp + ".jpg";
            filename = timeStamp;
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                /*previewCapturedImage();*/
                String cons_ImgAdd = pathname;
                Bitmap bitmap = null;
                try {
                    bitmap = functionCall.getImage(cons_ImgAdd, getActivity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File destination = functionCall.fileStorePath("Compressed", pathextension);
                if (bitmap != null)
                    saveExternalPrivateStorage(destination, timestampItAndSave(bitmap));
                pathname = functionCall.filepath("Compressed") + File.separator + pathextension;
                Bitmap bitmap1 = null;
                try {
                    bitmap1 = functionCall.getImage(pathname, getActivity());
                    ImageDecode = pathname;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmap1);
                //  imagetaken = true;
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                functionCall.showToast(getActivity(), getString(R.string.user_cancelled_image));
            } else {
                // failed to capture image
                functionCall.showToast(getActivity(), getString(R.string.image_capture_failed));
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveExternalPrivateStorage(File folderDir, Bitmap bitmap) {
        if (folderDir.exists()) {
            folderDir.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(folderDir);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****************Below code is for adding Watermark************************/
    private Bitmap timestampItAndSave(Bitmap bitmap) {
        Bitmap watermarkimage = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // Bitmap bitmap = BitmapFactory.decodeFile(getOutputMediaFile().getAbsolutePath());

        // Bitmap src = BitmapFactory.decodeResource(); // the original file is cuty.jpg i added in resources
        Bitmap dest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(42);
        tPaint.setColor(Color.RED);
        tPaint.setStyle(Paint.Style.FILL);
        cs.drawBitmap(bitmap, 0f, 0f, null);

        float height = tPaint.measureText("yY");
        cs.drawText(filename, 20f, height + 15f, tPaint);
        // cs.drawText(dateTime, 2000f, 1500f, tPaint);

        try {
            dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(pathname)));
            watermarkimage = BitmapFactory.decodeStream(new FileInputStream(new File(pathname)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return watermarkimage;
    }

    @Override
    public void responseSuccess(PostOfficeModel postOfficeModel) {
        progressDialog.dismiss();
        functionCall.showToast(getContext(), complaintList.get(0).getMessage());
        description.setText("");
        mobile_no.setText("");
        spinner.setSelection(0);
    }

    @Override
    public void responseFailure() {
        progressDialog.dismiss();
        functionCall.showToast(getContext(), "Failure");
    }
}