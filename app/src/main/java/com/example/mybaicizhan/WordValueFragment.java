package com.example.mybaicizhan;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybaicizhan.Adapter.SentenceAdapter;
import com.example.mybaicizhan.Db.MyDatabase;
import com.example.mybaicizhan.Http.WordValue;
import com.example.mybaicizhan.Utils.FileUtils;
import com.example.mybaicizhan.Utils.HttpDownloadUtil;
import com.example.mybaicizhan.Utils.MusicPlayer;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WordValueFragment extends Fragment implements View.OnClickListener {

    private View view;
    private MyDatabase myDatabase;
    private MusicPlayer musicPlayer;

    private static final int PLAYMUSIC_PRONE = 0;
    private static final int PLAYMUSIC_PRONA = 1;
    private static final String TAG = "WVF";
    public static final String PRONE = "PRONE/";
    public static final String PRONA = "PRONA/";

    private WordValue wv;

    Button pronEBtn;
    Button pronABtn;

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            String fileName = (String) msg.obj;
            switch (msg.what) {
                case PLAYMUSIC_PRONE:
                    if(pronEBtn != null){
                        AnimationDrawable animationDrawable = (AnimationDrawable) pronEBtn.getBackground();
                        if(musicPlayer != null){
                            musicPlayer.setAnimationDrawable(animationDrawable);
                            musicPlayer.initMediaPlayerByNetWork(PRONE,fileName);
                        }
                        animationDrawable.start();
                    }
                    break;
                case PLAYMUSIC_PRONA:
                    if(pronABtn != null){
                        AnimationDrawable animationDrawable = (AnimationDrawable) pronABtn.getBackground();
                        if(musicPlayer != null){
                            musicPlayer.setAnimationDrawable(animationDrawable);
                            musicPlayer.initMediaPlayerByNetWork(PRONA,fileName);
                        }
                        animationDrawable.start();
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        myDatabase = new MyDatabase(getActivity());
        musicPlayer = new MusicPlayer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.word_value_fragment,container,false);
        Button zhanBtn = (Button) view.findViewById(R.id.zhan);
        zhanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        Button historyBtn = (Button) view.findViewById(R.id.wv_history);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(new Intent(getActivity(),HistoryActivity.class));
            }
        });
        return view;
    }

    public void refresh(final WordValue wordValue){
        if(wordValue == null){
            Toast.makeText(getContext(),"查无此单词",Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return;
        }
        myDatabase.insertWordValue(wordValue);
        wv = wordValue;

        TextView word = (TextView) view.findViewById(R.id.wv_word);
        TextView psE = (TextView) view.findViewById(R.id.wv_psE);
        TextView psA = (TextView) view.findViewById(R.id.wv_psA);
        LinearLayout posLinearLayout = (LinearLayout) view.findViewById(R.id.wv_pos_ll);
        ListView sentListView = (ListView) view.findViewById(R.id.wv_sent_listview);
        pronEBtn = (Button) view.findViewById(R.id.wv_pronE);
        pronABtn = (Button) view.findViewById(R.id.wv_pronA);
        pronEBtn.setOnClickListener(this);
        pronABtn.setOnClickListener(this);

        word.setText(wordValue.getWord());
        psE.setText(wordValue.getPsE());
        psA.setText(wordValue.getPsA());

        ArrayList<String> posArrayList = wordValue.getPosArrayList();
        ArrayList<WordValue.Sentence> sentenceArrayList = wordValue.getSentenceArrayList();

        int len1 = posArrayList.size();
        for(int i = 0;i < len1;i++){
            TextView pos = new TextView(getContext());
            String temp = posArrayList.get(i);
            pos.setText(temp);
            posLinearLayout.addView(pos);
        }

        SentenceAdapter adapter = new SentenceAdapter(getContext(),R.layout.sentence_item,sentenceArrayList);
        sentListView.setAdapter(adapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        musicPlayer.stop();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.wv_pronE:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String word =wv.getWord();
                        String fileName = word + ".mp3";
                        boolean exist = FileUtils.getInstance().isFileExist(PRONE,fileName);
                        if(!exist){
                            HttpDownloadUtil httpDownloadUtil = new HttpDownloadUtil();
                            String result = httpDownloadUtil.download(wv.getPronE(),PRONE,fileName);
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
                break;
            case R.id.wv_pronA:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String word =wv.getWord();
                        String fileName = word + ".mp3";
                        boolean exist = FileUtils.getInstance().isFileExist(PRONA,fileName);
                        if(!exist){
                            HttpDownloadUtil httpDownloadUtil = new HttpDownloadUtil();
                            String result = httpDownloadUtil.download(wv.getPronA(),PRONA,fileName);
                            if(result.equals("downloadOk"))
                                exist = true;
                        }
                        if(exist){
                            Message msg = handler.obtainMessage();
                            msg.obj = fileName;
                            msg.what = PLAYMUSIC_PRONA;
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
                break;
        }
    }
}
