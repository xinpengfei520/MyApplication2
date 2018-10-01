package com.atguigu.l05_app_handler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by xinpengfei on 2016/9/7.
 * 处理图片的下载和三级缓存的问题
 */
public class ImageLoader {

    private Context context;
    private int loading;//正在加载的图片
    private int error;//加载失败的图片
    //一级缓存对应的集合
    //真实的项目中，使用LruCache实现一级的缓存
    private HashMap<String,Bitmap> bitmapHashMap = new HashMap<>();

    public ImageLoader(Context context, int loading, int error) {
        this.context = context;
        this.loading = loading;
        this.error = error;
    }

    /**
     * 实现ImageView图片的显示
     * @param imagepath:要加载显示的图片，在服务器端的路径
     * @param iv_item_icon:要显示到的ImageView
     */
    public void loadImage(String imagepath, ImageView iv_item_icon) {

        //将imageView要加载显示的url设置为imageView的标签
        iv_item_icon.setTag(imagepath);

        //一级缓存
        Bitmap bitmap = fromFirstCache(imagepath);
        if(bitmap != null) {
            iv_item_icon.setImageBitmap(bitmap);
            return;
        }

        //二级缓存
        bitmap = fromSecondCache(imagepath);
        if(bitmap != null) {
            iv_item_icon.setImageBitmap(bitmap);

            //保存在一级缓存中
            bitmapHashMap.put(imagepath,bitmap);
            return;
        }

        //三级缓存
        fromThirdCache(imagepath,iv_item_icon);

    }

    //二级缓存
    private Bitmap fromSecondCache(String imagepath) {

        String fileName = imagepath.substring(imagepath.lastIndexOf("/") + 1);
        String filePath = context.getExternalFilesDir(null).getAbsolutePath() + "/" + fileName;

        File file = new File(filePath);
        //如果在本地找到了指定的文件，则将此文件加载到内存中，生成Bitmap对象
        //否则，返回null
        
        if(file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            return  bitmap;
        }else {

            return null;
        }

    }

    /**
     * 三级缓存
     * @param imagepath
     * @param iv_item_icon
     */
    private void fromThirdCache(final String imagepath, final ImageView iv_item_icon) {

        new AsyncTask<Void,Void,Bitmap>(){

            //对应联网操作的第一步：主线程显示提示视图
            @Override
            protected void onPreExecute() {
                iv_item_icon.setImageResource(loading);
            }

            //对应联网操作的第二步：分线程联网下载数据
            @Override
            protected Bitmap doInBackground(Void... params) {
                //在分线程联网之前，判断要联网下载的图片的url：imagepath与
                //当前imageView要显示的图片对应的url:currentUrl是否一致。
                //如果不一致，则不要在联网了。
                String currentUrl = (String) iv_item_icon.getTag();
                if(!imagepath.equals(currentUrl)) {

                    return null;
                }

                InputStream is = null;
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(imagepath);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();

                    if(conn.getResponseCode() == 200) {

                        is = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        //将此Bitmap对象保存到一级、二级缓存上
                        bitmapHashMap.put(imagepath,bitmap);//一级缓存

                        //二级缓存
                        String fileName = imagepath.substring(imagepath.lastIndexOf("/")+ 1);
                        String filePath = context.getExternalFilesDir(null).getAbsolutePath() + "/" + fileName;
                        OutputStream os = new FileOutputStream(filePath);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);//内存中

                        return bitmap;
                    }else{
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(conn!= null) {
                        conn.disconnect();
                    }
                    if(is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }

            //对应联网操作第三步：主线程更新视图

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                //如果imagePath路径下载得到的Bitmap与当前imageView要加载显示的currentUrl
                //如果一致，则显示。
                String currentUrl = (String) iv_item_icon.getTag();
                if(!imagepath.equals(currentUrl)) {
                 return;
                }

                //加载显示ImageView
                if(bitmap != null) {
                    iv_item_icon.setImageBitmap(bitmap);
                }else{
                    iv_item_icon.setImageResource(error);
                }

            }
        }.execute();
    }

    //一级缓存
    private Bitmap fromFirstCache(String imagepath) {
        return bitmapHashMap.get(imagepath);
    }
}
