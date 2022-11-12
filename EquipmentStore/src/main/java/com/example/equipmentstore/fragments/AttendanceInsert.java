package com.example.equipmentstore.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.equipmentstore.R;
import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.models.AttendanceSummary;
import com.example.equipmentstore.models.EquipmentDetails;
import com.example.equipmentstore.network.RegisterApi;
import com.example.equipmentstore.network.RetroClient;
import com.example.equipmentstore.network.RetroClient1;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import android.location.LocationListener;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.example.equipmentstore.R.drawable.ic_add_a_photo;
import static com.example.equipmentstore.extra.constants.MyPREFERENCES;
import static com.example.equipmentstore.extra.constants.sPref_ID;
import static com.example.equipmentstore.extra.constants.sPref_USERNAME;


public class AttendanceInsert extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    View view;
    LatLng latLng;
    static String pathname = "";
    static String pathextension = "";
    static String filename = "";
    static File mediaFile;
    ImageView imageView;
    FunctionCall functionCall;
    TextView user_name, user_id;
    SharedPreferences settings;
    List<AttendanceSummary> summaryList;
    ProgressDialog progressdialog;
    Button bt_submit;
    String cons_imageextension = "", cons_ImgAdd = "", employee_id = "", employee_name = "", file_encode = "", ImageDecode = "", address = "NA", remark = "";
    EditText et_remarks;
    boolean imagetaken = false;
    private FusedLocationProviderClient fusedLocationClient;

    public AttendanceInsert() {
        // Required empty public constructor
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendance_insert, container, false);
        functionCall = new FunctionCall();
        settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        onLocation();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(4000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        progressdialog = new ProgressDialog(getContext());
        imageView = view.findViewById(R.id.imageView);
        user_name = view.findViewById(R.id.user_name);
        user_id = view.findViewById(R.id.user_id);
        bt_submit = view.findViewById(R.id.bt_submit);
        et_remarks = view.findViewById(R.id.et_remarks);
        user_id.setText(settings.getString(sPref_ID, "0"));
        user_name.setText(settings.getString(sPref_USERNAME, "0"));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_details();
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());


        return view;
    }

    @SuppressLint("MissingPermission")
    public void getUserLocation(){
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    //Place current location marker
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Objects.requireNonNull(locationManager).getBestProvider(new Criteria(), true);
                    //move map camera
                    float zoomLevel = 16.0f; //This goes up to 21
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            }
        });
    }

    public void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
             //   startLocationUpdate();
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(getActivity(), 1001);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }


            }
        });
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null)
                return;
            for (Location location : locationResult.getLocations()) {

                if (mCurrLocationMarker != null){
                    mCurrLocationMarker.remove();
                }
                //Place current location marker
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Objects.requireNonNull(locationManager).getBestProvider(new Criteria(), true);
                //move map camera
                float zoomLevel = 16.0f; //This goes up to 21
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

            }
        }
    };

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdate() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    //---------------------------------------------------------------------------------------------------------------------------------------
    private void submit_details() {

        employee_id = user_id.getText().toString();
        employee_name = user_name.getText().toString();
        remark = et_remarks.getText().toString();
        ImageDecode = pathname;
        file_encode = functionCall.encoded(ImageDecode);
        if (!functionCall.isInternetOn(getActivity())) {
            functionCall.showToast(getContext(), "Please Connect to Internet");
            return;
        }

        if (!imagetaken) {
            functionCall.showToast(getContext(), "Please Take Photo!!");
            return;
        }

        if (TextUtils.isEmpty(remark)) {
            et_remarks.setError("Enter Remarks");
            return;
        }
        double lat = 0.00, lon = 0.00;
        if (latLng != null) {
            lat = latLng.latitude;
            lon = latLng.longitude;
            try {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses.size() > 0)
                    address = (addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getAddressLine(2));
            } catch (Exception e) {
                e.printStackTrace();
                address = "NA";
            }
        }
        attendanceInsert("0", employee_id, employee_name, filename, String.valueOf(lon), String.valueOf(lat), remark, address, file_encode);

    }

    @Override
    public void onPause() {
        super.onPause();
    //    stopLocationUpdate();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                // buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                getUserLocation();
               // checkSettingsAndStartLocationUpdates();
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            // buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
            //checkSettingsAndStartLocationUpdates();
            getUserLocation();
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation();
                  //  checkSettingsAndStartLocationUpdates();
                    //buildGoogleApiClient();
                    mGoogleMap.setMyLocationEnabled(true);
                }

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

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
                imagetaken = true;
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


    private void onLocation() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (!Objects.requireNonNull(lm).isProviderEnabled(LocationManager.GPS_PROVIDER) || !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Alert");
            builder.setMessage("Please enable GPS");
            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    //Request or post data **********************************************************************************************************
    public void attendanceInsert(String EMINO, String EMPID, String EMPNAME, String PHOTO, String LOG, String LAT, String REMARK, String ADDRESS, String Encodefile) {
        if (TextUtils.isEmpty(EMINO)) {
            EMINO = "0";
        }

        RetroClient1 retroClient = new RetroClient1();
        RegisterApi api = retroClient.getApiService();
        functionCall.showprogressdialog("Please wait to complete", progressdialog, "Inserting Data");
        api.attendanceInsert(EMINO, EMPID, EMPNAME, PHOTO, LOG, LAT, REMARK, ADDRESS, Encodefile).enqueue(new Callback<List<AttendanceSummary>>() {
            @Override
            public void onResponse(@NonNull Call<List<AttendanceSummary>> call, @NonNull Response<List<AttendanceSummary>> response) {
                if (response.isSuccessful()) {
                    summaryList = response.body();
                    if (summaryList.size() > 0)
                        recvresponsesuccess(summaryList.get(0));
                    else recvresponsefailure();
                } else recvresponsefailure();
            }

            @Override
            public void onFailure(@NonNull Call<List<AttendanceSummary>> call, @NonNull Throwable t) {
                recvresponsefailure();
            }
        });
    }

    public void recvresponsesuccess(AttendanceSummary attendanceSummary) {
        progressdialog.dismiss();
        imageView.setImageDrawable(getResources().getDrawable(ic_add_a_photo));
        et_remarks.setText("");
        Toast.makeText(getContext(), attendanceSummary.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void recvresponsefailure() {
        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_LONG).show();
        progressdialog.dismiss();
    }

}