package com.example.mybaicizhan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybaicizhan.Db.MyDatabase;
import com.example.mybaicizhan.Http.MyOkHttp;
import com.example.mybaicizhan.Http.SentencePerDay;
import com.example.mybaicizhan.Http.WordValue;
import com.example.mybaicizhan.Utils.FileUtils;
import com.example.mybaicizhan.Utils.HttpDownloadUtil;
import com.example.mybaicizhan.Utils.MusicPlayer;
import com.example.mybaicizhan.XML.JsonReader;
import com.example.mybaicizhan.XML.XMLReader;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static private final String TAG = "MainActivity";
    static private final String SPDPATH = "SPDPATH/";
    static private final int DOWNLOAD_XML_FINISH = 0;
    static private final int DOWNLOAD_JSON_FINISH = 1;
    static private final int GET_DATA_SUCCESS = 2;
    static private final int SERVER_ERROR = 3;
    static private final int NETWORK_ERROR = 4;
    static private final int PLAYMUSIC_SPD = 5;

    private MyOkHttp myOkHttp;
    private MyDatabase myDatabase;
    private MusicPlayer musicPlayer;

    private Button btn;
    private EditText editText;
    private ImageView imageView;
    //private TextView spdTextView;
    private LinearLayout historyLL;
    private TextView noneTv;
    private TextView moreTv;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Stack<View> viewStack;

    private SentencePerDay sentencePerDay;

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case DOWNLOAD_XML_FINISH:
                    String downloadXML = myOkHttp.getDownloadXML();
                    Intent intent = new Intent(MainActivity.this,WordValueActivity.class);
                    intent.putExtra("downloadXML",downloadXML);
                    startActivity(intent);
                    break;
                case DOWNLOAD_JSON_FINISH:
                    String downloadJson = myOkHttp.getDownloadJson();
                    JsonReader jsonReader = new JsonReader();
                    sentencePerDay = jsonReader.parseJsonToSentencePerDay(downloadJson);
                    //spdTextView.setText("  " + sentencePerDay.getContent());
                    if(imageView != null){
                        imageView.setClickable(true);
                        imageView.setOnClickListener(MainActivity.this);
                        setImageURL(sentencePerDay.getFenxiang_img());
                    }
                case GET_DATA_SUCCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    imageView.setImageBitmap(bitmap);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(MainActivity.this,"服务器发生错误",Toast.LENGTH_SHORT).show();
                    break;
                case PLAYMUSIC_SPD:
                    if(musicPlayer != null){
                        String fileName = (String) msg.obj;
                        musicPlayer.initMediaPlayerByNetWork(SPDPATH,fileName);
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myOkHttp = new MyOkHttp();
        viewStack = new Stack<>();
        //spdTextView = (TextView) findViewById(R.id.main_spd_tv);

        initView();
        verifyStoragePermissions(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                myOkHttp.getSententcePerDayByApi();
                handler.sendEmptyMessage(DOWNLOAD_JSON_FINISH);
            }
        }).start();
    }

    @Override
    public void onStart(){
        super.onStart();
        setHistory();
        musicPlayer = new MusicPlayer();

        navigationView.setCheckedItem(R.id.nav_item_main);
    }

    @Override
    public void onStop(){
        super.onStop();
        musicPlayer.stop();
    }

    private void initView(){
        btn = (Button) findViewById(R.id.search_word_btn);
        editText = (EditText) findViewById(R.id.search_word_edit);
        imageView = (ImageView) findViewById(R.id.main_spd_image);
        historyLL = (LinearLayout) findViewById(R.id.main_history_ll);
        noneTv = (TextView) findViewById(R.id.main_history_none);
        moreTv = (TextView) findViewById(R.id.main_more);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_item_main);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_item_history:
                        startActivity(new Intent(MainActivity.this,HistoryActivity.class));
                        break;
                    case R.id.nav_item_remember:
                        startActivity(new Intent(MainActivity.this,RememberActivity.class));
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        moreTv.setClickable(true);
        moreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HistoryActivity.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String searchText = editText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        myOkHttp.getWordByApiForEnglish(searchText);
                        handler.sendEmptyMessage(DOWNLOAD_XML_FINISH);
                    }
                }).start();
            }
        });
    }

    private void setHistory(){
        myDatabase = new MyDatabase(MainActivity.this);
        WordValue[] wordValues = myDatabase.queryWordValuesLimit3();
        boolean empty = true;
        clearHistoryLL();
        for(WordValue item : wordValues){
            if(item != null){
                empty = false;
                TextView tv1 = new TextView(MainActivity.this);
                tv1.setText(item.getWord());
                tv1.setTextSize(25);
                tv1.setTextColor(Color.BLACK);
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                lp1.setMarginStart(5);
                tv1.setLayoutParams(lp1);

                TextView tv2 = new TextView(MainActivity.this);
                tv2.setText(item.getPosArrayList().get(0));
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                lp2.setMarginStart(5);
                tv2.setLayoutParams(lp2);

                historyLL.addView(tv1);
                historyLL.addView(tv2);

                viewStack.push(tv1);
                viewStack.push(tv2);
            }
        }
        if(empty){
            noneTv.setVisibility(View.VISIBLE);
        }
        else{
            noneTv.setVisibility(View.GONE);
        }
    }

    private void clearHistoryLL(){
        if(historyLL == null || viewStack == null)
            return;
        while(!viewStack.empty()){
            historyLL.removeView(viewStack.pop());
        }
    }

    public void setImageURL(final String path) {
        //开启一个线程用于联网
        new Thread() {
            @Override
            public void run() {
                try {
                    //把传过来的路径转成URL
                    URL url = new URL(path);
                    //获取连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //使用GET方法访问网络
                    connection.setRequestMethod("GET");
                    //超时时间为10秒
                    connection.setConnectTimeout(10000);
                    //获取返回码
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        //使用工厂把网络的输入流生产Bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        //利用Message把图片发给Handler
                        Message msg = Message.obtain();
                        msg.obj = bitmap;
                        msg.what = GET_DATA_SUCCESS;
                        handler.sendMessage(msg);
                        inputStream.close();
                    }else {
                        //服务启发生错误
                        handler.sendEmptyMessage(SERVER_ERROR);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //网络连接错误
                    handler.sendEmptyMessage(NETWORK_ERROR);
                }
            }
        }.start();
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.main_spd_image:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(sentencePerDay != null) {
                            String fileName = sentencePerDay.getSid() +".mp3";
                            boolean exist = FileUtils.getInstance().isFileExist(SPDPATH, fileName);
                            if (!exist) {
                                HttpDownloadUtil httpDownloadUtil = new HttpDownloadUtil();
                                String result = httpDownloadUtil.download(sentencePerDay.getTts(), SPDPATH, fileName);
                                if (result.equals("downloadOk"))
                                    exist = true;
                            }
                            if (exist) {
                                Message msg = handler.obtainMessage();
                                msg.obj = fileName;
                                msg.what = PLAYMUSIC_SPD;
                                handler.sendMessage(msg);
                            }
                        }
                    }
                }).start();
                break;
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
