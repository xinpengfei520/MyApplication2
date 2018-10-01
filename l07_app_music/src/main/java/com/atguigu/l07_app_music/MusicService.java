package com.atguigu.l07_app_music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by xinpengfei on 2016/9/11.
 */
public class MusicService extends Service {

    MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getStringExtra("action");
        
        if("play".equals(action)) {//此处"play"写.equals前面可放空指针异常
            play();
        }else  if("pause".equals(action)) {
            pause();
        }else if("stop".equals(action)) {
            stop();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void stop() {

        if(player != null){
            player.stop();
            player.reset();
            player.release();//释放加载的文件
            player = null;//不要忘了！
        }
    }

    private void pause() {

        if(player != null) {
            player.pause();
        }
    }

    private void play() {

        if(player == null) {
            player = MediaPlayer.create(this, R.raw.a_little_love);
        }

        if(player != null && !player.isPlaying()) {
            player.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();//停止音乐
    }
}
