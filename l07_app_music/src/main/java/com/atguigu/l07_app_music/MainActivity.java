package com.atguigu.l07_app_music;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btn_main_play;
    private Button btn_main_pause;
    private Button btn_main_stop;
    private Button btn_main_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_main_play = (Button) findViewById(R.id.btn_main_play);
        btn_main_pause = (Button) findViewById(R.id.btn_main_pause);
        btn_main_stop = (Button) findViewById(R.id.btn_main_stop);
        btn_main_exit = (Button) findViewById(R.id.btn_main_exit);

        btn_main_play.setOnClickListener(this);
        btn_main_pause.setOnClickListener(this);
        btn_main_stop.setOnClickListener(this);
        btn_main_exit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this,MusicService.class);

        if(v == btn_main_play) {//播放
            intent.putExtra("action","play");
            startService(intent);
            Toast.makeText(this,"开始播放",Toast.LENGTH_SHORT).show();

        }else if(v == btn_main_pause) {//暂停
            intent.putExtra("action","pause");
            startService(intent);
            Toast.makeText(this,"暂停播放",Toast.LENGTH_SHORT).show();

        }else if(v == btn_main_stop) {//停止
            intent.putExtra("action","stop");
            startService(intent);
            Toast.makeText(this,"停止播放",Toast.LENGTH_SHORT).show();

        }else if(v == btn_main_exit) {//退出界面，同时停止服务
            finish();
            stopService(intent);
            Toast.makeText(this,"退出播放",Toast.LENGTH_SHORT).show();
        }
            
    }
}
