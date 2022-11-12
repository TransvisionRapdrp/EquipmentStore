package com.example.equipmentstore.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.equipmentstore.R;
import com.example.equipmentstore.adapters.ReceivedAdapter;
import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.interfac.Recv_responseInterface;
import com.example.equipmentstore.models.EquipmentDetails;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.example.equipmentstore.extra.constants.MyPREFERENCES;
import static com.example.equipmentstore.extra.constants.sPref_SUBDIVCODE;

public class Received_Aproval extends Fragment implements Recv_responseInterface, ReceivedAdapter.CameraInterface {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    static String pathname = "";
    static String pathextension = "";
    static String filename = "";
    static File mediaFile;
    View view;
    RecyclerView recyclerView;
    List<EquipmentDetails> equipmentDetails;
    EquipmentDetails equipdetails;
    ReceivedAdapter receivedAdapter;
    ImageView imageView;
    FunctionCall functionCall;
    ProgressDialog progressDialog;

    public Received_Aproval() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_received__aproval, container, false);
        Initialization();
        return view;
    }

    public void Initialization() {
        functionCall = new FunctionCall();
        progressDialog = new ProgressDialog(getContext());
      /*  String date = "/Date(1376841597000)/";
        Calendar calendar = Calendar.getInstance();
        String datereip = date.replace("/Date(", "").replace(")/", "");
        Long timeInMillis = Long.valueOf(datereip);
        calendar.setTimeInMillis(timeInMillis);
        calendar.getTime().toInstant();*/
        //   calendar.getTime().toString("dd-MMM-yyyy");//It Will Be in format 29-OCT-2014 2:26 PM

        SharedPreferences settings = getActivity().getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);/// Retrieve the Subdiv code from shared preference....
        String sub = settings.getString(sPref_SUBDIVCODE, "1");

        recyclerView = view.findViewById(R.id.approval_recycler);
        equipmentDetails = new ArrayList<>();

        equipdetails = new EquipmentDetails();
        receive_equipment(sub);
    }

    public void receive_equipment(String subdiv) {
        functionCall.showprogressdialog("Please wait to complete", progressDialog, "Fetching Data");
        RetroClient retroClient = new RetroClient();
        RegisterApi registerApi = retroClient.getApiService();
        registerApi.getReceivedApproval(subdiv).enqueue(new Callback<List<EquipmentDetails>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<EquipmentDetails>> call, Response<List<EquipmentDetails>> response) {
                if (response.isSuccessful()) {
                    equipmentDetails.clear();

                    for (EquipmentDetails equipmen : response.body())
                        if (TextUtils.isEmpty(equipmen.getRECEIVED_FLAG()))
                            equipmentDetails.add(equipmen);
                    Collections.reverse(equipmentDetails);
                    receivedAdapter = new ReceivedAdapter(getActivity(), equipmentDetails);
                    receivedAdapter.adopterListener(Received_Aproval.this::getReceivedData);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(receivedAdapter);
                    //Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<EquipmentDetails>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void gettoolbar() {
//        //toolbar = view.findViewById(R.id.rec_toolbar);
//        toolbar.setTitle("Equipment Received Aproval");
//        toolbar.setNavigationIcon(R.drawable.ic_cancel_24);
//        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().finish();
//            }
//        });
//
//    }

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

    //-----------------------------------------------------------------------------------------------------------------------------------
    public void receivedSubmit(String id, String remark) {
        File file = new File(pathname);
        String imageNameOnly = file.getName();
        String encode = functionCall.encoded(pathname);
        if (TextUtils.isEmpty(imageNameOnly)) {
            functionCall.showToast(getActivity(), "Please take pic & proceed.");
            return;
        }
        functionCall.showprogressdialog("Please wait to complete", progressDialog, "Uploading Data");
        receivedApproval(id, imageNameOnly, encode, remark, this);
    }

    public void receivedApproval(String ID, String FILENAME, String ENCODE, String REMARK, Recv_responseInterface recv_responseInterface) {

        RetroClient retroClient = new RetroClient();
        RegisterApi api = retroClient.getApiService();
        EquipmentDetails equipmentDetails1 = new EquipmentDetails();
        equipmentDetails1.setID(ID);
        equipmentDetails1.setFileName(FILENAME);
        equipmentDetails1.setEnodedFile(ENCODE);
        equipmentDetails1.setRESV_Remark(REMARK);
        api.receivedApproval(equipmentDetails1).enqueue(new Callback<EquipmentDetails>() {
            @Override
            public void onResponse(@NonNull Call<EquipmentDetails> call, @NonNull Response<EquipmentDetails> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    equipmentDetails.clear();
                    equipdetails = response.body();
                    recv_responseInterface.recvresponsesuccess(response.body());
                } else recv_responseInterface.recvresponsefailure();
            }

            @Override
            public void onFailure(@NonNull Call<EquipmentDetails> call, @NonNull Throwable t) {
                recv_responseInterface.recvresponsefailure();
            }
        });
    }

    @Override
    public void recvresponsesuccess(EquipmentDetails equipmentDetails) {
        progressDialog.dismiss();
        Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();

    }

    @Override
    public void recvresponsefailure() {
        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
    }

    @Override
    public void getReceivedData(EquipmentDetails details) {
        final AlertDialog alertDialog;
        AlertDialog.Builder insert_out = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams") LinearLayout received_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.received_layout, null);
        insert_out.setView(received_layout);
        insert_out.setCancelable(false);
        alertDialog = insert_out.create();

        Button btn_submit = received_layout.findViewById(R.id.btn_submit);
        TextView tv_close = received_layout.findViewById(R.id.txt_close);
        EditText et_remark = received_layout.findViewById(R.id.et_received_remark);

        tv_close.setOnClickListener(v -> alertDialog.dismiss());
        imageView = received_layout.findViewById(R.id.items_pic);
        imageView.setOnClickListener(v -> captureImage());
        btn_submit.setOnClickListener(v -> {
            alertDialog.dismiss();
            receivedSubmit(details.getID(), et_remark.getText().toString());
        });
        
        alertDialog.show();
    }


}
