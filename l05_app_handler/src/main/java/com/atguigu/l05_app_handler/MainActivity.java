package com.atguigu.l05_app_handler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 应用：联网下载数据，并显示在页面上
 * 步骤：
 * 1.布局的设置：FrameLayout或者RelativeLayout
 * 2.联网操作。三步：
 * 2.1 主线程：显示提示视图：显示ProgressDialog
 * 2.2 分线程：联网下载数据。 url = http://192.168.56.1:8080/Web_server/ShopListServlet
 * 可以使用HttpURLConnection实现联网操作。返回的是json字符串
 * 使用Gson解析json字符串，得到List<ShopInfo>
 * 2.3 主线程：使用ListView + Adapter + List<ShopInfo> + item Layout 显示数据列表（除图片外）
 * <p/>
 * 3.联网下载图片。使用异步任务AsyncTask实现。
 * onPreExecute():默认显示一个正在加载的图片
 * doInBackground():联网下载指定的图片的url对应的数据：Bitmap.将Bitmap对象返回。
 * onPostExecute():如果加载成功，将Bitmap设置给界面中的ImageView显示.如果加载失败，可以给ImageView
 * 设置加载失败的图片。
 * 4.图片的三级缓存。
 * 一级缓存：内存级别
 * 二级缓存：在本地的存储中
 * 三级缓存：在网络服务器上存储
 */

public class MainActivity extends Activity {

    private static final int MESSAGE_SUCCESS = 1;
    private static final int MESSAGE_FAIL = 2;
    private static final int MESSAGE_ERROR = 3;
    private ListView lv_main;
    private LinearLayout ll_loading;
    private List<ShopInfo> list;
    private MyAdapter adapter;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //联网操作步骤三：主线程处理消息，更新界面

            switch (msg.what) {
                case MESSAGE_SUCCESS:
                    //显示列表
                    lv_main.setAdapter(adapter);
                    break;
                case MESSAGE_FAIL:
                    Toast.makeText(MainActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_ERROR:
                    Toast.makeText(MainActivity.this, "联网失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            //设置进度的消失
            ll_loading.setVisibility(View.GONE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_main = (ListView) findViewById(R.id.lv_main);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        adapter = new MyAdapter();

        //联网操作步骤一：主线程显示提示视图；
        ll_loading.setVisibility(View.VISIBLE);

        //联网操作步骤二：分线程联网下载数据；
        new Thread() {
            @Override
            public void run() {

                String path = "http://192.168.23.1:8080/Web_server/ShopListServlet";
                InputStream is = null;
                ByteArrayOutputStream baos = null;
                HttpURLConnection conn = null;

                try {
                    URL url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();

                    if (conn.getResponseCode() == 200) {
                        is = conn.getInputStream();
                        baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = is.read(buffer)) != -1) {
                            baos.write(buffer, 0, len);
                            SystemClock.sleep(30);
                        }

                        String jsonArr = baos.toString();
                        //使用GSon解析json数组
                        Gson gson = new Gson();
                        list = gson.fromJson(jsonArr, new TypeToken<List<ShopInfo>>() {
                        }.getType());

                        //发送空消息，表示数据下载成功
                        handler.sendEmptyMessage(MESSAGE_SUCCESS);

                    } else {
                        //发送空消息，表示数据下载失败
                        handler.sendEmptyMessage(MESSAGE_FAIL);
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                    //发送空消息，表示联网失败
                    handler.sendEmptyMessage(MESSAGE_ERROR);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (baos != null) {
                        try {
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }.start();


    }

    class MyAdapter extends BaseAdapter {

        private ImageLoader imageLoader;

        public MyAdapter() {
            imageLoader = new ImageLoader(MainActivity.this, R.drawable.loading, R.drawable.error);

        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list == null ? 0 : list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_main, null);
            }

            //1.加载convertView中的具体的视图对象
            ImageView iv_item_icon = (ImageView) convertView.findViewById(R.id.iv_main_icon);
            TextView tv_main_name = (TextView) convertView.findViewById(R.id.tv_main_name);
            TextView tv_main_content = (TextView) convertView.findViewById(R.id.tv_main_content);

            //2.获取集合中指定的position 位置的数据
            ShopInfo shopInfo = list.get(position);
            String name = shopInfo.getName();
            double price = shopInfo.getPrice();
            String imagepath = shopInfo.getImagepath();

            //3.装配数据
            tv_main_name.setText(name);
            tv_main_content.setText("商品价格：￥" + price);
//            Log.e("TAG",imagepath);
            imageLoader.loadImage(imagepath, iv_item_icon);

            return convertView;
        }
    }


}
