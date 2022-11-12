package com.example.equipmentstore.extra;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.equipmentstore.R;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class FunctionCall {
    public void logStatus(String message) {

        Log.d("debug", message);
    }

    public void showToast(Context context, String Message) {
        Toast toast = Toast.makeText(context, Message, Toast.LENGTH_SHORT);
        toast.show();
    }

    //-----------------------------------------method for show progress dialog----------------------------------------------------//
    public void showprogressdialog(String Message, ProgressDialog dialog, String Title) {
        dialog.setTitle(Title);
        dialog.setMessage(Message);
        dialog.setCancelable(false);
        dialog.show();
    }

    public Button btnId(View view, int id) {
        return view.findViewById(id);
    }

    public TextView textId(View view, int id) {
        return view.findViewById(id);
    }
    //--------------------------------------CHECKING INTERNET IS ON OR NOT-------------------------------------------------------//
    public final boolean isInternetOn(Activity activity) {
        ConnectivityManager connect = (ConnectivityManager) activity.getSystemService(activity.getBaseContext().CONNECTIVITY_SERVICE);
        if (connect.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connect.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                connect.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public static String Appfoldername() {
        return "Equipments Store";
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
//    public String encoded(String filepath) {
//        File originalFile = new File(filepath);
//        String encodedBase64;
//        try {
//            byte[] bytes = FileUtils.readFileToByteArray(originalFile);
//            encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
//        } catch (IOException | OutOfMemoryError e) {
//            e.printStackTrace();
//            byte[] bytes = new byte[0];
//            try {
//                bytes = FileUtils.readFileToByteArray(originalFile);
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
//        }
//        return encodedBase64;
//    }

    public File fileStorePath(String value, String file) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), Appfoldername()
                + File.separator + value);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, File.separator + file);
    }

    public String encoded(String filepath) {
        File originalFile = new File(filepath);
        String encodedBase64;
        try {
            byte[] bytes = FileUtils.readFileToByteArray(originalFile);
            encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException | OutOfMemoryError e) {
            e.printStackTrace();
            byte[] bytes = new byte[0];
            try {
                bytes = FileUtils.readFileToByteArray(originalFile);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        }
        return encodedBase64;
    }

    public void progressdialog(String Message, ProgressDialog dialog, String Title) {
        dialog.setTitle(Title);
        dialog.setMessage(Message);
        dialog.setCancelable(false);
        dialog.show();
    }

    public String dateSet() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(currentDate);
    }

    public int getDays(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date1 = sdf.parse(date);
            Date date2 = Calendar.getInstance().getTime();
            long diff = date2.getTime() - date1.getTime();
            return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean compareDates(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date strDate = null,date1 = null;
        try {
            strDate = sdf.parse(date);
            date1 = sdf.parse(dateSet());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date1.equals(strDate);
    }

    public void setSnackBar(Context context, View root, String snackTitle) {
        @SuppressLint("WrongConstant") Snackbar snackbar = Snackbar.make(root, snackTitle, Snackbar.LENGTH_LONG);
        snackbar.show();
        View view = snackbar.getView();
        view.setBackgroundColor(context.getResources().getColor(R.color.red));
    }

    public File filestorepath(String value, String file) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), Appfoldername()
                + File.separator + value);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, File.separator + file);
    }
    public String filepath(String value) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), Appfoldername()
                + File.separator + value);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.toString();
    }

    public Bitmap getImage(String path, Context con) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;
        int[] newWH = new int[2];
        newWH[0] = srcWidth / 2;
        newWH[1] = (newWH[0] * srcHeight) / srcWidth;

        int inSampleSize = 1;
        while (srcWidth / 2 >= newWH[0]) {
            srcWidth /= 2;
            srcHeight /= 2;
            inSampleSize *= 2;
        }

        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = inSampleSize;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap sampledSrcBitmap = BitmapFactory.decodeFile(path, options);
        ExifInterface exif = new ExifInterface(path);
        String s = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        System.out.println("Orientation>>>>>>>>>>>>>>>>>>>>" + s);
        Matrix matrix = new Matrix();
        float rotation = rotationForImage(con, Uri.fromFile(new File(path)));
        if (rotation != 0f) {
            matrix.preRotate(rotation);
        }
        return Bitmap.createBitmap(
                sampledSrcBitmap, 0, 0, sampledSrcBitmap.getWidth(), sampledSrcBitmap.getHeight(), matrix, true);
    }

    private float rotationForImage(Context context, Uri uri) {
        if (Objects.requireNonNull(uri.getScheme()).equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            @SuppressLint("Recycle") Cursor c = context.getContentResolver().query(
                    uri, projection, null, null, null);
            if (c != null && c.moveToFirst()) {
                return c.getInt(0);
            }
        } else if (uri.getScheme().equals("file")) {
            try {
                ExifInterface exif = new ExifInterface(uri.getPath());
                return (int) exifOrientationToDegrees(
                        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return 0f;
    }

    private static float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public boolean compare(String v1, String v2) {
        String s1 = normalisedVersion(v1);
        String s2 = normalisedVersion(v2);
        int cmp = s1.compareTo(s2);
        String cmpStr = cmp < 0 ? "<" : cmp > 0 ? ">" : "==";
        return cmpStr.equals("<");
    }

    private String normalisedVersion(String version) {
        String[] split = Pattern.compile(".", Pattern.LITERAL).split(version);
        StringBuilder sb = new StringBuilder();
        for (String s : split) sb.append(String.format("%" + 4 + 's', s));
        return sb.toString();
    }

    public String bytesToMeg(long result) {
        double bytes = (double) result;
        double fileSizeInKB = bytes / 1024;
        double fileSizeInMB = fileSizeInKB / 1024;
        double fileSizeInGB = fileSizeInMB / 1024;
        if (fileSizeInMB > 1)
            if (fileSizeInGB > 1)
                return decimalRound(fileSizeInGB) + " GB";
            else return decimalRound(fileSizeInMB) + " MB";
        else return decimalRound(fileSizeInKB) + " KB";
    }

    public String decimalRound(double value) {
        BigDecimal a = new BigDecimal(value);
        BigDecimal roundOff = a.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return "" + roundOff;
    }

    public void updateApp(Context context, File apkFile) {
        Uri path;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            path = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", apkFile);
        } else path = Uri.fromFile(apkFile);
        Intent objIntent = new Intent(Intent.ACTION_VIEW);
        objIntent.setDataAndType(path, "application/vnd.android.package-archive");
        objIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(objIntent);
    }
}

