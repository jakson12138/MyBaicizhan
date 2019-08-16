package com.example.mybaicizhan.Utils;

import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.util.Log;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private FileUtils fileUtils;
    private static final String TAG = "MUSICPLAYER";
    private AnimationDrawable animationDrawable;
    public void setAnimationDrawable(AnimationDrawable ad){
        animationDrawable = ad;
    }

    public MusicPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.reset();
                if(animationDrawable != null){
                    animationDrawable.stop();
                    animationDrawable.selectDrawable(0);
                    animationDrawable = null;
                }
            }
        });
        fileUtils = FileUtils.getInstance();
    }

    public void initMediaPlayerByNetWork(String path,String fileName){
        try{
            Log.d(TAG,fileUtils.getSDPATH() + path + fileName);
            mediaPlayer.setDataSource(fileUtils.getSDPATH() + path + fileName);
            mediaPlayer.prepare();
            Log.d(TAG,"prepare success");
            mediaPlayer.start();
        }catch(Exception e){
            Log.d(TAG,"exception");
            e.printStackTrace();
        }
    }

    public void stop(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

}
