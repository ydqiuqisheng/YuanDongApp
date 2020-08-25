package net.huansi.equipment.equipmentapp.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

import java.io.IOException;

/**
 * Created by Administrator on 2016/2/17.
 * 播放音效
 */
public class BroadVoice {
    private Handler handler;
    private MediaPlayer player;
    public BroadVoice(Context context, int i){
        player= MediaPlayer.create(context,i);
        handler=new Handler();
    }

    /**
     * 开始播放音乐
     * @throws IOException
     */
    public void start(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                player.start();
            }
        },500);
    }

    /**
     * 停止播放音乐
     */
    public void stop(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(player!=null){
                    player.stop();
                    player.release();
                    player=null;
                }
            }
        },1000);
    }

//    /**
//     * 是否正在播放
//     * @return
//     */
//    public boolean isPlaying(){
//        return player.isPlaying();
//    }
//
//    class VoiceTask extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            player.start();
//        }
//    }
}
