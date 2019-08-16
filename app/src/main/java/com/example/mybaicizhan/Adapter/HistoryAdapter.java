package com.example.mybaicizhan.Adapter;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mybaicizhan.HistoryActivity;
import com.example.mybaicizhan.Http.WordValue;
import com.example.mybaicizhan.R;

import org.w3c.dom.Text;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<HistoryAdapter.WordHistory> {

    public static class WordHistory{
        private String word;
        private String pos;
        public void setWord(String w){
            word = w;
        }
        public void setPos(String p){
            pos = p;
        }
        public String getWord(){
            return word;
        }
        public String getPos(){
            return pos;
        }

        public WordHistory(String w,String p){
            word = w;
            pos = p;
        }
    }

    private int resourceId;

    public HistoryAdapter(Context context, int viewId, List<HistoryAdapter.WordHistory> objects){
        super(context,viewId,objects);
        resourceId = viewId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        HistoryAdapter.WordHistory wh = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.wordTv = (TextView) view.findViewById(R.id.history_wordTv);
            viewHolder.posTv = (TextView) view.findViewById(R.id.history_posTv);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.wordTv.setText(wh.getWord());
        viewHolder.posTv.setText(wh.getPos());
        return view;
    }

    class ViewHolder{
        TextView wordTv;
        TextView posTv;
    }

}

