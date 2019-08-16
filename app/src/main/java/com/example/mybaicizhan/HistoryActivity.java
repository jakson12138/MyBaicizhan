package com.example.mybaicizhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mybaicizhan.Adapter.HistoryAdapter;
import com.example.mybaicizhan.Db.MyDatabase;
import com.example.mybaicizhan.Http.WordValue;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private ListView listView;
    private MyDatabase myDatabase;

    private static final String TAG = "HISTORYACTIVITY";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.history_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        listView = (ListView) findViewById(R.id.history_listview);
        initListView();
    }

    private void initListView(){
        if(myDatabase == null){
            myDatabase = new MyDatabase(HistoryActivity.this);
        }
        ArrayList<WordValue> wordValues = myDatabase.queryWordValues();
        int len = wordValues.size();
        ArrayList<HistoryAdapter.WordHistory> list = new ArrayList<>(len);
        for(int i = 0;i < len;i++){
            String word = wordValues.get(i).getWord();
            String pos = wordValues.get(i).getPosArrayList().get(0);
            list.add(new HistoryAdapter.WordHistory(word,pos));
        }

        final HistoryAdapter adapter = new HistoryAdapter(HistoryActivity.this,R.layout.history_item,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"click");
                String word = adapter.getItem(position).getWord();
                if(word != null){
                    Intent intent = new Intent(HistoryActivity.this,WordValueActivity.class);
                    intent.putExtra("word",word);
                    startActivity(intent);
                }
            }
        });
    }

    private void deleteAll(){
        if(myDatabase == null){
            myDatabase = new MyDatabase(HistoryActivity.this);
        }
        myDatabase.deleteAll();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.history_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
       switch(item.getItemId()){
           case android.R.id.home:
               finish();
               break;
           case R.id.delete:
               deleteAll();
               initListView();
               break;
           default:
               break;
       }
       return true;
    }

}
