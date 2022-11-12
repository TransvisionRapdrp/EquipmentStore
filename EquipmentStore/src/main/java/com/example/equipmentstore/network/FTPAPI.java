package com.example.equipmentstore.network;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.ProgressBar;

import com.example.equipmentstore.extra.FunctionCall;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static com.example.equipmentstore.extra.constants.APK_FILE_DOWNLOADED;
import static com.example.equipmentstore.extra.constants.APK_FILE_NOT_FOUND;
import static com.example.equipmentstore.extra.constants.APK_NAME;
import static com.example.equipmentstore.extra.constants.DIR_APK;
import static com.example.equipmentstore.extra.constants.DIR_FTP_APK;
import static com.example.equipmentstore.extra.constants.DOWNLOAD_PERCENTAGE;
import static com.example.equipmentstore.extra.constants.DOWNLOAD_RECEIVER;
import static com.example.equipmentstore.extra.constants.DOWNLOAD_SIZE;
import static com.example.equipmentstore.extra.constants.DOWNLOAD_UPDATE;
import static com.example.equipmentstore.extra.constants.FILE_APK_FORMAT;
import static com.example.equipmentstore.extra.constants.FTP_HOST;
import static com.example.equipmentstore.extra.constants.FTP_PASS;
import static com.example.equipmentstore.extra.constants.FTP_PORT;
import static com.example.equipmentstore.extra.constants.FTP_USER;

public class FTPAPI {
    private FunctionCall functionsCall = new FunctionCall();
    private String FTP_HOST1;
    private String FTP_USER1;
    private String FTP_PASS1;

    public FTPAPI() {
        FTP_HOST1 = FTP_HOST;
        FTP_USER1 = FTP_USER;
        FTP_PASS1 = FTP_PASS;
    }

    public class Download_apk implements Runnable {
        Context context;
        boolean downloadapk=false, file_found=false;
        Handler handler;
        ProgressBar progressBar;
        String mobilepath = functionsCall.filepath(DIR_APK) + File.separator;
        String update_version;

        public Download_apk(Context context, Handler handler, String update_version, ProgressBar progressBar) {
            this.context = context;
            this.handler = handler;
            this.update_version = update_version;
            this.progressBar = progressBar;
        }

        @Override
        public void run() {
            int count;
            long read = 0;
            progressBar.setProgress(0);

            FTPClient ftp_1 = new FTPClient();
            try {
                ftp_1.connect(FTP_HOST1, FTP_PORT);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
            try {
                downloadapk = ftp_1.login(FTP_USER1, FTP_PASS1);
            } catch (FTPConnectionClosedException | NullPointerException e) {
                e.printStackTrace();
                try {
                    downloadapk = false;
                    ftp_1.disconnect();
                } catch (IOException | NullPointerException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (downloadapk) {
                try {
                    ftp_1.setFileType(FTP.BINARY_FILE_TYPE);
                    ftp_1.enterLocalPassiveMode();
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    ftp_1.changeWorkingDirectory(DIR_FTP_APK);
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    FTPFile[] ftpFiles = ftp_1.listFiles(DIR_FTP_APK);
                    String namefile;
                    long filelength = 0;
                    for (FTPFile ftpFile : ftpFiles) {
                        namefile = ftpFile.getName();
                        boolean isFile = ftpFile.isFile();
                        if (isFile)
                            if (namefile.equals(APK_NAME + update_version + FILE_APK_FORMAT)) {
                                filelength = ftpFile.getSize();
                                file_found = true;
                                break;
                            }
                    }
                    if (file_found) {
                        File file = new File(mobilepath + APK_NAME + update_version + FILE_APK_FORMAT);
                        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                        InputStream inputStream = ftp_1.retrieveFileStream(DIR_FTP_APK + APK_NAME + update_version
                                + FILE_APK_FORMAT);
                        byte[] bytesIn = new byte[1024];
                        while ((count = inputStream.read(bytesIn)) != -1) {
                            read += count;
                            progressBar.setProgress((int)((read * 100) / filelength));
                            sendBroadcastMessage(context, functionsCall.bytesToMeg(filelength), functionsCall.bytesToMeg(read),
                                    String.valueOf((int)((read * 100) / filelength)));
                            try {
                                outputStream.write(bytesIn, 0, count);
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        inputStream.close();
                        outputStream.close();

                        if (ftp_1.completePendingCommand())
                            handler.sendEmptyMessage(APK_FILE_DOWNLOADED);
                    } else handler.sendEmptyMessage(APK_FILE_NOT_FOUND);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                ftp_1.logout();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendBroadcastMessage(Context context, String download_size, String download_update, String download_percentage) {
        Intent intent = new Intent();
        intent.setAction(DOWNLOAD_RECEIVER);
        intent.putExtra(DOWNLOAD_UPDATE, download_update);
        intent.putExtra(DOWNLOAD_SIZE, download_size);
        intent.putExtra(DOWNLOAD_PERCENTAGE, download_percentage);
        context.sendBroadcast(intent);
    }
}
