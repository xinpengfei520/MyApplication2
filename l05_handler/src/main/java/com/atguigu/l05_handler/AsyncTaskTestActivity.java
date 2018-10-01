package com.atguigu.l05_handler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 使用异步任务实现apk文件的联网下载
 */
public class AsyncTaskTestActivity extends Activity {

    private ProgressDialog dialog;
    private File apkFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task_test);
    }

    /**
     * downloadApk的回调方法
     */
    public void downloadApk(View v) {

        new AsyncTask<Void, Void, Void>() {


            //联网操作第一步：主线程：显示提示视图
            @Override
            protected void onPreExecute() {
                //1.显示ProgressDialog
                dialog = new ProgressDialog(AsyncTaskTestActivity.this);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.show();

                //2.创建File
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    //storage/sdcard/Android/data/应用包名/files/xxx.apk
                    File filesDir = AsyncTaskTestActivity.this.getExternalFilesDir(null);
                    apkFile = new File(filesDir, "update.apk");
                }
            }

            //联网操作第二步：分线程：联网下载数据
            @Override
            protected Void doInBackground(Void... params) {

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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                return null;
            }

            //联网操作第三步：主线程：更新视图列表
            @Override
            protected void onPostExecute(Void aVoid) {

                dialog.dismiss();

                //安装下载好的apkFile对应的应用
                Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
                intent.setData(Uri.parse("file:" + apkFile.getAbsolutePath()));

                startActivity(intent);
            }
        }.execute();
    }
}
