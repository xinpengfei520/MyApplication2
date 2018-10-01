package com.atguigu.l05_listviewtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv_baseadapter;
    private MyAdapter adapter;
    private List<FoodInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_baseadapter = (ListView) findViewById(R.id.lv_baseadapter);
        //初始化集合数据
        initData();

        //初始化adapter
        adapter = new MyAdapter();

        //显示列表
        lv_baseadapter.setAdapter(adapter);
    }

    private void initData() {
        list = new ArrayList<FoodInfo>();
        list.add(new FoodInfo(R.drawable.f1, "name - 1", "content - 1"));
        list.add(new FoodInfo(R.drawable.f2, "name - 2", "content - 2"));
        list.add(new FoodInfo(R.drawable.f3, "name - 3", "content - 3"));
        list.add(new FoodInfo(R.drawable.f4, "name - 4", "content - 4"));
        list.add(new FoodInfo(R.drawable.f5, "name - 5", "content - 5"));
        list.add(new FoodInfo(R.drawable.f6, "name - 6", "content - 6"));
        list.add(new FoodInfo(R.drawable.f7, "name - 7", "content - 7"));
        list.add(new FoodInfo(R.drawable.f8, "name - 8", "content - 8"));
        list.add(new FoodInfo(R.drawable.f9, "name - 9", "content - 9"));
        list.add(new FoodInfo(R.drawable.f10, "name - 10", "content - 10"));
    }


    //提供一个继承于BaseAdapter的内部类
    class MyAdapter extends BaseAdapter{

        //返回集合的个数
        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        //返回指定位置上的集合数据
        @Override
        public Object getItem(int position) {
            return list == null ? null : list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //ListView第三层面的优化：使用ViewHolder
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

           //优化以后的写法
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = View.inflate(MainActivity.this, R.layout.item_base,null);

                viewHolder = new ViewHolder();

                viewHolder.iv_base_icon = (ImageView) convertView.findViewById(R.id.iv_base_icon);
                viewHolder.tv_base_name = (TextView) convertView.findViewById(R.id.tv_base_name);
                viewHolder.tv_base_content = (TextView) convertView.findViewById(R.id.tv_base_content);

                convertView.setTag(viewHolder);//将一个convertView关联唯一的一个viewHolder
            }else{//如果convertView是复用的
                viewHolder = (ViewHolder) convertView.getTag();
            }

            FoodInfo foodInfo = list.get(position);

            viewHolder.iv_base_icon.setImageResource(foodInfo.getIcon());
            viewHolder.tv_base_name.setText(foodInfo.getName());
            viewHolder.tv_base_content.setText(foodInfo.getContent());

            return convertView;
        }
    }

    static class ViewHolder{
        ImageView iv_base_icon;
        TextView tv_base_name;
        TextView tv_base_content;
    }
}
