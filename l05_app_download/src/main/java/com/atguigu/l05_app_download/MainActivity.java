package com.atguigu.l05_app_download;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 功能描述:
 * 1. 下载远程服务器端的APK文件
 * 2.有SD卡时选择安装在SD卡上；没有SD
 * 卡，则安装在手机上
 * 3. 同步显示下载进度
 * 4. 下载完成自动安装
 * 关键技术点:
 * 1.手机内部文件/SD卡文件读写
 * 2. ProgressDialog的使用
 * 3. 分线程请求网络
 * 4. 安装APK
 * 5.权限的使用：联网的权限;外部存储空间的访问权限
 */
public class MainActivity extends Activity {

    private ProgressDialog dialog;
    private File apkFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * onclick的回调方法
     */
    public void apk_download(View view) {

        //1.主线程显示提示视图
        showPD();
        createApk();
        download();
    }


    public void download() {
        //2.分线程联网下载数据
        new Thread() {

            @Override
            public void run() {

                String path = "http://192.168.23.1:8080/Test/yunflow.apk";

                HttpURLConnection conn = null;
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    URL url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();

                    if (conn.getResponseCode() == 200) {

                        int contentLength = conn.getContentLength();
                        dialog.setMax(contentLength);

                        is = conn.getInputStream();
                        fos = new FileOutputStream(apkFile);

                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            dialog.incrementProgressBy(len);
                            SystemClock.sleep(10);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();

                        installApk();
                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "联网失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.dismiss();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }.start();

    }

    /**
     * 跳转到安装界面的方法(隐式意图)
     */
    private void installApk() {

        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setData(Uri.parse("file:" + apkFile.getAbsolutePath()));
        startActivity(intent);
    }

    /**
     * 创建本地对应目录下的apkFile文件
     */
    private void createApk() {
        File fileDir;
        //判断sd卡是否是挂载状态
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //获取当前应用的目录路径，/storage/sdcard/Android/data/应用包名/files/
            fileDir = this.getExternalFilesDir(null);
            Log.e("TAG",fileDir.getAbsolutePath());
        } else {
            fileDir = this.getFilesDir();
        //data/data/com.atguigu.l05_app_download/files
            Log.e("TAG",fileDir.getAbsolutePath());
        }
        apkFile = new File(fileDir, "update.apk");
    }

    /**
     * 显示一个progressDialog
     */
    private void showPD() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("下载更新");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();
    }
}
