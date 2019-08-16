package com.example.mybaicizhan;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mybaicizhan.Db.MyDatabase;
import com.example.mybaicizhan.Http.WordValue;
import com.example.mybaicizhan.Utils.FileUtils;
import com.example.mybaicizhan.Utils.HttpDownloadUtil;
import com.example.mybaicizhan.Utils.MusicPlayer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class RememberActivity extends AppCompatActivity implements View.OnClickListener {

    //状态栏
    private ImageView time_image;
    private TextView study_tv;
    private TextView ok_tv;
    private TextView last_word_tv;

    //单词 发音 句子
    private TextView word_tv;
    private TextView psE_tv;
    private TextView sent_tv;
    private View line_view;

    //选项
    private TextView item_A;
    private TextView item_B;
    private TextView item_C;
    private TextView item_D;

    //底部按钮
    private Button btn_zhan;
    private Button btn_hint;
    private Button btn_music;

    private static final String TAG = "REMEMBERACTIVITY";

    private static int hint_count = 0;

    //当前背诵单词
    private WordValue wordValue;
    private ArrayList<WordValue> list;
    private WordValue lastWordValue;
    private int currentIndex = 0;
    private int correctItem = 0;
    private ArrayList<String> strs;

    private MyDatabase myDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember);
        initView();
    }

    @Override
    protected void onStart(){
        super.onStart();

        myDatabase = new MyDatabase(this);
        if(myDatabase != null){
            list = myDatabase.queryWordValues();
        }
        if(list != null && list.size() > currentIndex){
            wordValue = list.get(currentIndex);
            strs = new ArrayList<>();
            for(int i = 0;i < list.size();i++){
                String word = list.get(i).getWord();
                String pos = list.get(i).getPosArrayList().get(0);
                strs.add(pos);
            }
        }
        else
            finish();

        refresh();
    }

    private void refresh(){
        if(list != null){
            int len = list.size();
            int ok = currentIndex;
            int study = len - ok;
            study_tv.setText("需学"+study);
            ok_tv.setText("已学"+ok);

            String ans = strs.get(currentIndex);
            Random random = new Random();
            correctItem = random.nextInt(4);
            int[] set = new int[4];
            int[] visit = new int[len];
            set[correctItem] = 1;
            visit[currentIndex] = 1;
            int[] views = new int[]{R.id.rem_item_A,R.id.rem_item_B,R.id.rem_item_C,R.id.rem_item_D};
            TextView correctTv = (TextView) findViewById(views[correctItem]);
            correctTv.setText(ans);
            for(int i = 0;i < 4;i++){
                if(set[i] == 0){
                    int index = random.nextInt(len);
                    if(visit[index] == 0){
                        String otherAns = strs.get(index);
                        TextView otherTv = (TextView) findViewById(views[i]);
                        otherTv.setText(otherAns);
                        set[i] = 1;
                        visit[index] = 1;
                    }
                    else
                        i--;
                }
            }
        }

        if(lastWordValue != null){
            String word = lastWordValue.getWord();
            String pos = lastWordValue.getPosArrayList().get(0);
            last_word_tv.setText(word + " " + pos);
        }

        if(wordValue != null){
            word_tv.setText(wordValue.getWord());
            psE_tv.setText("/ " + wordValue.getPsE() + " /");
            sent_tv.setText(wordValue.getSentenceArrayList().get(0).getOrig());
        }
    }

    private void initView(){
        initTopView();
        initWordView();
        initCardView();
        initBottomView();
    }

    private void initTopView(){
        time_image = (ImageView) findViewById(R.id.rem_img);
        study_tv = (TextView) findViewById(R.id.rem_study);
        ok_tv = (TextView) findViewById(R.id.rem_ok);
        last_word_tv = (TextView) findViewById(R.id.rem_last);

        time_image.setClickable(true);
        time_image.setOnClickListener(this);
    }

    private void initWordView(){
        word_tv = (TextView) findViewById(R.id.rem_word);
        psE_tv = (TextView) findViewById(R.id.rem_psE);
        line_view = (View) findViewById(R.id.rem_line_view);
        sent_tv = (TextView) findViewById(R.id.rem_sent);
    }

    private void initCardView(){
        item_A = (TextView) findViewById(R.id.rem_item_A);
        item_B = (TextView) findViewById(R.id.rem_item_B);
        item_C = (TextView) findViewById(R.id.rem_item_C);
        item_D = (TextView) findViewById(R.id.rem_item_D);

        item_A.setOnClickListener(this);
        item_B.setOnClickListener(this);
        item_C.setOnClickListener(this);
        item_D.setOnClickListener(this);
    }

    private void initBottomView(){
        btn_zhan = (Button) findViewById(R.id.rem_btn_zhan);
        btn_zhan.setOnClickListener(this);

        btn_hint = (Button) findViewById(R.id.rem_btn_hint);
        btn_hint.setOnClickListener(this);

        btn_music = (Button) findViewById(R.id.rem_btn_music);
        btn_music.setOnClickListener(this);
    }

    private void zhan(){
        lastWordValue = list.get(currentIndex);
        currentIndex++;
        if(currentIndex >= list.size())
            finish();
        else{
            wordValue = list.get(currentIndex);
            line_view.setVisibility(View.INVISIBLE);
            sent_tv.setVisibility(View.INVISIBLE);
            hint_count = 0;
            refresh();
        }
    }

    private void goToWordValueActivity(){
        String word = wordValue.getWord();
        Intent intent = new Intent(RememberActivity.this,WordValueActivity.class);
        intent.putExtra("word",word);
        startActivity(intent);
    }

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            String fileName = (String) msg.obj;
            switch (msg.what) {
                case PLAYMUSIC_PRONE:
                    MusicPlayer musicPlayer = new MusicPlayer();
                    musicPlayer.initMediaPlayerByNetWork(PRONE,fileName);
                    break;
            }
        }
    };

    public static final String PRONE = "PRONE/";
    private static final int PLAYMUSIC_PRONE = 0;
    private void music_btn(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String word = wordValue.getWord();
                String fileName = word + ".mp3";
                boolean exist = FileUtils.getInstance().isFileExist(PRONE,fileName);
                if(!exist){
                    HttpDownloadUtil httpDownloadUtil = new HttpDownloadUtil();
                    String result = httpDownloadUtil.download(wordValue.getPronE(),PRONE,fileName);
                    if(result.equals("downloadOk"))
                        exist = true;
                }
                if(exist){
                    Message msg = handler.obtainMessage();
                    msg.obj = fileName;
                    msg.what = PLAYMUSIC_PRONE;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rem_btn_zhan:
                zhan();
                break;
            case R.id.rem_btn_hint:
                if(hint_count == 0){
                    line_view.setVisibility(View.VISIBLE);
                    sent_tv.setVisibility(View.VISIBLE);
                    hint_count++;
                }
                else{
                    goToWordValueActivity();
                }
                break;
            case R.id.rem_img:
                finish();
                break;
            case R.id.rem_item_A:
            case R.id.rem_item_B:
            case R.id.rem_item_C:
            case R.id.rem_item_D:
                int[] views = new int[]{R.id.rem_item_A,R.id.rem_item_B,R.id.rem_item_C,R.id.rem_item_D};
                for(int i = 0;i < 4;i++){
                    if(views[i] == v.getId()){
                        if(i == correctItem){
                            zhan();
                        }
                        else{
                            goToWordValueActivity();
                        }
                        break;
                    }
                }
                break;
            case R.id.rem_btn_music:
                music_btn();
            default:
                break;
        }
    }

}
